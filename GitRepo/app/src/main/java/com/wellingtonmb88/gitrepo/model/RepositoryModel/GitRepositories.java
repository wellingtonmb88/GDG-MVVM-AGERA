package com.wellingtonmb88.gitrepo.model.RepositoryModel;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GitRepositories {

    @SerializedName("items")
    private List<GitRepository> gitRepositoryList;

    public List<GitRepository> getGitRepositoryList() {
        return gitRepositoryList;
    }

    public void setGitRepositoryList(ArrayList<GitRepository> gitRepositoryList) {
        this.gitRepositoryList = gitRepositoryList;
    }
}
