package com.operatorsapp.adapters;

import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.common.department.MachinesLineDetail;
import com.operatorsapp.R;
import com.operatorsapp.managers.PersistenceManager;

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

        viewHolder.mTextBckgrnd.setBackgroundColor(Color.parseColor(mMachineLineItems.get(position).getStatusColor()));
        viewHolder.mBarView.setBackgroundColor(Color.parseColor(mMachineLineItems.get(position).getStatusColor()));

        if (mMachineLineItems.get(position).getMachineID().equals(PersistenceManager.getInstance().getMachineId())) {
            viewHolder.mTitle.setBackgroundColor(viewHolder.mTextBckgrnd.getContext().getResources().getColor(R.color.white));
            viewHolder.mTitle.setTextColor(Color.parseColor(mMachineLineItems.get(position).getStatusColor()));
            viewHolder.mArrowDown.setColorFilter(Color.parseColor(mMachineLineItems.get(position).getStatusColor()));
            viewHolder.mArrowUp.setColorFilter(Color.parseColor(mMachineLineItems.get(position).getStatusColor()));
            viewHolder.mArrowDown.setVisibility(View.VISIBLE);
            viewHolder.mArrowUp.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mTitle.setBackgroundColor(Color.parseColor(mMachineLineItems.get(position).getStatusColor()));
            viewHolder.mTitle.setTextColor(viewHolder.mTextBckgrnd.getContext().getResources().getColor(R.color.white));
            viewHolder.mArrowDown.setVisibility(View.GONE);
            viewHolder.mArrowUp.setVisibility(View.GONE);
        }


        if (position == 0) {
            viewHolder.mBarView.setVisibility(View.INVISIBLE);
        } else {
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
        private final View mTextBckgrnd;
        private final ImageView mArrowDown;
        private final ImageView mArrowUp;

        ViewHolder(View itemView) {
            super(itemView);

            mBarView = itemView.findViewById(R.id.ILM_bar);
            mTextBckgrnd = itemView.findViewById(R.id.ILM_text_bckgrnd);
            mArrowDown = itemView.findViewById(R.id.ILM_arrow_down);
            mArrowUp = itemView.findViewById(R.id.ILM_arrow_up);
            mTitle = itemView.findViewById(R.id.ILM_tv);
        }

    }

    public interface MachineLineAdapterListener {

        void onMachineSelected(MachinesLineDetail departmentMachineValue);
    }
}
