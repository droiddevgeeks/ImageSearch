package com.example.imagesearch.ui.callback;

import android.widget.ImageView;

public interface IItemClick<T> {
    void onItemClick(T item, int position, ImageView view);
}
