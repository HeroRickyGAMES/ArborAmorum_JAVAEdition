package com.herorickystudios.lovecutey.ui.login;

//Programado por HeroRickyGames

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.herorickystudios.lovecutey.R;

import java.util.List;

public class CardStackAdapter extends RecyclerView.Adapter <CardStackAdapter.ViewHolder> {

    private List<ItemModel> usuarios;

    public CardStackAdapter(List<ItemModel> itens) {
        this.usuarios = itens;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(usuarios.get(position));

    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView nome;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(ItemModel data) {



        }
    }
}
