package com.example.beermir.model;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.beermir.BreweryFindAsync;
import com.example.beermir.OnBreweryClickListener;
import com.example.beermir.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import network.BreweryApi;
import network.entity.brewery.Brewery;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class BreweryLocationFragment extends Fragment {
    private final int LOCATION_PERMISSION_REQUEST = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private boolean permissionGranted;
    private LocationCallback locationCallback;
    private LocationResult mLocationResult;
    private BreweryApi api;
    private ProgressBar progressBar;
    private OnBreweryClickListener breweryClickListener;

    private RecyclerView recyclerView;
    private BreweryAdapter adapter;
    @Override
    public void onAttach(@NonNull final Context context) {
        super.onAttach(context);

        requestLocationPermission();

        try{
            breweryClickListener = (OnBreweryClickListener)getActivity();
        }catch (ClassCastException ex){
            throw new ClassCastException(getActivity().toString() + " must implements OnBreweryClickListener");
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if(locationResult!=null) {
                    mLocationResult = locationResult;
                    fusedLocationClient.removeLocationUpdates(locationCallback);
                    new BreweryFindAsync(context,api,adapter,progressBar).execute(locationResult.getLastLocation().getLatitude(),
                            locationResult.getLastLocation().getLongitude());
                }
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brewery_location,container,false);

        progressBar = getActivity().findViewById(R.id.progress_bar);

        recyclerView = view.findViewById(R.id.brewery_recycler_view);
        adapter = new BreweryAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://sandbox-api.brewerydb.com/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(BreweryApi.class);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        ((ViewGroup.MarginLayoutParams)getActivity().findViewById(R.id.main_container).getLayoutParams()).setMargins(0,10,0,0);

        if(permissionGranted)
            startLocationUpdates();
    }
    private LocationRequest getLocationRequest(){
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(30000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(15000);
        return locationRequest;
    }

    private void startLocationUpdates(){
        fusedLocationClient.requestLocationUpdates(getLocationRequest(),locationCallback,null);
    }

    private void requestLocationPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!= PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_REQUEST);
            }else
                permissionGranted();
        }
        permissionGranted();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST:
                if(grantResults.length!=0 && grantResults[0]==PERMISSION_GRANTED)
                    permissionGranted();
                else
                    permissionDenied();
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void permissionGranted(){
        permissionGranted = true;
        Log.i("Location permission",getContext().getString(R.string.permission_denied));
    }
    private void permissionDenied(){
        permissionGranted = false;
        Log.e("Location permission",getContext().getString(R.string.permission_denied));
    }

    private class BreweryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private LinearLayout layout;
        private ImageView breweryImage;
        private TextView breweryName;

        private Brewery brewery;
        public BreweryHolder(View view){
            super(view);
            layout = itemView.findViewById(R.id.brewery_layout);
            breweryImage = itemView.findViewById(R.id.brewery_image);
            breweryName = itemView.findViewById(R.id.brewery_name);

            layout.setOnClickListener(this);
        }
        public void bind(Brewery brewery){
            this.brewery = brewery;

            breweryName.setText(brewery.getName());

            if(brewery.getImages()!=null)
                Glide.with(getContext())
                        .load(brewery.getImages().getIcon())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(breweryImage);

        }

        @Override
        public void onClick(View v) {
            breweryClickListener.onBreweryClick(brewery);
        }
    }


    public class BreweryAdapter extends RecyclerView.Adapter<BreweryHolder> {
        private List<Brewery> list;

        public BreweryAdapter(){
            list = new ArrayList<>();
        }
        @NonNull
        @Override
        public BreweryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new BreweryHolder(inflater.inflate(R.layout.brewery_list_item,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull BreweryHolder holder, int position) {
            holder.bind(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public void addBrewery(Brewery brewery){
            list.add(brewery);
            notifyItemInserted(list.size()-1);
        }
        public void addBreweries(List<Brewery> breweries){
            list.addAll(breweries);
            notifyItemRangeInserted(0,list.size()-1);
        }
        public void removeItems(){
            notifyItemRangeRemoved(0,list.size()-1);
            list.clear();
        }
    }

}
