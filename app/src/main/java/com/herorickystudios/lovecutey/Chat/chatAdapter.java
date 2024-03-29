package com.herorickystudios.lovecutey.Chat;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.herorickystudios.lovecutey.R;
import com.herorickystudios.lovecutey.User;

import java.util.ArrayList;

public class chatAdapter extends RecyclerView.Adapter<chatAdapter.MyViewHolder> {

    Context context;
    ArrayList<cardsChat> list;
    public ImageView bgPhoto;
    public String st;


    public chatAdapter(Context context, ArrayList<cardsChat> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(context).inflate(R.layout.item_to_menssage,parent,false);
       return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {



        cardsChat users = list.get(position);
        holder.name.setText(users.getName());
        holder.mensage.setText(users.getMenssage());

        Glide.with(context).load(list.get(position).getPhotoBG()).into(holder.photoBG);
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView bgPhoto;
        TextView name, mensage;
        ImageView photoBG;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            name = itemView.findViewById(R.id.usernameCard);
            mensage = itemView.findViewById(R.id.Menssage);
            photoBG = itemView.findViewById(R.id.photoBG);

            //st = mensage.getText().toString();

        }

        @Override
        public void onClick(View v) {

        }
    }



}
