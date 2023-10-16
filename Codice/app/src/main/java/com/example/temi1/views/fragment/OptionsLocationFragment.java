package com.example.temi1.views.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.temi1.R;
import com.example.temi1.viewmodel.GlobalViewModel;
import org.jetbrains.annotations.NotNull;

public class OptionsLocationFragment extends Fragment {

    private NavController navController;
    private CardView bt_insert,bt_delete, bt_edit;
    private ImageButton bt_back;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_option_location, container, false);
    }


    @Override
    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        bt_insert = view.findViewById(R.id.cardView_insert_location_or_pointInterest);
        bt_insert.setOnClickListener(v -> button_insert_location_or_pointInterest());

        bt_delete = view.findViewById(R.id.cardView_delete_location_or_pointInterest);
        bt_delete.setOnClickListener(v -> button_delete_location_or_pointInterest());

        bt_edit = view.findViewById(R.id.cardView_edit_location_or_pointInterest);
        bt_edit.setOnClickListener(v -> button_edit_location_or_pointInterest());

        bt_back =  view.findViewById(R.id.btn_back);
        bt_back.setOnClickListener(v -> button_back());

    }


    private void button_back(){
        GlobalViewModel viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        viewModel.initializeForRouteCreation();
        navController.navigate(R.id.HomeFragment);
    }


    private void button_insert_location_or_pointInterest(){
        GlobalViewModel viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        viewModel.initializeForRouteCreation();
        navController.navigate(R.id.action_OptionLocationFragment_to_InsertLocationOrPointInterest);
    }


    private void button_delete_location_or_pointInterest(){
        GlobalViewModel viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        viewModel.initializeForRouteCreation();
        navController.navigate(R.id.action_OptionLocationFragment_to_DeleteLocationOrPointInterest);
    }


    private void button_edit_location_or_pointInterest(){
        GlobalViewModel viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        viewModel.initializeForRouteCreation();
        navController.navigate(R.id.action_OptionLocationFragment_to_EditLocationOrPointInterest);
    }
}
