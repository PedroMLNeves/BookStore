package com.example.bookstore.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VolumesResponse {

    @SerializedName("items")
    @Expose
    List<Volume> items = null;

    public List<Volume> getItems() {
        return items;
    }
}