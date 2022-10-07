package com.herorickystudios.lovecutey.Chat;

//Programado por HeroRickyGames


public class cardsChat {
    private String name;
    private String menssage;


    public cardsChat(String name, String menssage){
        this.menssage = menssage;
        this.name = String.valueOf(name);


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

    public void setMenssage(String menssage) {
        this.menssage = menssage;
    }
}
