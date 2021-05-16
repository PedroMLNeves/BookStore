package com.example.bookstore.Repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bookstore.Model.Volume;
import com.example.bookstore.R;
import com.example.bookstore.apis.BookApi;
import com.example.bookstore.Model.VolumesResponse;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookRepository {
    private static final String BASE_URL = "https://www.googleapis.com/";

    private BookApi bookApi;
    private MutableLiveData<VolumesResponse> volumesResponseMutableLiveData;

    private boolean nextClickFavourite = true;

    private SharedPreferences preferences;

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

    //performs the initial request that will return 10 results
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
    private boolean loadingData = false;

    //Makes extra request when the users scrolls through the list. Each request returns 10 extra books
    public void performSearchExtra(String query, LinearLayoutManager linearLayoutManager) {

        int currentMaxIndex = volumesResponseMutableLiveData.getValue().getItems().size();
        List<Volume> allBooks = volumesResponseMutableLiveData.getValue().getItems();
        VolumesResponse volumesResponse = new VolumesResponse();

        if(linearLayoutManager.findLastVisibleItemPosition() == currentMaxIndex - 7 && !loadingData){
            loadingData = true;
            bookApi.searchVolumes(query, "" + 10, "" + currentMaxIndex)
                    .enqueue(new Callback<VolumesResponse>() {
                        @Override
                        public void onResponse(Call<VolumesResponse> call, Response<VolumesResponse> response) {
                            if (response.body() != null && response.body().getItems()!= null) {
                                Log.d("BookRepo","" + allBooks.size());
                                allBooks.addAll(response.body().getItems());
                                volumesResponse.setItems(allBooks);
                                volumesResponseMutableLiveData.postValue(volumesResponse);
                                loadingData = false;
                            }
                        }

                        @Override
                        public void onFailure(Call<VolumesResponse> call, Throwable t) {
                            volumesResponseMutableLiveData.postValue(null);
                        }
                    });
        }


    }

    public LiveData<VolumesResponse> getVolumesResponseLiveData() {
        return volumesResponseMutableLiveData;
    }

    //Returns the color yellow if the user favourited a book
    //Returns the color black if the used has never favourited or if he unfavourited the book
    public int getFavouriteStarColour(Context context, String id) {

        preferences = context.getSharedPreferences("BookStore", Context.MODE_PRIVATE);

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

    //changes from favourited to unfavourited and vice versa and returns the corresponding colour
    public int clickFavourite(Context context, String id) {
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

    private List<Volume> favouriteBooks;
    private List<Volume> allBooks;
    private boolean showingFavourites = false;

    //Makes the change from the list of all books to the list of favourited books and vice versa
    public int changeList(Context context){
        if(!showingFavourites){
            preferences = context.getSharedPreferences("BookStore", Context.MODE_PRIVATE);

            favouriteBooks = new ArrayList<>();

            if(volumesResponseMutableLiveData.getValue()!= null){
                allBooks = volumesResponseMutableLiveData.getValue().getItems();


                for(int i=0;i<volumesResponseMutableLiveData.getValue().getItems().size();i++){
                    String id = volumesResponseMutableLiveData.getValue().getItems().get(i).getId();


                    if(preferences.contains("Book-" + id)){
                        if(preferences.getBoolean("Book-" + id, false)){
                            favouriteBooks.add(volumesResponseMutableLiveData.getValue().getItems().get(i));
                        }
                    }
                }
                VolumesResponse volumesResponse = new VolumesResponse();

                volumesResponse.setItems(favouriteBooks);

                volumesResponseMutableLiveData.postValue(volumesResponse);
                showingFavourites = true;
                return R.color.yellow;

            }else{
                return R.color.black;
            }

        }else{
            VolumesResponse volumesResponse = new VolumesResponse();

            volumesResponse.setItems(allBooks);
            volumesResponseMutableLiveData.postValue(volumesResponse);
            showingFavourites = false;
            return R.color.black;
        }


    }
}
