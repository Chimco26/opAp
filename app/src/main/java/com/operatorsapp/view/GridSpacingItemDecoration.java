package com.operatorsapp.view;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Android Studio
 * User: Ailurus(ailurus@foxmail.com)
 * Date: 2015-10-28
 * Time: 15:20
 */
public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private final int spacingBottom;
    private int spanCount;
    private int spacing;
    private int spacingTop;
    private boolean includeEdge;
    private int headerNum;

    public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge, int headerNum) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.spacingTop = spacing;
        this.spacingBottom = spacing;
        this.includeEdge = includeEdge;
        this.headerNum = headerNum;
    }

    public int getSpacingTop() {
        return spacingTop;
    }

    public void setSpacingTop(int spacingTop) {
        this.spacingTop = spacingTop;
    }

    public int getSpacingBottom() {
        return spacingBottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view) - headerNum; // item position

        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
            spanCount = layoutManager.getSpanCount();
        }

        if (position >= 0) {
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacingTop;
                }
                outRect.bottom = spacingBottom; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacingTop; // item top
                }
            }
        } else {
            outRect.left = 0;
            outRect.right = 0;
            outRect.top = 0;
            outRect.bottom = 0;
        }
    }
}

