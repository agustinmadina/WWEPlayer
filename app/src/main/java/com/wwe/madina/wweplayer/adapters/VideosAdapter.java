package com.wwe.madina.wweplayer.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.squareup.picasso.Picasso;
import com.wwe.madina.wweplayer.R;
import com.wwe.madina.wweplayer.activities.FullScreenVideoActivity;
import com.wwe.madina.wweplayer.activities.MainActivity;
import com.wwe.madina.wweplayer.models.Video;
import com.wwe.madina.wweplayer.network.VideoApiService;
import com.wwe.madina.wweplayer.utils.ExoPlayerVideoHandler;

import java.util.List;

/**
 * Created by Madina on 27/9/2017.
 */

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder> {

    private static final String HTTP_PREFIX = "http:";
    private List<Video> videoList;
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
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        Video video = videoList.get(position);
        holder.title.setText(video.getTitle() != null ? video.getTitle() : context.getString(R.string.title_not_available));
        if (video.getPlaybackUrl() != null) {
            String videoUrl = HTTP_PREFIX + video.getPlaybackUrl();
            ExoPlayerVideoHandler exoPlayerVideoHandler = new ExoPlayerVideoHandler(context, Uri.parse(videoUrl), holder.playerView);
        } else {
            holder.title.setText(context.getString(R.string.video_not_available));
        }
//        if (video.getThumb() != null) {
//            String thumbUrl = VideoApiService.BASE_URL + video.getThumb().replaceAll("\\\\", "");;
//            Picasso.with(context).load(thumbUrl).into(holder.thumbnail);
//        }
//        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, FullScreenVideoActivity.class);
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    @Override
    public void onViewRecycled(VideoViewHolder holder) {
        super.onViewRecycled(holder);
        holder.playerView.getPlayer().stop();
        holder.playerView.getPlayer().release();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        private ImageView thumbnail;
        private TextView title;
        private TextView duration;
        private TextView date;
        private SimpleExoPlayerView playerView;

        public VideoViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.description);
            playerView = view.findViewById(R.id.video_view);
//            thumbnail = view.findViewById(R.id.thumbnail);
        }
    }
}
