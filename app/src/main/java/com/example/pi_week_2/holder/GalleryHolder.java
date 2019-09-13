package com.example.pi_week_2.holder;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pi_week_2.R;
import com.example.pi_week_2.db.flickr.Image;

public class GalleryHolder extends RecyclerView.ViewHolder {
    private ImageView photo;
    private Image image;

    public GalleryHolder(View view) {
        super(view);

        photo = itemView.findViewById(R.id.my_photo_image_view);
    }

    public void bind(Image image) {
        this.image = image;

        photo.setImageBitmap(image.getPhoto());
    }

    public Image getImage() {
        return image;
    }
}
