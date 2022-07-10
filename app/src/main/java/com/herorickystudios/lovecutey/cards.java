package com.herorickystudios.lovecutey;

//Programado por HeroRickyGames

public class cards {
    private String userID;
    private String name;
    private String idade;
    private String local;
    public cards(String value, String userID , String name, String idade, String local){
        this.userID = userID;
        this.name = name;
        this.idade = idade;
        this.local = local;
    }
    public String getUserID(){
        return userID;
    }
    public void setUserID(String userID){
        this.userID = userID;
    }

    public String getName(){
        return userID;
    }
    public void setName(String name){
        this.name = name;
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
}
