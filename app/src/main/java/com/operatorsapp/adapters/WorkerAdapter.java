package com.operatorsapp.adapters;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.UpsertType;
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
    private final WorkerAdapterListener listener;

    public WorkerAdapter(ArrayList<Worker> workerItems, OnStartDragListener onStartDragListener, WorkerAdapterListener listener) {
        list = workerItems;
        this.onStartDragListener = onStartDragListener;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WorkerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new WorkerAdapter.ViewHolder(inflater.inflate(R.layout.item_worker, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WorkerAdapter.ViewHolder viewHolder, int position) {

        Worker worker = list.get(position);
        viewHolder.numberTv.setText(worker.getWorkerID());
        viewHolder.nameTv.setText(worker.getWorkerName());
        if (position == 0) {
            viewHolder.itemView.setBackgroundColor(viewHolder.itemView.getContext().getResources().getColor(R.color.blue1));
            viewHolder.numberTv.setTextColor(viewHolder.itemView.getContext().getResources().getColor(R.color.white));
            viewHolder.nameTv.setTextColor(viewHolder.itemView.getContext().getResources().getColor(R.color.white));
            viewHolder.removeBtn.setColorFilter(viewHolder.itemView.getContext().getResources().getColor(R.color.white));
            if (worker.getUpsertType() != UpsertType.INSERT.getValue() && !worker.isHeadWorker()){
                worker.setUpsertType(UpsertType.UPDATE.getValue());
            }
            worker.setHeadWorker(true);
        } else {
            if (worker.getUpsertType() != UpsertType.INSERT.getValue() && worker.isHeadWorker()){
                worker.setUpsertType(UpsertType.UPDATE.getValue());
            }
            worker.setHeadWorker(false);
            viewHolder.itemView.setBackgroundColor(viewHolder.itemView.getContext().getResources().getColor(R.color.reports_background));
            viewHolder.numberTv.setTextColor(viewHolder.itemView.getContext().getResources().getColor(R.color.black));
            viewHolder.nameTv.setTextColor(viewHolder.itemView.getContext().getResources().getColor(R.color.black));
            viewHolder.removeBtn.setColorFilter(viewHolder.itemView.getContext().getResources().getColor(R.color.black));
        }

        viewHolder.setListeners(viewHolder);
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

        public void setListeners(@NonNull ViewHolder viewHolder) {
            viewHolder.removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onRemoveWorker(WorkerAdapter.this.list.get(getAdapterPosition()));
                    WorkerAdapter.this.list.remove(getAdapterPosition());
                    if (getAdapterPosition() == 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyItemRemoved(getAdapterPosition());
                    }
                }
            });

            viewHolder.itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        onStartDragListener.onStartDrag(ViewHolder.this);
                    }
                    return false;
                }
            });
        }
    }

    public interface WorkerAdapterListener {

        void onRemoveWorker(Worker worker);
    }
}

