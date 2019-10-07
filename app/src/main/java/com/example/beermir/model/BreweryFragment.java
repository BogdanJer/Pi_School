package com.example.beermir.model;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.beermir.BreweryBeerFindAsync;
import com.example.beermir.OnBeerClickListener;
import com.example.beermir.R;

import java.util.ArrayList;
import java.util.List;

import network.BreweryApi;
import network.entity.beer.Beer;
import network.entity.brewery.Brewery;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BreweryFragment extends Fragment{
    private BreweryApi api;
    private RecyclerView recyclerView;
    private BreweryBeerAdapter adapter;
    private ProgressBar progressBar;

    private OnBeerClickListener beerClickListener;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try{
            beerClickListener = (OnBeerClickListener)getActivity();
        }catch (ClassCastException ex){
            throw new ClassCastException(getActivity().toString() + " must implements OnBeerClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brewery,container,false);

        Brewery brewery = (Brewery)getArguments().getSerializable("Brewery");

        ImageView breweryImage = view.findViewById(R.id.brewery_image_info);
        TextView breweryDescription = view.findViewById(R.id.brewery_description_info);

        recyclerView = view.findViewById(R.id.brewery_beers_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new BreweryBeerAdapter();
        recyclerView.setAdapter(adapter);

        progressBar = getActivity().findViewById(R.id.progress_bar);

        breweryDescription.setText(brewery.getDescription());

        if(brewery.getImages()!=null)
            Glide.with(getContext())
                    .load(brewery.getImages().getMedium())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(breweryImage);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.brewerydb.com/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(BreweryApi.class);

        new BreweryBeerFindAsync(getContext(),api,adapter,progressBar).execute(brewery.getId());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ViewGroup.MarginLayoutParams)getActivity().findViewById(R.id.main_container).getLayoutParams()).setMargins(0,10,0,0);
    }


    private class BreweryBeerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView beerImage;
        private TextView beerName;
        private LinearLayout layout;
        private Beer beer;

        public BreweryBeerHolder(View view){
            super(view);

            beerImage = itemView.findViewById(R.id.beer_image);
            beerName = itemView.findViewById(R.id.beer_name);
            layout = itemView.findViewById(R.id.beer_layout);

            layout.setOnClickListener(this);
        }
        public void bind(Beer beer){
            this.beer = beer;

            beerName.setText(beer.getName());

            if(beer.getLabels()!=null)
                Glide.with(getContext())
                        .load(beer.getLabels().getMedium())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(beerImage);
        }

        @Override
        public void onClick(View v) {
            beerClickListener.onBeerClick(beer);
        }
    }

    public class BreweryBeerAdapter extends RecyclerView.Adapter<BreweryBeerHolder>{
        private List<Beer> list;

        public BreweryBeerAdapter(){
            list = new ArrayList<>();
        }
        @NonNull
        @Override
        public BreweryBeerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            return new BreweryBeerHolder(inflater.inflate(R.layout.beer_list_item,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull BreweryBeerHolder holder, int position) {
            holder.bind(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public void addItems(List<Beer> beers){
            list.addAll(beers);
            notifyItemRangeInserted(0,list.size()-1);
        }

        public void removeItems(){
            notifyItemRangeRemoved(0,list.size()-1);
            list.clear();
        }
    }
}
