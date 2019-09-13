package com.example.pi_week_2.holder;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pi_week_2.R;
import com.example.pi_week_2.model.LookPhotoActivity;

import static com.example.pi_week_2.model.MainActivity.SEARCH_TAG;
import static com.example.pi_week_2.model.MainActivity.URL_TAG;
import static com.example.pi_week_2.model.MainActivity.USER_TAG;
import static com.example.pi_week_2.model.MainActivity.name;

public class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ImageView photoView;
    private TextView searchWordView;
    private CardView cardView;

    private Context context;

    private String tag;
    private String link;

    public PhotoHolder(View itemView, Context context) {
        super(itemView);

        this.context = context;

        photoView = itemView.findViewById(R.id.photo_card_view);
        searchWordView = itemView.findViewById(R.id.cardview_text);
        cardView = itemView.findViewById(R.id.list_card_view_item);

        cardView.setOnClickListener(this);
    }

    public void bind(String tag, String link) {
        this.link = link;
        this.tag = tag;

        searchWordView.setText(tag);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, LookPhotoActivity.class);
        intent.putExtra(USER_TAG, name);
        intent.putExtra(SEARCH_TAG, tag);
        intent.putExtra(URL_TAG, link);

        context.startActivity(intent);
    }

    public String getTag() {
        return tag;
    }

    public String getLink() {
        return link;
    }
}