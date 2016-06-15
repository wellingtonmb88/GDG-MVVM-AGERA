package com.wellingtonmb88.gitrepo.fragment;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wellingtonmb88.gitrepo.R;
import com.wellingtonmb88.gitrepo.adapter.RepositoryAdapter;
import com.wellingtonmb88.gitrepo.databinding.FragmentRepositoryListBinding;
import com.wellingtonmb88.gitrepo.listener.EndlessRecyclerOnScrollListener;
import com.wellingtonmb88.gitrepo.model.GitRepository;
import com.wellingtonmb88.gitrepo.preferences.RepositoryPreferences;
import com.wellingtonmb88.gitrepo.utils.ConnectionUtils;
import com.wellingtonmb88.gitrepo.utils.DividerItemDecoration;
import com.wellingtonmb88.gitrepo.viewmodel.RepositoryListViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RepositoryListFragment extends Fragment {

    @BindView(R.id.list_repository)
    public RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private RepositoryListViewModel mViewModel;
    private Unbinder mUnbinder;
    private static List<GitRepository> sDataSet;

    public RepositoryListFragment() {
        // Required empty public constructor
    }

    public static RepositoryListFragment newInstance() {
        return new RepositoryListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_repository_list, container, false);

        mUnbinder = ButterKnife.bind(this, view);

        Context context = getContext();

        FragmentRepositoryListBinding binding = DataBindingUtil.bind(view);
        mViewModel = new RepositoryListViewModel();
        binding.setViewModel(mViewModel);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(ContextCompat.
                getDrawable(getContext(), R.drawable.divider)));

        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);

        sDataSet = new ArrayList<>();

        RepositoryAdapter repositoryAdapter = new RepositoryAdapter();
        repositoryAdapter.setDataSet(sDataSet);
        mRecyclerView.setAdapter(repositoryAdapter);

        loadRepositoryListFromPreferences();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mViewModel != null) {
            mViewModel.addUpdatable();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mViewModel != null) {
            mViewModel.removeUpdatable();
        }
    }

    private void loadRepositoryListFromPreferences() {

        Context context = getContext();

        String repositoryListJson = RepositoryPreferences.getRepositoryList(context);
        if (!repositoryListJson.isEmpty()) {

            Gson gson = new Gson();
            List<GitRepository> gitRepositoryList = gson.fromJson(repositoryListJson,
                    new TypeToken<List<GitRepository>>() {
                    }.getType());

            if (gitRepositoryList != null && !gitRepositoryList.isEmpty()) {
                sDataSet.clear();
                sDataSet.addAll(gitRepositoryList);
                String query = RepositoryPreferences.getRepositoryQuery(context);

                if (!query.isEmpty()) {
                    loadRecyclerView(query, true);
                }
            }
        }
    }

    private void loadRecyclerView(final String query, boolean dataSaved) {

        if (ConnectionUtils.isConnected(getContext())) {

            if (!dataSaved) {
                sDataSet.clear();
                mViewModel.downloadGitRepositoryList(query, 1, false);
            }

            mRecyclerView.getAdapter().notifyDataSetChanged();
            mRecyclerView.removeOnScrollListener(null);

            RepositoryPreferences.saveRepositoryQuery(getContext(), query);

            mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
                @Override
                public void onLoadMore(int currentPage) {
                    mViewModel.downloadGitRepositoryList(query, currentPage, true);
                }
            });
        } else {
            Toast.makeText(getContext(), R.string.toast_message_no_internet_connection, Toast.LENGTH_SHORT).show();
        }
    }

    @BindingAdapter({"dataSet", "errorMessage"})
    public static void setDataSet(RecyclerView view, List<GitRepository> dataSet, String errorMessage) {
        if (dataSet != null && !dataSet.isEmpty()) {

            if (sDataSet.size() == 0) {
                //Saving into the preferences just the items of first page
                Gson gson = new Gson();
                String dataSetJson = gson.toJson(dataSet);
                RepositoryPreferences.saveRepositoryList(view.getContext(), dataSetJson);
            }

            sDataSet.addAll(dataSet);
            int curSize = view.getAdapter().getItemCount();
            view.getAdapter().notifyItemRangeInserted(curSize, sDataSet.size() - 1);
        } else {
            if (errorMessage != null) {
                Toast.makeText(view.getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (mViewModel != null) {
                    loadRecyclerView(query, false);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                return false;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
