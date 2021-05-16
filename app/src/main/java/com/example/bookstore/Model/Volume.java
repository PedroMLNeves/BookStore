package com.example.bookstore.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Volume {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("selfLink")
    @Expose
    private String selfLink;

    @SerializedName("volumeInfo")
    @Expose
    VolumeInfo volumeInfo;

    @SerializedName("saleInfo")
    @Expose
    private VolumeSaleInfo saleInfo;

    public String getId() {
        return id;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public VolumeInfo getVolumeInfo() {
        return volumeInfo;
    }

    public VolumeSaleInfo getSaleInfo() {
        return saleInfo;
    }
}