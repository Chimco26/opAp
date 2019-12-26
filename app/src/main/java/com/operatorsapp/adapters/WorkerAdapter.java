package com.operatorsapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.common.machineData.Worker;
import com.operatorsapp.R;
import com.operatorsapp.interfaces.ItemTouchHelperAdapter;
import com.operatorsapp.interfaces.ItemTouchHelperViewHolder;
import com.operatorsapp.interfaces.OnStartDragListener;

import java.util.ArrayList;
import java.util.Collections;

public class WorkerAdapter extends RecyclerView.Adapter<WorkerAdapter.ViewHolder>
        implements ItemTouchHelperAdapter {
    private final ArrayList<Worker> list;
    private final OnStartDragListener onStartDragListener;

    public WorkerAdapter(ArrayList<Worker> workerItems, OnStartDragListener onStartDragListener) {
        list = workerItems;
        this.onStartDragListener = onStartDragListener;
    }

    @NonNull
    @Override
    public WorkerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new WorkerAdapter.ViewHolder(inflater.inflate(R.layout.item_worker, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final WorkerAdapter.ViewHolder viewHolder, int position) {

        viewHolder.numberTv.setText(list.get(position).getWorkerID());
        viewHolder.nameTv.setText(list.get(position).getWorkerName());
        if (position == 0) {
            viewHolder.itemView.setBackgroundColor(viewHolder.itemView.getContext().getResources().getColor(R.color.blue1));
            viewHolder.numberTv.setTextColor(viewHolder.itemView.getContext().getResources().getColor(R.color.white));
            viewHolder.nameTv.setTextColor(viewHolder.itemView.getContext().getResources().getColor(R.color.white));
            viewHolder.removeBtn.setColorFilter(viewHolder.itemView.getContext().getResources().getColor(R.color.white));
        } else {
            viewHolder.itemView.setBackgroundColor(viewHolder.itemView.getContext().getResources().getColor(R.color.reports_background));
            viewHolder.numberTv.setTextColor(viewHolder.itemView.getContext().getResources().getColor(R.color.black));
            viewHolder.nameTv.setTextColor(viewHolder.itemView.getContext().getResources().getColor(R.color.black));
            viewHolder.removeBtn.setColorFilter(viewHolder.itemView.getContext().getResources().getColor(R.color.black));
        }

        viewHolder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.remove(viewHolder.getAdapterPosition());
                if (viewHolder.getAdapterPosition() == 0) {
                    notifyDataSetChanged();
                } else {
                    notifyItemRemoved(viewHolder.getAdapterPosition());
                }
            }
        });

        viewHolder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    onStartDragListener.onStartDrag(viewHolder);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public void onItemDismiss(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(list, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

        private TextView numberTv;
        private TextView nameTv;
        private ImageView removeBtn;

        ViewHolder(View itemView) {
            super(itemView);

            numberTv = itemView.findViewById(R.id.IW_number);
            nameTv = itemView.findViewById(R.id.IW_name);
            removeBtn = itemView.findViewById(R.id.IW_remove_btn);
        }

        @Override
        public void onItemSelected() {
        }

        @Override
        public void onItemClear() {
            try {
                notifyDataSetChanged();
            }catch (IllegalStateException e){

            }
        }
    }

    public interface WorkerAdapterListener {

        void onRemoveWorker(Worker worker);
    }
}

