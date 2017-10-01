package com.wwe.madina.wweplayer.network;

import com.wwe.madina.wweplayer.models.Video;

import java.util.List;

/**
 * @author Madina on 29/9/2017.
 */

public interface RetrofitHandler {

    void setVideos(List<Video> videos);

    void errorLoadingVideos();

}
