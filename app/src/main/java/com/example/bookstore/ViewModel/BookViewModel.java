package com.example.bookstore.ViewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bookstore.Model.VolumesResponse;
import com.example.bookstore.Repository.BookRepository;

public class BookViewModel extends AndroidViewModel {
    private BookRepository bookRepository;
    private LiveData<VolumesResponse> volumesResponseLiveData;

    public BookViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        bookRepository = new BookRepository();
        volumesResponseLiveData = bookRepository.getVolumesResponseLiveData();
    }
    //Call method that performs the request for books
    public void searchVolumes(String query, String maxResults, String startIndex) {
        bookRepository.searchVolumes(query, maxResults, startIndex);
    }
    //Call method that performs the extra request when user scrolls through the list
    public void performSearchExtra(String query, LinearLayoutManager linearLayoutManager) {
        bookRepository.performSearchExtra(query, linearLayoutManager);
    }
    //returns the VolumeResponse LiveData
    public LiveData<VolumesResponse> getVolumesResponseLiveData() {
        return volumesResponseLiveData;
    }
    //Call method that change the list from the list of books to the list of favourite books and vice versa
    public int changeList(Context context){
        return bookRepository.changeList(context);
    }
}
