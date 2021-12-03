package com.services.teconectapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {

    private View v;
    private Context mycontext;
    private TextView pro_name,pro_tags,pro_des,pro_project,pro_email,pro_git;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_profile,container,false);
        Button user_signOut=v.findViewById(R.id.user_signout);
        pro_name=v.findViewById(R.id.pro_name);
        pro_tags=v.findViewById(R.id.pro_tags);
        pro_des=v.findViewById(R.id.pro_des);
        pro_email=v.findViewById(R.id.pro_email);
        pro_project=v.findViewById(R.id.pro_project);
        pro_git=v.findViewById(R.id.pro_git);
        progressBar=v.findViewById(R.id.pro_progressBar);
        progressBar.setVisibility(View.VISIBLE);


        user_signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edit_Token();
                String user_token=getToken();

                if(user_token==null)
                {
                    Intent intent=new Intent(getContext(),Main_Login_Activity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
                else
                {
                    Toast.makeText(getContext(), "try again", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return v;
    }

    private void edit_Token()
    {
        SharedPreferences.Editor editor = mycontext.getSharedPreferences(New_User_Registration.My_Prefs_filename_token, MODE_PRIVATE).edit();
        editor.putString("token",null);
        editor.commit();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiveMsg();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mycontext=context;
    }

    public void receiveMsg()
    {
        HttpLoggingInterceptor httpLoggingInterceptor=new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient=new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
        Retrofit retrofit= new Retrofit.Builder().baseUrl("https://tconectapi.herokuapp.com/api/").addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build();
        fetch_profile(retrofit);
    }

    private void fetch_profile(Retrofit retrofit)
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
                    String Name=response.body().getData().getUsername();
                    String Desc=response.body().getData().getDescription();
                    String Email="Email : " +response.body().getData().getEmail();
                    String git="Github : " + response.body().getData().getGithub();
                    String project="Project : " +response.body().getData().getProject();
                    StringBuilder tags= new StringBuilder();
                    for(int i=0;i<response.body().getData().getTags().size();i++)
                    {
                        tags.append(" #" + response.body().getData().getTags().get(i));
                    }

                    display(Name,Desc,Email,git,tags,project);
                    progressBar.setVisibility(View.INVISIBLE);
                }
                else
                {
                    Toast.makeText(mycontext, "load failed, try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<profile_response> call, Throwable t) {

            }
        });
    }

    private void display(String name, String desc, String email, String git, StringBuilder tags,String project)
    {
        pro_name.setText(name);
        pro_des.setText(desc);
        pro_email.setText(email);
        pro_project.setText(project);
        pro_git.setText(git);
        pro_tags.setText("Tags " + tags);
    }

    private String getToken()
    {
        SharedPreferences prefs=mycontext.getSharedPreferences(New_User_Registration.My_Prefs_filename_token,MODE_PRIVATE);
        return prefs.getString("token",null);
    }


}
