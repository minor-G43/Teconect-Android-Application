package com.services.teconectapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class UserList_RecyclerAdapter extends RecyclerView.Adapter<UserList_RecyclerAdapter.User_ViewHolder> {

    private ArrayList<userlist> data;
    private connect_list list;
    private Context context;
    private String current_id;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public UserList_RecyclerAdapter(Context context, ArrayList<userlist> data, connect_list list,String current_id)
    {
            this.data=data;
            this.list=list;
            this.context=context;
            this.current_id=current_id;
    }

    @NonNull
    @Override
    public User_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.fetch_user_layout, parent, false);
        User_ViewHolder viewHolder = new User_ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull User_ViewHolder holder, int position)
    {
        String name=data.get(position).getUsername();
        String email=data.get(position).getEmail();
        StringBuilder tags=new StringBuilder();

        holder.username.setText(name);
        holder.email.setText(email);
        holder.description.setText(data.get(position).getDescription());

        for (int i=0;i<data.get(position).getTags().size();i++)
            tags.append("#"+data.get(position).getTags().get(i).trim());

        holder.tag.setText(tags);

       String id=data.get(position).getId();

       if(id.equals(current_id))
       {
           holder.bt.setVisibility(View.VISIBLE);
           holder.bt.setEnabled(false);
           holder.bt2.setVisibility(View.GONE);
           holder.bt3.setVisibility(View.GONE);
           holder.bt4.setVisibility(View.GONE);
       }
       else
           if(check_friend(id,holder))
       {
           holder.bt.setVisibility(View.GONE);
           holder.bt2.setVisibility(View.VISIBLE);
           holder.bt3.setVisibility(View.GONE);
           holder.bt4.setVisibility(View.GONE);
       }
       else
           if (check_inrequest(id,holder))
           {
               holder.bt.setVisibility(View.GONE);
               holder.bt2.setVisibility(View.GONE);
               holder.bt3.setVisibility(View.VISIBLE);
               holder.bt4.setVisibility(View.GONE);
           }
           else
           if (check_outrequest(id,holder))
           {
               holder.bt.setVisibility(View.GONE);
               holder.bt2.setVisibility(View.GONE);
               holder.bt3.setVisibility(View.GONE);
               holder.bt4.setVisibility(View.VISIBLE);
           }
           else
           {
               holder.bt2.setVisibility(View.GONE);
               holder.bt3.setVisibility(View.GONE);
               holder.bt4.setVisibility(View.GONE);
               holder.bt.setVisibility(View.VISIBLE);
           }

           holder.bt.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v)
               {
                   db.collection(id).document("collection").update("inreq", FieldValue.arrayUnion(current_id));
                   db.collection(current_id).document("collection").update("outreq", FieldValue.arrayUnion(id));
                   holder.bt2.setVisibility(View.GONE);
                   holder.bt3.setVisibility(View.GONE);
                   holder.bt.setVisibility(View.GONE);
                   holder.bt4.setVisibility(View.VISIBLE);
               }
           });


           holder.bt3.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   holder.bt2.setVisibility(View.VISIBLE);
                   holder.bt3.setVisibility(View.GONE);
                   holder.bt.setVisibility(View.GONE);
                   holder.bt4.setVisibility(View.GONE);
                   db.collection(id).document("collection").update("friend", FieldValue.arrayUnion(current_id));
                   db.collection(current_id).document("collection").update("friend", FieldValue.arrayUnion(id));
                   db.collection(current_id).document("collection").update("inreq", FieldValue.arrayRemove(id));
                   db.collection(id).document("collection").update("outreq", FieldValue.arrayRemove(current_id));
               }
           });

           holder.bt2.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   openWhatsApp(data.get(position).getPhoneNo());
               }
           });




    }
    public void openWhatsApp(String phoneNo){
        try {
            String text = "This is a test";// Replace with your message.

            String toNumber = "91"+phoneNo; // Replace with mobile phone number without +Sign or leading zeros, but with country code
            //Suppose your country is India and your phone number is “xxxxxxxxxx”, then you need to send “91xxxxxxxxxx”.


            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+toNumber +"&text="+text));
            context.startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean check_outrequest(String id, User_ViewHolder holder)
    {
        for(int i=0;i<list.getOutreq().size();i++)
        {
            if(id.equals(list.getOutreq().get(i)))
            {
                return true;
            }
        }
        return false;
    }

    private boolean check_inrequest(String id, User_ViewHolder holder)
    {
        for(int i=0;i<list.getInreq().size();i++)
        {
            if(id.equals(list.getInreq().get(i)))
            {
                return true;
            }
        }
        return false;
    }


    private boolean check_friend(String id, User_ViewHolder holder)
    {
        for(int i=0;i<list.getFriend().size();i++)
        {
            if(id.equals(list.getFriend().get(i)))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class User_ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView username;
        public TextView email,tag,description;
        public Button bt,bt2,bt3,bt4;
        public User_ViewHolder(View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.username_for_userlist);
            email=itemView.findViewById(R.id.email_for_userlist);
            bt=itemView.findViewById(R.id.connect_user_button);
            bt2=itemView.findViewById(R.id.redirect_to_whatsapp_user_button);
            bt3=itemView.findViewById(R.id.accept_user_button);
            bt4=itemView.findViewById(R.id.pending_user_button);
            tag=itemView.findViewById(R.id.tags_for_userlist);
            description=itemView.findViewById(R.id.description_for_userlist);
        }
    }
}
