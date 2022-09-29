package com.herorickystudios.lovecutey;

//Programado por HeroRickyGames


public class cards {
    private String userID;
    private String name;
    private String idade;
    private String local;
    private String profileImageURI;

    public cards(String userID, String name){
        this.userID = userID;
        this.name = name;
        this.idade = idade;
        this.local = local;
        this.profileImageURI = profileImageURI;

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

    public String getProfileImageURI() {
        return profileImageURI;
    }

    public void setProfileImageURI(String profileImageURI) {
        this.profileImageURI = profileImageURI;
    }
}
