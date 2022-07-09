package com.example.quizme.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.quizme.databinding.FragmentStartupBinding;


public class StartupFragment extends Fragment {

    public StartupFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    FragmentStartupBinding binding;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentStartupBinding.inflate(inflater, container, false);


        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}