package com.example.bookstore.apis;

import com.example.bookstore.Model.VolumesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BookApi {
    @GET("/books/v1/volumes")
    Call<VolumesResponse> searchVolumes(
            @Query("q") String query,
            @Query("maxResults") String maxResults,
            @Query("startIndex") String startIndex
    );
}
