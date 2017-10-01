package com.wwe.madina.wweplayer;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.LayoutInflater;
import android.view.View;

import com.wwe.madina.wweplayer.activities.HomeScreenActivity;
import com.wwe.madina.wweplayer.adapters.VideosAdapter;
import com.wwe.madina.wweplayer.models.Video;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Madina on 30/9/2017.
 */
@RunWith(AndroidJUnit4.class)
public class VideosAdapterTest {

    private Video video;
    private VideosAdapter adapter;
    VideosAdapter.VideoViewHolder videoViewHolder;

    @Rule
    public ActivityTestRule<HomeScreenActivity> homeScreenActivityRule  = new  ActivityTestRule<>(HomeScreenActivity.class);

    @Before
    public void setUp() throws Exception {
        HomeScreenActivity homeScreenActivity = homeScreenActivityRule.getActivity();
        video = new Video();
        List<Video> videoList = new ArrayList<>();
        videoList.add(video);
        adapter = new VideosAdapter(videoList);
        View itemView =  LayoutInflater.from(homeScreenActivity).inflate(R.layout.video_list_row, null, false);
        videoViewHolder = new VideosAdapter.VideoViewHolder(itemView);
    }

    @Test
    public void itShouldLikeOnThumbsUpClick() throws Exception {
        videoViewHolder.getThumbUpButton().setOnClickListener(adapter.thumbUpListener(videoViewHolder, video));

        videoViewHolder.getThumbUpButton().performClick();

        assertTrue(video.isLikedVideo());
    }

    @Test
    public void itShouldRemoveLikeIfWasPreviouslyLikedOnThumbsUpClick() throws Exception {
        video.setLikedVideo(true);
        videoViewHolder.getThumbUpButton().setOnClickListener(adapter.thumbUpListener(videoViewHolder, video));

        videoViewHolder.getThumbUpButton().performClick();

        assertFalse(video.isLikedVideo());
    }

    @Test
    public void itShouldRemoveDislikeOnThumbsUpClick() throws Exception {
        video.setDislikedVideo(true);
        videoViewHolder.getThumbUpButton().setOnClickListener(adapter.thumbUpListener(videoViewHolder, video));

        videoViewHolder.getThumbUpButton().performClick();

        assertFalse(video.isDislikedVideo());
    }

    @Test
    public void itShouldDislikeOnThumbsDownClick() throws Exception {
        videoViewHolder.getThumbDownButton().setOnClickListener(adapter.thumbUpListener(videoViewHolder, video));

        videoViewHolder.getThumbDownButton().performClick();

        assertTrue(video.isLikedVideo());
    }

    @Test
    public void itShouldRemoveDislikeIfWasPreviouslyDislikedOnThumbsDownClick() throws Exception {
        video.setDislikedVideo(true);
        videoViewHolder.getThumbDownButton().setOnClickListener(adapter.thumbUpListener(videoViewHolder, video));

        videoViewHolder.getThumbDownButton().performClick();

        assertFalse(video.isDislikedVideo());
    }

    @Test
    public void itShouldRemoveLikeOnThumbsDownClick() throws Exception {
        video.setLikedVideo(true);
        videoViewHolder.getThumbDownButton().setOnClickListener(adapter.thumbUpListener(videoViewHolder, video));

        videoViewHolder.getThumbDownButton().performClick();

        assertFalse(video.isLikedVideo());
    }
}