package com.example.pi_week_2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pi_week_2.async.LoadPhotosAsync;
import com.example.pi_week_2.db.flickr.FlickrDAO;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class PhotoFavoriteRecyclerView extends RecyclerViewHelper {
    private Context context;
    private RecyclerView recyclerView;
    private String userName;
    private FavoriteAdapter adapter;
    private RecyclerView.LayoutManager manager;
    private ProgressBar progressBar;
    private List<Photo> photos;

    public PhotoFavoriteRecyclerView(String userName, RecyclerView recyclerView, ProgressBar progressBar, List<Photo> photos) {
        this.recyclerView = recyclerView;
        this.userName = userName;
        this.progressBar = progressBar;
        this.photos = photos;
    }

    public void build(Context context) {
        this.context = context;

        manager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(manager);

        adapter = new FavoriteAdapter(photos);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.removeItem((FavoriteHolder) viewHolder);
            }
        };

        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    public void reBuild(RecyclerView.LayoutManager manager) {
        recyclerView.setLayoutManager(manager);
    }

    @Override
    public void getLoadedData(List<Photo> photos, int indexFrom, int indexTo) {
        adapter.notifyItemRangeChanged(indexFrom, indexTo - indexFrom);

        adapter.downloading = false;
        adapter.currentPhoto += indexTo - indexFrom;
    }

    public Context getContext() {
        return context;
    }

    private class FavoriteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView photo;
        private TextView tag;
        private ImageButton deleteBut;

        private Photo p;

        public FavoriteHolder(LayoutInflater inflater, ViewGroup container) {
            super(inflater.inflate(R.layout.item_favorite_cardview, container, false));

            photo = itemView.findViewById(R.id.favorite_photo_card_view);
            tag = itemView.findViewById(R.id.favorite_cardview_text);
            deleteBut = itemView.findViewById(R.id.delete_photo_but);

            deleteBut.setOnClickListener(this);
        }

        public void bind(Photo p) {
            this.p = p;
            photo.setImageBitmap(p.getPhoto());
            tag.setText(p.getTag());
        }

        @Override
        public void onClick(View v) {
            FlickrDAO.getDao(context).deletePhoto(userName, p.getLink());
            adapter.removeItem(this);
        }
    }

    public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteHolder> {
        private List<Photo> photos;
        private int currentPhoto = 0;
        private int perDownload = 10;
        private boolean downloading = false;

        public FavoriteAdapter(List<Photo> photos) {
            this.photos = photos;
            loadData();
        }

        @NonNull
        @Override
        public FavoriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new FavoriteHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull FavoriteHolder holder, int position) {
            holder.bind(photos.get(position));
        }

        @Override
        public int getItemCount() {
            return currentPhoto;
        }

        public void removeItem(FavoriteHolder favorite) {
            int position = favorite.getAdapterPosition();

            photos.remove(position);
            notifyItemRemoved(position);

            Snackbar.make(favorite.itemView, String.format("%s " + context.getString(R.string.photo_is_deleted), favorite.p.getTag()),
                    Snackbar.LENGTH_LONG).setAction("UNDO", (v) -> {
                photos.add(position, favorite.p);
                notifyItemInserted(position);
            }).show();
        }

        public void loadData() {
            downloading = true;

            if (progressBar.getVisibility() == View.VISIBLE)
                progressBar.setVisibility(View.GONE);

            if (photos.size() - perDownload < 0) {
                new LoadPhotosAsync(context, PhotoFavoriteRecyclerView.this, progressBar, photos, currentPhoto, currentPhoto + photos.size()).execute();
            } else {
                new LoadPhotosAsync(context, PhotoFavoriteRecyclerView.this, progressBar, photos, currentPhoto, currentPhoto + perDownload).execute();
            }
        }
    }
}
