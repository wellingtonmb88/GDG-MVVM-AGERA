package com.wellingtonmb88.gitrepo.bindingadapter;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wellingtonmb88.gitrepo.adapter.RepositoryAdapter;
import com.wellingtonmb88.gitrepo.model.GitRepository;
import com.wellingtonmb88.gitrepo.preferences.RepositoryPreferences;

import java.util.List;

public class RecyclerViewBindingAdapter {

    @BindingAdapter({"dataSet", "errorMessage"})
    public static void setDataSet(RecyclerView view, List<GitRepository> dataSet, String errorMessage) {
        if (dataSet != null && !dataSet.isEmpty()) {
            RecyclerView.Adapter adapter = view.getAdapter();
            if (adapter instanceof RepositoryAdapter) {
                RepositoryAdapter repositoryAdapter = (RepositoryAdapter) adapter;
                if (repositoryAdapter.getItemCount() == 0) {
                    //Saving into the preferences just the items of first page
                    Gson gson = new Gson();
                    String dataSetJson = gson.toJson(dataSet);
                    RepositoryPreferences.saveRepositoryList(view.getContext(), dataSetJson);
                }
                repositoryAdapter.addToDataSet(dataSet);
                int curSize = view.getAdapter().getItemCount();
                view.getAdapter().notifyItemRangeInserted(curSize, repositoryAdapter.getItemCount() - 1);
            }
        } else {
            if (errorMessage != null) {
                Toast.makeText(view.getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
