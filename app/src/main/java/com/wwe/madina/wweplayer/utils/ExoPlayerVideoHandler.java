package com.wwe.madina.wweplayer.utils;

import android.content.Context;
import android.net.Uri;
import android.view.SurfaceView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;


/**
 * Created by Madina on 27/9/2017.
 */

public class ExoPlayerVideoHandler {

    private SimpleExoPlayer player;
    private long playbackPosition;
    private int currentWindow;
    private boolean fromStart;

    public ExoPlayerVideoHandler(Context context, Uri uri, SimpleExoPlayerView playerView) {
        if (context != null && uri != null && playerView != null) {
            playerView.requestFocus();
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            DataSource.Factory mediaDataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context, "mediaPlayerSample"), (TransferListener<? super DataSource>) bandwidthMeter);
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);

            DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);

            playerView.setPlayer(player);
            player.setVolume(0f);
            MediaSource mediaSource = new HlsMediaSource(uri, mediaDataSourceFactory, null, null);
            player.prepare(mediaSource);
            player.setPlayWhenReady(true);
        }
    }

//    public void seekTo(int currentWindow, long playbackPosition) {
//        player.seekTo(currentWindow, playbackPosition);
//    }

    public void goToBackground() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.setPlayWhenReady(false);
            player.stop();
            player.release();
        }
    }

    public void goToForeground() {
        if (player != null) {
            player.setPlayWhenReady(true);
            player.seekTo(currentWindow, playbackPosition);
        }
    }
}