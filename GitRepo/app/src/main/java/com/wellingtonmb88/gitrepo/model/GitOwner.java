package com.wellingtonmb88.gitrepo.model;


import com.google.gson.annotations.SerializedName;

public class GitOwner {

    @SerializedName("id")
    private long id;
    @SerializedName("login")
    private String username;
    @SerializedName("avatar_url")
    String avatarUrl;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
