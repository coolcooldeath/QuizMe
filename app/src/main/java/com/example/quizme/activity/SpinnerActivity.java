package com.example.quizme.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.quizme.SpinWheel.LuckyWheelView;
import com.example.quizme.SpinWheel.model.LuckyItem;
import com.example.quizme.databinding.ActivitySpinnerBinding;
import com.example.quizme.retrofit.ApiManager;
import com.example.quizme.retrofit.Coin;
import com.example.quizme.retrofit.FlagObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpinnerActivity extends AppCompatActivity {

    ActivitySpinnerBinding binding;
    String name;
    long coins = 0;
    SharedPreferences mSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpinnerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        List<LuckyItem> data = new ArrayList<>();

        LuckyItem luckyItem1 = new LuckyItem();
        luckyItem1.topText = "25";
        luckyItem1.secondaryText = "МОНЕТ";
        luckyItem1.textColor = Color.parseColor("#212121");
        luckyItem1.color = Color.parseColor("#eceff1");
        data.add(luckyItem1);

        LuckyItem luckyItem2 = new LuckyItem();
        luckyItem2.topText = "30";
        luckyItem2.secondaryText = "МОНЕТ";
        luckyItem2.color = Color.parseColor("#00cf00");
        luckyItem2.textColor = Color.parseColor("#ffffff");
        data.add(luckyItem2);

        LuckyItem luckyItem3 = new LuckyItem();
        luckyItem3.topText = "35";
        luckyItem3.secondaryText = "МОНЕТ";
        luckyItem3.textColor = Color.parseColor("#212121");
        luckyItem3.color = Color.parseColor("#eceff1");
        data.add(luckyItem3);

        LuckyItem luckyItem4 = new LuckyItem();
        luckyItem4.topText = "40";
        luckyItem4.secondaryText = "МОНЕТ";
        luckyItem4.color = Color.parseColor("#7f00d9");
        luckyItem4.textColor = Color.parseColor("#ffffff");
        data.add(luckyItem4);

        LuckyItem luckyItem5 = new LuckyItem();
        luckyItem5.topText = "45";
        luckyItem5.secondaryText = "МОНЕТ";
        luckyItem5.textColor = Color.parseColor("#212121");
        luckyItem5.color = Color.parseColor("#eceff1");
        data.add(luckyItem5);

        LuckyItem luckyItem6 = new LuckyItem();
        luckyItem6.topText = "50";
        luckyItem6.secondaryText = "МОНЕТ";
        luckyItem6.color = Color.parseColor("#dc0000");
        luckyItem6.textColor = Color.parseColor("#ffffff");
        data.add(luckyItem6);

        LuckyItem luckyItem7 = new LuckyItem();
        luckyItem7.topText = "55";
        luckyItem7.secondaryText = "МОНЕТ";
        luckyItem7.textColor = Color.parseColor("#212121");
        luckyItem7.color = Color.parseColor("#eceff1");
        data.add(luckyItem7);

        LuckyItem luckyItem8 = new LuckyItem();
        luckyItem8.topText = "0";
        luckyItem8.secondaryText = "МОНЕТ";
        luckyItem8.color = Color.parseColor("#008bff");
        luckyItem8.textColor = Color.parseColor("#ffffff");
        data.add(luckyItem8);


        binding.wheelview.setData(data);
        binding.wheelview.setRound(5);

        binding.spinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random r = new Random();
                int randomNumber = r.nextInt(8);

                binding.wheelview.startLuckyWheelWithTargetIndex(randomNumber);
            }
        });

        binding.wheelview.setLuckyRoundItemSelectedListener(new LuckyWheelView.LuckyRoundItemSelectedListener() {
            @Override
            public void LuckyRoundItemSelected(int index) {
                updateCash(index);
            }
        });
    }

    void updateCash(int index) {
        long cash = 0;
        switch (index) {
            case 0:
                cash = 25;
                break;
            case 1:
                cash = 30;
                break;
            case 2:
                cash = 35;
                break;
            case 3:
                cash = 40;
                break;
            case 4:
                cash = 45;
                break;
            case 5:
                cash = 20;
                break;
            case 6:
                cash = 55;
                break;
            case 7:
                cash = 0;
                break;
        }
        ApiManager apiManager = ApiManager.getInstance();
        mSettings = getSharedPreferences("mysettings", 0);
        SharedPreferences.Editor editor = mSettings.edit();
        coins = mSettings.getLong("coins",0)+cash;
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

        Toast.makeText(SpinnerActivity.this, "Монеты добавлены в аккаунт!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SpinnerActivity.this, MainActivity.class);
        startActivity(intent);

    }

}