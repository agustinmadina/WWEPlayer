package com.wwe.madina.wweplayer.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Madina on 25/9/2017.
 */
public class Video {

    @Expose
    private int id;
    @Expose
    private String title;
    @Expose
    private String type;
    @Expose
    private int date;
    @Expose
    private int duration;
    @SerializedName("vms_id")
    @Expose
    private int vmsId;
    @SerializedName("media_id")
    @Expose
    private String mediaId;
    @SerializedName("brightcove_id")
    @Expose
    private String brightcoveId;
    @SerializedName("playback_url")
    @Expose
    private String playbackUrl;
    @Expose
    private String thumb;
    @Expose
    private Formats formats;
    @Expose
    private String body;
    @Expose
    private int icon;
    @SerializedName("network_content")
    @Expose
    private int networkContent;
    @Expose
    private String url;
    @Expose
    private List<Tag> tags = null;
    private boolean likedVideo = false;
    private boolean dislikedVideo = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getVmsId() {
        return vmsId;
    }

    public void setVmsId(int vmsId) {
        this.vmsId = vmsId;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getBrightcoveId() {
        return brightcoveId;
    }

    public void setBrightcoveId(String brightcoveId) {
        this.brightcoveId = brightcoveId;
    }

    public String getPlaybackUrl() {
        return playbackUrl;
    }

    public void setPlaybackUrl(String playbackUrl) {
        this.playbackUrl = playbackUrl;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public Formats getFormats() {
        return formats;
    }

    public void setFormats(Formats formats) {
        this.formats = formats;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getNetworkContent() {
        return networkContent;
    }

    public void setNetworkContent(int networkContent) {
        this.networkContent = networkContent;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public boolean isLikedVideo() {
        return likedVideo;
    }

    public void setLikedVideo(boolean likedVideo) {
        this.likedVideo = likedVideo;
    }

    public boolean isDislikedVideo() {
        return dislikedVideo;
    }

    public void setDislikedVideo(boolean dislikedVideo) {
        this.dislikedVideo = dislikedVideo;
    }
}