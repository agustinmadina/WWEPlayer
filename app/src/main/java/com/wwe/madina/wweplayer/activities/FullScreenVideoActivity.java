package com.wwe.madina.wweplayer.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.util.Util;
import com.wwe.madina.wweplayer.R;
import com.wwe.madina.wweplayer.utils.ExoPlayerVideoHandler;

import static com.wwe.madina.wweplayer.utils.Constants.DOCKED_VIDEO_URL;
import static com.wwe.madina.wweplayer.utils.Constants.FULLSCREEN_VIDEO_URL;
import static com.wwe.madina.wweplayer.utils.Constants.VIDEO_CURRENT_POSITION;

/**
 * Activity for displaying previously selected video in full screen mode
 *
 * @author Madina on 27/9/2017.
 */
public class FullScreenVideoActivity extends AppCompatActivity {

    private SimpleExoPlayerView playerView;
    private ExoPlayerVideoHandler exoPlayerVideoHandler;
    private String videoUrl;
    private long currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_video);
        ImageButton dockButton = (ImageButton) findViewById(R.id.dock_button);
        playerView = (SimpleExoPlayerView) findViewById(R.id.fullscreen_video_view);
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        videoUrl = getIntent().getStringExtra(FULLSCREEN_VIDEO_URL);
        currentPosition = getIntent().getLongExtra(VIDEO_CURRENT_POSITION, 0);
        dockButton.setOnClickListener(dockVideoListener(videoUrl));
    }

    private void initializePlayer() {
        exoPlayerVideoHandler = new ExoPlayerVideoHandler(this, Uri.parse(videoUrl), playerView);
        exoPlayerVideoHandler.setVolumeOn();
        exoPlayerVideoHandler.seekTo(currentPosition);
    }

    /**
     * Listener that returns videoUrl to HomeScreenActivity in order to play video in docked mode.
     */
    private View.OnClickListener dockVideoListener(final String videoUrl) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dockVideoIntent = new Intent();
                dockVideoIntent.putExtra(DOCKED_VIDEO_URL, videoUrl);
                dockVideoIntent.putExtra(VIDEO_CURRENT_POSITION, exoPlayerVideoHandler.getCurentPosition());
                setResult(Activity.RESULT_OK, dockVideoIntent);
                finish();
            }
        };
    }

    /**
     * When going to foreground it initializes de player, and if it was stopped due to going to background, it resumes it from where it was left.
     */
    @Override
    public void onResume() {
        super.onResume();
        initializePlayer();
    }

    /**
     * When going to background, releases exoPlayer instances in order to save device memory and avoid performance problems but saves currentPosition
     * in order to resume video when going to foreground again.
     */
    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            currentPosition = exoPlayerVideoHandler.getCurentPosition();
            exoPlayerVideoHandler.goToBackground();
        }
    }

    /**
     * When going to background, releases exoPlayer instances in order to save device memory and avoid performance problems but saves currentPosition
     * in order to resume video when going to foreground again.
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            currentPosition = exoPlayerVideoHandler.getCurentPosition();
            exoPlayerVideoHandler.goToBackground();
        }
    }
}
