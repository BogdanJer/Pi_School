package com.example.beermir.model;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.beermir.MainActivity;
import com.example.beermir.R;
import com.example.beermir.db.AppDatabase;

import network.entity.beer.Beer;

public class BeerInfoFragment extends Fragment {
    private Beer beer;

    private TextView beerInfo;
    private ImageView beerImage;
    private TextView beerGlass;
    private TextView beerCategory;

    private AppDatabase appDatabase;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        appDatabase = ((MainActivity)getActivity()).getAppDatabase();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beer_info,container,false);

        getActivity().findViewById(R.id.search_view).setVisibility(View.GONE);

        beer = (Beer)getArguments().getSerializable("Beer");

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(beer.getName());
        setHasOptionsMenu(true);

        beerInfo = view.findViewById(R.id.beer_description_info);
        beerImage = view.findViewById(R.id.beer_image_info);
        beerGlass = view.findViewById(R.id.beer_glass_info);
        beerCategory = view.findViewById(R.id.beer_category_info);

        if(beer.getLabels()!=null)
        Glide.with(getContext())
                .load(beer.getLabels().getMedium())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(beerImage);

        if(beer.getGlass()!=null)
            beerGlass.setText(beer.getGlass().getName());
        if(beer.getStyle()!=null) {
            beerInfo.setText(beer.getStyle().getDescription());
            if(beer.getStyle().getCategory()!=null)
                beerCategory.setText(beer.getStyle().getCategory().getName());
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ViewGroup.MarginLayoutParams)getActivity().findViewById(R.id.main_container).getLayoutParams()).setMargins(0,10,0,0);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.favorites_toolbar){
            addNewFavoriteBeer();
            return true;
        }
        return false;
    }

    private void addNewFavoriteBeer(){
        Toast.makeText(getContext(),"Beer added!",Toast.LENGTH_LONG).show();
        appDatabase.getBeerDao().addFavoriteBeer(createBeer());
    }

    private com.example.beermir.db.entity.beer.Beer createBeer(){
        com.example.beermir.db.entity.beer.Beer dbBeer = new com.example.beermir.db.entity.beer.Beer();

        dbBeer.setId(beer.getId());
        /**if(beer.getLabels()!=null) {
            Labels labels = new Labels();

            labels.setBeerId(beer.getId());
            labels.setIcon(beer.getLabels().getIcon());
            labels.setMedium(beer.getLabels().getMedium());
            labels.setLarge(beer.getLabels().getLarge());
            labels.setContentAwareIcon(beer.getLabels().getContentAwareIcon());
            labels.setContentAwareMedium(beer.getLabels().getContentAwareMedium());
            labels.setContentAwareLarge(beer.getLabels().getContentAwareLarge());

            dbBeer.setLabels(labels);
        }

        if(beer.getStyle()!=null){
            Style style = new Style();

            style.setId(beer.getStyleId());

            Category category = new Category();
            category.setId(beer.getStyle().getCategoryId());
            category.setName(beer.getStyle().getCategory().getName());
            category.setCreateDate(beer.getStyle().getCategory().getCreateDate());

            style.setCategoryId(beer.getStyle().getCategoryId());
            style.setCategory(category);
            style.setName(beer.getStyle().getName());
            style.setShortName(beer.getStyle().getShortName());
            style.setDescription(beer.getStyle().getDescription());
            style.setIbuMin(beer.getStyle().getIbuMin());
            style.setIbuMax(beer.getStyle().getIbuMax());
            style.setAbvMin(beer.getStyle().getAbvMin());
            style.setAbvMax(beer.getStyle().getAbvMax());
            style.setSrmMin(beer.getStyle().getSrmMin());
            style.setSrmMax(beer.getStyle().getSrmMax());
            style.setOgMin(beer.getStyle().getOgMin());
            style.setFgMin(beer.getStyle().getFgMin());
            style.setFgMax(beer.getStyle().getFgMax());
            style.setCreateDate(beer.getStyle().getCreateDate());
            style.setUpdateDate(beer.getStyle().getUpdateDate());

            dbBeer.setStyle(style);
        }

        dbBeer.setId(beer.getId());
        dbBeer.setName(beer.getName());
        dbBeer.setNameDisplay(beer.getNameDisplay());
        dbBeer.setGlassWareId(beer.getGlasswareId());

        dbBeer.setIsOrganic(beer.getIsOrganic());
        dbBeer.setIsRetired(beer.getIsRetired());
        dbBeer.setStatus(beer.getStatus());
        dbBeer.setStatusDisplay(beer.getStatusDisplay());
        dbBeer.setCreateDate(beer.getCreateDate());
        dbBeer.setUpdateDate(beer.getUpdateDate());*/




        return dbBeer;
    }
}
