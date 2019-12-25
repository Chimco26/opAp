package com.operatorsapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.common.StopLogs.Event;
import com.operatorsapp.R;
import com.operatorsapp.application.OperatorApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.operatorsapp.utils.ReasonImage.getImageForStopReason;

public class StopEventLogAdapter extends RecyclerView.Adapter<StopEventLogAdapter.ViewHolder> implements Filterable {

    private ArrayList<Event> items;
    private StopEventLogAdapterListener mListener;
    private Filter mFilter = new StopLogsFilter();
    private ArrayList<Event> itemsFiletered = new ArrayList<>();

    public StopEventLogAdapter(ArrayList<Event> machineLineItems, StopEventLogAdapterListener listener) {
        items = machineLineItems;
        itemsFiletered.addAll(machineLineItems);
        mListener = listener;
    }

    @NonNull
    @Override
    public StopEventLogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == 0) {
            return new StopEventLogAdapter.ViewHolder(inflater.inflate(R.layout.item_stop_event_log_titles, parent, false));
        } else {
            return new StopEventLogAdapter.ViewHolder(inflater.inflate(R.layout.item_stop_event_log, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final StopEventLogAdapter.ViewHolder viewHolder, final int position) {

        if (getItemViewType(position) == 0) {
            return;
        }

        final Event event = itemsFiletered.get(position);

        ViewHolder viewHolderItems = (ViewHolder) viewHolder;

        initViewSubOrRoot(event, viewHolderItems);

        setTexts(event, viewHolderItems);

        viewHolderItems.stopIc.setImageDrawable(viewHolder.itemView.getContext().
                getResources().getDrawable(getImageForStopReason(event.getEventGroupID())));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onLogSelected(event);
            }
        });

        viewHolderItems.expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                event.setExpand(!event.isExpand());
                updateEvents(event);
                getFilter().filter("");
//                notifyDataSetChanged(position + 1, updateEvents(event));
            }
        });
    }

    private int updateEvents(Event event) {
        int count = 0;
        for (Event eventItem : items) {
            if (eventItem.getRootEventID().equals(event.getEventID())) {
                eventItem.setShowSub(event.isExpand());
                count++;
            }
        }
        return count;
    }

    public void setTexts(Event event, ViewHolder viewHolderItems) {
        String nameByLang = OperatorApplication.isEnglishLang() ? event.getEventSubTitleEname() : event.getEventSubTitleLname();
        viewHolderItems.title.setText(nameByLang);
        viewHolderItems.start.setText(event.getEventTime());
        viewHolderItems.end.setText(event.getEventEndTime());
        viewHolderItems.duration.setText(String.format(Locale.getDefault(), "%d", event.getEventDuration()));
        viewHolderItems.machine.setText(event.getMachineName());
    }

    public void initViewSubOrRoot(Event event, ViewHolder viewHolderItems) {
        if (event.getRootEventID() != 0) {
            viewHolderItems.subMarginView.setVisibility(View.VISIBLE);
            viewHolderItems.expand.setVisibility(View.GONE);
        } else {
            viewHolderItems.subMarginView.setVisibility(View.GONE);
            viewHolderItems.expand.setVisibility(View.VISIBLE);
            if (event.isExpand()) {
                viewHolderItems.expand.setRotationX(180);
            } else {
                viewHolderItems.expand.setRotationX(0);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        if (itemsFiletered != null) {
            return itemsFiletered.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final View expand;
        private final View subMarginView;
        private final ImageView stopIc;
        private final TextView title;
        private final TextView start;
        private final TextView end;
        private final TextView duration;
        private final TextView machine;

        ViewHolder(View itemView) {
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

    private class StopLogsFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            ArrayList<Event> events = new ArrayList<>();
            events.addAll(items);
            for (Event event : items) {
                if (!event.isShowSub() && event.getRootEventID() != 0) {
                    events.remove(event);
                }

            }

            results.values = events;

            results.count = events.size();

            return results;

        }


        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            itemsFiletered = reorderEvents((List<Event>) results.values);

            notifyDataSetChanged();
        }

    }

    private ArrayList<Event> reorderEvents(List<Event> events) {
        ArrayList<Event> toRemove = new ArrayList<>();
        ArrayList<Event> list = new ArrayList<>();
        ArrayList<Event> rootList = new ArrayList<>();
        for (Event event: events){
            if (event.getRootEventID() == 0){
                rootList.add(event);
                toRemove.add(event);
            }
        }
        events.removeAll(toRemove);

        for (Event event: rootList){
            list.add(event);
            for (Event eventSub: events){
                if (eventSub.getRootEventID().equals(event.getEventID())){
                    list.add(eventSub);
                    toRemove.add(eventSub);
                }
            }
            events.removeAll(toRemove);
        }
        return list;
    }

    public interface StopEventLogAdapterListener {

        void onLogSelected(Event item);
    }
}
