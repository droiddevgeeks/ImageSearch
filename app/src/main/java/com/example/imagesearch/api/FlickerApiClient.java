package com.example.imagesearch.api;

import com.example.imagesearch.api.model.ApiResponse;

import javax.inject.Inject;

import io.reactivex.Single;

public class FlickerApiClient {
    private final FlickerApi api;

    @Inject
    public FlickerApiClient(FlickerApi api) {
        this.api = api;
    }

    public Single<ApiResponse> getImages(String method, String apiKey, String format, String keyword, int pageNo) {
        return api.getImages(method, apiKey, format, keyword,pageNo,ApiConstants.PER_PAGE_LIMIT,"?");
    }
}
