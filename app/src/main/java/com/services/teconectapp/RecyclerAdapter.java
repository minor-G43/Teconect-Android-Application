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
        private connect_list connectList;
        private String Current_id;

    public RecyclerAdapter(Context context, ArrayList<project_list> list, connect_list connectList, String Current_id)
    {
        this.context=context;
        this.list=list;
        this.connectList=connectList;
        this.Current_id=Current_id;
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


        String Owner_id=list.get(position).getOwnerid();

        if(!Current_id.equals(Owner_id))
        {
            for(int i=0;i<connectList.getInreq().size();i++)
            {
                if(Owner_id.equals(connectList.getInreq().get(i)))
                {
                    holder.project_accept_button.setVisibility(View.VISIBLE);
                }
            }

            for(int i=0;i<connectList.getOutreq().size();i++)
            {
                if(Owner_id.equals(connectList.getOutreq().get(i)))
                {
                    holder.project_pending_button.setVisibility(View.VISIBLE);
                }
            }

            for(int i=0;i<connectList.getFriend().size();i++)
            {
                if(Owner_id.equals(connectList.getFriend().get(i)))
                {
                    holder.project_whatsapp_redirect.setVisibility(View.VISIBLE);
                }
            }

        }
        else {
            holder.project_accept_button.setVisibility(View.GONE);
            holder.project_pending_button.setVisibility(View.GONE);
            holder.project_whatsapp_redirect.setVisibility(View.GONE);
            holder.project_connect_button.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView project_title,project_desc;
        public Button project_view_button,project_connect_button,project_accept_button,project_pending_button,project_whatsapp_redirect;



        public ViewHolder(View itemView) {
            super(itemView);
            project_title=itemView.findViewById(R.id.fetch_project_title);
            project_desc=itemView.findViewById(R.id.fetch_project_desc);
            project_view_button=itemView.findViewById(R.id.fetch_project_view_project);
            project_connect_button=itemView.findViewById(R.id.fetch_project_connect);
            project_accept_button=itemView.findViewById(R.id.accept_project_user_button);
            project_pending_button=itemView.findViewById(R.id.pending_project_user_button);
            project_whatsapp_redirect=itemView.findViewById(R.id.redirect_to_whatsapp_project_user_button);
        }
    }
}