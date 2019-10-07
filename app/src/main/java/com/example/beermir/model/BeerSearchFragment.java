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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.beermir.BeerFindAsync;
import com.example.beermir.OnBeerClickListener;
import com.example.beermir.R;

import java.util.ArrayList;
import java.util.List;

import network.BreweryApi;
import network.entity.beer.Beer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BeerSearchFragment extends Fragment implements SearchView.OnQueryTextListener {
    private OnBeerClickListener clickListener;
    private RecyclerView recyclerView;
    private BeerAdapter adapter;
    private ProgressBar progressBar;

    private BreweryApi breweryApi;
    private boolean searching;

    private BeerFindAsync bfa;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try{
            clickListener = (OnBeerClickListener)getActivity();
        }catch (ClassCastException ex){
            throw new ClassCastException(getActivity().toString() + " must implements OnBeerClickListener!");
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.brewerydb.com/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        breweryApi = retrofit.create(BreweryApi.class);

        progressBar = getActivity().findViewById(R.id.progress_bar);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beer_search,container,false);

        SearchView searchView = getActivity().findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(this);

        recyclerView = view.findViewById(R.id.beer_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new BeerAdapter(new ArrayList<Beer>());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle();
        getActivity().findViewById(R.id.search_view).setVisibility(View.VISIBLE);
        ((ViewGroup.MarginLayoutParams)getActivity().findViewById(R.id.main_container).getLayoutParams()).setMargins(0,56,0,0);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Toast.makeText(getContext(),query,Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    public boolean onQueryTextChange(final String newText) {
        adapter.removeItems();
        if(newText.length()>1){
            if(bfa!=null)
                bfa.cancel(true);
            bfa = (BeerFindAsync)new BeerFindAsync(getContext(),breweryApi,adapter,progressBar).execute(newText);
        }
        return true;
    }

    private class BeerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView beerImage;
        private TextView beerName;
        private LinearLayout layout;

        private Beer beer;

        public BeerHolder(View view){
            super(view);
            beerImage = itemView.findViewById(R.id.beer_image);
            beerName = itemView.findViewById(R.id.beer_name);
            layout = itemView.findViewById(R.id.beer_layout);

            layout.setOnClickListener(this);
        }
        public void bind(Beer beer) {
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
            clickListener.onBeerClick(beer);
        }
    }

    public class BeerAdapter extends RecyclerView.Adapter<BeerHolder> {
        private List<Beer> list;

        public BeerAdapter(List<Beer> list){
            this.list = list;
        }
        @NonNull
        @Override
        public BeerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new BeerHolder(inflater.inflate(R.layout.beer_list_item,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull BeerHolder holder, int position) {
            holder.bind(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public void addItem(Beer beer){
            list.add(beer);
            notifyItemInserted(list.size()-1);
        }
        public void addItems(List<Beer> beerList){
            list.addAll(beerList);
            notifyItemRangeInserted(0,list.size()-1);
        }
        public void removeItems(){
            notifyItemRangeRemoved(0,list.size());
            list.clear();
        }
    }

}
