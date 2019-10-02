package com.operatorsapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.operatorsapp.R;

import java.util.List;

public class LauncherAdapter extends RecyclerView.Adapter<LauncherAdapter.ViewHolder> {
    private final Context mContext;
    private final List<ResolveInfo> mSelectedApps;

    public LauncherAdapter(Context mContext, List<ResolveInfo> selectedApps) {
        this.mContext = mContext;
        this.mSelectedApps = selectedApps;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.launcher_adapter_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        viewHolder.name.setText(getAppName(mSelectedApps.get(position).activityInfo.packageName));
        viewHolder.app.setImageDrawable(mSelectedApps.get(position).activityInfo.loadIcon( mContext.getPackageManager()));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchIntent = mContext.getPackageManager().getLaunchIntentForPackage(mSelectedApps.get(position).activityInfo.packageName);
                mContext.startActivity(launchIntent);
            }
        });
    }

    private String getAppName(String name) {

        if (name.contains("anydesk")){
            name = "AnyDesk";
        }else if (name.contains("calculator")){
            name = "Calculator";
        }else if (name.contains("teamviewer")){
            name = "TeamViewer";
        }else if (name.contains("zoom")){
            name = "Zoom";
        }else if (name.contains("acrobat")){
            name = "Acrtobat";
        }else if (name.contains("vending")){
            name = "Play Store";
        }else if (name.contains("file")){
            name = "File Manager";
        }else if (name.contains("chrome")){
            name = "Chrome";
        }else if (name.contains("pdf")){
            name = "PDF";
        }
        return name;
    }

    @Override
    public int getItemCount() {
        return mSelectedApps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView app;
        private TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            app = itemView.findViewById(R.id.launcher_adapter_item_iv);
            name = itemView.findViewById(R.id.launcher_adapter_item_name_tv);
        }
    }
}
