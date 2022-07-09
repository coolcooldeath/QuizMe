package com.example.quizme.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quizme.activity.LoginActivity;
import com.example.quizme.R;
import com.example.quizme.retrofit.ApiManager;
import com.example.quizme.retrofit.FlagObject;
import com.example.quizme.retrofit.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {

    SharedPreferences mSettings;
    public ProfileFragment() {
        // Required empty public constructor
    }

    private void openInternetDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(
                getContext());
        quitDialog.setTitle(R.string.internet).setMessage(R.string.internet_message);

        quitDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        quitDialog.show();
    }
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        mSettings = getContext().getSharedPreferences("mysettings", 0);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile,
                container, false);
        ApiManager apiManager = ApiManager.getInstance();
        EditText passwordBox = (EditText)view.findViewById(R.id.passBox);
        EditText nameBox = (EditText)view.findViewById(R.id.nameBox);
        mSettings = getContext().getSharedPreferences("mysettings", 0);
        String username = mSettings.getString("username","");
        nameBox.setText(username);
        Button button = (Button) view.findViewById(R.id.exit);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putBoolean("authorize", false);
                editor.apply();
                button.setEnabled(false);
                getActivity().finish();
                    startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });

        Button buttonChangePassword = (Button) view.findViewById(R.id.updateBtn);
        buttonChangePassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                User user = new User();
                user.setLogin(username);
                user.setPassword(String.valueOf(passwordBox.getText()));
                if(isNetworkAvailable(getContext())){ if(!passwordBox.getText().toString().isEmpty()){
                    apiManager.setPassword(user,"Bearer "+mSettings.getString("access_token",""),new Callback<FlagObject>() {
                        @Override
                        public void onResponse(Call<FlagObject> call, Response<FlagObject> response) {
                            FlagObject responseFlag = response.body();

                            if (response.isSuccessful() && responseFlag != null) {
                                Log.d("HttpCodeID","code " + response.code());
                                Log.d("HttpCodeID","responseFlag " + responseFlag.isSuccessful());
                                Toast.makeText(getContext(), R.string.password_changed, Toast.LENGTH_SHORT).show();
                                passwordBox.setText("");

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

                }
                else{
                    Toast.makeText(getContext(), "Пароль не может быть пустым!", Toast.LENGTH_SHORT).show();

                }
                }
                else{
                    openInternetDialog();
                }


            }
        });
        return view;
    }







}