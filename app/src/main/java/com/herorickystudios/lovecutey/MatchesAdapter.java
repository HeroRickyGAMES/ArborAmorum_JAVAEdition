package com.herorickystudios.lovecutey;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesViewHolders> {
    private List<MatchesObject> matchesList;
    private Context context;


    public MatchesAdapter(List<MatchesObject> matchesList, Context context){
        this.matchesList = matchesList;
        this.context = context;
    }

    @NonNull
    @Override
    public MatchesViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_matches, null, false);

        RecyclerView.LayoutParams Ip = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(Ip);
        MatchesViewHolders rov = new MatchesViewHolders((layoutView));

        return rov;
    }

    @Override
    public void onBindViewHolder(@NonNull MatchesViewHolders holder, int position) {
        holder.MatchID.setText(matchesList.get(position).getUserID());
        holder.MatchName.setText(matchesList.get(position).getName());
        Glide.with(context).load(matchesList.get(position).getProfileImageUrl()).into(holder.matchImage);
    }

    @Override
    public int getItemCount() {

        Integer itemCount = matchesList.size();
        return itemCount;
    }
}
