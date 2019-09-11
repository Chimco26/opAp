package com.operatorsapp.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.operatorsapp.R;
import com.operatorsapp.adapters.NotificationHistoryAdapter;

import java.util.List;

public class LauncherDialog extends Dialog {

    private final Context mContext;
    private final String calculator = "";
    private final String anyDesk = "";
    private ImageView mCalc;
    private ImageView mAnyDesk;
    private ApplicationInfo anyDeskApp;
    private ApplicationInfo calcApp;
    private ResolveInfo anyDeskApp_ri;
    private ResolveInfo calcApp_ri;

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

        mCalc = findViewById(R.id.launcher_dialog_calculator);
        mAnyDesk = findViewById(R.id.launcher_dialog_anydesk);


        getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        PackageManager pm = mContext.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> allApps = pm.queryIntentActivities(i, 0);


        for(ResolveInfo ri:allApps) {

            if (ri.activityInfo.packageName.contains("anydesk")){
                anyDeskApp_ri = ri;


                mAnyDesk.setImageDrawable(ri.activityInfo.loadIcon(pm));


            }else if (ri.activityInfo.packageName.contains("calc")) {
                calcApp_ri = ri;
                mCalc.setImageDrawable(ri.activityInfo.loadIcon(pm));
            }


        }

        
//        for (ApplicationInfo app : packages) {
//
//            if (app.packageName.contains("anydesk")){
//                anyDeskApp = app;
//
//                Intent i = new Intent(Intent.ACTION_MAIN, null);
//                i.addCategory(Intent.CATEGORY_LAUNCHER);
//
//
//                Drawable drawable = anyDeskApp.icon;
//                mAnyDesk.setImageDrawable(anyDeskApp.icon);
//            }else if (app.packageName.contains("calc")) {
//                calcApp = app;
//                mCalc.setImageDrawable(calcApp.icon);
//            }
//        }

        mCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchIntent = mContext.getPackageManager().getLaunchIntentForPackage(calcApp_ri.activityInfo.packageName);
                mContext.startActivity(launchIntent);
            }
        });

        mAnyDesk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchIntent = mContext.getPackageManager().getLaunchIntentForPackage(anyDeskApp_ri.activityInfo.packageName);
                mContext.startActivity(launchIntent);
            }
        });

    }
}
