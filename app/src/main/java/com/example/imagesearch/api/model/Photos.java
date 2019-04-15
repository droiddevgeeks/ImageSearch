package com.example.imagesearch.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Photos {

    @SerializedName("photo")
    @Expose
    private ArrayList<Photo> photo;

    public ArrayList<Photo> getPhoto() {
        return photo;
    }
}
