package com.operatorsapp.adapters;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.operatorsapp.R;
import com.operatorsapp.utils.FilterStatus;

import java.util.ArrayList;

public class LegendDialogAdapter extends RecyclerView.Adapter<LegendDialogAdapter.ViewHolder> {

    private ArrayList<FilterStatus> mLegendList;

    public LegendDialogAdapter(Context context) {
        setLegendHelpers(context);
    }

    private void setLegendHelpers(Context context){

        mLegendList = new ArrayList<>();
        mLegendList.add(new FilterStatus(context.getString(R.string.working), "#1aa917"));
        mLegendList.add(new FilterStatus(context.getString(R.string.working_setup), "#127510"));
        mLegendList.add(new FilterStatus(context.getString(R.string.parameter_deviation), "#f5a623"));
        mLegendList.add(new FilterStatus(context.getString(R.string.stop), "#c01b29"));
        mLegendList.add(new FilterStatus(context.getString(R.string.stop_setup), "#850f16"));
        mLegendList.add(new FilterStatus(context.getString(R.string.idle), "#a6a8ab"));
        mLegendList.add(new FilterStatus(context.getString(R.string.working_no_production), "#7f7f7f"));
        mLegendList.add(new FilterStatus(context.getString(R.string.no_communication_p), "#21809e"));


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.legend_helper_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mName.setText(mLegendList.get(position).getName());
        holder.mColor.setBackgroundColor(Color.parseColor(mLegendList.get(position).getColor()));
    }

    @Override
    public int getItemCount() {
        return mLegendList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mName;
        private View mColor;

        public ViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.LHI_name);
            mColor = itemView.findViewById(R.id.LHI_color);
        }
    }
}
