package com.example.pi_week_2.holder;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pi_week_2.R;

public class SimpleStringHolder extends RecyclerView.ViewHolder {
    private TextView numberView;
    private TextView textView;

    public SimpleStringHolder(LayoutInflater inflater, ViewGroup container) {
        super(inflater.inflate(R.layout.item_string_view, container, false));

        numberView = itemView.findViewById(R.id.number_history_item);
        textView = itemView.findViewById(R.id.text_history_item);
    }

    public void bind(String number, String text) {
        numberView.setText(number);
        textView.setText(text);
    }
}
