package com.example.imagesearch.api;


public class ApiConstants {
    //https://www.flickr.com/services/rest?method=flickr.photos.getRecent&api_key=d7ef663da10506e9f372ed4ee57bfb83&format=json&text=avenger
    public static final String API_KEY = "PLACE YOUR KEY";
    public static final String GET_PHOTO_METHOD = "flickr.photos.getRecent";
    public static final String RESPONSE_FORMAT = "json";
    public static final String BASE_URL = "https://www.flickr.com/services/";
    public static final int PER_PAGE_LIMIT = 25;
    public static final int STARTING_OFFSET = 1;

    public static final String HEADER_CACHE_CONTROL = "Cache-Control";
    public static final String HEADER_PRAGMA = "Pragma";

    //image url https://farm8.staticflickr.com/7884/32645738857_9333009077.jpg
}
