package com.herorickystudios.lovecutey;

public class MatchesObject {

    private String userID;
    private String name;
    private String profileImageUrl;
    private String isOnline;

    public MatchesObject(String userID, String name, String profileImageUrl, String isOnline){
        this.userID = userID;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.isOnline = isOnline;
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

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }
}
