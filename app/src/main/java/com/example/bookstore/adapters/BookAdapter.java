package com.example.bookstore.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore.Model.Volume;
import com.example.bookstore.R;
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
        }

        if (volume.getVolumeInfo().getAuthors() != null) {
            String authors = TextUtils.join(", ", volume.getVolumeInfo().getAuthors());
            //String authors = String.join(", ", volume.getVolumeInfo().getAuthors());
            holder.authorsTV.setText(authors);
        }
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
