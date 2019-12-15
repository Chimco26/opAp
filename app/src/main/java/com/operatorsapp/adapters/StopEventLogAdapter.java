package com.operatorsapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.operatorsapp.R;

import java.util.List;

import static com.operatorsapp.utils.ReasonImage.getImageForStopReason;

public class StopEventLogAdapter extends RecyclerView.Adapter {

    private List<String> items;
    private StopEventLogAdapterListener mListener;

    public StopEventLogAdapter(List<String> machineLineItems, StopEventLogAdapterListener listener) {
        items = machineLineItems;
        mListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == 0) {
            return new StopEventLogAdapter.ViewHolderTitle(inflater.inflate(R.layout.item_stop_event_log_titles, parent, false));
        } else {
            return new StopEventLogAdapter.ViewHolderItems(inflater.inflate(R.layout.item_stop_event_log, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        if (getItemViewType(0) == 0){return;}

        ViewHolderItems viewHolderItems = (ViewHolderItems) viewHolder;

        if (true){
            viewHolderItems.subMarginView.setVisibility(View.VISIBLE);
        }else {
            viewHolderItems.subMarginView.setVisibility(View.GONE);
        }

        viewHolderItems.stopIc.setImageDrawable(viewHolder.itemView.getContext().
                getResources().getDrawable(getImageForStopReason(0)));

        viewHolderItems.title.setText("");
        viewHolderItems.start.setText("");
        viewHolderItems.end.setText("");
        viewHolderItems.duration.setText("");
        viewHolderItems.machine.setText("");

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onLogSelected("");
            }
        });

        viewHolderItems.expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.expand(true, items.get(position));
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return 0;
        }else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    public class ViewHolderItems extends RecyclerView.ViewHolder {

        private final View expand;
        private final View subMarginView;
        private final ImageView stopIc;
        private final TextView title;
        private final TextView start;
        private final TextView end;
        private final TextView duration;
        private final TextView machine;

        ViewHolderItems(View itemView) {
            super(itemView);

            expand = itemView.findViewById(R.id.ISEL_expand_img);
            subMarginView = itemView.findViewById(R.id.ISEL_sub_item_margin);
            stopIc = itemView.findViewById(R.id.ISEL_ic);
            title = itemView.findViewById(R.id.ISEL_item_title);
            start = itemView.findViewById(R.id.ISEL_start);
            end = itemView.findViewById(R.id.ISEL_end);
            duration = itemView.findViewById(R.id.ISEL_duration);
            machine = itemView.findViewById(R.id.ISEL_machine);
        }

    }

    public class ViewHolderTitle extends RecyclerView.ViewHolder {

        ViewHolderTitle(View itemView) {
            super(itemView);
        }

    }

    public interface StopEventLogAdapterListener {

        void onLogSelected(String item);

        void expand(boolean expand, String mainItem);
    }
}
