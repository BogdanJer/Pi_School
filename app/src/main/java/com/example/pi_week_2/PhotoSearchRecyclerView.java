package com.example.pi_week_2;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pi_week_2.async.FindPhotosAsync;
import com.example.pi_week_2.async.LoadPhotosAsync;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import static com.example.pi_week_2.MainActivity.SEARCH_TAG;
import static com.example.pi_week_2.MainActivity.URL_TAG;
import static com.example.pi_week_2.MainActivity.USER_TAG;

public class PhotoSearchRecyclerView extends RecyclerViewHelper {
    public static PhotoSearchRecyclerView photoSearchRecyclerView;
    public static boolean isBuild = false;
    private Context context;
    private PhotoTouchHelper touchHelper;
    private PhotoScrollListener scrollListener;
    private RecyclerView recyclerView;
    private GridLayoutManager manager;
    private ProgressBar pb;
    private PhotoAdapter adapter;
    private String userName;
    private String tag;

    public PhotoSearchRecyclerView(RecyclerView recyclerView, String userName, String tag) {
        this.recyclerView = recyclerView;
        this.userName = userName;
        this.tag = tag;
        photoSearchRecyclerView = this;
        isBuild = true;
    }

    public void build(Context context, ProgressBar pb) {
        this.context = context;
        this.pb = pb;


        //manager = new GridLayoutManager(context,2);
        //recyclerView.setLayoutManager(manager);

        new FindPhotosAsync(context, pb, this, tag).execute();

        PhotoTouchHelper pth = new PhotoTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(pth);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        //recyclerView.addOnScrollListener(new PhotoScrollListener());
        scrollListener = new PhotoScrollListener();
        recyclerView.addOnScrollListener(scrollListener);
    }

    public void getLinks(List<String> links) {
        if (adapter != null)
            adapter.newDataUpdate(links);
        else {
            adapter = new PhotoAdapter(links);
            recyclerView.setAdapter(adapter);
            manager = new GridLayoutManager(context, 2);
            recyclerView.setLayoutManager(manager);
        }


        //ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new PhotoTouchHelper(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT));
        //itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void reBuild(String tag) {
        recyclerView.removeOnScrollListener(null);
        new FindPhotosAsync(context, pb, this, tag).execute();
    }

    @Override
    public void getLoadedData(List<Photo> loadedPhotos, int indexFrom, int indexTo) {
        Log.i("Size", String.valueOf(loadedPhotos.size()));

        adapter.notifyItemRangeChanged(indexFrom, indexTo);

        adapter.downloading = false;
        adapter.currentPhoto += indexTo - indexFrom;
    }

    public Context getContext() {
        return context;
    }

    /**
     * Holder
     */
    public class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView photo;
        private TextView searchWordView;
        private CardView cardView;

        private Photo p;

        public PhotoHolder(LayoutInflater inflater, ViewGroup container) {
            super(inflater.inflate(R.layout.item_cardview, container, false));

            photo = itemView.findViewById(R.id.photo_card_view);
            searchWordView = itemView.findViewById(R.id.cardview_text);
            cardView = itemView.findViewById(R.id.list_card_view_item);

            cardView.setOnClickListener(this);
        }

        public void bind(Photo p) {
            this.p = p;
            photo.setImageBitmap(p.getPhoto());
            searchWordView.setText(tag);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, LookPhotoActivity.class);
            intent.putExtra(USER_TAG, userName);
            intent.putExtra(SEARCH_TAG, tag);
            intent.putExtra(URL_TAG, p.getLink());

            context.startActivity(intent);
        }
    }

    /**
     * Adapter
     */
    public class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
        private List<Photo> photos;
        private List<String> links;
        private int perDownload = 10;
        private int currentPhoto = 0;
        private boolean downloading = false;

        public PhotoAdapter(List<String> links) {
            this.photos = new ArrayList<>();
            this.links = links;
            loadData();
        }

        @NonNull
        @Override
        public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new PhotoHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
            holder.bind(photos.get(position));
        }

        @Override
        public int getItemCount() {
            return currentPhoto;
        }

        public void newDataUpdate(List<String> links) {
            notifyItemRangeRemoved(0, this.links.size());
            this.links = links;
            currentPhoto = 0;
            loadData();
        }

        public void removeItem(PhotoHolder photoHolder) {
            int position = photoHolder.getAdapterPosition();

            photos.remove(position);
            notifyItemRemoved(position);

            Snackbar.make(photoHolder.itemView, String.format("%s " + getContext().getString(R.string.photo_is_deleted), photoHolder.searchWordView.getText().toString()),
                    Snackbar.LENGTH_LONG).setAction("UNDO", (v) -> {
                photos.add(position, photoHolder.p);
                notifyItemInserted(position);
            }).show();
        }

        public void loadData() {
            Log.i("LOAD DATA", String.valueOf(photos.size()));
            Log.i("CURRENT PHOTO", String.valueOf(currentPhoto));

            for (int i = currentPhoto; i < currentPhoto + perDownload; i++)
                photos.add(new Photo(links.get(i), tag));

            downloading = true;

            if (pb.getVisibility() == View.VISIBLE)
                pb.setVisibility(View.GONE);

            if (photos.size() - perDownload < 0)
                new LoadPhotosAsync(context, PhotoSearchRecyclerView.this, pb, photos, currentPhoto, currentPhoto + photos.size()).execute();
            else
                new LoadPhotosAsync(context, PhotoSearchRecyclerView.this, pb, photos, currentPhoto, currentPhoto + perDownload).execute();
        }


        public boolean isDownLoading() {
            return downloading;
        }
    }

    // Touch helper
    public class PhotoTouchHelper extends ItemTouchHelper.SimpleCallback {
        public PhotoTouchHelper(int dragDirs, int swipeDirs) {
            super(dragDirs, swipeDirs);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            adapter.removeItem((PhotoHolder) viewHolder);
        }
    }

    /**
     * Scroll listener
     */
    public class PhotoScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            if (!adapter.isDownLoading()) {
                Log.i("VISIBLE ITEM", String.valueOf(manager.findLastVisibleItemPosition()));
                Log.i("ITEM COUNT", String.valueOf(manager.getItemCount()));

                if (manager.findLastCompletelyVisibleItemPosition() >= manager.getItemCount() - 1) {
                    adapter.loadData();
                }
            }
        }
    }
}