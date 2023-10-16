package com.example.temi1.db.TaskTemi;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.example.temi1.model.TemiTask;

import java.util.List;

public class TaskManager {
    private final TemiTaskRepository repository;
    private final Context context; // Aggiunto un campo per il contesto

    public TaskManager(Context context, TemiTaskRepository repository) {
        this.context = context; // Inizializza il contesto
        this.repository = repository;
    }

    public LiveData<List<TemiTask>> getAllTasks() {
        return repository.getAllTaskFromDb();
    }

    public void executeTask(TemiTask task) {
        // Qui inserisci la logica per eseguire l'attività su Temi
        // Ad esempio, puoi usare Temi's SDK per eseguire il task.

        Toast.makeText(context, "Sto eseguendo l'attività", Toast.LENGTH_SHORT).show();

    }
}