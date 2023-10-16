package com.example.temi1.db.UserTemi;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.temi1.db.PetrolTemi.TemiPatrolRoomDatabase;

import java.util.List;

import com.example.temi1.model.TemiUser;

public class TemiUserRepository {
    private final String TAG = "TemiUserRepository";
    private TemiUserDao mTemiUserDao;

    private LiveData<List<TemiUser>> user;

    public TemiUserRepository(Application application) {
        TemiUserRoomDatabase db = TemiUserRoomDatabase.getDatabase(application);
        // Only need Dao to access DB, no need exposure to DB
        mTemiUserDao = db.userDao();
        user = mTemiUserDao.getUserFromDb();

        loggedInUser.setValue(null); // Initialize the user as not logged in

    }

    public LiveData<List<TemiUser>> getAllUserFromDb() {
        if (user.getValue() == null) {
            user = mTemiUserDao.getUserFromDb();
        }
        Log.i(TAG, "returning " + user.getValue());
        return user;
    }

    public void insertTemiUserIntoDb(TemiUser temiUser) {
        TemiPatrolRoomDatabase.getDbWriterExecutor().execute(() -> {
            Log.i(TAG, "Inserting route...");
            mTemiUserDao.insertUserIntoDb(temiUser);
        });
    }

    public void deleteTemiUserFromDb(TemiUser temiUser) {
        TemiPatrolRoomDatabase.getDbWriterExecutor().execute(() -> {
            Log.i(TAG, "Deleting route...");
            mTemiUserDao.deleteUser(temiUser);
        });
    }

    public void updateUserInDb(TemiUser temiUser) {
        TemiPatrolRoomDatabase.getDbWriterExecutor().execute(() -> {
            Log.i(TAG, "Updating route...");
            mTemiUserDao.updateUser(temiUser);
        });
    }

    private static TemiUserRepository instance;
    private MutableLiveData<TemiUser> loggedInUser = new MutableLiveData<>();
    private TemiUserRoomDatabase temiUserRoomDatabase;

    public static TemiUserRepository getInstance(Application application) {
        if (instance == null) {
            instance = new TemiUserRepository(application);
        }
        return instance;
    }

    public LiveData<TemiUser> getLoggedInUser() {
        return loggedInUser;
    }

    public void logoutUser() {
        // Perform logout operations, such as setting the currently logged in user as null
        loggedInUser.postValue(null);
    }

}