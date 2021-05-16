package com.example.bookstore.ViewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.bookstore.Model.VolumesResponse;
import com.example.bookstore.Repository.BookRepository;

public class BookDetailsViewModel extends AndroidViewModel {
    private BookRepository bookRepository;
    private LiveData<VolumesResponse> volumesResponseLiveData;

    public BookDetailsViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        bookRepository = new BookRepository();
        volumesResponseLiveData = bookRepository.getVolumesResponseLiveData();
    }

    public int getFavouriteStarColour(Context context, String id) {
        return bookRepository.getFavouriteStarColour(context, id);
    }

    public int clickFavourite(Context context, String id) {
        return bookRepository.clickFavourite(context, id);
    }
}
