package com.example.beermir;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.beermir.db.AppDatabase;
import com.example.beermir.model.BeerInfoFragment;
import com.example.beermir.model.BeerSearchFragment;
import com.example.beermir.model.BreweryFragment;
import com.example.beermir.model.BreweryLocationFragment;
import com.example.beermir.model.FavoriteBeerFragment;
import com.facebook.stetho.Stetho;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.room.Room;

import network.entity.beer.Beer;
import network.entity.brewery.Brewery;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
        OnBeerClickListener, OnBreweryClickListener {
    private FragmentManager fm;

    private AppDatabase appDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Stetho.initializeWithDefaults(this);

        appDatabase = Room.databaseBuilder(this,
                AppDatabase.class, "database")
                .allowMainThreadQueries()
                .build();

        fm = getSupportFragmentManager();
        Fragment mainFragment = new BeerSearchFragment();
        fm.beginTransaction()
                .add(R.id.main_container,mainFragment)
                .addToBackStack(null)
                .commit();

        BottomNavigationView navView = findViewById(R.id.nav_view);

        navView.setOnNavigationItemSelectedListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**recyclerView = findViewById(R.id.beer_recycler_view);
        adapter = new BeerAdapter(new ArrayList<Beer>());
        LinearLayoutManager manager = new LinearLayoutManager(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);*/



        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
       /** AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
        // R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);*/
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        switch (menuItem.getItemId()){
            case R.id.search_beer_nav:
                fragment = new BeerSearchFragment();
                break;
            case R.id.location_pubs_nav:
                fragment = new BreweryLocationFragment();
                break;
            case R.id.favorites_nav:
                fragment = new FavoriteBeerFragment();
                break;
        }

        System.out.println(fm.getFragments().size() + "\t" + fm.getFragments().get(fm.getFragments().size()-1).toString() + "\t" + fragment.toString());

            fm.beginTransaction()
                    .replace(R.id.main_container,fragment)
                    .addToBackStack(null)
                    .commit();
        return true;
    }

    @Override
    public void onBeerClick(Beer beer) {
        Fragment fragment = new BeerInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("Beer",beer);
        fragment.setArguments(bundle);

        fm.beginTransaction()
                .replace(R.id.main_container,fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBreweryClick(Brewery brewery) {
        Fragment fragment = new BreweryFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("Brewery",brewery);
        fragment.setArguments(bundle);

        fm.beginTransaction()
                .replace(R.id.main_container,fragment)
                .addToBackStack(null)
                .commit();
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
