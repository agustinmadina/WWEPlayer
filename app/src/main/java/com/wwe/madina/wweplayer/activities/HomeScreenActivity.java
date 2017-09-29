package com.wwe.madina.wweplayer.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.google.android.exoplayer2.util.Util;
import com.wwe.madina.wweplayer.R;
import com.wwe.madina.wweplayer.adapters.VideosAdapter;
import com.wwe.madina.wweplayer.network.VideoApiService;
import com.wwe.madina.wweplayer.models.Video;
import com.wwe.madina.wweplayer.models.VideoListResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeScreenActivity extends AppCompatActivity {

    private static final String TAG = HomeScreenActivity.class.getSimpleName();
    private static Retrofit retrofit = null;

    private List<Video> videosList = new ArrayList<>();
    private VideosAdapter videosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        setupToolbar();
        getVideoList();
    }

    private void populateRecyclerView() {
        videosAdapter = new VideosAdapter(videosList);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(videosAdapter);
        videosAdapter.notifyDataSetChanged();

        Log.d(TAG, getString(R.string.home_screen_videos_received_tag) + videosList.size());
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (videosAdapter != null) {
            videosAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            videosAdapter.releasePlayers();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            videosAdapter.notifyDataSetChanged();
        }
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
                    videosList = response.body().getVideos();
                    populateRecyclerView();
                }
            }

            @Override
            public void onFailure(Call<VideoListResponse> call, Throwable t) {
                Toast.makeText(getBaseContext(), R.string.home_screen_videos_not_loaded, Toast.LENGTH_LONG).show();
                Log.e(TAG, t.toString());
            }
        });
    }
}
