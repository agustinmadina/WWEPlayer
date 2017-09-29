package com.wwe.madina.wweplayer.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.wwe.madina.wweplayer.R;
import com.wwe.madina.wweplayer.activities.FullScreenVideoActivity;
import com.wwe.madina.wweplayer.models.Video;
import com.wwe.madina.wweplayer.utils.ExoPlayerVideoHandler;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.wwe.madina.wweplayer.utils.Constants.FULLSCREEN_VIDEO_URL;
import static com.wwe.madina.wweplayer.utils.Constants.HTTP_PREFIX;

/**
 * Created by Madina on 27/9/2017.
 */

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder> {

    private List<Video> videoList;
    private List<ExoPlayerVideoHandler> videoHandlersList = new ArrayList<>();
    private Context context;

    public VideosAdapter(List<Video> videoList) {
        this.videoList = videoList;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list_row, parent, false);
        context = parent.getContext();
        return new VideoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final VideoViewHolder holder, int position) {
        final Video video = videoList.get(position);
        populateViews(video, holder);
    }

    private void populateViews(final Video video, VideoViewHolder holder) {
        holder.title.setText(video.getTitle() != null ? video.getTitle() : context.getString(R.string.title_not_available));
        holder.date.setText(video.getDate() != 0 ? DateFormat.getDateInstance(DateFormat.MEDIUM).format(video.getDate() * 1000L) : "");
        holder.duration.setText(video.getDuration() != 0 ? DateUtils.formatElapsedTime(video.getDuration()) : "");
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (video.getPlaybackUrl() != null) {
                    Intent fullScreenIntent = new Intent(context, FullScreenVideoActivity.class);
                    fullScreenIntent.putExtra(FULLSCREEN_VIDEO_URL, video.getPlaybackUrl());
                    context.startActivity(fullScreenIntent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    @Override
    public void onViewAttachedToWindow(VideoViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        attachVideoToViewHolder(holder);
    }

    private void attachVideoToViewHolder(VideoViewHolder holder) {
        Video video = videoList.get(holder.getAdapterPosition());
        if (video.getPlaybackUrl() != null) {
            String videoUrl = HTTP_PREFIX + video.getPlaybackUrl();
            ExoPlayerVideoHandler exoPlayerVideoHandler = new ExoPlayerVideoHandler(context, Uri.parse(videoUrl), holder.playerView);
            videoHandlersList.add(exoPlayerVideoHandler);
        } else {
            holder.title.setText(context.getString(R.string.video_not_available));
        }
    }

    @Override
    public void onViewDetachedFromWindow(VideoViewHolder holder) {
        Video video = videoList.get(holder.getAdapterPosition());
        if (video.getPlaybackUrl() != null) {
            holder.playerView.getPlayer().stop();
            holder.playerView.getPlayer().release();
        }
        super.onViewDetachedFromWindow(holder);
    }

    public void releasePlayers() {
        for (ExoPlayerVideoHandler exoPlayerVideoHandler : videoHandlersList) {
            exoPlayerVideoHandler.goToBackground();
        }
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout container;
        private ImageView thumbnail;
        private TextView title;
        private TextView duration;
        private TextView date;
        private SimpleExoPlayerView playerView;

        public VideoViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.video_title);
            playerView = view.findViewById(R.id.video_view);
            container = view.findViewById(R.id.viewholder_container);
            date = view.findViewById(R.id.video_date);
            duration = view.findViewById(R.id.video_duration);
        }
    }
}
