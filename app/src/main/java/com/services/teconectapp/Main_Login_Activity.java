package com.services.teconectapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Main_Login_Activity extends AppCompatActivity {

    private Button bt_New_Account,bt_Direct_Login,bt_Forgat;
    private TextInputEditText et_main_username, et_main_password;
    ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__login__page);

        HttpLoggingInterceptor httpLoggingInterceptor=new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient=new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
        Retrofit retrofit= new Retrofit.Builder().baseUrl("https://tconectapi.herokuapp.com/api/").addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build();


        //action bar
        ActionBar actionBar=getSupportActionBar();

        if(actionBar!=null)
        actionBar.hide();

        //connection
        loadingBar=new ProgressDialog(this);
        bt_New_Account=findViewById(R.id.bt_New_Register);
        bt_Direct_Login=findViewById(R.id.bt_Direct_Login);
        bt_Forgat=findViewById(R.id.bt_Forgat);
        et_main_username=findViewById(R.id.et_main_username);
        et_main_password=findViewById(R.id.et_main_password);

        //New user register button
        bt_New_Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(Main_Login_Activity.this,Phone_Register_Activity.class);
                startActivity(intent);
            }
        });

        bt_Direct_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingBar.setTitle("please wait");
                loadingBar.show();
                fetch(retrofit);
            }
        });

        bt_Forgat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(Main_Login_Activity.this,Forgat_Password.class);
                startActivity(intent);
            }
        });

    }

    private void fetch(Retrofit retrofit)
    {
        String email=et_main_username.getText().toString().trim();
        String password=et_main_password.getText().toString().trim();

        if(email.isEmpty() || password.isEmpty())
        {
            loadingBar.dismiss();
            Toast.makeText(this, "Empty field", Toast.LENGTH_SHORT).show();
            return;
        }

        group43 g43=retrofit.create(group43.class);
        login_request loginRequest=new login_request(email,password);
        Call<login_response> login=g43.user_login(loginRequest);
        login.enqueue(new Callback<login_response>() {
            @Override
            public void onResponse(Call<login_response> call, Response<login_response> response)
            {
                if(response.isSuccessful())
                {
                        save_token_locally(response.body().getToken());
                        Intent intent=new Intent(Main_Login_Activity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                }

                else
                {
                    loadingBar.dismiss();
                    Toast.makeText(Main_Login_Activity.this, "Please Enter valid detail", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<login_response> call, Throwable t)
            {
                Toast.makeText(Main_Login_Activity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void save_token_locally(String token)
    {
        SharedPreferences.Editor editor = getSharedPreferences(New_User_Registration.My_Prefs_filename_token, MODE_PRIVATE).edit();
        editor.putString("token",token);
        editor.commit();
        loadingBar.dismiss();
    }

    private Boolean get_token()
    {
        SharedPreferences prefs=getSharedPreferences(New_User_Registration.My_Prefs_filename_token,MODE_PRIVATE);
        String user_token=prefs.getString("token",null);
        return user_token != null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(get_token())
        {
            Intent intent=new Intent(Main_Login_Activity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        else
            Toast.makeText(this, "login required", Toast.LENGTH_SHORT).show();

    }
}