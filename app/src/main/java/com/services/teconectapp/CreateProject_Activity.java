package com.services.teconectapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateProject_Activity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextInputEditText project_name;
    private TextInputEditText project_url;
    private TextInputEditText project_des;
    private TextInputEditText project_tech;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project_);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Add Project");
        actionBar.setDisplayHomeAsUpEnabled(true);

        HttpLoggingInterceptor httpLoggingInterceptor=new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient=new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
        Retrofit retrofit= new Retrofit.Builder().baseUrl("https://tconectapi.herokuapp.com/api/").addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build();

        Button create_project_bt=findViewById(R.id.create_project_button);
        project_name=findViewById(R.id.create_project_name2);
        project_url=findViewById(R.id.create_project_url2);
        project_des=findViewById(R.id.create_project_desc2);
        project_tech=findViewById(R.id.create_project_tech2);
        progressBar=findViewById(R.id.create_project_progress_bar);

        create_project_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                progressBar.setVisibility(View.VISIBLE);
                String name=project_name.getText().toString();
                String pro_url=project_url.getText().toString();
                String pro_des=project_des.getText().toString();
                String pro_tech=project_tech.getText().toString();

                if(name.isEmpty() || pro_url.isEmpty() || pro_des.isEmpty() || pro_tech.isEmpty())
                    Toast.makeText(CreateProject_Activity.this, "Empty field", Toast.LENGTH_SHORT).show();
                else
                    fetch(retrofit,name,pro_url,pro_des,pro_tech);
            }
        });
    }

    private void fetch(Retrofit retrofit, String title, String pro_url, String pro_des, String pro_tech)
    {
        create_project createProject=new create_project(title,pro_url,pro_des,pro_tech);
        group43 g43=retrofit.create(group43.class);

        Call<create_project_response> call=g43.createProject(createProject,getToken());

        call.enqueue(new Callback<create_project_response>() {
            @Override
            public void onResponse(Call<create_project_response> call, Response<create_project_response> response)
            {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful())
                {
                    Toast.makeText(CreateProject_Activity.this, "Done", Toast.LENGTH_SHORT).show();
                    project_name.setText("");
                    project_url.setText("");
                    project_des.setText("");
                    project_tech.setText("");
                }
                else
                    Toast.makeText(CreateProject_Activity.this, "fail", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<create_project_response> call, Throwable t) {

            }
        });

    }

    private String getToken()
    {
        SharedPreferences prefs=getSharedPreferences(New_User_Registration.My_Prefs_filename_token,MODE_PRIVATE);
        return prefs.getString("token",null);
    }
}