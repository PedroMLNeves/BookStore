package com.example.bookstore.adapters;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore.Model.Volume;
import com.example.bookstore.R;
import com.example.bookstore.Views.BookSearchFragmentDirections;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private List<Volume> bookInfo = new ArrayList<>();
    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_rv_item, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Volume volume = bookInfo.get(position);
        holder.nameTV.setText(volume.getVolumeInfo().getTitle());
        holder.publisherTV.setText(volume.getVolumeInfo().getPublisher());
        holder.pageCountTV.setText("No of Pages : " + volume.getVolumeInfo().getPageCount());
        holder.dateTV.setText(volume.getVolumeInfo().getPublishedDate());
        if (volume.getVolumeInfo().getImageLinks() != null) {
            Picasso.get().load(volume.getVolumeInfo().getImageLinks().getThumbnail()
                    .replace("http://", "https://")).into(holder.bookIV);
        }else{
            holder.bookIV.setImageDrawable(null);
        }

        if (volume.getVolumeInfo().getAuthors() != null) {
            String authors = TextUtils.join(", ", volume.getVolumeInfo().getAuthors());
            //String authors = String.join(", ", volume.getVolumeInfo().getAuthors());
            holder.authorsTV.setText(authors);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookSearchFragmentDirections.ActionBookSearchFragmentToBookDetailsFragment2 action = BookSearchFragmentDirections.actionBookSearchFragmentToBookDetailsFragment2();
                action.setTitle(volume.getVolumeInfo().getTitle());
                action.setId(volume.getId());


                if (volume.getSaleInfo().getBuyLink() != null) {
                    action.setBuylink(volume.getSaleInfo().getBuyLink());
                }
                if (volume.getVolumeInfo().getDescription() != null) {
                    action.setDescription(volume.getVolumeInfo().getDescription());
                }

                if (volume.getVolumeInfo().getImageLinks() != null) {
                    action.setThumbnail(volume.getVolumeInfo().getImageLinks().getThumbnail());
                }
                if (volume.getVolumeInfo().getAuthors() != null) {
                    String authors = TextUtils.join(", ", volume.getVolumeInfo().getAuthors());
                    action.setAuthors(authors);
                }


                //Navigation.findNavController(view).navigate(R.id.action_bookSearchFragment_to_bookDetailsFragment2);
                Navigation.findNavController(view).navigate(action);
            }
        });
    }

    public void setItems(List<Volume> bookInfoList) {
        this.bookInfo = bookInfoList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return bookInfo.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        TextView nameTV, publisherTV, authorsTV, pageCountTV, dateTV;
        ImageView bookIV;

        public BookViewHolder(View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.idTVBookTitle);
            publisherTV = itemView.findViewById(R.id.idTVpublisher);
            authorsTV = itemView.findViewById(R.id.idTVAuthors);
            pageCountTV = itemView.findViewById(R.id.idTVPageCount);
            dateTV = itemView.findViewById(R.id.idTVDate);
            bookIV = itemView.findViewById(R.id.idIVbook);
        }
    }
}
