package com.example.pi_week_2.holder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pi_week_2.R;
import com.example.pi_week_2.adapter.FavoriteAdapter;

public class FavoriteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ImageView photo;
    public LinearLayout layout;
    public LinearLayout title;
    private ImageButton deleteBut;
    private FavoriteAdapter adapter;

    private String tag;
    private String link;

    public FavoriteHolder(View viewItem, FavoriteAdapter adapter) {
        super(viewItem);

        this.adapter = adapter;

        photo = itemView.findViewById(R.id.favorite_photo_card_view);
        deleteBut = itemView.findViewById(R.id.delete_photo_but);
        layout = itemView.findViewById(R.id.item_favorite_layout);
        title = itemView.findViewById(R.id.item_favorite_title_layout);

        deleteBut.setOnClickListener(this);
    }

    public void bind(String tag, String link) {
        this.tag = tag;
        this.link = link;
    }

    @Override
    public void onClick(View v) {
        adapter.removeItem(this);
    }

    public String getTag() {
        return tag;
    }

    public String getLink() {
        return link;
    }
}