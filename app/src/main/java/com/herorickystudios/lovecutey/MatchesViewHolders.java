package com.herorickystudios.lovecutey;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MatchesViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView MatchID, MatchName;
    public ImageView matchImage;

    public MatchesViewHolders(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        MatchID = (TextView) itemView.findViewById(R.id.matchid);
        MatchName = (TextView) itemView.findViewById(R.id.matchName);
        matchImage = (ImageView) itemView.findViewById(R.id.matchImage);
    }

    @Override
    public void onClick(View v) {

    }
}
