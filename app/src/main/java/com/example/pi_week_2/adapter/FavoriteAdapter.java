package com.example.pi_week_2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pi_week_2.MainActivity;
import com.example.pi_week_2.Photo;
import com.example.pi_week_2.R;
import com.example.pi_week_2.db.flickr.FlickrDAO;
import com.example.pi_week_2.holder.FavoriteHolder;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteHolder> {
    private Context context;
    private List<Photo> linksTags;
    private String title = null;

    public FavoriteAdapter(Context context, List<Photo> linksTags) {
        this.context = context;
        this.linksTags = linksTags;
    }

    @NonNull
    @Override
    public FavoriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite_cardview, parent, false);
        return new FavoriteHolder(viewItem, this);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteHolder holder, int position) {
        Glide.with(context)
                .load(linksTags.get(position).getLink())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.photo);

        LinearLayout titleLayout = holder.title;

        String tag = linksTags.get(position).getTag();

        if (title != null && title.compareTo(tag) == 0)
            titleLayout.setVisibility(View.GONE);
        else
            ((TextView) titleLayout.findViewById(R.id.favorite_title_view)).setText(tag);

        title = tag;
        holder.bind(linksTags.get(position).getTag(), linksTags.get(position).getLink());
    }

    @Override
    public int getItemCount() {
        return linksTags.size();
    }


    public void removeItem(FavoriteHolder favorite) {
        if (RecyclerView.NO_POSITION == favorite.getAdapterPosition())
            return;

        int position = favorite.getAdapterPosition();

        Photo photo = linksTags.get(position);
        linksTags.remove(position);
        notifyItemRemoved(position);

        FlickrDAO.getDao(context).deletePhoto(MainActivity.name, favorite.getLink());

        Snackbar.make(favorite.itemView, String.format("%s " + context.getString(R.string.photo_is_deleted), favorite.getTag()),
                Snackbar.LENGTH_LONG).setAction(R.string.cancel_deleting, (v) -> {
            linksTags.add(photo);
            notifyItemInserted(position);
            FlickrDAO.getDao(context).insertPhoto(MainActivity.name, favorite.getTag(), favorite.getLink());
        }).show();
    }

}