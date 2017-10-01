package com.wwe.madina.wweplayer.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Madina on 25/9/2017.
 */
public class Formats {

    @SerializedName("flv_rtmp_rtmpe")
    @Expose
    private int flvRtmpRtmpe;
    @SerializedName("h264_mp4_rtmp")
    @Expose
    private int h264Mp4Rtmp;
    @SerializedName("h264_mp4_http")
    @Expose
    private int h264Mp4Http;
    @SerializedName("h264_m2ts_http")
    @Expose
    private int h264M2tsHttp;

    public int getFlvRtmpRtmpe() {
        return flvRtmpRtmpe;
    }

    public void setFlvRtmpRtmpe(int flvRtmpRtmpe) {
        this.flvRtmpRtmpe = flvRtmpRtmpe;
    }

    public int getH264Mp4Rtmp() {
        return h264Mp4Rtmp;
    }

    public void setH264Mp4Rtmp(int h264Mp4Rtmp) {
        this.h264Mp4Rtmp = h264Mp4Rtmp;
    }

    public int getH264Mp4Http() {
        return h264Mp4Http;
    }

    public void setH264Mp4Http(int h264Mp4Http) {
        this.h264Mp4Http = h264Mp4Http;
    }

    public int getH264M2tsHttp() {
        return h264M2tsHttp;
    }

    public void setH264M2tsHttp(int h264M2tsHttp) {
        this.h264M2tsHttp = h264M2tsHttp;
    }

}