package ru.asedias.vkbugtracker.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.ui.adapters.DataAdapter;

/**
 * Created by rorom on 20.10.2018.
 */

public class RecyclerFragment<I extends RecyclerView.Adapter> extends LoaderFragment implements SwipeRefreshLayout.OnRefreshListener {

    protected RecyclerView mList;
    protected View mEmptyView;
    protected I mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    protected SwipeRefreshLayout mSwipeRefresh;

    @Override
    protected View OnCreateContentView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View tempContent = inflater.inflate(R.layout.appkit_recycler_fragment, null, false);
        this.mList = tempContent.findViewById(R.id.list);
        this.mEmptyView = tempContent.findViewById(R.id.empty);
        this.mSwipeRefresh = tempContent.findViewById(R.id.refresh_layout);
        this.mSwipeRefresh.setOnRefreshListener(this);
        this.mSwipeRefresh.setRefreshing(isRefreshing);
        this.mList.setLayoutManager(getLayoutManager());
        this.mList.setAdapter(getAdapter());
        return tempContent;
    }

    public I getAdapter() {
        return mAdapter;
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        if(this.mLayoutManager == null) this.mLayoutManager = new LinearLayoutManager(getActivity());
        return mLayoutManager;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getAdapter() != null) {
            getAdapter().notifyDataSetChanged();
        }
        if(isRequestRunning()||!isRequestDone()) showProgress();
    }

    public void setScrollToTop() {
        mLayoutManager.smoothScrollToPosition(mList, null, 0);
    }

    @Override
    public void showContent() {
        isRefreshing = false;
        this.mSwipeRefresh.setRefreshing(isRefreshing);
        if(getAdapter() instanceof DataAdapter) {
            if(((DataAdapter)getAdapter()).data.getSize() == 0) {
                showEmptyText();
                return;
            }
        }
        super.showContent();
    }

    @Override
    public void onRefresh() {
        this.isRefreshing = true;
        if(!isRequestRunning()) {
            this.request = getRequest();
            if (request != null) {
                request.execute();
            }
        }
    }
}
