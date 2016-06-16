package com.wellingtonmb88.gitrepo.viewmodel;


import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.agera.Function;
import com.google.android.agera.Functions;
import com.google.android.agera.Predicate;
import com.google.android.agera.Receiver;
import com.google.android.agera.Repository;
import com.google.android.agera.RepositoryConfig;
import com.google.android.agera.Result;
import com.google.android.agera.Updatable;
import com.wellingtonmb88.gitrepo.BR;
import com.wellingtonmb88.gitrepo.model.GitRepositories;
import com.wellingtonmb88.gitrepo.model.GitRepository;
import com.wellingtonmb88.gitrepo.retrofit.RetrofitFactory;
import com.wellingtonmb88.gitrepo.retrofit.service.RepositoryService;

import java.util.List;
import java.util.concurrent.ExecutorService;

import me.drakeet.retrofit2.adapter.agera.HttpException;
import retrofit2.Response;

import static com.google.android.agera.Repositories.repositoryWithInitialValue;
import static java.util.concurrent.Executors.newSingleThreadExecutor;

public class RepositoryListViewModel extends BaseObservable implements Updatable, Receiver<List<GitRepository>> {


    private static final ExecutorService NETWORK_EXECUTOR = newSingleThreadExecutor();

    private Repository<Result<List<GitRepository>>> mBackground;

    private boolean mIsLoading = false;
    private boolean mIsLoadingMore = false;

    private String mErrorMessage;

    private List<GitRepository> mGitRepositoryList;

    public RepositoryListViewModel() {
    }

    public void addUpdatable() {
        // Start listening to the repository, triggering the flow
        if (mBackground != null) {
            mBackground.addUpdatable(this);
        }
    }

    public void removeUpdatable() {
        // Stop listening to the repository, deactivating it
        if (mBackground != null) {
            mBackground.removeUpdatable(this);
        }
    }

    @Bindable
    public Boolean getIsLoading() {
        return mIsLoading;
    }

    private void setLoading(boolean isLoading) {
        if (mIsLoading != isLoading) {
            mIsLoading = isLoading;
            notifyPropertyChanged(BR.isLoading);
        }
    }

    @Bindable
    public Boolean getIsLoadingMore() {
        return mIsLoadingMore;
    }

    private void setLoadingMore(boolean isLoading) {
        if (mIsLoadingMore != isLoading) {
            mIsLoadingMore = isLoading;
            notifyPropertyChanged(BR.isLoadingMore);
        }
    }

    @Bindable
    public String getErrorMessage() {
        return mErrorMessage;
    }


    private void setErrorMessage(String errorMessage) {
        if (errorMessage != null && !errorMessage.equals(mErrorMessage)) {
            mErrorMessage = errorMessage;
            notifyPropertyChanged(BR.errorMessage);
        }
    }

    @Bindable
    public List<GitRepository> getGitRepositoryList() {
        return mGitRepositoryList;
    }

    private void setGitRepositoryList(List<GitRepository> gitRepositoryList) {
        if (mGitRepositoryList != gitRepositoryList) {
            mGitRepositoryList = gitRepositoryList;
            notifyPropertyChanged(BR.gitRepositoryList);
        }
    }

    public void downloadGitRepositoryList(String language, int page, final boolean loadMore) {

        if (language != null && !language.isEmpty()) {

            setLoading(!loadMore);
            setLoadingMore(loadMore);

            RepositoryService repositoryService = RetrofitFactory.createRetrofitService(RepositoryService.class);

            if (mBackground != null) {
                mBackground.removeUpdatable(this);
            }

            mBackground = repositoryWithInitialValue(Result.<List<GitRepository>>absent())
                    .observe()
                    .onUpdatesPer(500)
                    .goTo(NETWORK_EXECUTOR)
                    .attemptGetFrom(repositoryService.getGitRepositories(language, "starts", String.valueOf(page)))
                    .orEnd(getEndThrowable())
                    .thenTransform(getRepositoryListWithMoreStars())
                    .onDeactivation(RepositoryConfig.SEND_INTERRUPT)
                    .compile();

            mBackground.addUpdatable(this);
        }
    }

    private Function<GitRepositories, Result<List<GitRepository>>> getRepositoryListWithMoreStars() {
        return Functions.functionFrom(GitRepositories.class)
                .unpack(new Function<GitRepositories, List<GitRepository>>() {
                    @NonNull
                    @Override
                    public List<GitRepository> apply(@NonNull GitRepositories gitRepositories) {
                        return gitRepositories.getGitRepositoryList();
                    }
                }).filter(new Predicate<GitRepository>() {
                    @Override
                    public boolean apply(@NonNull GitRepository gitRepository) {
                        return gitRepository.getStarsCount() > 10;
                    }
                })
                .thenApply(new Function<List<GitRepository>, Result<List<GitRepository>>>() {
                    @NonNull
                    @Override
                    public Result<List<GitRepository>> apply(@NonNull List<GitRepository> input) {
                        return Result.absentIfNull(input);
                    }
                });
    }


    private Function<Response<GitRepositories>, Result<List<GitRepository>>> transformResponse() {
        return new Function<Response<GitRepositories>, Result<List<GitRepository>>>() {
            @NonNull
            @Override
            public Result<List<GitRepository>> apply(@NonNull Response<GitRepositories> response) {
                Result<List<GitRepository>> result;
                if (response.isSuccessful()) {
                    result = Result.success(response.body().getGitRepositoryList());
                } else {
                    result = Result.failure(new HttpException(response));
                }
                return result;
            }
        };
    }

    private Function<Throwable, Result<List<GitRepository>>> getEndThrowable() {
        return new Function<Throwable, Result<List<GitRepository>>>() {
            @NonNull
            @Override
            public Result<List<GitRepository>> apply(@NonNull Throwable throwable) {
                setLoading(false);
                setLoadingMore(false);

                Log.e("onFailure", throwable.getLocalizedMessage());
                setGitRepositoryList(null);
                setErrorMessage("Request Failure!");

                return Result.absent();
            }
        };
    }

    @Override
    public void update() {
        setLoading(false);
        setLoadingMore(false);

        if (mBackground.get().succeeded()) {

            mBackground.get().ifSucceededSendTo(this);
            setErrorMessage(null);
        } else if (mBackground.get().failed() && !mBackground.get().isAbsent()) {

            Throwable throwable = mBackground.get().getFailure();
            Log.e("onFailure", throwable.getLocalizedMessage());
            setGitRepositoryList(null);
            setErrorMessage("Request Failure!");
        }

    }

    @Override
    public void accept(@NonNull List<GitRepository> gitRepositoryList) {
        setGitRepositoryList(gitRepositoryList);
    }


}
