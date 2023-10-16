package com.example.temi1.TaskBroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.example.temi1.Services.RescheduleTaskService;
import com.example.temi1.Services.SearchingUserService;

import java.util.Calendar;

public class TaskBroadcastReceiver extends BroadcastReceiver {

    public static final String MONDAY = "MONDAY";
    public static final String TUESDAY = "TUESDAY";
    public static final String WEDNESDAY = "WEDNESDAY";
    public static final String THURSDAY = "THURSDAY";
    public static final String FRIDAY = "FRIDAY";
    public static final String SATURDAY = "SATURDAY";
    public static final String SUNDAY = "SUNDAY";
    public static final String RECURRING = "RECURRING";
    public static final String TASK_INFO ="TASKINFO";
    public static final String TASK_LOCATION ="TASK_LOCATION";
    public static final String TASK_IDUSER = "TASKIDUSER";


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("BroadcastReceiver", "Received");
        Log.d("BroadcastReceiver", "Received"+intent.getAction());

        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){

            Toast.makeText(context, "Task Reboot", Toast.LENGTH_SHORT).show();

            startRescheduleTaskService(context);
        } else {
            Toast.makeText(context, "Task Received", Toast.LENGTH_SHORT).show();

            if (!intent.getBooleanExtra(RECURRING, false) || taskIsToday(intent)) {
                startTaskService(context, intent);
            }
        }
    }


    private void startRescheduleTaskService(Context context) {
        Intent intentService = new Intent(context, RescheduleTaskService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
        String toast = String.format("Task has been postponed a 10 minutes");
        Toast.makeText(context,toast,Toast.LENGTH_LONG).show();
    }


    private boolean taskIsToday(Intent intent) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        switch (today) {
            case Calendar.MONDAY:
                return intent.getBooleanExtra(MONDAY, false);
            case Calendar.TUESDAY:
                return intent.getBooleanExtra(TUESDAY, false);
            case Calendar.WEDNESDAY:
                return intent.getBooleanExtra(WEDNESDAY, false);
            case Calendar.THURSDAY:
                return intent.getBooleanExtra(THURSDAY, false);
            case Calendar.FRIDAY:
                return intent.getBooleanExtra(FRIDAY, false);
            case Calendar.SATURDAY:
                return intent.getBooleanExtra(SATURDAY, false);
            case Calendar.SUNDAY:
                return intent.getBooleanExtra(SUNDAY, false);
        }
        return false;
    }


    private void startTaskService(Context context, Intent intent) {
        String taskInfo = intent.getStringExtra(TASK_INFO);
        String taskLocation = intent.getStringExtra(TASK_LOCATION);
        int userId = intent.getIntExtra(TASK_IDUSER, 0);

        Log.d("TaskBroadcastReceiver", "Task info: "+taskInfo);
        Log.d("TaskBroadcastReceiver", "Task location: "+taskLocation);
        Log.d("TaskBroadcastReceiver", "User ID: " + userId);


        Intent intentService = new Intent(context, SearchingUserService.class);
        intentService.putExtra(SearchingUserService.TASK_INFO, taskInfo);
        intentService.putExtra(SearchingUserService.TASK_LOCATION, taskLocation);
        intentService.putExtra(SearchingUserService.TASK_IDUSER, userId);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }

        Toast.makeText(context, "Starting SearchingTask!", Toast.LENGTH_SHORT).show();
    }
}
