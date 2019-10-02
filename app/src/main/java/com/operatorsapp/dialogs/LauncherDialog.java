package com.operatorsapp.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.operatorsapp.R;
import com.operatorsapp.adapters.LauncherAdapter;

import java.util.ArrayList;
import java.util.List;

public class LauncherDialog extends Dialog {

    private final Context mContext;
    private RecyclerView mRecycler;

    public LauncherDialog(Context context) {
        super(context);
        mContext = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams wlp = getWindow().getAttributes();
        wlp.x = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        getWindow().setAttributes(wlp);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.launcher_dialog);

        getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        initRecycler();
    }

    private void initRecycler() {

        PackageManager pm = mContext.getPackageManager();

        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> allApps = pm.queryIntentActivities(i, 0);

        List<ResolveInfo> selectedApps  = new ArrayList<>();


        for(ResolveInfo ri:allApps) {

            if (ri.activityInfo.packageName.contains("anydesk") ||
                    ri.activityInfo.packageName.contains("calc") ||
                    ri.activityInfo.packageName.contains("teamviewer") ||
                    ri.activityInfo.packageName.contains("zoom") ||
                    ri.activityInfo.packageName.contains("acrobat") ||
                    ri.activityInfo.packageName.contains("vending") ||
                    ri.activityInfo.packageName.contains("file") ||
                    ri.activityInfo.packageName.contains("chrome"))
            {
                selectedApps.add(ri);
            }
        }

        mRecycler = findViewById(R.id.launcher_dialog_rv);
        mRecycler.setLayoutManager(new GridLayoutManager(mContext, 3));
        mRecycler.setAdapter(new LauncherAdapter(mContext, selectedApps));
    }
}
