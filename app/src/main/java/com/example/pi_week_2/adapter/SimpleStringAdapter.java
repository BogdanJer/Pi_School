package com.example.pi_week_2.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pi_week_2.holder.SimpleStringHolder;

import java.util.List;

public class SimpleStringAdapter extends RecyclerView.Adapter<SimpleStringHolder> {
    private List<String> list;

    public SimpleStringAdapter(List<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public SimpleStringHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new SimpleStringHolder(inflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleStringHolder holder, int position) {
        holder.bind(String.valueOf(position + 1), list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
