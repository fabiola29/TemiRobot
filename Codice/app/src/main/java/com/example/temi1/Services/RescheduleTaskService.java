package com.example.temi1.Services;

import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.Observer;

import com.example.temi1.db.TaskTemi.TemiTaskRepository;
import com.example.temi1.model.TemiTask;

import java.util.List;

public class RescheduleTaskService extends LifecycleService {


    @Override
    public void onCreate() {
        super.onCreate();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        TemiTaskRepository taskRepository = new TemiTaskRepository(getApplication());

        taskRepository.getAllTaskFromDb().observe(this, new Observer<List<TemiTask>>() {
            @Override
            public void onChanged(List<TemiTask> tasks) {
                for (TemiTask newTask : tasks){
                    if(newTask.isStarted()){
                        newTask.schedule(getApplicationContext());
                    }
                }
            }
        });

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return null;
    }
}
