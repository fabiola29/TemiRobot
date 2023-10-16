package com.example.temi1.db.TaskTemi;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.temi1.model.TemiTask;

import java.util.List;

public class TemiTaskRepository {
    private final String TAG = "TemiTaskRepository";
    private TemiTaskDao mTemiTaskDao;

    private LiveData<List<TemiTask>> task;

    public TemiTaskRepository(Application application) {
        TemiTaskRoomDatabase db = TemiTaskRoomDatabase.getDatabase(application);
        // Only need Dao to access DB, no need exposure to DB
        mTemiTaskDao = db.taskDao();
        task = mTemiTaskDao.getTaskFromDb();
    }

    public LiveData<List<TemiTask>> getAllTaskFromDb() {
        if (task.getValue() == null) {
            task = mTemiTaskDao.getTaskFromDb();
        }
        Log.i(TAG, "returning " + task.getValue());
        return task;
    }

    public LiveData<List<TemiTask>> getAllTask(){
        return  task;
    }

    public void insertTemiTaskIntoDb(TemiTask temiTask) {
        TemiTaskRoomDatabase.getDbWriterExecutor().execute(() -> {
            Log.i(TAG, "Inserting route...");
            mTemiTaskDao.insertTaskIntoDb(temiTask);
        });
    }

    public void deleteTemiTaskFromDb(TemiTask temiTask) {
        TemiTaskRoomDatabase.getDbWriterExecutor().execute(() -> {
            Log.i(TAG, "Deleting route...");
            mTemiTaskDao.deleteTask(temiTask);
        });
    }

    public void updateTaskInDb(TemiTask temiTask) {
        TemiTaskRoomDatabase.getDbWriterExecutor().execute(() -> {
            Log.i(TAG, "Updating route...");
            mTemiTaskDao.updateTask(temiTask);
        });
    }

    private  static class UpdateTaskAsyncTask extends AsyncTask<TemiTask, Void, Void> {
        private final TemiTaskDao taskDao;

        private UpdateTaskAsyncTask(TemiTaskDao taskDao){
            this.taskDao = taskDao;
        }


        @Override
        protected Void doInBackground(TemiTask... temiTasks) {
            taskDao.update(temiTasks[0]);
            return null;
        }
    }

    public void update(TemiTask temiTask){
        new UpdateTaskAsyncTask(mTemiTaskDao).execute(temiTask);

    }
}