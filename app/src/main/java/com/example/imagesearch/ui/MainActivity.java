package com.example.imagesearch.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.example.imagesearch.R;
import com.example.imagesearch.api.CustomViewModelFactory;
import com.example.imagesearch.api.SharedPrefsHelper;
import com.example.imagesearch.api.model.ApiResponse;
import com.example.imagesearch.api.model.Photo;
import com.example.imagesearch.base.BaseActivity;
import com.example.imagesearch.databinding.ActivityMainBinding;
import com.example.imagesearch.ui.adapter.ImageAdapter;
import com.example.imagesearch.ui.callback.IItemClick;
import com.example.imagesearch.ui.viewmodel.SearchViewModel;
import com.example.imagesearch.util.EndlessScrollListener;
import com.example.imagesearch.util.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements IItemClick<ArrayList<Photo>> {

    private ImageAdapter adapter;
    private SearchViewModel viewModel;
    private Bundle reenterState = null;
    private GridLayoutManager layoutManager;
    private EndlessScrollListener scrollListener;
    private SharedElementCallback exitSharedElement;

    public static final String EXTRA_STARTING_ALBUM_POSITION = "extra_starting_item_position";
    public static final String EXTRA_CURRENT_ALBUM_POSITION = "extra_current_item_position";

    @Inject
    CustomViewModelFactory factory;

    @Inject
    SharedPrefsHelper prefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.setExitSharedElementCallback(this, exitSharedElement);
        viewModel = ViewModelProviders.of(this, factory).get(SearchViewModel.class);
        initImageAdapter();
        initSharedElementCallback();
        observeSearchView();
        observeDataChange();
        observeEndlessScrolling();
    }

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    private void initImageAdapter() {
        adapter = new ImageAdapter(this, new ArrayList<>());
        layoutManager = new GridLayoutManager(this, 3);
        binding.rvImage.setLayoutManager(layoutManager);
        binding.rvImage.setAdapter(adapter);
        adapter.addListener(this);
    }

    private void initSharedElementCallback() {
        exitSharedElement = new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                if (reenterState != null) {
                    int startPos = reenterState.getInt(EXTRA_STARTING_ALBUM_POSITION);
                    int currentPos = reenterState.getInt(EXTRA_CURRENT_ALBUM_POSITION);
                    if (startPos != currentPos) {
                        ArrayList<Photo> photos = new ArrayList<>();
                        if (viewModel.getMoreImageData().getValue() != null) {
                            ArrayList<Photo> list1 = viewModel.getImageData().getValue().getPhotos().getPhoto();
                            ArrayList<Photo> list2 = viewModel.getMoreImageData().getValue().getPhotos().getPhoto();
                            photos.addAll(list1);
                            photos.addAll(list2);
                        } else {
                            ArrayList<Photo> list1 = viewModel.getImageData().getValue().getPhotos().getPhoto();
                            photos.addAll(list1);
                        }
                        String newTransitionName = "view_" + photos.get(currentPos).getId();
                        View newSharedElement = binding.rvImage.findViewWithTag(newTransitionName);
                        if (newSharedElement != null) {
                            names.clear();
                            names.add(newTransitionName);
                            sharedElements.clear();
                            sharedElements.put(newTransitionName, newSharedElement);
                        }
                    }
                    reenterState = null;
                }
            }
        };

    }

    private void observeSearchView() {
        binding.svImageKey.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (!Utilities.isNetworkConnected(MainActivity.this)) {
                    if (prefsHelper.getQuery().equalsIgnoreCase(s)) {
                        viewModel.fetchImages(s);
                    } else {
                        showToast(MainActivity.this, getString(R.string.internet_error));
                    }
                } else {
                    viewModel.fetchImages(s);
                    prefsHelper.setQuery(s);
                }
                binding.svImageKey.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                return false;
            }
        });
    }

    private void observeDataChange() {
        viewModel.getImageData().observe(this, this::loadImageList);
        viewModel.getMoreImageData().observe(this, this::loadMoreImageList);
        viewModel.getIsLoadingData().observe(this, this::showLoading);
        viewModel.getIsLoadingMoreData().observe(this, this::showLoading);
        viewModel.getErrorData().observe(this, this::showError);
    }

    private void observeEndlessScrolling() {
        scrollListener = new EndlessScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (Utilities.isNetworkConnected(MainActivity.this)) {
                    viewModel.fetchMoreImages(binding.svImageKey.getQuery().toString(), page);
                } else {
                    showToast(MainActivity.this, getString(R.string.internet_error));
                }
            }
        };
        binding.rvImage.addOnScrollListener(scrollListener);
    }

    private void showLoading(Boolean loading) {
        binding.setShowLoading(loading);
    }

    private void loadImageList(ApiResponse data) {
        adapter.swapData(data.getPhotos().getPhoto());
    }

    private void loadMoreImageList(ApiResponse moreData) {
        adapter.appendData(moreData.getPhotos().getPhoto());
    }

    private void showError(Throwable throwable) {
        showToast(MainActivity.this, getString(R.string.some_error));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_option, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.grid2:
                layoutManager.setSpanCount(2);
                return true;
            case R.id.grid3:
                layoutManager.setSpanCount(3);
                return true;
            case R.id.grid4:
                layoutManager.setSpanCount(4);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(ArrayList<Photo> photos, int position, ImageView sharedView) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putParcelableArrayListExtra(DetailActivity.ITEM_LIST, photos);
        intent.putExtra(DetailActivity.ITEM_ID, photos.get(position).getId());

        Bundle bundle = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Pair<View, String> p1 = Pair.create(sharedView, ViewCompat.getTransitionName(sharedView));
            bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1).toBundle();
        }
        startActivity(intent, bundle);
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        reenterState = new Bundle(data.getExtras());
        if (reenterState != null) {
            int startPos = reenterState.getInt(EXTRA_STARTING_ALBUM_POSITION);
            int currentPos = reenterState.getInt(EXTRA_CURRENT_ALBUM_POSITION);
            if (startPos != currentPos) {
                binding.rvImage.smoothScrollToPosition(currentPos);
            }
            ActivityCompat.postponeEnterTransition(this);
            binding.rvImage.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    binding.rvImage.getViewTreeObserver().removeOnPreDrawListener(this);
                    ActivityCompat.startPostponedEnterTransition(MainActivity.this);
                    return true;
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null) {
            adapter.removeListener();
            adapter = null;
            scrollListener = null;
        }
    }
}
