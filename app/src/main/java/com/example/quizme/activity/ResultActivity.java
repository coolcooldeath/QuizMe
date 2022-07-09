package com.example.quizme.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.example.quizme.databinding.ActivityResultBinding;
import com.example.quizme.db.DbHelper;
import com.example.quizme.retrofit.ApiManager;
import com.example.quizme.retrofit.Coin;
import com.example.quizme.retrofit.FlagObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultActivity extends AppCompatActivity {

    ActivityResultBinding binding;
    int POINTS = 10;
    long coins = 0;
    String name;
    DbHelper databaseHelper;
    SQLiteDatabase db;
    SharedPreferences mSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseHelper = new DbHelper(this.getApplicationContext());
        db = databaseHelper.getWritableDatabase();
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ApiManager apiManager = ApiManager.getInstance();
        int correctAnswers = getIntent().getIntExtra("correct", 0);
        /*int totalQuestions = getIntent().getIntExtra("total", 0);*/
        long points = correctAnswers * POINTS;
        mSettings = getSharedPreferences("mysettings", 0);
        SharedPreferences.Editor editor = mSettings.edit();
        coins = mSettings.getLong("coins",0)+points;
        Log.d("HttpCodeID","coins " + coins);
        name = mSettings.getString("username","");
        Coin coin = new Coin();
        coin.setCoins(coins);
        coin.setUsername(name);
        editor.putLong("coins", coins );
        editor.commit();



        apiManager.addCoins(coin,"Bearer "+mSettings.getString("access_token",""), new Callback<FlagObject>() {
            @Override
            public void onResponse(Call<FlagObject> call, Response<FlagObject> response) {
                FlagObject responseFlag = response.body();

                if (response.isSuccessful() && responseFlag != null) {
                    Log.d("HttpCodeID","code " + response.code());
                    Log.d("HttpCodeID","responseFlag " + responseFlag.isSuccessful());


                } else {
                   /* dialog.dismiss();
                    openInvalidLoginDialog();*/

                }
            }

            @Override
            public void onFailure(Call<FlagObject> call, Throwable t) {
                /*openInvalidLoginDialog();*/
                Log.d("HttpCodeID","Error is " + t.getMessage());
            }
        });
       /* binding.score.setText(String.format("%d/%d", correctAnswers, totalQuestions));*/
        binding.earnedCoins.setText(String.valueOf(points));

        /*databaseHelper.addMoney((int) points);*/

        binding.restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.restartBtn.setEnabled(false);
                startActivity(new Intent(ResultActivity.this, MainActivity.class));
                finishAffinity();
            }
        });


    }

    @Override
    public void onBackPressed() {

    }
}