package com.example.temi1.db.UserTemi;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.temi1.model.TemiUser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {TemiUser.class}, version = 2, exportSchema = false)
public abstract class TemiUserRoomDatabase extends RoomDatabase {

    public abstract TemiUserDao userDao();

    //Volatile keyword is used to modify the value of a variable by different threads.
    // It means that multiple threads can use a method and instance of the classes at the same time
    // without any problem
    private static volatile TemiUserRoomDatabase INSTANCE;
    private static final int THREAD_COUNT = 4;

    // For running DB operations asynchronously
    static final ExecutorService dbWriterExecutor = Executors.newFixedThreadPool(THREAD_COUNT);

    public static TemiUserRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            // Can append addCallback(sRoomDatabaseCallback) for future development if needed
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    TemiUserRoomDatabase.class, "TemiUserDatabase").build();
        }
        return INSTANCE;
    }

    public static ExecutorService getDbWriterExecutor() {
        return dbWriterExecutor;
    }

    private static Callback sRoomDatabaseCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // Creazione della tabella task_table
            db.execSQL("CREATE TABLE IF NOT EXISTS userTable (userId INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, biometricKey TEXT, imagePath TEXT)");

        }
    };

}

