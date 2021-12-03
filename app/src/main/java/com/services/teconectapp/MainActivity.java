package com.services.teconectapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    HttpLoggingInterceptor httpLoggingInterceptor;
    CardView Searching_Using_Tag;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Searching_Using_Tag=findViewById(R.id.search_card);
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        httpLoggingInterceptor=new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient=new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
        Retrofit retrofit= new Retrofit.Builder().baseUrl("https://tconectapi.herokuapp.com/api/").addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build();

        bottomNavigationView.setOnItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();

        Searching_Using_Tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity.this,SearchTagActivity.class);
                startActivity(intent);
            }
        });
    }

    private BottomNavigationView.OnItemSelectedListener navListener=
            new BottomNavigationView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selected_frag = null;

                    switch (item.getItemId())
                    {
                        case R.id.nav_home:
                            selected_frag = new HomeFragment();
                            break;
                        case R.id.nav_projects:
                            selected_frag = new ProjectsFragment();
                            break;
                        case R.id.nav_profile:
                            selected_frag = new ProfileFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selected_frag).commit();

                    return true;
                }
            };
}