package com.example.imagesearch.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.imagesearch.R;
import com.example.imagesearch.api.model.Photo;
import com.example.imagesearch.databinding.ItemImageBinding;
import com.example.imagesearch.ui.callback.IItemClick;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private ArrayList<Photo> imageList;
    private LayoutInflater layoutInflater;
    private IItemClick<ArrayList<Photo>> listener;
    private Context context;

    public ImageAdapter(Context context, ArrayList<Photo> imageList) {
        this.context = context;
        this.imageList = imageList;
        layoutInflater = LayoutInflater.from(context);
    }

    public void swapData(final List<Photo> list) {
        if (!this.imageList.isEmpty()) {
            this.imageList.clear();
        }
        this.imageList.addAll(list);
        notifyDataSetChanged();
    }

    public void appendData(final List<Photo> list) {
        int previousSize = imageList.size();
        imageList.addAll(list);
        notifyItemRangeInserted(previousSize, imageList.size());
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(ItemImageBinding.inflate(layoutInflater, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Photo currentImage = imageList.get(i);
        String flickerImageUrl = "https://farm" + currentImage.getFarm() + ".staticflickr.com/" +
                currentImage.getServer() + "/" + currentImage.getId() + "_" +
                currentImage.getSecret() + ".jpg";

        Glide.with(context)
                .load(flickerImageUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(viewHolder.binding.itemImage);

        ViewCompat.setTransitionName(viewHolder.binding.itemImage, "item_"+currentImage.getId());
        viewHolder.binding.itemImage.setOnClickListener(v -> listener.onItemClick(imageList, viewHolder.getAdapterPosition(), viewHolder.binding.itemImage));
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public void addListener(IItemClick itemClick) {
        this.listener = itemClick;
    }

    public void removeListener() {
        listener = null;
    }

    static final class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemImageBinding binding;

        public ViewHolder(ItemImageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
