package com.operatorsapp.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.operators.infra.Machine;
import com.operatorsapp.R;
import com.operatorsapp.managers.PersistenceManager;

import java.util.ArrayList;

public class LenoxMachineAdapter extends RecyclerView.Adapter<LenoxMachineAdapter.ViewHolder> {

    private final ArrayList<Machine> mMachines;
    private final LenoxMachineAdapterListener mListener;
    private int mSelectedMachineId;

    public LenoxMachineAdapter(ArrayList<Machine> machines, LenoxMachineAdapterListener listener){
        
        mMachines = machines;

        mListener = listener;

        mSelectedMachineId = PersistenceManager.getInstance().getMachineId();
    }

    @NonNull
    @Override
    public LenoxMachineAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new LenoxMachineAdapter.ViewHolder(inflater.inflate(R.layout.item_lenox_machines, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final LenoxMachineAdapter.ViewHolder holder, int position) {

        updateBackground(holder, position);

        holder.mTv.setText(mMachines.get(position).getMachineEName());

        holder.mTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSelectedMachineId = mMachines.get(holder.getAdapterPosition()).getId();
                mListener.onLenoxMachineClicked(mMachines.get(holder.getAdapterPosition()));
                notifyDataSetChanged();
            }
        });
    }

    private void updateBackground(@NonNull ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        if (mMachines.get(position).getId() == mSelectedMachineId){
            holder.mTv.setBackgroundColor(context.getResources().getColor(R.color.machine_blue));
            holder.mTv.setTextColor((context.getResources().getColor(R.color.white)));
        }else {
            holder.mTv.setBackground(context.getResources().getDrawable(R.drawable.blue_stroke));
            holder.mTv.setTextColor((context.getResources().getColor(R.color.machine_blue)));
        }
    }

    @Override
    public int getItemCount() {
        if (mMachines != null){
            return mMachines.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTv;

        public ViewHolder(View inflate) {
            super(inflate);
            mTv = inflate.findViewById(R.id.ILM_tv);
        }
    }

    public interface LenoxMachineAdapterListener{

        void onLenoxMachineClicked(Machine machine);
    }
}
