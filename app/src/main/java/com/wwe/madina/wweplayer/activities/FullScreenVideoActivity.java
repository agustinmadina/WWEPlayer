package com.wwe.madina.wweplayer.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.util.Util;
import com.wwe.madina.wweplayer.R;
import com.wwe.madina.wweplayer.utils.ExoPlayerVideoHandler;

import static com.wwe.madina.wweplayer.utils.Constants.DOCKED_VIDEO_URL;
import static com.wwe.madina.wweplayer.utils.Constants.FULLSCREEN_VIDEO_URL;
import static com.wwe.madina.wweplayer.utils.Constants.HTTP_PREFIX;

/**
 * Activity for displaying previously selected video in full screen mode
 *
 * @author Madina on 27/9/2017.
 */
public class FullScreenVideoActivity extends AppCompatActivity {

    SimpleExoPlayerView playerView;
    ExoPlayerVideoHandler exoPlayerVideoHandler;
    String videoUrl;

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
        dockButton.setOnClickListener(dockVideoListener(videoUrl));
    }

    private void initializePlayer() {
        exoPlayerVideoHandler = new ExoPlayerVideoHandler(this, Uri.parse(videoUrl), playerView);
        exoPlayerVideoHandler.setVolumeOn();
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
                setResult(Activity.RESULT_OK, dockVideoIntent);
                finish();
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        initializePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            exoPlayerVideoHandler.goToBackground();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            exoPlayerVideoHandler.goToBackground();
        }
    }
}
