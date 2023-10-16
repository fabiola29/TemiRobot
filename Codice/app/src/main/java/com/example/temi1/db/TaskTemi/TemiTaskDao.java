package com.example.temi1.db.TaskTemi;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.temi1.model.TemiTask;
import com.example.temi1.model.TemiUser;

import java.util.List;

@Dao
public interface TemiTaskDao {

    @Insert
    void insertTaskIntoDb(TemiTask temiTask);

    @Query("SELECT * FROM task_table")
    List<TemiTask> getAll();

    @Query("DELETE from task_table")
    void deleteAll();

    @Delete
    void deleteTask(TemiTask task);

    @Update
    void updateTask(TemiTask updatedTask);

    // Todo Deleting only one entry
    @Query("SELECT * FROM task_table ORDER BY taskId ASC")
    LiveData<List<TemiTask>> getTaskFromDb();


    @Query("SELECT * FROM task_table WHERE taskName = :name AND hour = :hour AND minute = :minute " +
            "AND monday = :mon AND tuesday = :tue AND wednesday = :wed AND thursday = :thu AND friday = :fri AND saturday = :sat AND sunday = :sun")
    List<TemiTask> getTaskByNameAndTimeAndDays(String name, int hour, int minute,
                                           boolean mon, boolean tue, boolean wed, boolean thu, boolean fri, boolean sat, boolean sun);


    @Query("SELECT * FROM task_table WHERE hour = :hour AND minute = :minute " +
            "AND monday = :mon AND tuesday = :tue AND wednesday = :wed AND thursday = :thu " +
            "AND friday = :fri AND saturday = :sat AND sunday = :sun")
    List<TemiTask> getTaskByTimeAndDays(int hour, int minute, boolean mon, boolean tue,
                                    boolean wed, boolean thu, boolean fri, boolean sat, boolean sun);

    @Query("SELECT * FROM task_table WHERE hour = :hour AND minute = :minute ")
    List<TemiTask> getTaskByTime(int hour, int minute);

    @Update
    void update(TemiTask temiTask);

}
