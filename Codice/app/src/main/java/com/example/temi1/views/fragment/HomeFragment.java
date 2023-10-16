package com.example.temi1.views.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.example.temi1.viewmodel.GlobalViewModel;
import com.example.temi1.R;
import org.jetbrains.annotations.NotNull;
import androidx.appcompat.app.AppCompatActivity;

public class HomeFragment extends Fragment{

    private CardView bt_create_route,bt_start_patrol,bt_option_location,optionActivity_bt;
    private ImageButton logOut_bt;
    private NavController navController;
    private GlobalViewModel viewModel;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);

        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        bt_create_route = view.findViewById(R.id.cardView_create_route);
        bt_create_route.setOnClickListener(v -> button_create_route());

        bt_start_patrol = view.findViewById(R.id.cardView_start_patrol);
        bt_start_patrol.setOnClickListener(v -> button_start_patrol());

        bt_option_location = view.findViewById(R.id.cardView_option_location);
        bt_option_location.setOnClickListener(v -> button_option_location());

        optionActivity_bt = view.findViewById(R.id.cardView_option_activity);
        optionActivity_bt.setOnClickListener(v -> button_optionActivity());

        logOut_bt = view.findViewById(R.id.btn_logout);
        logOut_bt.setOnClickListener(v -> button_logOut());
    }


    private void button_create_route(){
        GlobalViewModel viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        viewModel.initializeForRouteCreation();
        navController.navigate(R.id.action_homeFragment_to_createRouteFragment);
    }


    private void button_optionActivity(){
        GlobalViewModel viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        viewModel.initializeForRouteCreation();
        navController.navigate(R.id.action_homeFragment_to_activityFragment);
    }


    private void button_logOut() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext(),R.style.CustomAlertDialogStyle);
        alert.setTitle("LogOut");
        alert.setMessage("Do you really want to quit?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GlobalViewModel viewModel = new ViewModelProvider(requireActivity()).get(GlobalViewModel.class);
                viewModel.initializeForRouteCreation();
                navController.navigate(R.id.action_HomeFragment_to_RegisterOrViewActivityFragment);
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.create().show();
    }


    private void button_start_patrol(){
        GlobalViewModel viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        viewModel.initializeForRouteCreation();
        navController.navigate(R.id.action_homeFragment_to_routeExecutionFragment);
    }


    private void button_option_location(){
        GlobalViewModel viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        viewModel.initializeForRouteCreation();
        navController.navigate(R.id.action_HomeFragment_to_optionLocationFragment);
    }

}