package com.herorickystudios.lovecutey;

//Programado por HeroRickyGames

import com.herorickystudios.lovecutey.Chat.Users;

public class User extends Users {

    public String getUserID() {
        return userID;
    }

    public String setUserID(String userID) {
        this.userID = userID;
        return userID;
    }

    private String userID;
    public User() {
    }

}
