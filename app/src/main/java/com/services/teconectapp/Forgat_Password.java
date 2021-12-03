package com.services.teconectapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Forgat_Password extends AppCompatActivity {

    private EditText et_email;
    private Button bt_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgat__password);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Forgat Password");
        actionBar.setDisplayHomeAsUpEnabled(true);

        et_email=findViewById(R.id.et_ffp_email_id);
        bt_submit=findViewById(R.id.submit_email);

        HttpLoggingInterceptor httpLoggingInterceptor=new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient=new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
        Retrofit retrofit= new Retrofit.Builder().baseUrl("https://tconectapi.herokuapp.com/api/").addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build();


        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String Email=et_email.getText().toString();
                if(!Email.isEmpty())
                {
                    send_email_to_server(Email,retrofit);
                }
                else
                    Toast.makeText(Forgat_Password.this, "field empty", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void send_email_to_server(String Email,Retrofit retrofit)
    {
        group43 g43=retrofit.create(group43.class);
        email_for_forget_password entering_email=new email_for_forget_password(Email);
        Call<forget_password_response> res=g43.change_password(entering_email);
        res.enqueue(new Callback<forget_password_response>() {
            @Override
            public void onResponse(Call<forget_password_response> call, Response<forget_password_response> response)
            {
                if(response.isSuccessful())
                    Toast.makeText(Forgat_Password.this, response.body().getSuccess()+" "+response.body().getData() + " " +response.body().getError(), Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Forgat_Password.this, response.body().getError(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<forget_password_response> call, Throwable t) {

            }
        });

    }
}