package com.example.bookstore.Views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore.Model.VolumesResponse;
import com.example.bookstore.R;
import com.example.bookstore.ViewModel.BookDetailsViewModel;
import com.example.bookstore.ViewModel.BookViewModel;
import com.example.bookstore.adapters.BookAdapter;
import com.squareup.picasso.Picasso;

public class BookDetailsFragment extends Fragment {
    private BookDetailsViewModel viewModel;

    private Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        context = this.getActivity();

        viewModel = ViewModelProviders.of(this).get(BookDetailsViewModel.class);
        viewModel.init();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bookdetails_fragment, container, false);

        TextView idTVTitle, idTVauthors,idTVDescription;
        ImageView idIVbook, idBtnFavourite;
        Button idBtnBuy;
        idIVbook = view.findViewById(R.id.idIVbook);
        idTVTitle = view.findViewById(R.id.idTVTitle);
        idTVauthors = view.findViewById(R.id.idTVauthors);
        idTVDescription = view.findViewById(R.id.idTVDescription);
        idBtnBuy = view.findViewById(R.id.idBtnBuy);
        idBtnFavourite = view.findViewById(R.id.idBtnFavourite);

        if(getArguments() != null){
            BookDetailsFragmentArgs args = BookDetailsFragmentArgs.fromBundle(getArguments());

            Picasso.get().load(args.getThumbnail()
                    .replace("http://", "https://")).into(idIVbook);

            idTVTitle.setText(args.getTitle());
            idTVauthors.setText(args.getAuthors());
            idTVDescription.setText(args.getDescription());

            if(!args.getBuylink().equals("default")){
                idBtnBuy.setVisibility(View.VISIBLE);
                idBtnBuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse(args.getBuylink());
                        Intent i = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(i);
                    }
                });
            }else{
                idBtnBuy.setVisibility(View.GONE);
            }
            idBtnFavourite.setColorFilter(getResources().getColor(getFavouriteStarColour(context, args.getId())));

            idBtnFavourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    idBtnFavourite.setColorFilter(getResources().getColor(clickFavourite(context, args.getId())));
                }
            });
        }

        return view;
    }
    //Calls method that returns a color depending if the book is favourite or not
    public int getFavouriteStarColour(Context context, String id) {

        return viewModel.getFavouriteStarColour(context, id);
    }
    //Calls method to change between favourite and not favourite
    public int clickFavourite(Context context, String id) {

        return viewModel.clickFavourite(context, id);
    }
}
