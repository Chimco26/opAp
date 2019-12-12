package com.operatorsapp.adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.common.department.MachinesLineDetail;
import com.operatorsapp.R;

import java.util.List;

public class MachineLineAdapter extends RecyclerView.Adapter<MachineLineAdapter.ViewHolder> {

    private List<MachinesLineDetail> mMachineLineItems;
    private MachineLineAdapterListener mListener;

    public MachineLineAdapter(List<MachinesLineDetail> machineLineItems, MachineLineAdapterListener machineLineAdapterListener) {
        mMachineLineItems = machineLineItems;
        mListener = machineLineAdapterListener;
    }

    @NonNull
    @Override
    public MachineLineAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new MachineLineAdapter.ViewHolder(inflater.inflate(R.layout.item_line_machine, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MachineLineAdapter.ViewHolder viewHolder, final int position) {

        viewHolder.mTitle.setText(mMachineLineItems.get(position).getMachineName());
        viewHolder.mTitle.setBackgroundColor(Color.parseColor(mMachineLineItems.get(position).getJobColor()));
        viewHolder.mBarView.setBackgroundColor(Color.parseColor(mMachineLineItems.get(position).getJobColor()));

        if (position == 0){
            viewHolder.mBarView.setVisibility(View.GONE);
        }else {
            viewHolder.mBarView.setVisibility(View.VISIBLE);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onMachineSelected(mMachineLineItems.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mMachineLineItems != null) {
            return mMachineLineItems.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTitle;
        private final View mBarView;

        ViewHolder(View itemView) {
            super(itemView);

            mBarView = itemView.findViewById(R.id.ILM_bar);
            mTitle = itemView.findViewById(R.id.ILM_tv);
        }

    }

    public interface MachineLineAdapterListener {

        void onMachineSelected(MachinesLineDetail departmentMachineValue);
    }
}
