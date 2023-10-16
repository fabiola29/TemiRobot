package com.example.temi1.db.TaskTemi;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.temi1.model.TemiTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {TemiTask.class}, version = 3, exportSchema = false)
public abstract class TemiTaskRoomDatabase extends RoomDatabase {

    public abstract TemiTaskDao taskDao();

    //Volatile keyword is used to modify the value of a variable by different threads.
    // It means that multiple threads can use a method and instance of the classes at the same time
    // without any problem
    private static volatile TemiTaskRoomDatabase INSTANCE;
    private static final int THREAD_COUNT = 4;

    // For running DB operations asynchronously
    static final ExecutorService dbWriterExecutor = Executors.newFixedThreadPool(THREAD_COUNT);

    public static TemiTaskRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            // Can append addCallback(sRoomDatabaseCallback) for future development if needed
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    TemiTaskRoomDatabase.class, "TemiTaskDatabase").build();
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

            db.execSQL("CREATE TABLE IF NOT EXISTS task_table (taskId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "hour INTEGER NOT NULL, " +
                    "minute INTEGER NOT NULL, " +
                    "started INTEGER NOT NULL, " +
                    "recurring INTEGER NOT NULL, " +
                    "monday INTEGER NOT NULL, " +
                    "tuesday INTEGER NOT NULL, " +
                    "wednesday INTEGER NOT NULL, " +
                    "thursday INTEGER NOT NULL, " +
                    "friday INTEGER NOT NULL, " +
                    "saturday INTEGER NOT NULL, " +
                    "sunday INTEGER NOT NULL, " +
                    "taskName TEXT NOT NULL, " +
                    "taskActivity TEXT, " +
                    "location TEXT, " +
                    "created INTEGER NOT NULL, " +
                    "userId INTEGER NOT NULL, " +
                    "routeIdx INTEGER NOT NULL, " +
                    "FOREIGN KEY(userId) REFERENCES userTable(userId) ON DELETE CASCADE, " +
                    "FOREIGN KEY(routeIdx) REFERENCES routeTable(routeIdx) ON DELETE CASCADE)");
        }
    };
}

