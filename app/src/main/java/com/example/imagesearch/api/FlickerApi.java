package com.example.imagesearch.api;

import com.example.imagesearch.api.model.ApiResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FlickerApi {

    //https://www.flickr.com/services/rest?method=flickr.photos.getRecent&api_key=d7ef663da10506e9f372ed4ee57bfb83&format=json&text=avenger&page=1&per_page=10
    @GET("rest?")
    Single<ApiResponse> getImages(@Query("method") String method,
                                  @Query("api_key") String apiKey,
                                  @Query("format") String format,
                                  @Query("text") String keyword,
                                  @Query("page") int pageNo,
                                  @Query("per_page") int pageLimit,
                                  @Query("nojsoncallback") String callback);
}
