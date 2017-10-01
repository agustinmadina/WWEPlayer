package com.wwe.madina.wweplayer.network;

import com.wwe.madina.wweplayer.models.VideoListResponse;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author  Madina on 26/9/2017.
 */
public interface VideoApiService {

    String BASE_URL = "http://www.wwe.com/";

    @GET("feeds/sapphire/videos/all/all/0,20")
    Call<VideoListResponse> getVideosResponse();
}
