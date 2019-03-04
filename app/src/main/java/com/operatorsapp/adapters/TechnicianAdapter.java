package com.operatorsapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.operators.reportfieldsformachineinfra.Technician;
import com.operatorsapp.R;
import com.operatorsapp.activities.DashboardActivity;
import com.operatorsapp.application.OperatorApplication;

import java.util.List;

public class TechnicianAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final List<Technician> mTechList;
    private final Context mContext;
    private final TechItemListener mListener;

    public TechnicianAdapter(Context context, List<Technician> techniciansList, TechItemListener listener) {
        mContext = context;
        mTechList = techniciansList;
        mListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.technician_recycler_item, parent, false);

        return new TechViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final TechViewHolder techViewHolder = (TechViewHolder) holder;
        if (OperatorApplication.isEnglishLang()){

            techViewHolder.mTechNameTv.setText(mTechList.get(position).getEName());
        }else {
            techViewHolder.mTechNameTv.setText(mTechList.get(position).getEName());
        }
        techViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onTechnicianSelected(mTechList.get(position));
            }
        });    }

    @Override
    public int getItemCount() {
        return mTechList.size();
    }

    public class TechViewHolder extends RecyclerView.ViewHolder {
        private TextView mTechNameTv;

        public TechViewHolder(View v) {
            super(v);
            mTechNameTv = itemView.findViewById(R.id.technician_recycler_item_name_tv);
        }
    }

    public interface TechItemListener{
        void onTechnicianSelected(Technician technician);
    }

}
