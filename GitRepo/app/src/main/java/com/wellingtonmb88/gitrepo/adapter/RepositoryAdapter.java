package com.wellingtonmb88.gitrepo.adapter;


import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.wellingtonmb88.gitrepo.R;
import com.wellingtonmb88.gitrepo.model.GitRepository;
import com.wellingtonmb88.gitrepo.viewmodel.RepositoryViewModel;

import java.util.ArrayList;
import java.util.List;

public class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.ViewHolder> {
    private List<RepositoryViewModel> mDataSet = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding mViewDataBinding;

        public ViewHolder(ViewDataBinding viewDataBinding) {
            super(viewDataBinding.getRoot());
            mViewDataBinding = viewDataBinding;
            mViewDataBinding.executePendingBindings();
        }

        public ViewDataBinding getViewDataBinding() {
            return mViewDataBinding;
        }
    }

    public void addToDataSet(List<GitRepository> dataSet) {
        for (GitRepository repo : dataSet) {
            RepositoryViewModel viewModel = new RepositoryViewModel(repo);
            mDataSet.add(viewModel);
        }
    }

    public void clearDataSet() {
        mDataSet.clear();
    }

    @Override
    public RepositoryAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater
                .from(viewGroup.getContext()), R.layout.repository_item, viewGroup, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.getViewDataBinding().setVariable(com.wellingtonmb88.gitrepo.BR.viewModel,
                mDataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
