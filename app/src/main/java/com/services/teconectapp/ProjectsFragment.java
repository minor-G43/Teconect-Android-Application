package com.services.teconectapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import static android.content.Context.MODE_PRIVATE;

public class ProjectsFragment extends Fragment {

    private View v;
    private RecyclerView recyclerView;
    private Retrofit retrofit;
    private Context mycontext;
    ProgressBar progressBar;
    private String id;
    private connect_list  note;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_project,container,false);
        Button bt=v.findViewById(R.id.create_project);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mycontext,CreateProject_Activity.class);
                startActivity(intent);
            }
        });

        HttpLoggingInterceptor httpLoggingInterceptor=new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient=new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
        retrofit= new Retrofit.Builder().baseUrl("https://tconectapi.herokuapp.com/api/").addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build();
        getid();


        return v;


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mycontext=context;
    }

    private void initViews()
    {
        recyclerView = v.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        progressBar=v.findViewById(R.id.progress_bar_fetch_project);
        progressBar.setVisibility(View.VISIBLE);
        loadJson();
    }

    private void loadJson()
    {
        group43 g43=retrofit.create(group43.class);
        Call<fetch_project_response> call=g43.fetchProject(getToken());

        call.enqueue(new Callback<fetch_project_response>() {
            @Override
            public void onResponse(Call<fetch_project_response> call, Response<fetch_project_response> response)
            {
                progressBar.setVisibility(View.GONE);
               if(response.isSuccessful())
               {
                   ArrayList<project_list> list=response.body().getData();
                   RecyclerAdapter recyclerAdapter = new RecyclerAdapter(getContext(),list,note,id);
                   recyclerView.setAdapter(recyclerAdapter);
               }
            }

            @Override
            public void onFailure(Call<fetch_project_response> call, Throwable t) {

            }
        });


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
                        initViews();
                    }
                }
                else {

                }
            }
        });
    }

    private void getid()
    {
        String user_token = getToken();
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

    private String getToken()
    {
        SharedPreferences prefs=mycontext.getSharedPreferences(New_User_Registration.My_Prefs_filename_token,MODE_PRIVATE);
        return prefs.getString("token",null);
    }
}
