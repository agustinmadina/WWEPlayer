package com.wwe.madina.wweplayer.models;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Madina on 25/9/2017.
 */
public class VideoListResponse {

    @Expose
    private List<Video> videos = null;

    public List<Video> getVideos() {
        return videos;
    }

}