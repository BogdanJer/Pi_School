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
import com.example.pi_week_2.Photo;
import com.example.pi_week_2.R;
import com.example.pi_week_2.RemovableAdapter;
import com.example.pi_week_2.db.flickr.FlickrDAO;
import com.example.pi_week_2.holder.FavoriteHolder;
import com.example.pi_week_2.model.MainActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteHolder> implements RemovableAdapter {
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

    @Override
    public void removeItem(RecyclerView.ViewHolder holder) {
        if (RecyclerView.NO_POSITION == holder.getAdapterPosition())
            return;

        int position = holder.getAdapterPosition();

        Photo photo = linksTags.get(position);
        linksTags.remove(position);
        notifyItemRemoved(position);

        FavoriteHolder favoriteHolder = (FavoriteHolder) holder;

        FlickrDAO.getDao(context).deletePhoto(MainActivity.name, favoriteHolder.getLink());

        Snackbar.make(favoriteHolder.itemView, String.format("%s " + context.getString(R.string.photo_is_deleted), favoriteHolder.getTag()),
                Snackbar.LENGTH_LONG).setAction(R.string.cancel_deleting, (v) -> {
            linksTags.add(photo);
            notifyItemInserted(position);
            FlickrDAO.getDao(context).insertPhoto(MainActivity.name, favoriteHolder.getTag(), favoriteHolder.getLink());
        }).show();
    }

}