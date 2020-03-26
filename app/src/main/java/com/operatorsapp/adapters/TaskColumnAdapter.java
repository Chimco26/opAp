package com.operatorsapp.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.common.SelectableString;
import com.example.common.task.TaskProgress;
import com.operatorsapp.R;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.utils.TaskUtil;
import com.operatorsapp.utils.TimeUtils;
import com.woxthebox.draglistview.DragItemAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TaskColumnAdapter extends DragItemAdapter<TaskProgress, TaskColumnAdapter.ViewHolder>
        implements Filterable {

    private LinearLayout headerView;
    private static String mSearchExpression = "";
    private List<TaskProgress> list;
    private TaskFilter mFilter = new TaskFilter();
    private List<TaskProgress> mListFiltered = new ArrayList<>();
    private Long minDateToShow;
    private TaskColumnAdapterListener mListener;

    public TaskColumnAdapter(List<TaskProgress> tasks, long minDateToShow, LinearLayout view, TaskColumnAdapterListener listener) {
        this.minDateToShow = minDateToShow;
        headerView = view;
        list = tasks;
        mListFiltered.addAll(list);
        mListener = listener;
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
        holder.mTitle.setText(String.format(Locale.getDefault(), "%s - %d", mListFiltered.get(position).getSubjectTrans(),
                mListFiltered.get(position).getTaskID()));
        holder.mText.setText(mListFiltered.get(position).getText());
        String delegate = mListFiltered.get(position).getAssigneeDisplayName();
        if (delegate == null || delegate.isEmpty()) {
            holder.mDelegate.setVisibility(View.INVISIBLE);
        } else {
            holder.mDelegate.setVisibility(View.VISIBLE);
            holder.mDelegate.setText(mListFiltered.get(position).getAssigneDisplayName2Chars());
        }

        holder.mPriorityIc.setColorFilter(TaskUtil.getPriorityColor(mListFiltered.get(position).getTaskPriorityID(), holder.mPriorityIc.getContext()));

        if (mListFiltered.get(position).isCriticalState()) {
            holder.mAlertMarge.setVisibility(View.VISIBLE);
        } else {
            holder.mAlertMarge.setVisibility(View.INVISIBLE);
        }
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

            itemView.findViewById(R.id.ITask_main_rl).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onTaskClicked(mListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    private class TaskFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList<SelectableString> periodToShow = PersistenceManager.getInstance().getTaskFilterPeriodToShow();
            List<SelectableString> priorityToShow = PersistenceManager.getInstance().getTaskFilterPriorityToShow();
            mSearchExpression = constraint.toString();
            FilterResults results = new FilterResults();

            mListFiltered.clear();
            ArrayList<TaskProgress> allItems = new ArrayList<>();
            for (TaskProgress task : list) {
                if ((constraint.length() == 0 ||
                        (isContainLetters(String.format(Locale.getDefault(), "%s - %d", task.getSubjectTrans(),
                                task.getTaskID()), constraint.toString().toLowerCase())
                                || isContainLetters(task.getText(), constraint.toString().toLowerCase())))
                        && (minDateToShow == 0 || TimeUtils.convertDateToMillisecond(task.getHistoryCreateDate(), TimeUtils.SQL_T_FORMAT_NO_SECOND) >= minDateToShow)
                        && isPriorityToShow(task.getTaskPriorityID(), priorityToShow)
                        && isCriticalToShow(task.isCriticalState(), periodToShow)) {
                    if (!allItems.contains(task)) {
                        allItems.add(task);
                    }
                }else {
                    Log.d("TAG", "performFiltering: ");
                }
            }
            mListFiltered.addAll(allItems);
            results.values = allItems;
            results.count = allItems.size();
            return results;

        }

        private boolean isCriticalToShow(boolean criticalState, ArrayList<SelectableString> periodToShow) {
            for (SelectableString integer : periodToShow) {
                if ((Integer.parseInt(integer.getId()) == 1 && integer.isSelected()
                        && !criticalState)
                        || (Integer.parseInt(integer.getId()) == 2 && integer.isSelected()
                        && criticalState)) {
                    return true;
                }
            }
            return false;
        }

        private boolean isPriorityToShow(int taskPriorityID, List<SelectableString> priorityToShow) {
            for (SelectableString integer : priorityToShow) {
                if (Integer.parseInt(integer.getId()) == taskPriorityID && integer.isSelected()) {
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
            itemCount1.setText(String.format(Locale.getDefault(), "%d", results.count));
            setItemList(mListFiltered);
        }

        private boolean isContainLetters(String text, String searchLetters) {
            if (text == null || text.length() < 1) {
                return false;
            }
            String[] searchLettersExpressions = searchLetters.split(" ");
            for (String string : searchLettersExpressions) {
                if (!text.toLowerCase().contains(string)) {
                    return false;
                }
            }
            return true;
        }
    }

    public interface TaskColumnAdapterListener {
        void onTaskClicked(TaskProgress taskProgress);
    }
}
