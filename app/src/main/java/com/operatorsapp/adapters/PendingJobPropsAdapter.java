package com.operatorsapp.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.operators.reportrejectnetworkbridge.server.response.activateJob.Header;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.Property;
import com.operatorsapp.R;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.operatorsapp.utils.StringUtil.getResizedString;
import static com.operatorsapp.utils.TimeUtils.updateDateForRtl;

public class PendingJobPropsAdapter extends RecyclerView.Adapter<PendingJobPropsAdapter.ViewHolder> {

    private final List<Property> mProps;
    private final HashMap<String, Header> mHashMapHeaders;

    public PendingJobPropsAdapter(List<Property> properties, HashMap<String, Header> hashMapHeaders) {

        mProps = properties;
        mHashMapHeaders = hashMapHeaders;
    }

    @NonNull
    @Override
    public PendingJobPropsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new PendingJobPropsAdapter.ViewHolder(inflater.inflate(R.layout.item_pending_jobs_props, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final PendingJobPropsAdapter.ViewHolder viewHolder, final int position) {

        SimpleDateFormat actualFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy", Locale.getDefault());

        if (mProps.get(position) != null && mHashMapHeaders.get(mProps.get(position).getKey()) != null) {
            viewHolder.mTitleTv.setText(getResizedString(mHashMapHeaders.get(mProps.get(position).getKey()).getDisplayName(), 20));
        }
        if (viewHolder.itemView.getContext().getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            viewHolder.mValueTv.setText(getResizedString(updateDateForRtl(mProps.get(position), actualFormat, dateFormat), 22));
        } else {
            viewHolder.mValueTv.setText(getResizedString(mProps.get(position).getValue(), 22));
        }
        if (mProps.get(position).getValue() != null) {
            if (mProps.get(position).getValue().length() > 18) {
                viewHolder.mValueTv.setSelected(true);
            } else {
                viewHolder.mValueTv.setSelected(false);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mProps != null) {
            return mProps.size();
        } else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private final TextView mTitleTv;
        private final TextView mValueTv;

        ViewHolder(View itemView) {
            super(itemView);

            mTitleTv = itemView.findViewById(R.id.IPJP_title);
            mValueTv = itemView.findViewById(R.id.IPJP_value);

        }

    }
}
