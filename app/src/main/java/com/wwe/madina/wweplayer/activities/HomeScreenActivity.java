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
import com.wwe.madina.wweplayer.models.Video;
import com.wwe.madina.wweplayer.network.RetrofitHandler;
import com.wwe.madina.wweplayer.network.RetrofitHelper;

import java.util.ArrayList;
import java.util.List;

public class HomeScreenActivity extends AppCompatActivity implements RetrofitHandler{

    private static final String TAG = HomeScreenActivity.class.getSimpleName();
    private VideosAdapter videosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        RetrofitHelper.getVideos(this);
        setupToolbar();
    }

    @Override
    public void setVideos(List<Video> videos) {
        videosAdapter = new VideosAdapter(videos);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(videosAdapter);
        videosAdapter.notifyDataSetChanged();

        Log.d(TAG, getString(R.string.home_screen_videos_received_tag) + videos.size());
    }

    @Override
    public void errorLoadingVideos() {
        Toast.makeText(this, R.string.home_screen_videos_not_loaded, Toast.LENGTH_LONG).show();
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
            videosAdapter.releasePlayers();
        }
    }
}
