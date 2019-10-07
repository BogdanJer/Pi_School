package com.example.beermir.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.beermir.db.dao.BeerDao;
import com.example.beermir.db.entity.beer.*;

@Database(entities = {Beer.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract BeerDao getBeerDao();
}