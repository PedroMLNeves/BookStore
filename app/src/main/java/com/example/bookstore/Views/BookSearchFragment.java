package com.example.bookstore.Views;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore.adapters.BookAdapter;
import com.example.bookstore.Model.VolumesResponse;
import com.example.bookstore.R;
import com.example.bookstore.ViewModel.BookViewModel;

public class BookSearchFragment extends Fragment {
    private BookViewModel viewModel;
    private BookAdapter adapter;

    private EditText keywordEditText;
    private Button searchButton;
    private ImageView Favourite;

    private Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        adapter =  new BookAdapter();

        context = this.getActivity();

        viewModel = ViewModelProviders.of(this).get(BookViewModel.class);
        viewModel.init();
        viewModel.getVolumesResponseLiveData().observe(this, new Observer<VolumesResponse>() {
            @Override
            public void onChanged(VolumesResponse volumesResponse) {
                if (volumesResponse != null) {
                    Log.d("bookSearchFragment", "onChanged");
                    adapter.setItems(volumesResponse.getItems());
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.booksearch_fragment, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.idRVBooks);
        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            //int initialY = 0;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                performSearchExtra(linearLayoutManager);
            }

        });

        keywordEditText = view.findViewById(R.id.idEdtSearchBooks);
        searchButton = view.findViewById(R.id.idBtnSearch);
        Favourite = view.findViewById(R.id.idBtnFavouriteList);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });

        Favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Favourite.setColorFilter(getResources().getColor(changeList(context)));
            }
        });

        return view;
    }
    //Call method that performs the request for books
    public void performSearch() {
        String keyword = keywordEditText.getEditableText().toString();
        viewModel.searchVolumes(keyword,"10","0");
    }

    //Call method that performs the extra request when user scrolls through the list
    public void performSearchExtra(LinearLayoutManager linearLayoutManager) {
        String keyword = keywordEditText.getEditableText().toString();
        viewModel.performSearchExtra(keyword, linearLayoutManager);
    }

    //Call method that change the list from the list of books to the list of favourite books and vice versa
    public int changeList(Context context){
        return viewModel.changeList(context);
    }
}
