package com.example.temi1.views.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.temi1.R;
import com.example.temi1.viewmodel.GlobalViewModel;
import com.google.android.material.textfield.TextInputLayout;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;
import com.robotemi.sdk.listeners.OnRobotReadyListener;

import java.util.regex.Pattern;

public class RegisterOrViewActivityFragment extends Fragment implements OnRobotReadyListener {
    private ImageButton bt_back;
    private NavController navController;
    private CardView bt_register_activity,bt_login_activity;
    private Robot robot;
    private ImageButton listActivityBtn;


    /**
     * Setting up all the event listeners
     */
    @Override
    public void onStart() {
        super.onStart();

        // Add robot event listeners
        robot.addOnRobotReadyListener(this);
    }


    /**
     * Removing the event listeners upon leaving the app.
     */
    @Override
    public void onStop() {
        super.onStop();

        // Remove robot event listeners
        robot.removeOnRobotReadyListener(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register_or_view_activity, container, false);
    }


    @Override
    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        bt_back =  view.findViewById(R.id.btn_back);
        bt_back.setOnClickListener(v -> button_back());

        bt_register_activity = view.findViewById(R.id.cardView_register_activity);
        bt_register_activity.setOnClickListener(v -> button_register_activity());

        bt_login_activity = view.findViewById(R.id.cardView_add_activity);
        bt_login_activity.setOnClickListener(v -> button_login_activity());

        listActivityBtn = view.findViewById(R.id.btn_listActivity);
        listActivityBtn.setOnClickListener(v -> button_list_activity());

        robot = Robot.getInstance();  // get an instance of the robot
    }


    private void button_back(){
        getActivity().finish();
    }


    private void button_register_activity(){
        GlobalViewModel viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        viewModel.initializeForRouteCreation();
        navController.navigate(R.id.action_RegisterOrViewActivityFragment_RegisterUserFragment);
    }


    private void button_login_activity(){
        GlobalViewModel viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        viewModel.initializeForRouteCreation();
        navController.navigate(R.id.action_RegisterOrViewActivityfragment_to_LoginUserFragment);
    }


    @Override
    public void onRobotReady(boolean isReady) {
        if (isReady) {
            try {
                final ActivityInfo activityInfo = getActivity().getPackageManager().getActivityInfo(getActivity().getComponentName(), PackageManager.GET_META_DATA);
                robot.onStart(activityInfo);
            } catch (PackageManager.NameNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private void button_list_activity(){
        GlobalViewModel viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        viewModel.initializeForRouteCreation();
        navController.navigate(R.id.action_RegisterOrViewActivityFragment_ListActivityTasksFragment);
    }
}