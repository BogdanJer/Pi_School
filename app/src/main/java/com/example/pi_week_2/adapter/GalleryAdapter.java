package com.example.pi_week_2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pi_week_2.R;
import com.example.pi_week_2.RemovableAdapter;
import com.example.pi_week_2.db.flickr.Image;
import com.example.pi_week_2.holder.GalleryHolder;
import com.example.pi_week_2.storage.ExternalStorage;
import com.example.pi_week_2.storage.InternalStorage;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryHolder> implements RemovableAdapter {
    private Context context;
    private List<Image> imageList;
    private InternalStorage storage;
    private ExternalStorage externalStorage;

    public GalleryAdapter(List<Image> images, Context context) {
        imageList = images;
        this.context = context;
        storage = new InternalStorage(context);
        externalStorage = new ExternalStorage(context);
    }

    @NonNull
    @Override
    public GalleryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        return new GalleryHolder(inflater.inflate(R.layout.my_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryHolder holder, int position) {
        holder.bind(imageList.get(position));
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    @Override
    public void removeItem(RecyclerView.ViewHolder holder) {
        int position = holder.getAdapterPosition();

        notifyItemRemoved(position);
        imageList.remove(position);

        GalleryHolder galleryHolder = (GalleryHolder) holder;

        Snackbar.make(holder.itemView, "Photo is removed!", Snackbar.LENGTH_LONG).setAction(R.string.cancel_deleting, v -> {
            imageList.add(position, galleryHolder.getImage());
            notifyItemInserted(position);
        }).addCallback(new Snackbar.Callback() {
            @Override
            public void onShown(Snackbar sb) {
                super.onShown(sb);
            }

            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                System.out.println("DISMISSED");
                if (!storage.deletePhoto(galleryHolder.getImage().getPath()))
                    externalStorage.deletePhoto(galleryHolder.getImage().getPath());
            }
        }).show();
    }
}
