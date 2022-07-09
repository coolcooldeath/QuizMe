package com.example.quizme.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.quizme.R;
import com.example.quizme.activity.SpinnerActivity;
import com.example.quizme.adapters.CategoryAdapter;
import com.example.quizme.databinding.FragmentHomeBinding;
import com.example.quizme.db.CategoryModel;
import com.example.quizme.db.DbHelper;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    DbHelper databaseHelper;
    SQLiteDatabase db;
    public int i = 0;
    FragmentHomeBinding binding;
    SharedPreferences mSettings;



    public HomeFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DbHelper(getActivity().getApplicationContext());
        db = databaseHelper.getReadableDatabase();




    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        binding.spinwheel.setEnabled(false);
        binding.spinwheel.setBackgroundColor(Color.GRAY);
        mSettings =  getActivity().getSharedPreferences("mysettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        i = mSettings.getInt("levels",5);

        binding.time.setText("Прохождений до награды - "+i);
        if(i<=0){
            binding.spinwheel.setEnabled(true);
            binding.spinwheel.setBackgroundResource(R.drawable.background);

        }

        ArrayList<CategoryModel> categories = new ArrayList<>();
        final CategoryAdapter adapter = new CategoryAdapter(getContext(), categories);
        for (CategoryModel cat : databaseHelper.getAllCategories()) {
            if(!databaseHelper.getAllCatQuestions(cat.getCategoryId()).isEmpty())
            categories.add(cat);
        }


        adapter.notifyDataSetChanged();

        binding.categoryList.setLayoutManager(new GridLayoutManager(getContext(),2));
        binding.categoryList.setAdapter(adapter);


        binding.spinwheel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.spinwheel.setEnabled(false);
                Log.d("HttpCodeID", "onClick: yes ");
                binding.spinwheel.setEnabled(false);
                binding.spinwheel.setBackgroundColor(Color.GRAY);
                i=5;
                binding.time.setText("Прохождений до награды - "+i);
                editor.putInt("levels", i );
                editor.commit();
                startActivity(new Intent(getContext(), SpinnerActivity.class));

            }
        });


        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}