package com.example.bookstore.Repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bookstore.R;
import com.example.bookstore.apis.BookApi;
import com.example.bookstore.Model.VolumesResponse;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookRepository {
    private static final String BASE_URL = "https://www.googleapis.com/";

    private BookApi bookApi;
    private MutableLiveData<VolumesResponse> volumesResponseMutableLiveData;

    boolean nextClickFavourite = true;

    public BookRepository(){
        volumesResponseMutableLiveData = new MutableLiveData<>();


        OkHttpClient client = new OkHttpClient.Builder().build();

        bookApi = new retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(BookApi.class);
    }

    public void searchVolumes(String query, String maxResults, String startIndex) {
        bookApi.searchVolumes(query, maxResults, startIndex)
                .enqueue(new Callback<VolumesResponse>() {
                    @Override
                    public void onResponse(Call<VolumesResponse> call, Response<VolumesResponse> response) {
                        if (response.body() != null) {
                            volumesResponseMutableLiveData.postValue(response.body());
                            Log.d("BookRepository", "onResponse");
                        }
                    }

                    @Override
                    public void onFailure(Call<VolumesResponse> call, Throwable t) {
                        volumesResponseMutableLiveData.postValue(null);
                    }
                });
    }

    public LiveData<VolumesResponse> getVolumesResponseLiveData() {
        return volumesResponseMutableLiveData;
    }

    public int getFavouriteStarColour(Context context, String id) {

        SharedPreferences preferences = context.getSharedPreferences("BookStore", Context.MODE_PRIVATE);

        if(!preferences.contains("Book-" + id)){
            return R.color.black;
        }else{
            if(preferences.getBoolean("Book-" + id, false)){
                nextClickFavourite = false;
                return R.color.yellow;
            }else{
                nextClickFavourite = true;
                return R.color.black;
            }
        }
    }

    public int clickFavourite(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences("BookStore", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        if(nextClickFavourite){
            editor.putBoolean("Book-" + id, true);
            nextClickFavourite = false;
            editor.apply();
            return R.color.yellow;
        }else{
            editor.putBoolean("Book-" + id, false);
            nextClickFavourite = true;
            editor.apply();
            return R.color.black;
        }
    }
}
