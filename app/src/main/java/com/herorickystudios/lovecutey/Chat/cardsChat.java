package com.herorickystudios.lovecutey.Chat;

//Programado por HeroRickyGames


import android.widget.ImageView;

public class cardsChat {
    private String name;
    private String menssage;
    private String photoBG;


    public cardsChat(String name, String menssage, String photoBG){
        this.menssage = menssage;
        this.name = String.valueOf(name);
        this.photoBG = photoBG;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMenssage() {
        return menssage;
    }

    public String getPhotoBG() {
        return photoBG;
    }

    public void setPhotoBG(String photoBG) {
        this.photoBG = photoBG;
    }

    public void setMenssage(String menssage) {
        this.menssage = menssage;
    }
}
