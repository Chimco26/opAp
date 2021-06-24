package com.operatorsapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.StopLogs.Event;
import com.operatorsapp.R;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.server.responses.Notification;
import com.operatorsapp.utils.Consts;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.operatorsapp.utils.ReasonImage.getImageForStopReason;

public class StopEventLogAdapter extends RecyclerView.Adapter<StopEventLogAdapter.ViewHolder> implements Filterable {

    private ArrayList<Event> items;
    private StopEventLogAdapterListener mListener;
    private Filter mFilter = new StopLogsFilter();
    private ArrayList<Event> itemsFiltered = new ArrayList<>();

    public StopEventLogAdapter(ArrayList<Event> machineLineItems, StopEventLogAdapterListener listener) {
        items = machineLineItems;
        ArrayList<Event> events = filterEvents(items);
        itemsFiltered = reorderEvents((List<Event>) events);
        mListener = listener;
    }

    @NonNull
    @Override
    public StopEventLogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new StopEventLogAdapter.ViewHolder(inflater.inflate(R.layout.item_stop_event_log, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final StopEventLogAdapter.ViewHolder viewHolder, final int position) {

        final boolean isSelectedMode = isAtLeastOneSelected(items);

        final Event event = itemsFiltered.get(position);

        initViewSubOrRoot(event, viewHolder);

        setTexts(event, viewHolder);

        if (isSelectedMode) {
            viewHolder.multiSelectionCheckBox.setVisibility(View.VISIBLE);
            viewHolder.multiSelectionCheckBox.setChecked(event.isSelected());
        }else {
            viewHolder.multiSelectionCheckBox.setVisibility(View.INVISIBLE);
        }

        viewHolder.stopIc.setImageDrawable(viewHolder.itemView.getContext().
                getResources().getDrawable(getImageForStopReason(event.getEventGroupID())));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSelectedMode) {
                    viewHolder.multiSelectionCheckBox.setChecked(!event.isSelected());
                } else {
                    mListener.onLogSelected(itemsFiltered.get(viewHolder.getAdapterPosition()));
                }
            }
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                resetSelection();
                viewHolder.multiSelectionCheckBox.setChecked(true);
                return true;
            }
        });

        viewHolder.multiSelectionCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                itemsFiltered.get(viewHolder.getAdapterPosition()).setSelected(isChecked);
                mListener.onUpdateLogSelection(getSelectedItems());
                mFilter.filter("");
            }
        });

        viewHolder.expandBackgrnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Event event1 = itemsFiltered.get(viewHolder.getAdapterPosition());
                event1.setExpand(!event1.isExpand());
                updateEvents(event1);
                getFilter().filter("");
            }
        });

        Notification notification = checkTechCallForEvent(event);
        if (notification != null) {
            setTechCallStatusForEvent(viewHolder, notification);
            viewHolder.techCall.setVisibility(View.GONE);
        } else {
            viewHolder.techCallStatusLil.setVisibility(View.GONE);
            viewHolder.techCall.setVisibility(View.VISIBLE);
            viewHolder.techCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onTechCallSelected(itemsFiltered.get(viewHolder.getAdapterPosition()));
                }
            });
        }
    }

    private ArrayList<Event> getSelectedItems() {
        ArrayList<Event> list = new ArrayList<>();
        for (Event item: items){
            if (item.isSelected()){
                list.add(item);
            }
        }
        return list;
    }

    private boolean isAtLeastOneSelected(ArrayList<Event> items) {
        for (Event item: items){
            if (item.isSelected()){
                return true;
            }
        }
        return false;
    }

    private void resetSelection() {
        for (Event item : items) {
            item.setSelected(false);
        }
    }

    private void setTechCallStatusForEvent(ViewHolder viewHolder, Notification notification) {

        Context context = viewHolder.itemView.getContext();
        int icon = R.drawable.technician_blue_svg;
        String txt = context.getResources().getString(R.string.waiting_for_replay);
        switch (notification.getmResponseType()) {

            case Consts.NOTIFICATION_RESPONSE_TYPE_UNSET:
                icon = R.drawable.call_recieved;
                txt = context.getResources().getString(R.string.waiting_for_replay);
                break;
            case Consts.NOTIFICATION_RESPONSE_TYPE_APPROVE:
                icon = R.drawable.call_sent_blue;
                txt = context.getResources().getString(R.string.call_approved);
                break;
            case Consts.NOTIFICATION_RESPONSE_TYPE_DECLINE:
                icon = R.drawable.call_declined;
                txt = context.getResources().getString(R.string.call_declined);
                break;
            case Consts.NOTIFICATION_RESPONSE_TYPE_CANCELLED:
                icon = R.drawable.cancel_blue;
                txt = context.getResources().getString(R.string.service_call_was_canceled);
//                techViewHolder.mManageCallFl.setVisibility(View.INVISIBLE);
//                techViewHolder.mRemoveIv.setVisibility(View.INVISIBLE);
                break;
            case Consts.NOTIFICATION_RESPONSE_TYPE_START_SERVICE:
                icon = R.drawable.at_work_blue;
                txt = context.getResources().getString(R.string.at_work);
//                techViewHolder.mManageCallIv.setImageDrawable(context.getResources().getDrawable(R.drawable.check_all));
//                techViewHolder.mManageCallTv.setText(context.getResources().getString(R.string.finish_service));
                break;
            case Consts.NOTIFICATION_RESPONSE_TYPE_END_SERVICE:
                icon = R.drawable.service_done;
                txt = context.getResources().getString(R.string.service_completed);
//                techViewHolder.mManageCallFl.setVisibility(View.INVISIBLE);
                break;
        }
        viewHolder.techCall.setVisibility(View.GONE);
        viewHolder.techCallStatusLil.setVisibility(View.VISIBLE);
        viewHolder.techCallStatusIv.setImageResource(icon);
        viewHolder.techCallStatusTv.setText(txt);

    }

    private Notification checkTechCallForEvent(Event event) {
        for (Notification notification : PersistenceManager.getInstance().getNotificationHistory()) {
            if (notification.getmEventID() == event.getEventID()) {
                return notification;
            }
        }
        return null;
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

    private void initViewSubOrRoot(Event event, ViewHolder viewHolderItems) {
        if (event.isHaveChild()) {
            viewHolderItems.expand.setVisibility(View.VISIBLE);
            viewHolderItems.expandBackgrnd.setVisibility(View.VISIBLE);
            viewHolderItems.subMarginView.setVisibility(View.VISIBLE);
            if (event.isExpand()) {
                viewHolderItems.expand.setRotationX(180);
            } else {
                viewHolderItems.expand.setRotationX(0);
            }
        } else if (event.getRootEventID() != 0) {
            viewHolderItems.expand.setVisibility(View.INVISIBLE);
            viewHolderItems.expandBackgrnd.setVisibility(View.INVISIBLE);
            viewHolderItems.subMarginView.setVisibility(View.INVISIBLE);
        } else {
            viewHolderItems.expand.setVisibility(View.INVISIBLE);
            viewHolderItems.expandBackgrnd.setVisibility(View.VISIBLE);
            viewHolderItems.subMarginView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if (itemsFiltered != null) {
            return itemsFiltered.size();
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
        private final FrameLayout techCall;
        private final View expandBackgrnd;
        private final LinearLayout techCallStatusLil;
        private final ImageView techCallStatusIv;
        private final TextView techCallStatusTv;
        private CheckBox multiSelectionCheckBox;

        ViewHolder(View itemView) {
            super(itemView);

            expand = itemView.findViewById(R.id.ISEL_expand_img);
            expandBackgrnd = itemView.findViewById(R.id.ISEL_expand_img_rl);
            subMarginView = itemView.findViewById(R.id.ISEL_sub_item_margin);
            multiSelectionCheckBox = itemView.findViewById(R.id.ISEL_sub_item_check_box);
            stopIc = itemView.findViewById(R.id.ISEL_ic);
            title = itemView.findViewById(R.id.ISEL_item_title);
            start = itemView.findViewById(R.id.ISEL_start);
            end = itemView.findViewById(R.id.ISEL_end);
            duration = itemView.findViewById(R.id.ISEL_duration);
            machine = itemView.findViewById(R.id.ISEL_machine);
            techCall = itemView.findViewById(R.id.ISEL_tech_call_fl);
            techCallStatusLil = itemView.findViewById(R.id.ISEL_tech_call_info_lil);
            techCallStatusIv = itemView.findViewById(R.id.ISEL_tech_call_status_iv);
            techCallStatusTv = itemView.findViewById(R.id.ISEL_tech_call_subtext_tv);
        }

    }

    private class StopLogsFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            ArrayList<Event> events = filterEvents(items);

            events = reorderEvents((List<Event>) events);

            results.values = events;

            results.count = events.size();

            return results;

        }


        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            itemsFiltered = ((ArrayList<Event>) results.values);

            notifyDataSetChanged();
        }

    }

    @NotNull
    private ArrayList<Event> filterEvents(ArrayList<Event> items) {
        ArrayList<Event> events = new ArrayList<>();
        events.addAll(items);
        for (Event event : items) {
            if (!event.isShowSub() && event.getRootEventID() != 0) {
                events.remove(event);
            }

        }
        return events;
    }

    private ArrayList<Event> reorderEvents(List<Event> events) {
        ArrayList<Event> toRemove = new ArrayList<>();
        ArrayList<Event> list = new ArrayList<>();
        ArrayList<Event> rootList = new ArrayList<>();
        for (Event event : events) {
            if (event.getRootEventID() == 0) {
                rootList.add(event);
                toRemove.add(event);
            }
        }
        events.removeAll(toRemove);

        for (Event event : rootList) {
            list.add(event);
            for (Event eventSub : events) {
                if (eventSub.getRootEventID().equals(event.getEventID())) {
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

        void onUpdateLogSelection(ArrayList<Event> item);

        void onTechCallSelected(Event item);
    }
}
