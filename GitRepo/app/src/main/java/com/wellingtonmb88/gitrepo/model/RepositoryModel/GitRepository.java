package com.wellingtonmb88.gitrepo.model.RepositoryModel;


import com.google.gson.annotations.SerializedName;

public class GitRepository {

    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("forks_count")
    private long forksCount;
    @SerializedName("stargazers_count")
    private long starsCount;
    @SerializedName("owner")
    private GitOwner gitOwner;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getForksCount() {
        return forksCount;
    }

    public void setForksCount(long forksCount) {
        this.forksCount = forksCount;
    }

    public long getStarsCount() {
        return starsCount;
    }

    public void setStarsCount(long starsCount) {
        this.starsCount = starsCount;
    }

    public GitOwner getGitOwner() {
        return gitOwner;
    }

    public void setGitOwner(GitOwner gitOwner) {
        this.gitOwner = gitOwner;
    }
}
