package com.example.temi1.views.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.temi1.model.TemiRoute;
import com.example.temi1.temi.TemiPatroller;
import com.example.temi1.temi.TemiSpeaker;
import com.example.temi1.viewmodel.GlobalViewModel;
import com.example.temi1.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Page shown when Temi is patrolling
public class PatrolFragment extends Fragment {

    private static final String TAG = "PatrolFragment";
    private GlobalViewModel viewModel;
    private ExecutorService globalExecutorService;
    private ExecutorService postmanExecutorService;
    private NavController navController;
    private TemiPatroller temiPatroller;
    private TemiSpeaker temiSpeaker;
    private boolean isStationaryPatrol;
    private ImageButton bt_came_back;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Patrol Fragment created");
        viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        globalExecutorService = viewModel.getExecutorService();
        postmanExecutorService = Executors.newFixedThreadPool(20);
        temiSpeaker = new TemiSpeaker(viewModel.getTemiRobot());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start_patrol, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);


        bt_came_back = view.findViewById(R.id.fragment_start_patrol_button_back);
        bt_came_back.setOnClickListener(v -> button_back());

        Button bt_stop = view.findViewById(R.id.fragment_stop_patrol);
        bt_stop.setOnClickListener(v -> {
            Log.i(TAG, "Stopping...");
            navigateToHomePage();
            if (!isStationaryPatrol) {
                Log.i(TAG, "Stopping robot movement");
                temiPatroller.getTemiRobot().stopMovement();
            }
            Log.i(TAG, "After stopping");
        });

        // No patrol for stationary patrol
        if (viewModel.getSelectedRoute().getDestinations().size() != 0) {
            isStationaryPatrol = false;
            startPatrol();
        } else {
            isStationaryPatrol = true;
        }
    }


    public void button_back(){
        GlobalViewModel viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        viewModel.initializeForRouteCreation();
        navController.navigate(R.id.action_PatrolFragment_to_HomeFragment);
    }


    private void startPatrol() {
        temiPatroller = TemiPatroller.getInstance(this, viewModel.getTemiRobot(), isStationaryPatrol);

        viewModel.getTemiRobot().addOnGoToLocationStatusChangedListener(temiPatroller);
        viewModel.getTemiRobot().addTtsListener(temiPatroller);

        TemiRoute selectedRoute = viewModel.getSelectedRoute();
        temiPatroller.patrolRoute(selectedRoute);
    }


    public void navigateToHomePage() {

        if (!isStationaryPatrol) {
            viewModel.getTemiRobot().removeTtsListener(temiPatroller);
            viewModel.getTemiRobot().removeOnGoToLocationStatusChangedListener(temiPatroller);
        }
        if (navController.getCurrentDestination().getId() != R.id.HomeFragment) {
            navController.navigate(R.id.action_PatrolFragment_to_HomeFragment);
        }
    }
}