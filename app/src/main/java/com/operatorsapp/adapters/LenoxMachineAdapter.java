package com.operatorsapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.operators.infra.Machine;
import com.operatorsapp.R;

import java.util.ArrayList;

public class LenoxMachineAdapter extends RecyclerView.Adapter<LenoxMachineAdapter.ViewHolder> {

    private final ArrayList<Machine> mMachines;

    public LenoxMachineAdapter(ArrayList<Machine> machines){

        mMachines = machines;
    }

    @NonNull
    @Override
    public LenoxMachineAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new LenoxMachineAdapter.ViewHolder(inflater.inflate(R.layout.item_lenox_machines, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LenoxMachineAdapter.ViewHolder holder, int position) {

        holder.mTv.setText(mMachines.get(position).getMachineEName());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTv;

        public ViewHolder(View inflate) {
            super(inflate);
            mTv = inflate.findViewById(R.id.ILM_tv);
        }
    }
}
