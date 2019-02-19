package com.operatorsapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.operatorsapp.R;
import com.operatorsapp.interfaces.OnStopClickListener;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.model.TechCallInfo;
import com.ravtech.david.sqlcore.Event;

import java.util.ArrayList;

import static com.operatorsapp.utils.TimeUtils.convertDateToMillisecond;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private static final String SQL_NO_T_FORMAT = "dd/MM/yyyy HH:mm:ss";
    private static final int PIXEL_FOR_MINUTE = 4;
    private static final int PIXEL_FOR_BIG_MINUTE = 10;
    private static final int NEW_EVENT_ID = 5555;
    private final ArrayList<TechCallInfo> mTechs;
    private boolean mClosedState;
    private boolean mIsSelectionMode;
    private final OnStopClickListener mOnStopClickListener;

    private Context mContext;
    private ArrayList<Event> mEvents;

    public EventsAdapter(Context context, OnStopClickListener onStopClickListener, boolean selectMode, boolean closedState, ArrayList<Event> events) {
        mContext = context;
        mOnStopClickListener = onStopClickListener;
        mIsSelectionMode = selectMode;
        mClosedState = closedState;
        mEvents = events;
        mTechs = PersistenceManager.getInstance().getCalledTechnician();

    }

//    public void setEvents(ArrayList<Event> events) {
//        updateList(events);
//        notifyDataSetChanged();
//    }

    public void setIsSelectionMode(boolean mIsSelectionMode) {
        this.mIsSelectionMode = mIsSelectionMode;
    }

    public void setClosedState(boolean isSelectionMode) {
        mClosedState = isSelectionMode;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.event_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.updateItem(position);

    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        private final View mView;
        private final RelativeLayout mTimeContainer;
        private final View mCircle;
        private TextView mText;
        private TextView mTime;
        private View mLine;
        private LinearLayout mCheckContainer;
        private RelativeLayout mRelative;
        private CheckBox mCheckBox;
        private Event mEvent;
        //        private ImageView mTechCall;
        private RelativeLayout mTechContainer;

//        private Event mEvent;


        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mView = itemView;
            mText = itemView.findViewById(R.id.EI_text);
            mTimeContainer = itemView.findViewById(R.id.time_container);
            mTime = itemView.findViewById(R.id.EI_time);
            mLine = itemView.findViewById(R.id.EI_line);
            mCircle = itemView.findViewById(R.id.EI_circle);
            mCheckContainer = itemView.findViewById(R.id.EI_check_container);
            mRelative = itemView.findViewById(R.id.EI_relative);
            mCheckBox = itemView.findViewById(R.id.EI_check_box);
            mCheckBox.setOnCheckedChangeListener(this);
//            mTechCall = itemView.findViewById(R.id.EI_tech_call);
            mTechContainer = itemView.findViewById(R.id.EI_service_call_container);

        }

        private void updateItem(int position) {
            mEvent = mEvents.get(position);

            setViewHeight();

            mLine.setBackgroundColor(Color.parseColor(mEvent.getColor()));
            GradientDrawable circleBackground = (GradientDrawable) mCircle.getBackground();
            circleBackground.setColor(Color.parseColor(mEvent.getColor()));

            String startTime = "";
            if (mEvent.getEventTime() != null && mEvent.getEventTime().length() > 0) {
                startTime = mEvent.getEventTime().substring(10, 16);
            }
            if (mEvent.getEventEndTime() != null && mEvent.getEventEndTime().length() > 0) {
                startTime += "; " + mEvent.getEventEndTime().substring(10, 16) + " id; " + mEvent.getEventID();
            }
            mTime.setText(startTime);

//            if (!mEvent.getColor().equals("#1aa917")) {

            mText.setVisibility(View.VISIBLE);

            String text = mEvent.getDuration() + "m " + mEvent.getSubtitleLname();
            if (!mClosedState && text.length() > 12) {
                mText.setText(String.format("%s...", text.substring(0, 12)));

            } else {
                mText.setText(text + "; " + startTime);
            }
            GradientDrawable textBackground = (GradientDrawable) mText.getBackground();
            textBackground.setColor(Color.parseColor(mEvent.getColor()));

//            } else {
//                mText.setVisibility(View.GONE);
//            }


            if (mIsSelectionMode) {
                mCheckContainer.setVisibility(View.VISIBLE);

                if (mEvent.getEventID() != NEW_EVENT_ID) {

                    mCheckBox.setVisibility(View.VISIBLE);
                    mCheckBox.setChecked(mEvent.isChecked());
                } else {
                    mCheckBox.setVisibility(View.GONE);

                }
            } else {
                mCheckContainer.setVisibility(View.GONE);
                mCheckBox.setVisibility(View.GONE);

            }

//            updateTech();
        }

        private void setViewHeight() {

            ViewGroup.LayoutParams params = mView.getLayoutParams();

//            if (mIsSelectionMode && mEvent.getEventID() == NEW_EVENT_ID || mEvent.getDuration() == 0) {
            if (mIsSelectionMode && mEvent.getEventID() == NEW_EVENT_ID) {
                params.height = 0;
            } else if ((int) mEvent.getDuration() * PIXEL_FOR_MINUTE > 300) {
                params.height = 300;
            } else if (mEvent.getDuration() > 4) {
                params.height = (int) mEvent.getDuration() * PIXEL_FOR_MINUTE;
//            } else if (mEvent.getDuration() > 0){
            } else {
                params.height = 5 * PIXEL_FOR_MINUTE;
            }
//            params.height = 20;
            mView.setLayoutParams(params);
        }

        private void updateTech() {
            if (mTechContainer.getChildCount() > 0) {
                mTechContainer.removeAllViews();
            }

            Long eventStartMilli = convertDateToMillisecond(mEvent.getEventTime());
            Long eventEndMilli = convertDateToMillisecond(mEvent.getEventEndTime());

            for (TechCallInfo techCallInfo : mTechs) {
                if (techCallInfo.getmCallTime() > eventStartMilli && techCallInfo.getmCallTime() < eventEndMilli) {
                    View view = LayoutInflater.from(mContext).inflate(R.layout.service_call_item, mTechContainer, false);
//                    view.findViewById(R.id.SCI_time).se
                    long height = (techCallInfo.getmCallTime() - eventStartMilli) * PIXEL_FOR_MINUTE;
                    if (height > mView.getHeight()) {
                        view.setPadding(0, mView.getHeight() - view.getHeight(), 0, 0);
                    } else {
                        view.setPadding(0, (int) height, 0, 0);
                    }

                    mTechContainer.addView(view);

                }
            }
        }

        @Override
        public void onClick(View view) {
            mEvent.setChecked(!mEvent.isChecked());

            if (!mIsSelectionMode) {

                if (mOnStopClickListener != null && mEvent.getEventID() != NEW_EVENT_ID) {
                    mIsSelectionMode = true;
                    mOnStopClickListener.onSelectMode(mEvent);
                    notifyDataSetChanged();
                }
            } else {
                mCheckBox.setChecked(mEvent.isChecked());
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
            mEvent.setChecked(checked);

            if (mOnStopClickListener != null && mEvent.getEventID() != NEW_EVENT_ID) {
                mOnStopClickListener.onStopEventSelected(mEvent.getEventID(), checked);
            }
        }
    }


}
