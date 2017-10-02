package com.wwe.madina.wweplayer.utils;

import android.content.Context;
import android.net.Uri;

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
import com.wwe.madina.wweplayer.R;


/**
 * Helper class that created instances of ExoPlayer and attaches them to SimpleExoPlayer View that was passed as parameter.
 *
 * @author Madina on 27/9/2017.
 */

public class ExoPlayerVideoHandler {

    private SimpleExoPlayer player;

    public ExoPlayerVideoHandler(Context context, Uri uri, SimpleExoPlayerView playerView) {
        if (context != null && uri != null && playerView != null) {
            playerView.requestFocus();
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            DataSource.Factory mediaDataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context, context.getString(R.string.app_name)), (TransferListener<? super DataSource>) bandwidthMeter);
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

    /**
     * Set volumes on for Full Screen and Docked Video modes, since videos are muted by default
     */
    public void setVolumeOn() {
        player.setVolume(100f);
    }

    /**
     * Seeks video to where it was left when going to Background or Dock feature selected
     */
    public void seekTo(long currentPosition) {
        player.seekTo(currentPosition);
    }

    /**
     * @return video current position
     */
    public long getCurentPosition() {
        return player.getCurrentPosition();
    }

    /**
     * Releases exoPlayer in order to save device memory and avoid performance problems.
     */
    public void goToBackground() {
        if (player != null) {
            player.stop();
            player.release();
        }
    }
}