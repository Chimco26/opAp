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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.common.Event;
import com.example.common.actualBarExtraResponse.Inventory;
import com.example.common.actualBarExtraResponse.Notification;
import com.example.common.actualBarExtraResponse.Reject;
import com.operatorsapp.R;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.interfaces.OnStopClickListener;

import java.util.ArrayList;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private static final int PIXEL_FOR_MINUTE = 4;
    private boolean mClosedState;
    private boolean mIsSelectionMode;
    private final OnStopClickListener mOnStopClickListener;

    private Context mContext;
    private ArrayList<Event> mEvents;
    private ArrayList<Float> mSelectedEvents;

    public EventsAdapter(Context context, OnStopClickListener onStopClickListener, boolean selectMode, boolean closedState, ArrayList<Event> events, ArrayList<Float> selectedEvents) {
        mContext = context;
        mOnStopClickListener = onStopClickListener;
        mIsSelectionMode = selectMode;
        mClosedState = closedState;
        mEvents = events;
        mSelectedEvents = selectedEvents;
    }

    public void setSelectedEvents(ArrayList<Float> selectedEvents) {
        mSelectedEvents = selectedEvents;
    }

    public void setIsSelectionMode(boolean mIsSelectionMode) {
        this.mIsSelectionMode = mIsSelectionMode;
        notifyDataSetChanged();
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
        holder.updateItem(position, holder);

    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final View mView;
        private final RelativeLayout mTimeContainer;
        private final View mCircle;
        private TextView mText;
        private TextView mTime;
        private View mLine;
        private LinearLayout mCheckContainer;
        private RelativeLayout mRelative;
        private CheckBox mCheckBox;
        private RelativeLayout mTechContainer;

        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            mText = itemView.findViewById(R.id.EI_text);
            mTimeContainer = itemView.findViewById(R.id.time_container);
            mTime = itemView.findViewById(R.id.EI_time);
            mLine = itemView.findViewById(R.id.EI_line);
            mCircle = itemView.findViewById(R.id.EI_circle);
            mCheckContainer = itemView.findViewById(R.id.EI_check_container);
            mRelative = itemView.findViewById(R.id.EI_relative);
            mCheckBox = itemView.findViewById(R.id.EI_check_box);
//            mTechCall = itemView.findViewById(R.id.EI_tech_call);
            mTechContainer = itemView.findViewById(R.id.EI_service_call_container);

        }

        private void updateItem(int position, final ViewHolder holder) {
            if (position > mEvents.size() - 1){return;}
            final Event event = mEvents.get(position);

            holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (mOnStopClickListener != null && event.getType() != 1) {
                        mOnStopClickListener.onStopEventSelected(event.getEventID(), isChecked);
                    }

                }
            });
            event.setChecked(false);
            if (mSelectedEvents != null) {
                for (Float event1 : mSelectedEvents) {
                    if (event.getEventID() == event1) {
                        event.setChecked(true);
                    }
                }
            }

            if (mIsSelectionMode && event.isChecked()) {

                holder.mCheckBox.setChecked(true);
            } else {
                holder.mCheckBox.setChecked(false);
            }
            setViewHeight(event);
            updateNotification(event);

            mLine.setBackgroundColor(Color.parseColor(event.getColor()));
            GradientDrawable circleBackground = (GradientDrawable) mCircle.getBackground();
            circleBackground.setColor(Color.parseColor(event.getColor()));

            String startTime = "";
            if (event.getEventTime() != null && event.getEventTime().length() > 0) {
                startTime = event.getEventTime().substring(10, 16);
            }

            mTime.setText(startTime);

            mText.setVisibility(View.VISIBLE);

            long duration = event.getDuration();
            if (duration == 0) {
                duration = 1;
            }

            String text = duration + "m " + event.getSubtitleLname();
            if (!mClosedState && text.length() > 12) {
                mText.setText(String.format("%s...", text.substring(0, 12)));

            } else {
                mText.setText(text);
            }
            GradientDrawable textBackground = (GradientDrawable) mText.getBackground();
            textBackground.setColor(Color.parseColor(event.getColor()));


            if (mIsSelectionMode) {
                mCheckContainer.setVisibility(View.VISIBLE);
                mTechContainer.setVisibility(View.GONE);

                if (event.getType() != 1 || event.getEventGroupID() != 20) {

                    holder.mCheckBox.setVisibility(View.VISIBLE);
                    holder.mCheckBox.setChecked(event.isChecked());
                } else {
                    holder.mCheckBox.setVisibility(View.GONE);

                }
            } else {
                mCheckContainer.setVisibility(View.GONE);
                holder.mCheckBox.setVisibility(View.GONE);
                mTechContainer.setVisibility(View.VISIBLE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!mIsSelectionMode) {
                        if (mOnStopClickListener != null && event.getType() != 1 && event.getEventGroupID() != 20) {
                            mOnStopClickListener.onSelectMode(event);
                        }
                    } else {
                        holder.mCheckBox.setChecked(true);
                    }
                }
            });

        }

        private void setViewHeight(Event event) {
            ViewGroup.LayoutParams params = mView.getLayoutParams();

            if (mIsSelectionMode && event.getType() == 1) {
                params.height = 0;
            } else if ((int) event.getDuration() * PIXEL_FOR_MINUTE > 300) {
                params.height = 300;
            } else if (event.getDuration() > 4) {
                params.height = (int) event.getDuration() * PIXEL_FOR_MINUTE;
            } else {
                params.height = 5 * PIXEL_FOR_MINUTE;
            }
            mView.setLayoutParams(params);
        }

        private void updateNotification(Event event) {
            if (mTechContainer.getChildCount() > 0) {
                mTechContainer.removeAllViews();
            }


            if (event.getNotifications() != null && event.getNotifications().size() > 0) {
                for (Notification notification : event.getNotifications()) {
                    setNotification(event, 2, notification.getSentTime().substring(11, 16), notification.getText(), notification.getNotificationType());
                }
            }

            if (event.getRejects() != null && event.getRejects().size() > 0) {
                for (Reject reject : event.getRejects()) {

                    String name = OperatorApplication.isEnglishLang() ? reject.getEName() : reject.getLName();

                    setNotification(event, 3, reject.getTime().substring(11, 16), reject.getAmount() + " " + name, 0);
                }
            }

            if (event.getInventories() != null && event.getInventories().size() > 0) {
                for (Inventory inventory : event.getInventories()) {
                    setNotification(event, 1, inventory.getTime(), inventory.getAmount() + " " + inventory.getLName(), 0);
                }
            }

//                    long height = (techCallInfo.getmCallTime() - eventStartMilli) * PIXEL_FOR_MINUTE;
//                    if (height > mView.getHeight()) {
//                        view.setPadding(0, mView.getHeight() - view.getHeight(), 0, 0);
//                    } else {
//                        view.setPadding(0, (int) height, 0, 0);
//                    }


//                }
        }

        private void setNotification(Event event, int type, String time, String details, int icon) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.service_call_item, mTechContainer, false);
            TextView timeTV = view.findViewById(R.id.SCI_time);
            TextView detailsTV = view.findViewById(R.id.SCI_details);
            ImageView iconIV = view.findViewById(R.id.SCI_service_call_icon);

            if (event.getDuration() <= 5) {
                timeTV.setVisibility(View.GONE);
            } else {
                timeTV.setVisibility(View.VISIBLE);
            }

            switch (type) {
                case 1:
                    timeTV.setText(time);
                    detailsTV.setText(details);
                    iconIV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.production_black));
                    break;

                case 2:
                    timeTV.setText(time);
                    detailsTV.setText(details);
                    setNotificationIcon(iconIV, icon);
                    break;

                case 3:
                    timeTV.setText(time);
                    detailsTV.setText(details);
                    iconIV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.rejects_black));
                    break;


//                default:
//                    timeTV.setVisibility(View.GONE);
//                    detailsTV.setVisibility(View.GONE);
//                    iconIV.setVisibility(View.GONE);
//                    break;
            }
            mTechContainer.addView(view);
        }


        /**
         * @param iconIV
         * @param icon   the number of icon
         *               1 - 9 service calls
         *               case 10 message
         *               case 11 production
         *               case 12 rejects
         */
        private void setNotificationIcon(ImageView iconIV, int icon) {

            switch (icon) {
                case 1:
                    iconIV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.message_dark));
                    break;
                case 2:
                    iconIV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.technicaian_black));
                    break;
                case 3:
                    iconIV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.called_black));
                    break;
                case 4:
                    iconIV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.message_declined_black));
                    break;

                default:
                    iconIV.setImageDrawable(null);
                    break;
            }
        }
    }

}
