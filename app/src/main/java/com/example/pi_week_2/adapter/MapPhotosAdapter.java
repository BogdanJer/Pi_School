package com.example.pi_week_2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pi_week_2.R;
import com.example.pi_week_2.holder.MapPhotosHolder;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Locale;

public class MapPhotosAdapter extends RecyclerView.Adapter<MapPhotosHolder> {
    private List<String> links;
    private double lat;
    private double lon;
    private int size = 0;
    private Context context;

    public MapPhotosAdapter(Context context) {
        this.context = context;
    }

    public void setLinks(List<String> links, double lat, double lon) {
        this.links = links;
        this.lat = lat;
        this.lon = lon;
        size = links.size();

        notifyItemRangeInserted(0, size);

        if (size == 0)
            Toast.makeText(context, "Nothing to show!", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(context, String.format(Locale.getDefault(), "%d photos were found!", size), Toast.LENGTH_LONG).show();
    }

    @NonNull
    @Override
    public MapPhotosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new MapPhotosHolder(inflater.inflate(R.layout.map_photo, parent, false), context);
    }

    @Override
    public void onBindViewHolder(@NonNull MapPhotosHolder holder, int position) {
        Glide.with(context)
                .load(links.get(position))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.getPhoto());

        holder.bind(lat, lon, links.get(position));
    }

    @Override
    public int getItemCount() {
        return size;
    }

    public void removeItem(MapPhotosHolder holder) {
        if (RecyclerView.NO_POSITION == holder.getAdapterPosition())
            return;

        int position = holder.getAdapterPosition();

        links.remove(position);
        notifyItemRemoved(position);

        Snackbar.make(holder.itemView, "Photo was deleted!",
                Snackbar.LENGTH_LONG).setAction(R.string.cancel_deleting, (v) -> {
            notifyItemInserted(position);
            links.add(position, holder.getLink());
        }).show();
    }
}
