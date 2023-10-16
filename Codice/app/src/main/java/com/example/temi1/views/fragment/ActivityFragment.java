package com.example.temi1.views.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import com.example.temi1.R;
import com.example.temi1.viewmodel.GlobalViewModel;

import org.jetbrains.annotations.NotNull;

public class ActivityFragment extends Fragment  {

    private ImageButton bt_back;
    private NavController navController;
    private CardView bt_insert_activity,bt_viewList;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_activity, container, false);
    }


    @Override
    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        bt_back =  view.findViewById(R.id.btn_back);
        bt_back.setOnClickListener(v -> button_back());

        bt_insert_activity = view.findViewById(R.id.cardView_insert_activity);
        bt_insert_activity.setOnClickListener(v -> button_insert_activity());

        bt_viewList = view.findViewById(R.id.cardView_view_list_activity);
        bt_viewList.setOnClickListener(v -> button_view_list_activity());
    }


    private void button_back(){
        GlobalViewModel viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        viewModel.initializeForRouteCreation();
        navController.navigate(R.id.action_ActivityFragment_to_HomeFragment);
    }


    private void button_insert_activity(){
        GlobalViewModel viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        viewModel.initializeForRouteCreation();
        navController.navigate(R.id.action_ActivityFragment_to_insertActivityFragment);
    }


    private void button_view_list_activity(){
        GlobalViewModel viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        viewModel.initializeForRouteCreation();
        navController.navigate(R.id.action_ActivityFragment_to_ViewListActivityFragment);
    }
}