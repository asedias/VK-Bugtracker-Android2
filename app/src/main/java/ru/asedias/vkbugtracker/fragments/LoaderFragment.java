package ru.asedias.vkbugtracker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Response;
import ru.asedias.vkbugtracker.BuildConfig;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.api.WebRequest;

/**
 * Created by rorom on 20.10.2018.
 */

public class LoaderFragment extends BTFragment {

    protected View mContainer;
    protected FrameLayout mContent;
    protected WebRequest request;
    protected boolean mRequestDone;
    protected View mLoading;
    protected View mErrorView;
    protected boolean mRequestRunning;
    protected boolean isRefreshing = false;
    protected boolean canLoadMore = false;
    public boolean isLoadingMore = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if(this.mContainer == null) {
            this.mContainer = inflater.inflate(R.layout.appkit_loader_fragment, container, false);
            this.mLoading = this.mContainer.findViewById(R.id.loading);
            this.mErrorView = this.mContainer.findViewById(R.id.error);
            this.mContent = this.mContainer.findViewById(R.id.content_stub);
            this.mContent.removeAllViews();
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
        if(this.request != null) {
            this.request.cancel();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (this.mContainer != null) {
            ViewGroup parent = (ViewGroup) this.mContainer.getParent();
            if (parent != null) {
                parent.removeView(this.mContainer);
            }
        }
    }

    protected View OnCreateContentView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    public void showContent() {
        this.mErrorView.setVisibility(View.GONE);
        this.mLoading.setVisibility(View.GONE);
        this.mContent.setAlpha(1.0F);
        this.mRequestDone = true;
        this.mRequestRunning = false;
        this.isRefreshing = false;
        this.isLoadingMore = false;
    }

    protected void showProgress() {
        this.mErrorView.setVisibility(View.GONE);
        this.mLoading.setVisibility(View.VISIBLE);
        this.mContent.setAlpha(0.3F);
        //this.mContent.setVisibility(View.GONE);
        this.mRequestDone = false;
        this.mRequestRunning = true;
        this.isRefreshing = false;
        this.isLoadingMore = false;
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

    public boolean isRequestRunning() { return mRequestRunning; }

    public boolean isRefreshing() { return isRefreshing; }

    public boolean canLoadMode() { return canLoadMore; }

    public void showError(Throwable t) {
        showError(t.getLocalizedMessage());
        if(BuildConfig.DEBUG) t.printStackTrace();
    }

    public void showEmptyText() {
        this.mErrorView.setVisibility(View.VISIBLE);
        this.mErrorView.findViewById(R.id.error_image).setVisibility(View.GONE);
        this.mErrorView.findViewById(R.id.error_retry).setVisibility(View.GONE);
        this.mLoading.setVisibility(View.GONE);
        this.mContent.setAlpha(0.3F);
        //this.mContent.setVisibility(View.GONE);
        ((TextView)this.mErrorView.findViewById(R.id.error_text)).setText(R.string.empty);
        this.mRequestDone = true;
        this.mRequestRunning = false;
        this.isRefreshing = false;
        this.isLoadingMore = false;
    }

    public void showError(String text) {
        this.mErrorView.setVisibility(View.VISIBLE);
        this.mErrorView.findViewById(R.id.error_image).setVisibility(View.VISIBLE);
        this.mErrorView.findViewById(R.id.error_retry).setVisibility(View.VISIBLE);
        this.mLoading.setVisibility(View.GONE);
        this.mContent.setAlpha(0.3F);
        //this.mContent.setVisibility(View.GONE);
        ((TextView)this.mErrorView.findViewById(R.id.error_text)).setText(text);
        this.mErrorView.findViewById(R.id.error_retry).setOnClickListener(v -> {
            reExecuteRequest();
        });
        this.mRequestDone = false;
        this.mRequestRunning = false;
        this.isRefreshing = false;
        this.isLoadingMore = false;
    }

    public void loadMore(boolean isLoadingMore) {
        if(!isRequestRunning()) {
            this.isLoadingMore = isLoadingMore;
            this.request = getRequest();
            this.request.execute();
            this.mRequestRunning = true;
        }
    }

    public void reExecuteRequest() {
        showProgress();
        this.request.cancel();
        this.loadMore(false);
    }

    public void executeRequestIfNeeded() {
        if(request != null && !isRequestDone() && !isRequestRunning()) {
            request.execute();
            this.mRequestRunning = true;
        }
    }
}
