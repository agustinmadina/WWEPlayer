package com.wwe.madina.wweplayer.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.util.Util;
import com.wwe.madina.wweplayer.R;
import com.wwe.madina.wweplayer.adapters.VideosAdapter;
import com.wwe.madina.wweplayer.models.Video;
import com.wwe.madina.wweplayer.network.RetrofitHandler;
import com.wwe.madina.wweplayer.network.RetrofitHelper;
import com.wwe.madina.wweplayer.utils.ExoPlayerVideoHandler;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.wwe.madina.wweplayer.utils.Constants.DOCKED_VIDEO_RESULT;
import static com.wwe.madina.wweplayer.utils.Constants.DOCKED_VIDEO_URL;
import static com.wwe.madina.wweplayer.utils.Constants.FULLSCREEN_VIDEO_URL;

/**
 * Main activity that displays videos from service response in recycler view, also contains hidden view for displaying docked videos.
 *
 * @author Madina
 */
public class HomeScreenActivity extends AppCompatActivity implements RetrofitHandler {

    private static final String TAG = HomeScreenActivity.class.getSimpleName();
    private VideosAdapter videosAdapter;
    private SimpleExoPlayerView dockedPlayerView;
    private FrameLayout dockedVideoContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        dockedPlayerView = (SimpleExoPlayerView) findViewById(R.id.docked_video_view);
        ImageButton closeDockedVideo = (ImageButton) findViewById(R.id.close_docked_video);
        closeDockedVideo.setOnClickListener(closeDockedVideoListener());
        dockedVideoContainer = (FrameLayout) findViewById(R.id.docked_container);
        RetrofitHelper.getVideos(this);
        setupToolbar();
    }

    /**
     * Method called after service response, creates Adapter with the videoList and sets it to RecyclerView
     */
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

    /**
     * When coming back from FullScreenActivity, if docked mode was previously selected, sets up a new instance of ExoPlayerVideoHandler,
     * attaches it to hidden docked view and makes it visible.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DOCKED_VIDEO_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                String videoUrl = data.getStringExtra(DOCKED_VIDEO_URL);
                ExoPlayerVideoHandler exoPlayerVideoHandler = new ExoPlayerVideoHandler(this, Uri.parse(videoUrl), dockedPlayerView);
                exoPlayerVideoHandler.setVolumeOn();
                dockedVideoContainer.setOnClickListener(dockedVideoToFullScreenListener(this, videoUrl));
                dockedVideoContainer.setVisibility(VISIBLE);
            }
        }
    }

    /**
     * Listener that calls FullScreenActivity if docked video was clicked.
     */
    private View.OnClickListener dockedVideoToFullScreenListener(final Context context, final String videoUrl) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fullScreenIntent = new Intent(context, FullScreenVideoActivity.class);
                fullScreenIntent.putExtra(FULLSCREEN_VIDEO_URL, videoUrl);
                ((HomeScreenActivity) context).startActivityForResult(fullScreenIntent, DOCKED_VIDEO_RESULT);
            }
        };
    }

    /**
     * Listener that closes docked video..
     */
    private View.OnClickListener closeDockedVideoListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dockedPlayerView.getPlayer().stop();
                dockedPlayerView.getPlayer().release();
                dockedVideoContainer.setVisibility(GONE);
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (videosAdapter != null) {
            videosAdapter.notifyDataSetChanged();
        }
    }

    /**
     * When going to background, releases exoPlayer instances in order to save device memory and avoid performance problems.
     */
    @Override
    public void onPause() {
        super.onPause();
        if (videosAdapter != null && Util.SDK_INT <= 23) {
            videosAdapter.releasePlayers();
        }
        if (dockedPlayerView.getPlayer() != null) {
            dockedPlayerView.getPlayer().stop();
            dockedPlayerView.getPlayer().release();
            dockedVideoContainer.setVisibility(GONE);
        }
    }

    /**
     * When going to background, releases exoPlayer instances in order to save device memory and avoid performance problems.
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (videosAdapter != null && Util.SDK_INT > 23) {
            videosAdapter.releasePlayers();

        }
        if (dockedPlayerView.getPlayer() != null) {
            dockedPlayerView.getPlayer().stop();
            dockedPlayerView.getPlayer().release();
            dockedVideoContainer.setVisibility(GONE);
        }
    }
}
