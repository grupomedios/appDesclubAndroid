package com.grupomedios.desclub.desclubutil.ui.scroll;

import android.widget.AbsListView;
import android.widget.ListView;

import com.grupomedios.desclub.desclubutil.ui.list.PaginableActivity;


/**
 * Created by jhon on 12/02/15.
 */
public class EndlessScrollListener implements AbsListView.OnScrollListener {

    private final String TAG = "EndlessScrollListener";

    // how many entries earlier to start loading next page
    private int visibleThreshold = 3;
    private int currentPage = 0;
    private int previousTotal = 0;
    private boolean loading = true;
    private PaginableActivity activity;

    public EndlessScrollListener() {
    }

    public EndlessScrollListener(PaginableActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    public void reset(){
        currentPage = 0;
        previousTotal = 0;
        loading = true;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

        if(view.getAdapter() == null){
            return;
        }

        int realTotalItemCount = totalItemCount - ((ListView) view).getFooterViewsCount();

        if (loading) {
            if (realTotalItemCount > previousTotal) {
                loading = false;
                previousTotal = realTotalItemCount;
                currentPage++;
            }
        }
        if (!loading && firstVisibleItem + visibleItemCount >= realTotalItemCount) {
            activity.loadPage();
            loading = true;
        }
    }

    public int getCurrentPage() {
        return currentPage;
    }
}
