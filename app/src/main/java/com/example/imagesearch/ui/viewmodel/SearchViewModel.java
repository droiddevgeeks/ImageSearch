package com.example.imagesearch.ui.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.imagesearch.api.ApiConstants;
import com.example.imagesearch.api.FlickerApiClient;
import com.example.imagesearch.api.RxSingleSchedulers;
import com.example.imagesearch.api.model.ApiResponse;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

import static com.example.imagesearch.api.ApiConstants.STARTING_OFFSET;

public class SearchViewModel extends ViewModel {

    private CompositeDisposable disposable;
    private final FlickerApiClient apiClient;
    private final RxSingleSchedulers rxSingleSchedulers;
    private final MutableLiveData<ApiResponse> moreImageData = new MutableLiveData<>();
    private final MutableLiveData<ApiResponse> imageData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoadingData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoadingMoreData = new MutableLiveData<>();
    private final MutableLiveData<Throwable> errorData = new MutableLiveData<>();
    @Inject
    public SearchViewModel(FlickerApiClient apiClient, RxSingleSchedulers rxSingleSchedulers) {
        this.apiClient = apiClient;
        this.rxSingleSchedulers = rxSingleSchedulers;
        disposable = new CompositeDisposable();
    }


    public LiveData<Boolean> getIsLoadingMoreData() {
        return isLoadingMoreData;
    }

    public MutableLiveData<ApiResponse> getMoreImageData() {
        return moreImageData;
    }

    public MutableLiveData<ApiResponse> getImageData() {
        return imageData;
    }

    public MutableLiveData<Boolean> getIsLoadingData() {
        return isLoadingData;
    }

    public MutableLiveData<Throwable> getErrorData() {
        return errorData;
    }

    public void fetchImages(String keyword) {
        isLoadingData.postValue(true);
        disposable.add(apiClient.getImages(ApiConstants.GET_PHOTO_METHOD, ApiConstants.API_KEY, ApiConstants.RESPONSE_FORMAT, keyword, STARTING_OFFSET)
                .doOnEvent((imageList, throwable) -> isLoadingData.postValue(false))
                .compose(rxSingleSchedulers.applySchedulers())
                .subscribe(imageData::setValue, errorData::setValue));
    }

    public void fetchMoreImages(String query, int offset) {
        isLoadingMoreData.postValue(true);
        disposable.add(apiClient.getImages(ApiConstants.GET_PHOTO_METHOD, ApiConstants.API_KEY, ApiConstants.RESPONSE_FORMAT, query, offset)
                .doOnEvent((imageList, throwable) -> isLoadingMoreData.postValue(false))
                .compose(rxSingleSchedulers.applySchedulers())
                .subscribe(moreImageData::setValue, errorData::setValue));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposable != null) {
            disposable.clear();
            disposable = null;
        }
    }

}
