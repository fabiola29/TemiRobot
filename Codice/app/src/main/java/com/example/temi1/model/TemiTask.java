package com.example.temi1.model;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import static com.example.temi1.TaskBroadcastReceiver.TaskBroadcastReceiver.MONDAY;
import static com.example.temi1.TaskBroadcastReceiver.TaskBroadcastReceiver.TASK_IDUSER;
import static com.example.temi1.TaskBroadcastReceiver.TaskBroadcastReceiver.TASK_INFO;
import static com.example.temi1.TaskBroadcastReceiver.TaskBroadcastReceiver.TASK_LOCATION;
import static com.example.temi1.TaskBroadcastReceiver.TaskBroadcastReceiver.TUESDAY;
import static com.example.temi1.TaskBroadcastReceiver.TaskBroadcastReceiver.WEDNESDAY;
import static com.example.temi1.TaskBroadcastReceiver.TaskBroadcastReceiver.THURSDAY;
import static com.example.temi1.TaskBroadcastReceiver.TaskBroadcastReceiver.FRIDAY;
import static com.example.temi1.TaskBroadcastReceiver.TaskBroadcastReceiver.SATURDAY;
import static com.example.temi1.TaskBroadcastReceiver.TaskBroadcastReceiver.SUNDAY;

import static com.example.temi1.TaskBroadcastReceiver.TaskBroadcastReceiver.RECURRING;

import com.example.temi1.TaskBroadcastReceiver.TaskBroadcastReceiver;

import java.util.Calendar;

@Entity (tableName = "task_table")
public class TemiTask {

    @PrimaryKey
    @NonNull
    private int taskId;
    private  int hour;
    private  int minute;
    private boolean started;
    private final boolean recurring;
    private boolean monday;
    private  boolean tuesday;
    private  boolean wednesday;
    private  boolean thursday;
    private  boolean friday;
    private  boolean saturday;
    private  boolean sunday;
    private  String taskName;
    private  String taskActivity;
    private  String location;
    private final long created;
    private int userId;


    public TemiTask(int taskId, int hour, int minute, String taskName, long created, boolean started, boolean recurring, boolean monday, boolean tuesday, boolean wednesday, boolean thursday, boolean friday, boolean saturday, boolean sunday, int userId, String taskActivity, String location) {
        this.taskId = taskId;
        this.hour = hour;
        this.minute = minute;
        this.taskName = taskName;
        this.created = created;
        this.started = started;
        this.recurring = recurring;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
        this.userId = userId;
        this.taskActivity = taskActivity;
        this.location = String.valueOf(location);
    }


    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public long getCreated() {
        return created;
    }

    public boolean isStarted() {
        return started;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public boolean isMonday() {
        return monday;
    }

    public boolean isTuesday() {
        return tuesday;
    }

    public boolean isWednesday() {
        return wednesday;
    }

    public boolean isThursday() {
        return thursday;
    }

    public boolean isFriday() {
        return friday;
    }

    public boolean isSaturday() {
        return saturday;
    }

    public boolean isSunday() {
        return sunday;
    }

    public String getTaskActivity() {
        return taskActivity;
    }

    public void setTaskActivity(String taskActivity) {
        this.taskActivity = taskActivity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public void schedule(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, TaskBroadcastReceiver.class);
        intent.putExtra(RECURRING, recurring);
        intent.putExtra(MONDAY, monday);
        intent.putExtra(TUESDAY, tuesday);
        intent.putExtra(WEDNESDAY, wednesday);
        intent.putExtra(THURSDAY, thursday);
        intent.putExtra(FRIDAY, friday);
        intent.putExtra(SATURDAY, saturday);
        intent.putExtra(SUNDAY, sunday);
        intent.putExtra(TASK_INFO, taskActivity);
        intent.putExtra(TASK_LOCATION, location);
        intent.putExtra(TASK_IDUSER, userId);

        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, taskId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND,0);

        if (calendar.getTimeInMillis() <= System.currentTimeMillis()){
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)+1);
        }
        try {
            if (!recurring){
                String toastText = String.format("The task is triggered only once");
                Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),alarmPendingIntent);

            } else {
                String toastText = String.format("The task will be repeated");
                Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();

                final long RUN_DAILY = 24*60*60*1000;
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), RUN_DAILY,alarmPendingIntent);
            }
        } catch (Exception e){
            Log.d("Task", "schedule Exception: "+e);
        }

        this.started = true;
    }

    public String getRecurringDaysText() {
        if (!recurring) {
            return null;
        }

        String days = "";

        if (monday) {
            days += "Mo ";
        }

        if (tuesday) {
            days += "Tu ";
        }

        if (wednesday) {
            days += "We ";
        }

        if (thursday) {
            days += "Th ";
        }

        if (friday) {
            days += "Fr ";
        }

        if (saturday) {
            days += "Sa ";
        }

        if (sunday) {
            days += "Su ";
        }
        return days;
    }

    public void setRecurringDays(boolean monday, boolean tuesday, boolean wednesday,
                                 boolean thursday, boolean friday, boolean saturday, boolean sunday) {
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
    }

    @Override
    public String toString() {
        return "TemiTask{" +
                "taskId=" + taskId +
                ", hour=" + hour +
                ", minute=" + minute +
                ", recurring=" + recurring +
                ", monday=" + monday +
                ", tuesday=" + tuesday +
                ", wednesday=" + wednesday +
                ", thursday=" + thursday +
                ", friday=" + friday +
                ", saturday=" + saturday +
                ", sunday=" + sunday +
                ", taskName='" + taskName + '\'' +
                ", taskActivity='" + taskActivity + '\'' +
                ", location='" + location + '\'' +
                '}';
    }

    public void cancelTask(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, TaskBroadcastReceiver.class);
        PendingIntent taskPendingIntent = PendingIntent.getBroadcast(context, taskId, intent, 0);
        try{
            alarmManager.cancel(taskPendingIntent);
            Log.d("Task", "canceling Task");
            String toastText = String.format("The activity has been deleted");
            Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();
        } catch (Exception e){
            Log.d("Task", "cancelTask Exception: "+e);
        }
        this.started = false;
    }
}
