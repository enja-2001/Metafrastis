package com.example.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Contact;
import com.example.myapplication.R;

import java.util.ArrayList;

public class Contacts extends RecyclerView.Adapter<Contacts.MyViewHolder> {

    ArrayList<Contact> contactList;
    Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView tvName;
        public TextView tvPhoneNumber;
        public TextView tvImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName=itemView.findViewById(R.id.tvName);
            tvPhoneNumber=itemView.findViewById(R.id.tvPhoneNumber);
            tvImage=itemView.findViewById(R.id.tvImage);
        }
    }

    public Contacts(ArrayList<Contact> contactList, Context context){
        this.contactList = contactList;
        this.context = context;
    }

    @NonNull
    @Override
    public Contacts.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_contact_list,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Contacts.MyViewHolder holder, int position) {

        holder.tvName.setText(contactList.get(position).getName());
        holder.tvPhoneNumber.setText(contactList.get(position).getPhoneNumber());
        holder.tvImage.setText(""+contactList.get(position).getName().charAt(0));
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }
}
