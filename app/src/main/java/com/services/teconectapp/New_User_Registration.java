package com.services.teconectapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.net.URI;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class New_User_Registration extends AppCompatActivity
{
    private Button submit;
    private TextInputEditText et_Username, et_Email, et_Password, et_Github, et_Techstack, et_Tags, et_Projects, et_Description;
    private String Username, Email, Password, Github, Techstack, Tags, Projects, Description;
    private group43 G43;
    ProgressDialog loadingBar;
    public static final String My_Prefs_filename_token="com.services.teconectapp";
    private String moblie_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_registration);

        HttpLoggingInterceptor httpLoggingInterceptor=new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient=new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
        Retrofit retrofit= new Retrofit.Builder().baseUrl("https://tconectapi.herokuapp.com/api/").addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build();


        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Sign Up");
        actionBar.setDisplayHomeAsUpEnabled(true);

        moblie_number=getIntent().getStringExtra("Mobile_number");


        loadingBar=new ProgressDialog(this);
        submit=findViewById(R.id.bt_sign_in);
        et_Username=findViewById(R.id.et_reg_username);
        et_Email=findViewById(R.id.et_reg_email);
        et_Password=findViewById(R.id.et_reg_password);
        et_Github=findViewById(R.id.et_reg_github);
        et_Techstack=findViewById(R.id.et_reg_teckstack);
        et_Tags=findViewById(R.id.et_reg_tags);
        et_Projects=findViewById(R.id.et_reg_project);
        et_Description=findViewById(R.id.et_reg_description);




        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingBar.show();
                fetch_all_detail();
                post_request(retrofit);

            }
        });



    }

    private void post_request(Retrofit retrofit)
    {
        G43=retrofit.create(group43.class);
        Info info=new Info(Username,Email,moblie_number,Password,Github,Techstack,Tags,Projects,Description);
        Call<ResponseBack> call=G43.createInfo(info);

        call.enqueue(new Callback<ResponseBack>() {
            @Override
            public void onResponse(Call<ResponseBack> call, Response<ResponseBack> response)
            {
                if(response.isSuccessful())
                {
                    ResponseBack responseBack=response.body();
                    String token=responseBack.getToken();
                    Login(Username,Password,token);
                }
                else
                {
                    loadingBar.dismiss();
                    Toast.makeText(New_User_Registration.this, "sorry", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBack> call, Throwable t) {

            }
        });
    }

    private void Login(String username,String password,String token)
    {
        login_request Login_Request=new login_request(username,password);
        Call<login_response> login_call=G43.user_Register(Login_Request,token);
        login_call.enqueue(new Callback<login_response>() {
            @Override
            public void onResponse(Call<login_response> call, Response<login_response> response)
            {
                if(response.isSuccessful())
                {
                    login_response Login_Response=response.body();
                    Save_token_to_local_memory(Login_Response);//shared_preference
                    Intent intent=new Intent(New_User_Registration.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    loadingBar.dismiss();
                    Toast.makeText(New_User_Registration.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<login_response> call, Throwable t) {

            }
        });
    }

    private void Save_token_to_local_memory(login_response login_response)
    {
        SharedPreferences.Editor editor = getSharedPreferences(My_Prefs_filename_token, MODE_PRIVATE).edit();
        editor.putString("token",login_response.getToken());
        editor.commit();
        loadingBar.dismiss();
    }

    private void fetch_all_detail()
    {
        Username=et_Username.getText().toString().trim();
        Email=et_Email.getText().toString().trim();
        Password=et_Password.getText().toString().trim();
        Github=et_Github.getText().toString().trim();
        Techstack=et_Techstack.getText().toString().trim();
        Tags=et_Tags.getText().toString().trim();
        Projects=et_Projects.getText().toString().trim();
        Description=et_Description.getText().toString().trim();
    }
}