package ru.asedias.vkbugtracker.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.api.WebRequest;

/**
 * Created by rorom on 20.10.2018.
 */

public class LoaderFragment extends Fragment {

    protected View mContainer;
    protected FrameLayout mContent;
    protected WebRequest request;
    protected boolean mRequestDone;
    protected View mLoading;
    protected View mErrorView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if(!isViewCreated()) {
            this.mContainer = inflater.inflate(R.layout.appkit_loader_fragment, null);
            this.mLoading = this.mContainer.findViewById(R.id.loading);
            this.mErrorView = this.mContainer.findViewById(R.id.error);
            this.mContent = this.mContainer.findViewById(R.id.content_stub);
            this.mContent.addView(OnCreateContentView(inflater, container, savedInstanceState));
            mRequestDone = savedInstanceState != null && savedInstanceState.getBoolean("mRequestDone", false);
        }
        return this.mContainer;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("mRequestDone", mRequestDone);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.request.cancel();
    }

    protected View OnCreateContentView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    protected void showContent() {
        this.mErrorView.setVisibility(View.GONE);
        this.mLoading.setVisibility(View.GONE);
        this.mContent.setVisibility(View.VISIBLE);
        this.mRequestDone = true;
    }

    public WebRequest getRequest() {
        return null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        this.request = getRequest();
        executeRequestIfNeeded();
    }

    public boolean isViewCreated() {
        return this.mContainer != null;
    }

    public boolean isRequestDone() {
        return mRequestDone;
    }

    public void showError(String text) {
        ((TextView)this.mErrorView.findViewById(R.id.error_text)).setText(text);
        this.mErrorView.findViewById(R.id.error_retry).setOnClickListener(v -> {
            executeRequestIfNeeded();
        });
        this.mRequestDone = false;
    }

    public void executeRequestIfNeeded() {
        if(request != null && !isRequestDone()) {
            request.execute();
        }
    }
}
