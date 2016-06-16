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


public class RepositoryListFragment extends Fragment {

    private RepositoryListViewModel mViewModel;
    private LinearLayoutManager mLayoutManager;
    private FragmentRepositoryListBinding mBinding;
    private RepositoryAdapter mRepositoryAdapter;

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

        mBinding = DataBindingUtil.bind(view);
        mViewModel = new RepositoryListViewModel();
        mBinding.setViewModel(mViewModel);

        mBinding.listRepository.setHasFixedSize(true);
        mBinding.listRepository.addItemDecoration(new DividerItemDecoration(ContextCompat.
                getDrawable(getContext(), R.drawable.divider)));

        mLayoutManager = new LinearLayoutManager(getContext());
        mBinding.listRepository.setLayoutManager(mLayoutManager);

        mRepositoryAdapter  = new RepositoryAdapter();
        mBinding.listRepository.setAdapter(mRepositoryAdapter);

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
                mRepositoryAdapter.clearDataSet();
                mRepositoryAdapter.addToDataSet(gitRepositoryList);
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
                mRepositoryAdapter.clearDataSet();
                mViewModel.downloadGitRepositoryList(query, 1, false);
            }

            mBinding.listRepository.getAdapter().notifyDataSetChanged();
            mBinding.listRepository.removeOnScrollListener(null);

            RepositoryPreferences.saveRepositoryQuery(getContext(), query);

            mBinding.listRepository.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
                @Override
                public void onLoadMore(int currentPage) {
                    mViewModel.downloadGitRepositoryList(query, currentPage, true);
                }
            });
        } else {
            Toast.makeText(getContext(), R.string.toast_message_no_internet_connection, Toast.LENGTH_SHORT).show();
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

}
