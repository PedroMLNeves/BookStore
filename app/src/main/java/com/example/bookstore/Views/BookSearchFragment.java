package com.example.bookstore.Views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        adapter =  new BookAdapter();

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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        keywordEditText = view.findViewById(R.id.idEdtSearchBooks);
        searchButton = view.findViewById(R.id.idBtnSearch);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });

        return view;
    }
    public void performSearch() {//TODO Change This
        String keyword = keywordEditText.getEditableText().toString();

        viewModel.searchVolumes(keyword,"10","0");
    }

}
