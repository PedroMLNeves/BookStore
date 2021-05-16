package com.example.bookstore.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VolumeSaleInfo {
    @SerializedName("buyLink")
    @Expose
    private String buyLink;

    public String getBuyLink() {
        return buyLink;
    }
}
