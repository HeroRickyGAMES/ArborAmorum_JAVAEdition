package com.herorickystudios.lovecutey;

//Programado por HeroRickyGames

import android.media.Image;


public class cards {
    private String userID;
    private String name;
    private String idade;
    private String local;
    private Image image;
    public cards(String userID , String name, String idade, String local){
        this.userID = userID;
        this.name = name;
        this.idade = idade;
        this.local = local;
        this.image = image;
    }
    public String getUserID(){
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setuserID(String userID){
        this.name = userID;
    }

    public String getidade() {
        return idade;
    }

    public String getIdade() {
        return idade;
    }

    public void setIdade(String idade) {
        this.idade = idade;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
