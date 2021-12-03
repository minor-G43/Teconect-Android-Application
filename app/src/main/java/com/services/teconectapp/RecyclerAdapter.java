package com.services.teconectapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
        private Context context;
        private ArrayList<project_list> list;

    public RecyclerAdapter(Context context, ArrayList<project_list> list)
    {
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.fetch_project_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.project_title.setText(list.get(position).getTitle());
        holder.project_desc.setText(list.get(position).getDescription());
        holder.project_view_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                try
                {
                    Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(list.get(position).getUrl()));
                    context.startActivity(intent);
                }
                catch(Exception e)
                {
                    Toast.makeText(context,"Sorry cannot open, Incorrect Url provided by post owner." , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView project_title,project_desc;
        public Button project_view_button;



        public ViewHolder(View itemView) {
            super(itemView);
            project_title=itemView.findViewById(R.id.fetch_project_title);
            project_desc=itemView.findViewById(R.id.fetch_project_desc);
            project_view_button=itemView.findViewById(R.id.fetch_project_view_project);
        }
    }
}