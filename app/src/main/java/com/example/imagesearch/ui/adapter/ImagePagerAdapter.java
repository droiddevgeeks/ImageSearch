package com.example.imagesearch.ui.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.imagesearch.R;
import com.example.imagesearch.api.model.Photo;

import java.util.ArrayList;
import java.util.List;

public class ImagePagerAdapter extends PagerAdapter {

    private Activity activity;
    private List<Photo> photoList;
    private int currentPos;
    private SparseArray<View> view;

    public ImagePagerAdapter(Activity activity, ArrayList<Photo> photos, int currentPos) {
        this.activity = activity;
        this.photoList = photos;
        this.currentPos = currentPos;
        view = new SparseArray<>(photoList.size());
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final Photo currentImage = photoList.get(position);
        ImageView imageView = new ImageView(container.getContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ViewCompat.setTransitionName(imageView, "item_" + currentImage.getId());
        view.put(position, imageView);

        String flickrImageurl = "https://farm" + currentImage.getFarm() + ".staticflickr.com/" +
                currentImage.getServer() + "/" + currentImage.getId() + "_" +
                currentImage.getSecret() + ".jpg";

        Glide.with(activity)
                .load(flickrImageurl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        ActivityCompat.startPostponedEnterTransition(activity);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (position == currentPos) {
                            imageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                                @Override
                                public boolean onPreDraw() {
                                    imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                                    ActivityCompat.startPostponedEnterTransition(activity);
                                    return true;
                                }
                            });
                        }
                        return false;
                    }
                })
                .into(imageView);

        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        view.removeAt(position);
        container.removeView((View) object);
    }

    public View getView(int position) {
        return view.get(position);
    }

    @Override
    public int getCount() {
        return photoList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }
}
