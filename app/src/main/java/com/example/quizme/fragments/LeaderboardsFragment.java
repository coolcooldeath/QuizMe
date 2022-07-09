package com.example.quizme.fragments;

import static com.example.quizme.retrofit.ApiManager.ip;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quizme.R;
import com.example.quizme.retrofit.JsonUserApi;
import com.example.quizme.retrofit.User;
import com.example.quizme.adapters.LeaderboardsAdapter;
import com.example.quizme.databinding.FragmentLeaderboardsBinding;

import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LeaderboardsFragment extends Fragment {

    private void openInternetDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(
                getContext());
        quitDialog.setTitle(R.string.internet).setMessage(R.string.internet_leaderboard_message);

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



    public LeaderboardsFragment() {
        // Required empty public constructor
    }
    public static OkHttpClient.Builder getUnsafeOkHttpClient() {

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    SharedPreferences mSettings;
    ProgressDialog dialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentLeaderboardsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mSettings = getContext().getSharedPreferences("mysettings", 0);
        binding = FragmentLeaderboardsBinding.inflate(inflater, container, false);
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Подождите...");


        final ArrayList<User> users = new ArrayList<>();
        final LeaderboardsAdapter adapter = new LeaderboardsAdapter(getContext(), users);

        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if(isNetworkAvailable(this.getContext())){
            dialog.show();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ip)
                    .addConverterFactory(GsonConverterFactory.create()).client(getUnsafeOkHttpClient().build())
                    .build();

            JsonUserApi jsonUserApi = retrofit.create(JsonUserApi.class);

            Call<List<User>> callLeaderboard = jsonUserApi.getLeaderboard("Bearer "+mSettings.getString("access_token",""));
            Log.d("HttpCodeID", "Bearer "+mSettings.getString("access_token",""));

            callLeaderboard.enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                    if (!response.isSuccessful()) {

                        Log.d("HttpCodeID", "Code: " + response.code());
                        return;
                    }

                    List<User> usersfromApi = response.body();
                    try {
                        for (User user : usersfromApi) {
                            users.add(user);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            users.sort(Comparator.comparing(User::getCoins).reversed());
                        }
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    } catch (Exception ex) {
                        openInternetDialog();
                        Log.d("HttpCodeID", "Code: " + response.code());
                    }

                }




                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {
                    Log.d("HttpCodeID", t.getMessage());
                }
            });
        }
        else{
            openInternetDialog();
        }






        return binding.getRoot();
    }
}