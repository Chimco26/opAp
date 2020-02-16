package com.operatorsapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.common.task.TaskProgress;
import com.operatorsapp.R;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.utils.TimeUtils;
import com.woxthebox.draglistview.DragItemAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TaskColumnAdapter extends DragItemAdapter<TaskProgress, TaskColumnAdapter.ViewHolder>
        implements Filterable {

    private LinearLayout headerView;
    private String mSearchExpression = "";
    private List<TaskProgress> list;
    private TaskFilter mFilter = new TaskFilter();
    private List<TaskProgress> mListFiltered = new ArrayList<>();
    private Long minDateToShow;

    public TaskColumnAdapter(List<TaskProgress> tasks, long minDateToShow, LinearLayout view) {
        this.minDateToShow = minDateToShow;
        headerView = view;
        list = tasks;
        mListFiltered.addAll(list);
    }

    public String getSearchExpression() {
        return mSearchExpression;
    }

    public List<TaskProgress> getList() {
        return list;
    }

    public List<TaskProgress> getListFiltered() {
        return mListFiltered;
    }

    @Override
    public long getUniqueItemId(int position) {
        return Long.parseLong(String.valueOf(mListFiltered.get(position).getTaskID()));
    }

    @NonNull
    @Override
    public TaskColumnAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new TaskColumnAdapter.ViewHolder(inflater.inflate(R.layout.item_task, parent, false), R.id.ITask_main_rl, true);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.mTitle.setText(String.valueOf(mListFiltered.get(position).getSubjectTrans()));
        holder.mText.setText(mListFiltered.get(position).getText());
        String delegate = mListFiltered.get(position).getHistoryDisplayName();
        if (delegate == null || delegate.isEmpty()) {
            holder.mDelegate.setVisibility(View.INVISIBLE);
        } else {
            holder.mDelegate.setVisibility(View.VISIBLE);
            holder.mDelegate.setText(mListFiltered.get(position).getHistoryDisplayName2Chars());
        }

        holder.mPriorityIc.setColorFilter(getPriorityColor(mListFiltered.get(position), holder.mPriorityIc.getContext()));

        if (mListFiltered.get(position).isCriticalState()) {
            holder.mAlertMarge.setVisibility(View.VISIBLE);
        } else {
            holder.mAlertMarge.setVisibility(View.INVISIBLE);
        }
    }

    private int getPriorityColor(TaskProgress taskProgress, Context context) {
        int color;
        switch (taskProgress.getTaskPriorityID()) {
            case 1:
                color = ContextCompat.getColor(context, R.color.green_light);
                break;
//            case 2:
//                color = ContextCompat.getColor(context, R.color.alert);
//                break;
            case 3:
                color = ContextCompat.getColor(context, R.color.red_dark);
                break;
            case 4:
                color = ContextCompat.getColor(context, R.color.red_line);
                break;
            default:
                color = ContextCompat.getColor(context, R.color.alert);
                break;
        }
        return color;

    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public class ViewHolder extends DragItemAdapter.ViewHolder {
        private TextView mTitle;
        private TextView mText;
        private TextView mDelegate;
        private ImageView mPriorityIc;
        private View mAlertMarge;

        public ViewHolder(View itemView, int handleResId, boolean dragOnLongPress) {
            super(itemView, handleResId, dragOnLongPress);
            mTitle = itemView.findViewById(R.id.ITask_title);
            mText = itemView.findViewById(R.id.ITask_text);
            mDelegate = itemView.findViewById(R.id.ITask_author);
            mPriorityIc = itemView.findViewById(R.id.ITask_priority_ic);
            mAlertMarge = itemView.findViewById(R.id.ITask_priority_view);
        }

    }

    private class TaskFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            boolean onlyCritical = PersistenceManager.getInstance().getTaskFilterOnlyCritical();
            List<Integer> priorityToShow = PersistenceManager.getInstance().getTaskFilterPriorityToShow();
            mSearchExpression = constraint.toString();
            FilterResults results = new FilterResults();

            mListFiltered.clear();
            ArrayList<TaskProgress> allItems = new ArrayList<>();
            for (TaskProgress task : list) {
                if ((constraint.length() == 0 || (isContainLetters(task.getSubjectTrans().toLowerCase(), constraint.toString().toLowerCase())
                        || isContainLetters(task.getText().toLowerCase(), constraint.toString().toLowerCase()))
                ) && (minDateToShow == 0
                        || TimeUtils.convertDateToMillisecond(task.getHistoryCreateDate(), TimeUtils.SQL_T_FORMAT_NO_SECOND) >= minDateToShow)
                && isPriorityToShow(task.getTaskPriorityID(), priorityToShow)
                && isCriticalToShow(task.isCriticalState(), onlyCritical)) {
                    if (!allItems.contains(task)) {
                        allItems.add(task);
                    }
                }

                mListFiltered.addAll(allItems);
                results.values = allItems;
                results.count = allItems.size();

            }
            return results;

        }

        private boolean isCriticalToShow(boolean criticalState, boolean onlyCritical) {
            return !onlyCritical || criticalState;
        }

        private boolean isPriorityToShow(int taskPriorityID, List<Integer> priorityToShow) {
            for (Integer integer: priorityToShow){
                if (integer == taskPriorityID){
                    return true;
                }
            }
            return false;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            mListFiltered = (List<TaskProgress>) results.values;
            TextView itemCount1 = headerView.findViewById(R.id.TCH_count_generated);
            itemCount1.setText(String.format(Locale.getDefault(), "%d", mListFiltered.size()));
            setItemList(mListFiltered);
        }

        private boolean isContainLetters(String text, String searchLetters) {
            String[] searchLettersExpressions = searchLetters.split(" ");
            for (String string : searchLettersExpressions) {
                if (!text.contains(string)) {
                    return false;
                }
            }
            return true;
        }

    }
}
