package com.services.teconectapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchTagActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SearchView searchView;
    private Retrofit retrofit;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private connect_list note=null;
    private String id,querylist;
    private ArrayList<userlist> data=new ArrayList<>();
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tag);

        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        searchView=findViewById(R.id.searchview_tag);
        progressBar=findViewById(R.id.search_progressBar);
        recyclerView=findViewById(R.id.search_tag_recyclerview);
        LinearLayoutManager linearLayout=new LinearLayoutManager(SearchTagActivity.this);
        recyclerView.setLayoutManager(linearLayout);

        HttpLoggingInterceptor httpLoggingInterceptor=new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient=new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
        retrofit= new Retrofit.Builder().baseUrl("https://tconectapi.herokuapp.com/api/").addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
           public boolean onQueryTextSubmit(String query) {
               progressBar.setVisibility(View.VISIBLE);
               querylist=query;
               getid();
               return true;
           }

           @Override
           public boolean onQueryTextChange(String newText) {
               return false;
           }
       });
    }

    private void method_list()
    {
        ArrayList<userlist> search_list = new ArrayList<>();

        for(int i=0;i<data.size();i++)
            for(int j=0;j<data.get(i).getTags().size();j++)
                if(data.get(i).getTags().get(j).trim().equals(querylist))
                {
                    search_list.add(data.get(i));
                }

        UserList_RecyclerAdapter recyclerAdapter=new UserList_RecyclerAdapter(SearchTagActivity.this,search_list,note,id);
        recyclerView.setAdapter(recyclerAdapter);
        progressBar.setVisibility(View.GONE);
    }

    private void intview()
    {
        loadjson();
    }

    private void loadjson()
    {
        group43 g43=retrofit.create(group43.class);
        Call<userlist_response> call=g43.fetchUserList(get_token());
        call.enqueue(new Callback<userlist_response>() {
            @Override
            public void onResponse(Call<userlist_response> call, Response<userlist_response> response)
            {
                if (response.isSuccessful())
                {
                    data=response.body().getData();
                    method_list();
                }

            }

            @Override
            public void onFailure(Call<userlist_response> call, Throwable t) {

            }
        });
    }

    private void getid()
    {
        String user_token = get_token();
        group43 g43=retrofit.create(group43.class);
        Call<profile_response> Profile_Response=g43.getProfile(user_token);
        Profile_Response.enqueue(new Callback<profile_response>() {
            @Override
            public void onResponse(Call<profile_response> call, Response<profile_response> response)
            {
                if(response.isSuccessful())
                {
                    id=response.body().getData().get_id();
                    fetch_data_from_firestore();
                }
                else
                {

                }
            }

            @Override
            public void onFailure(Call<profile_response> call, Throwable t) {

            }
        });
    }

    private String get_token()
    {
        SharedPreferences sharedPreferences=getSharedPreferences(New_User_Registration.My_Prefs_filename_token, Context.MODE_PRIVATE);
        return sharedPreferences.getString("token",null);
    }

    private void fetch_data_from_firestore()
    {

        /*db.collection("619f6de399616854dd6224a5").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(@NotNull QuerySnapshot queryDocumentSnapshots) {

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            note = documentSnapshot.toObject(connect_list.class);
                            print();
                        }
                    }
                });*/

        db.collection(id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        note=document.toObject(connect_list.class);
                        intview();
                    }
                }
                else {

                }
            }
        });
    }
}