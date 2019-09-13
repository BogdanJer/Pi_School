package com.example.pi_week_2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pi_week_2.R;
import com.example.pi_week_2.RemovableAdapter;
import com.example.pi_week_2.holder.PhotoHolder;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> implements RemovableAdapter {
    private List<String> links;

    private Context context;
    private int size = 0;
    private String tag;

    public PhotoAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cardview, parent, false);

        return new PhotoHolder(viewItem, context);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
        Glide.with(context)
                .load(links.get(position))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.photoView);

        holder.bind(tag, links.get(position));
    }

    @Override
    public int getItemCount() {
        return size;
    }

    public void setLinks(List<String> links, String tag) {
        this.links = links;
        this.tag = tag;
        size = links.size();

        notifyItemRangeInserted(0, size);
    }

    @Override
    public void removeItem(RecyclerView.ViewHolder holder) {
        int position = holder.getAdapterPosition();

        links.remove(position);
        notifyItemRemoved(position);

        PhotoHolder photoHolder = (PhotoHolder) holder;

        Snackbar.make(photoHolder.itemView, String.format("%s " + context.getString(R.string.photo_is_deleted), photoHolder.getTag()),
                Snackbar.LENGTH_LONG).setAction(R.string.cancel_deleting, (v) -> {
            notifyItemInserted(position);
            links.add(position, photoHolder.getLink());
        }).show();
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}