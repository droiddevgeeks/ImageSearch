package com.example.imagesearch.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.imagesearch.R;
import com.example.imagesearch.api.model.Photo;
import com.example.imagesearch.base.BaseActivity;
import com.example.imagesearch.databinding.ActivityImageDetailBindingImpl;
import com.example.imagesearch.ui.adapter.ImagePagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DetailActivity extends BaseActivity<ActivityImageDetailBindingImpl> {

    private Boolean isReturning = false;
    private int startingPosition = 0;
    private int currentPosition = 0;
    private ImagePagerAdapter pagerAdapter;

    public static final String ITEM_ID = "itemId";
    public static final String ITEM_LIST = "itemlist";
    public static final String SAVED_CURRENT_PAGE_POSITION = "current_page_position";

    private SharedElementCallback enterElementCallback;

    @Override
    public int getLayout() {
        return R.layout.activity_image_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSharedElementCallback();
        ActivityCompat.postponeEnterTransition(this);
        ActivityCompat.setEnterSharedElementCallback(this, enterElementCallback);
        ArrayList<Photo> data = getIntent().getParcelableArrayListExtra(ITEM_LIST);
        int index = getItemIndex(data);
        startingPosition = index > 0 ? index : 0;
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(SAVED_CURRENT_PAGE_POSITION);
        } else {
            currentPosition = startingPosition;
        }

        pagerAdapter = new ImagePagerAdapter(this, data, currentPosition);
        binding.viewPager.setAdapter(pagerAdapter);
        binding.viewPager.setCurrentItem(currentPosition);
        addPageChangeListener();
    }

    private void initSharedElementCallback() {
        enterElementCallback = new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                if (isReturning) {
                    View sharedElement = pagerAdapter.getView(currentPosition);
                    if (startingPosition != currentPosition) {
                        names.clear();
                        names.add(ViewCompat.getTransitionName(sharedElement));
                        sharedElements.clear();
                        sharedElements.put(ViewCompat.getTransitionName(sharedElement), sharedElement);
                    }
                }
            }
        };
    }

    private int getItemIndex(ArrayList<Photo> data) {
        for (int i = 0; i < data.size(); i++) {
            Photo photo = data.get(i);
            if (photo.getId().equalsIgnoreCase(getIntent().getStringExtra(ITEM_ID))) {
                return i;
            }
        }
        return 0;
    }

    private void addPageChangeListener() {
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                currentPosition = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_CURRENT_PAGE_POSITION, currentPosition);
    }

    @Override
    public void finishAfterTransition() {
        isReturning = true;
        Intent data = new Intent();
        data.putExtra(MainActivity.EXTRA_STARTING_ALBUM_POSITION, startingPosition);
        data.putExtra(MainActivity.EXTRA_CURRENT_ALBUM_POSITION, currentPosition);
        setResult(Activity.RESULT_OK, data);
        super.finishAfterTransition();
    }

}
