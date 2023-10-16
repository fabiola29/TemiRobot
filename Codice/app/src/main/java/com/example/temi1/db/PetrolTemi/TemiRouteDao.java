package com.example.temi1.db.PetrolTemi;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.temi1.model.TemiRoute;

import java.util.List;

@Dao
public interface  TemiRouteDao {

   @Insert
   void insertRouteIntoDb(TemiRoute route);

    @Query("DELETE from routeTable")
    void deleteAll();

    @Delete
    void deleteRoute(TemiRoute temiRoute);

    @Update
    void updateRoute(TemiRoute updatedRoute);

    // Todo Deleting only one entry

    @Query("SELECT * FROM routeTable ORDER BY routeIdx ASC")
    LiveData<List<TemiRoute>> getRoutesFromDb();

}
