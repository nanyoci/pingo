package com.example.pingo;

import java.util.ArrayList;

public class User {
    private String username;
    private String profileUrl;
//    private ArrayList <String> friends = new ArrayList<>();

    public User(String username, String profileUrl) {
        this.username = username;
        this.profileUrl = profileUrl;
    }

    public User(String username) {
        this.username = username;
        this.profileUrl = "default";
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", profileUrl='" + profileUrl + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

}
