package com.example.temi1.db.PetrolTemi;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.temi1.model.TemiRoute;

import java.util.List;

public class TemiPatrolRepository {
    private final String TAG = "TemiPatrolRepository";
    private TemiRouteDao mTemiRouteDao;
    private LiveData<List<TemiRoute>> routes;

    public TemiPatrolRepository(Application application) {
        TemiPatrolRoomDatabase db = TemiPatrolRoomDatabase.getDatabase(application);
        // Only need Dao to access DB, no need exposure to DB
        mTemiRouteDao = db.routeDao();
        routes = mTemiRouteDao.getRoutesFromDb();
    }

    public LiveData<List<TemiRoute>> getAllRoutesFromDb() {
        if (routes.getValue() == null) {
            routes = mTemiRouteDao.getRoutesFromDb();
        }
        Log.i(TAG, "returning " + routes.getValue());
        return routes;
    }

    public void insertTemiRouteIntoDb(TemiRoute temiRoute) {
        TemiPatrolRoomDatabase.getDbWriterExecutor().execute(() -> {
            Log.i(TAG, "Inserting route...");
            mTemiRouteDao.insertRouteIntoDb(temiRoute);
        });
    }

    public void deleteTemiRouteFromDb(TemiRoute temiRoute) {
        TemiPatrolRoomDatabase.getDbWriterExecutor().execute(() -> {
            Log.i(TAG, "Deleting route...");
            mTemiRouteDao.deleteRoute(temiRoute);
        });
    }

    public void updateRouteInDb(TemiRoute temiRoute) {
        TemiPatrolRoomDatabase.getDbWriterExecutor().execute(() -> {
            Log.i(TAG, "Updating route...");
            mTemiRouteDao.updateRoute(temiRoute);
        });
    }
}
