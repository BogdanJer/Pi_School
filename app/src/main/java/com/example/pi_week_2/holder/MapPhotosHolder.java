package com.example.pi_week_2.holder;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pi_week_2.LookPhotoActivity;
import com.example.pi_week_2.R;

import static com.example.pi_week_2.MainActivity.SEARCH_TAG;
import static com.example.pi_week_2.MainActivity.URL_TAG;
import static com.example.pi_week_2.MainActivity.USER_TAG;
import static com.example.pi_week_2.MainActivity.name;

public class MapPhotosHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private Context context;

    private LinearLayout mapPhotoLayout;
    private ImageView photo;
    private TextView latlon;
    private String link;

    private double latitude;
    private double longitude;

    public MapPhotosHolder(View view, Context context) {
        super(view);

        this.context = context;

        mapPhotoLayout = itemView.findViewById(R.id.map_photo_layout);
        photo = itemView.findViewById(R.id.photo_map_search);
        latlon = itemView.findViewById(R.id.lat_lon_map_search);

        mapPhotoLayout.setOnClickListener(this);
    }

    public void bind(double latitude, double longitude, String link) {
        latlon.setText(String.format(latlon.getText().toString(), latitude, longitude));
        this.latitude = latitude;
        this.longitude = longitude;
        this.link = link;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, LookPhotoActivity.class);
        intent.putExtra(USER_TAG, name);
        intent.putExtra(SEARCH_TAG, String.format(context.getString(R.string.lat_lon_map_search), latitude, longitude));
        intent.putExtra(URL_TAG, link);

        context.startActivity(intent);
    }

    public ImageView getPhoto() {
        return photo;
    }

    public TextView getLatlon() {
        return latlon;
    }

    public String getLink() {
        return link;
    }
}
