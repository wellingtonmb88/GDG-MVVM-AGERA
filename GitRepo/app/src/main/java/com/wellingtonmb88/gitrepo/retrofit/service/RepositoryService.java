package com.wellingtonmb88.gitrepo.retrofit.service;

import com.google.android.agera.Result;
import com.google.android.agera.Supplier;
import com.wellingtonmb88.gitrepo.model.GitRepositories;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RepositoryService {
    String ENDPOINT_REPOSITORY = "search/repositories";

    @GET(ENDPOINT_REPOSITORY)
    Supplier<Result<GitRepositories>> getGitRepositories(@Query("q") String language, @Query("sort") String sortType,
                                                         @Query("page") String page);


    @GET(ENDPOINT_REPOSITORY)
    Supplier<Result<Response<GitRepositories>>> getResponseGitRepositories(@Query("q") String language, @Query("sort") String sortType,
                                                                           @Query("page") String page);
}
