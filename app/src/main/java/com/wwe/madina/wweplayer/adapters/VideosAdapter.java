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
import com.wwe.madina.wweplayer.activities.HomeScreenActivity;
import com.wwe.madina.wweplayer.models.Video;
import com.wwe.madina.wweplayer.utils.ExoPlayerVideoHandler;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.wwe.madina.wweplayer.utils.Constants.DOCKED_VIDEO_RESULT;
import static com.wwe.madina.wweplayer.utils.Constants.FULLSCREEN_VIDEO_URL;
import static com.wwe.madina.wweplayer.utils.Constants.HTTP_PREFIX;

/**
 * Adapter used for displaying videos viewholder, with logic to release player when view is not seen anymore in order to free memory from the device
 * and avoid performance problems.
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

    private void populateViews(final Video video, final VideoViewHolder holder) {
        holder.title.setText(video.getTitle() != null ? video.getTitle() : context.getString(R.string.title_not_available));
        holder.date.setText(video.getDate() != 0 ? DateFormat.getDateInstance(DateFormat.MEDIUM).format(video.getDate() * 1000L) : "");
        holder.duration.setText(video.getDuration() != 0 ? DateUtils.formatElapsedTime(video.getDuration()) : "");
        setUpLikeDislikeButtonsState(holder, video);
        holder.thumbUpButton.setOnClickListener(thumbUpListener(holder, video));
        holder.thumbDownButton.setOnClickListener(thumbDownListener(holder, video));
        holder.container.setOnClickListener(fullScreenVideoListener(video));
    }

    /**
     * Restores previous state of Like and Dislike buttons
     */
    private void setUpLikeDislikeButtonsState(VideoViewHolder holder, Video video) {
        if (video.isLikedVideo()) {
            holder.thumbUpButton.setImageResource(R.drawable.ic_thumb_up_pressed);
        } else {
            holder.thumbUpButton.setImageResource(R.drawable.ic_thumb_up);
        }

        if (video.isDislikedVideo()) {
            holder.thumbDownButton.setImageResource(R.drawable.ic_thumb_down_pressed);
        } else {
            holder.thumbDownButton.setImageResource(R.drawable.ic_thumb_down);
        }
    }

    /**
     * Listener that checks if video was previously liked or disliked in order to change icons and update state.
     */
    public View.OnClickListener thumbUpListener(final VideoViewHolder holder, final Video video) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!video.isLikedVideo()) {
                    holder.thumbUpButton.setImageResource(R.drawable.ic_thumb_up_pressed);
                    video.setLikedVideo(true);
                    if (video.isDislikedVideo()) {
                        holder.thumbDownButton.setImageResource(R.drawable.ic_thumb_down);
                        video.setDislikedVideo(false);
                    }
                } else {
                    holder.thumbUpButton.setImageResource(R.drawable.ic_thumb_up);
                    video.setLikedVideo(false);
                }
            }
        };
    }

    /**
     * Listener that checks if video was previously liked or disliked in order to change icons and update state.
     */
    public View.OnClickListener thumbDownListener(final VideoViewHolder holder, final Video video) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!video.isDislikedVideo()) {
                    holder.thumbDownButton.setImageResource(R.drawable.ic_thumb_down_pressed);
                    video.setDislikedVideo(true);
                    if (video.isLikedVideo()) {
                        holder.thumbUpButton.setImageResource(R.drawable.ic_thumb_up);
                        video.setLikedVideo(false);
                    }
                } else {
                    holder.thumbDownButton.setImageResource(R.drawable.ic_thumb_down);
                    video.setDislikedVideo(false);
                }
            }
        };
    }

    /**
     * Listener that triggers a new a activity with selected video in full screen.
     */
    private View.OnClickListener fullScreenVideoListener(final Video video) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (video.getPlaybackUrl() != null) {
                    Intent fullScreenIntent = new Intent(context, FullScreenVideoActivity.class);
                    String videoUrl = HTTP_PREFIX + video.getPlaybackUrl();
                    fullScreenIntent.putExtra(FULLSCREEN_VIDEO_URL, videoUrl);
                    ((HomeScreenActivity) context).startActivityForResult(fullScreenIntent, DOCKED_VIDEO_RESULT);
                }
            }
        };
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

    /**
     * When viewHolder is seen, sets up exoPlayer with corresponding video.
     */
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

    /**
     * When viewHolder is not seen anymore, releases exoPlayer in order to save device memory and avoid performance problems.
     */
    @Override
    public void onViewDetachedFromWindow(VideoViewHolder holder) {
        if (holder.playerView.getPlayer() != null) {
            holder.playerView.getPlayer().stop();
            holder.playerView.getPlayer().release();
        }
        super.onViewDetachedFromWindow(holder);
    }

    /**
     * Releases all video instances to save device memory, this method is called when app is going to background.
     */
    public void releasePlayers() {
        for (ExoPlayerVideoHandler exoPlayerVideoHandler : videoHandlersList) {
            exoPlayerVideoHandler.goToBackground();
        }
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout container;
        private TextView title;
        private TextView duration;
        private TextView date;
        private SimpleExoPlayerView playerView;
        private ImageView thumbUpButton;
        private ImageView thumbDownButton;

        public VideoViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.video_title);
            playerView = view.findViewById(R.id.video_view);
            container = view.findViewById(R.id.viewholder_container);
            date = view.findViewById(R.id.video_date);
            duration = view.findViewById(R.id.video_duration);
            thumbUpButton = view.findViewById(R.id.thumb_up);
            thumbDownButton = view.findViewById(R.id.thumb_down);
        }

        public ImageView getThumbUpButton() {
            return thumbUpButton;
        }

        public ImageView getThumbDownButton() {
            return thumbDownButton;
        }
    }
}
