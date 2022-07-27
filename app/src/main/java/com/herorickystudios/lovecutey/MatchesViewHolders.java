package com.herorickystudios.lovecutey;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.herorickystudios.lovecutey.Chat.ChatActivity;

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

        Intent intent = new Intent(v.getContext(), ChatActivity.class);

        Bundle b = new Bundle();
        b.putString("matchId",MatchID.getText().toString());

        String MatchIDd= MatchID.toString();

        intent.putExtras(b);
        intent.putExtra("MatchIdd", MatchIDd);
        v.getContext().startActivity(intent);
    }
}
