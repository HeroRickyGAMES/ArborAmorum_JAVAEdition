package com.herorickystudios.lovecutey.Chat;

//Programado por HeroRickyGames


import java.util.List;

public class cardsChat {
    private String name;
    //private String menssage;


    public cardsChat(List<String> name){
        //this.menssage = menssage;
        this.name = String.valueOf(name);


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
