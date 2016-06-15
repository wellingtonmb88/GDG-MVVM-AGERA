package com.wellingtonmb88.gitrepo.viewmodel;


import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.wellingtonmb88.gitrepo.BR;
import com.wellingtonmb88.gitrepo.model.GitRepository;

public class RepositoryViewModel extends BaseObservable {

    private GitRepository mGitRepository;

    public RepositoryViewModel(GitRepository gitRepository) {
        setRepository(gitRepository);
    }

    @Bindable
    public GitRepository getGitRepository() {
        return mGitRepository;
    }

    private void setRepository(GitRepository gitRepository) {
        if (mGitRepository != gitRepository) {
            mGitRepository = gitRepository;
            notifyPropertyChanged(BR.gitRepository);
        }
    }
}
