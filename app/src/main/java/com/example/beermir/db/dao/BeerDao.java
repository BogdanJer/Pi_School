package com.example.beermir.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.beermir.db.entity.beer.Beer;

import java.util.List;

@Dao
public interface BeerDao {
    @Query("SELECT * FROM beer")
    List<Beer> getAllBeers();

    @Insert
    void addFavoriteBeer(Beer beer);
}
