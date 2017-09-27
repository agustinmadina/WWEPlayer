package com.wwe.madina.wweplayer.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.wwe.madina.wweplayer.R;
import com.wwe.madina.wweplayer.network.VideoApiService;
import com.wwe.madina.wweplayer.models.Video;
import com.wwe.madina.wweplayer.models.VideoListResponse;
import com.wwe.madina.wweplayer.utils.ExoPlayerVideoHandler;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static Retrofit retrofit = null;

    private SimpleExoPlayerView playerView;
    private SimpleExoPlayerView playerView2;
    private List<Video> videoList;
    ExoPlayerVideoHandler exoPlayerVideoHandler;
    ExoPlayerVideoHandler exoPlayerVideoHandler2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getVideoList();
        playerView = (SimpleExoPlayerView) findViewById(R.id.video_view);
        playerView2 = (SimpleExoPlayerView) findViewById(R.id.video_view2);
        playerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FullScreenVideoActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getVideoList() {
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
                    videoList = response.body().getVideos();
                }
            }

            @Override
            public void onFailure(Call<VideoListResponse> call, Throwable t) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        exoPlayerVideoHandler = new ExoPlayerVideoHandler(this, Uri.parse("http://vod.wwe.com/vod/2017/,1080,720,540,432,360,288,/raw1270_mainevent_09252017.m3u8"), playerView);
        exoPlayerVideoHandler2 = new ExoPlayerVideoHandler(this, Uri.parse("http://vod.wwe.com/vod/2017/,1080,720,540,432,360,288,/raw1270_finn_goldust_092517.m3u8"), playerView2);
        exoPlayerVideoHandler.goToForeground();
        exoPlayerVideoHandler2.goToForeground();
    }

    @Override
    public void onPause() {
        super.onPause();
        exoPlayerVideoHandler.goToBackground();
        exoPlayerVideoHandler2.goToBackground();
    }
}
