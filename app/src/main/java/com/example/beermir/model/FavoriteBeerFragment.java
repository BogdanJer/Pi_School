package com.example.beermir.model;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.beermir.MainActivity;
import com.example.beermir.OnBeerClickListener;
import com.example.beermir.R;
import com.example.beermir.db.AppDatabase;
import com.example.beermir.db.entity.beer.Beer;

import java.util.ArrayList;
import java.util.List;

import network.BreweryApi;
import network.DataBeerById;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FavoriteBeerFragment extends Fragment {
    private RecyclerView recyclerView;
    private FavoriteBeerAdapter adapter;

    private AppDatabase appDatabase;
    private BreweryApi breweryApi;

    private OnBeerClickListener beerClickListener;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        getActivity().findViewById(R.id.search_view).setVisibility(View.GONE);

        try{
            beerClickListener = (OnBeerClickListener) getActivity();
        }catch (ClassCastException ex){
            throw new ClassCastException(getActivity().toString() + " must implements OnBeerClickListener!");
        }
        appDatabase = ((MainActivity)getActivity()).getAppDatabase();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.brewerydb.com/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        breweryApi = retrofit.create(BreweryApi.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_beer,container,false);

        recyclerView = view.findViewById(R.id.favorite_beers_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new FavoriteBeerAdapter();
        recyclerView.setAdapter(adapter);

        List<Beer> list = appDatabase.getBeerDao().getAllBeers();
        for(Beer b : list){
            breweryApi.getBeerById(b.getId(),getString(R.string.sandbox_key)).enqueue(new Callback<DataBeerById>() {
                @Override
                public void onResponse(Call<DataBeerById> call, Response<DataBeerById> response) {
                    if(response.isSuccessful()){
                        adapter.addBeer(response.body().getBeer());
                    }
                }
                @Override
                public void onFailure(Call<DataBeerById> call, Throwable t) {
                    Toast.makeText(getContext(),"Unsuccessful",Toast.LENGTH_LONG).show();
                }
            });
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Favorites");
        getActivity().findViewById(R.id.search_view).setVisibility(View.GONE);
        ((ViewGroup.MarginLayoutParams)getActivity().findViewById(R.id.main_container).getLayoutParams()).setMargins(0,56,0,0);
    }

    private class FavoriteBeerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView beerImage;
        private TextView beerName;
        private LinearLayout layout;

        private network.entity.beer.Beer beer;
        public FavoriteBeerHolder(View view){
            super(view);

            beerImage = itemView.findViewById(R.id.beer_image);
            beerName = itemView.findViewById(R.id.beer_name);
            layout = itemView.findViewById(R.id.beer_layout);

            layout.setOnClickListener(this);
        }
        public void bind(network.entity.beer.Beer beer){
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

    private class FavoriteBeerAdapter extends RecyclerView.Adapter<FavoriteBeerHolder>{
        private List<network.entity.beer.Beer> list;

        public FavoriteBeerAdapter(){
            list = new ArrayList<>();
        }
        @NonNull
        @Override
        public FavoriteBeerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            return new FavoriteBeerHolder(inflater.inflate(R.layout.beer_list_item,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull FavoriteBeerHolder holder, int position) {
            holder.bind(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public void addBeer(network.entity.beer.Beer beer){
            list.add(beer);
            notifyItemInserted(list.size()-1);
        }
    }

}
