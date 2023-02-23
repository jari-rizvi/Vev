package com.teamx.vevae.fragments.bottomLogin;


import android.os.Bundle;

import androidx.navigation.NavController;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamx.vevae.databinding.BottomSheetLoginBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class BottomSheetLoginFragment extends BottomSheetDialogFragment  {

    private BottomSheetLoginBinding bottomSheetLoginBinding;
    private String deviceToken;
    public NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_blank, container, false);
        bottomSheetLoginBinding = BottomSheetLoginBinding.inflate(getLayoutInflater(),container,false);



        return bottomSheetLoginBinding.getRoot();
    }




}