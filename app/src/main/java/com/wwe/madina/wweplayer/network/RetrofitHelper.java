package com.wwe.madina.wweplayer.network;

import android.util.Log;

import com.wwe.madina.wweplayer.models.VideoListResponse;
import com.wwe.madina.wweplayer.network.RetrofitHandler;
import com.wwe.madina.wweplayer.network.VideoApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.google.android.exoplayer2.ExoPlayerLibraryInfo.TAG;

/**
 * Created by Madina on 29/9/2017.
 */

public final class RetrofitHelper {

    private static Retrofit retrofit = null;

    private RetrofitHelper() {
    }

    public static void getVideos(final RetrofitHandler handler) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(VideoApiService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        VideoApiService service = retrofit.create(VideoApiService.class);
        Call<VideoListResponse> call = service.getVideosResponse();
        call.enqueue(new Callback<VideoListResponse>() {
            @Override
            public void onResponse(Call<VideoListResponse> call, Response<VideoListResponse> response) {
                if (response.body().getVideos() != null) {
                    handler.setVideos(response.body().getVideos());
                }
            }

            @Override
            public void onFailure(Call<VideoListResponse> call, Throwable t) {
                handler.errorLoadingVideos();
                Log.e(TAG, t.toString());
            }
        });
    }
}
