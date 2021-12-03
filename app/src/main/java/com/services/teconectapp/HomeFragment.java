package com.services.teconectapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeFragment extends Fragment {

    private View v;
    private RecyclerView recyclerView;
    private Retrofit retrofit;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private connect_list note=null;
    private ProgressBar progressBar;
    private Context myContext;
    String id;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home,container,false);
        HttpLoggingInterceptor httpLoggingInterceptor=new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient=new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
        retrofit= new Retrofit.Builder().baseUrl("https://tconectapi.herokuapp.com/api/").addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build();
        //db.collection("619f6de399616854dd6224a5").document("collection").update("friend",FieldValue.arrayUnion("123"));
        getid();
        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myContext=context;
    }

    private void intview()
    {
        progressBar=v.findViewById(R.id.home_progressbar);
        recyclerView=v.findViewById(R.id.home_recyclerview);
        LinearLayoutManager linearLayout=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayout);
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
                    progressBar.setVisibility(View.GONE);
                    ArrayList<userlist> data=response.body().getData();
                    UserList_RecyclerAdapter recyclerAdapter=new UserList_RecyclerAdapter(myContext,data,note,id);
                    recyclerView.setAdapter(recyclerAdapter);
                }

            }

            @Override
            public void onFailure(Call<userlist_response> call, Throwable t) {

            }
        });
    }

    public String get_token()
    {
        SharedPreferences sharedPreferences=myContext.getSharedPreferences(New_User_Registration.My_Prefs_filename_token, Context.MODE_PRIVATE);
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

}
