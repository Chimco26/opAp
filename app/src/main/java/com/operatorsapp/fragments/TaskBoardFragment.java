package com.operatorsapp.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.common.SelectableString;
import com.example.common.StandardResponse;
import com.example.common.callback.GetTaskListCallback;
import com.example.common.task.TaskHistory;
import com.example.common.task.TaskListResponse;
import com.example.common.task.TaskProgress;
import com.example.common.utils.TimeUtils;
import com.operators.reportrejectnetworkbridge.interfaces.UpdateTaskStatusCallback;
import com.operatorsapp.R;
import com.operatorsapp.adapters.SimpleSpinnerAdapter;
import com.operatorsapp.adapters.TaskColumnAdapter;
import com.operatorsapp.dialogs.TaskFilterDialog;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.SimpleRequests;
import com.operatorsapp.utils.TaskUtil;
import com.woxthebox.draglistview.BoardView;
import com.woxthebox.draglistview.ColumnProperties;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.text.format.DateUtils.DAY_IN_MILLIS;
import static androidx.annotation.Dimension.SP;

public class TaskBoardFragment extends Fragment implements TaskColumnAdapter.TaskColumnAdapterListener {
    private static final int COLUMN_COUNT = 4;
    private BoardView mBoardView;
    private ArrayList<TaskProgress> mTodoList;
    private ArrayList<TaskProgress> mInProgressList;
    private ArrayList<TaskProgress> mDoneList;
    private ArrayList<TaskProgress> mCancelledList;
    private ArrayList<ColumnObject> mColumnsObjectList = new ArrayList<>();
    private boolean isOrderByDate = true;
    private ImageView mFilterIc;
    private TaskBoardFragmentListener mListener;

    public static TaskBoardFragment newInstance() {
        return new TaskBoardFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TaskBoardFragmentListener){
            mListener = (TaskBoardFragmentListener) context;
        }else {
            throw new RuntimeException(context.toString()
                    + " must implement TaskBoardFragmentListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task_board, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initParams();
        initVars(view);
        initView(view);
        initListener(view);
        configBoard();
        initBoardListener();
        getTaskList();
    }

    private void initParams() {
        isOrderByDate = PersistenceManager.getInstance().getTasksOrderByDate();
        initFilterParams();
    }

    /**
     * because need to insert value from resources
     */
    private void initFilterParams() {
        ArrayList<SelectableString> priorityList = PersistenceManager.getInstance().getTaskFilterPriorityToShow();
        for (SelectableString selectableString : priorityList) {
            selectableString.setColor(TaskUtil.getPriorityColor(Integer.parseInt(selectableString.getId()), getContext()));
            selectableString.setString(TaskUtil.getPriorityName(Integer.parseInt(selectableString.getId()), getContext()));
        }
        PersistenceManager.getInstance().setTaskFilterPriorityToShow(priorityList);

        ArrayList<SelectableString> periodList = PersistenceManager.getInstance().getTaskFilterPeriodToShow();
        periodList.get(0).setColor(getResources().getColor(R.color.blue1));
        periodList.get(0).setString(getString(R.string.in_time));
        periodList.get(1).setColor(getResources().getColor(R.color.red_dark));
        periodList.get(1).setString(getString(R.string.past_time));
        PersistenceManager.getInstance().setTaskFilterPeriodToShow(periodList);
    }

    public void initVars(@NonNull View view) {
        mBoardView = view.findViewById(R.id.FTB_board_view);
        mFilterIc = view.findViewById(R.id.FTB_filter_ic);
        initOrderBySpinner((Spinner) view.findViewById(R.id.FTB_order_by_spinner));
    }

    private void initOrderBySpinner(Spinner spinner) {
        List<String> list = new ArrayList<>();
        list.add(getString(R.string.date));
        list.add(getString(R.string.priority));
        final SimpleSpinnerAdapter dataAdapter = new SimpleSpinnerAdapter(getActivity(), R.layout.base_spinner_item, list);
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_custom);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        if (!isOrderByDate) {
            spinner.setSelection(1, false);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        isOrderByDate = true;
                        break;
                    case 1:
                        isOrderByDate = false;
                        break;
                }
                PersistenceManager.getInstance().setTasksOrderByDate(isOrderByDate);
                orderAll();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void initListener(@NonNull View view) {
        view.findViewById(R.id.FTB_close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });
        view.findViewById(R.id.FTB_create_btn_ly).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onCreateTask();
            }
        });
        view.findViewById(R.id.FTB_filter_ly).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskFilterDialog taskFilterDialog = new TaskFilterDialog(getContext());
                AlertDialog dialog = taskFilterDialog.showTaskFilterDialog();
                dialog.show();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        orderAll();
                        initFilterIcon();
                    }
                });
            }
        });
        ((EditText) view.findViewById(R.id.FTB_search_et)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                for (ColumnObject columnObject : mColumnsObjectList) {
                    columnObject.getAdapter().getFilter().filter(charSequence);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void initView(@NonNull View view) {
        ((TextView) view.findViewById(R.id.FTB_title_tv)).setText(String.format(Locale.getDefault(),
                "%s - %d", getString(R.string.task_manager), PersistenceManager.getInstance().getMachineId()));
        initFilterIcon();
    }

    private void initFilterIcon(){
        if (TaskUtil.isFiltered()){
            mFilterIc.setImageDrawable(getResources().getDrawable(R.drawable.ic_filter_selected));
        }else {
            mFilterIc.setImageDrawable(getResources().getDrawable(R.drawable.ic_filter_default));
        }
    }

    private void initColumns(String name, List<TaskProgress> taskProgress, int id, long minDateToShow) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        int backgroundColor = ContextCompat.getColor(getContext(), R.color.white);

        LinearLayout view = initHeaderListView(name);

        TaskColumnAdapter listAdapter = new TaskColumnAdapter(taskProgress, minDateToShow, view, this);

        ColumnProperties columnProperties = ColumnProperties.Builder.newBuilder(listAdapter)
                .setLayoutManager(layoutManager)
                .setHasFixedItemSize(false)
                .setColumnBackgroundColor(Color.TRANSPARENT)
                .setItemsSectionBackgroundColor(backgroundColor)
                .setHeader(view)
//                .setColumnDrugView(textView)
                .build();

        mBoardView.addColumn(columnProperties);

        mColumnsObjectList.add(new ColumnObject(name, id, listAdapter));
    }

    @NotNull
    private LinearLayout initHeaderListView(String name) {
        LinearLayout view = new LinearLayout(getContext());
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50));
        view.setOrientation(LinearLayout.HORIZONTAL);
        view.setGravity(Gravity.CENTER_VERTICAL);
        view.setPadding(10, 0, 10, 0);
        view.setBackgroundColor(getContext().getResources().getColor(R.color.default_gray));
        TextView columnName = new TextView(getContext());
        columnName.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        columnName.setText(name);
        columnName.setTextColor(getContext().getResources().getColor(R.color.white));
        columnName.setTextSize(SP, 25);
        view.addView(columnName);
        View marginView = new View(getContext());
        marginView.setLayoutParams(new LinearLayout.LayoutParams(20, ViewGroup.LayoutParams.MATCH_PARENT));
        view.addView(marginView);
        TextView columnCounter = new TextView(getContext());
        columnCounter.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        columnCounter.setId(R.id.TCH_count_generated);
//        columnCounter.setText(String.valueOf(taskProgress.size()));
        columnCounter.setTextColor(getContext().getResources().getColor(R.color.white));
        columnCounter.setTextSize(SP, 25);
        view.addView(columnCounter);
        return view;
    }

    private void initBoardListener() {
        mBoardView.setBoardListener(new BoardView.BoardListener() {
            @Override
            public void onItemDragStarted(int column, int row) {
            }

            @Override
            public void onItemDragEnded(int fromColumn, int fromRow, int toColumn, int toRow) {
                if (fromColumn == toColumn) {
                    mBoardView.moveItem(toColumn, toRow, fromColumn, fromRow, false);
                } else {
                    updateTaskStatus(mColumnsObjectList.get(toColumn).getAdapter().getListFiltered().get(toRow), fromColumn, fromRow, toColumn, toRow);
                }
            }

            @Override
            public void onItemChangedPosition(int oldColumn, int oldRow, int newColumn, int newRow) {
            }

            @Override
            public void onItemChangedColumn(int oldColumn, int newColumn) {
            }

            @Override
            public void onFocusedColumnChanged(int oldColumn, int newColumn) {
            }

            @Override
            public void onColumnDragStarted(int position) {
            }

            @Override
            public void onColumnDragChangedPosition(int oldPosition, int newPosition) {
            }

            @Override
            public void onColumnDragEnded(int position) {
            }
        });
        mBoardView.setBoardCallback(new BoardView.BoardCallback() {
            @Override
            public boolean canDragItemAtPosition(int column, int dragPosition) {
                // Add logic here to prevent an item to be dragged
                return true;
            }

            @Override
            public boolean canDropItemAtPosition(int oldColumn, int oldRow, int newColumn, int newRow) {
                // Add logic here to prevent an item to be dropped
                return true;
            }
        });
    }

    private void configBoard() {
        mBoardView.setSnapToColumnsWhenScrolling(true);
        mBoardView.setSnapToColumnWhenDragging(true);
        mBoardView.setSnapDragItemToTouch(true);
        mBoardView.setSnapToColumnInLandscape(false);
        mBoardView.setColumnSnapPosition(BoardView.ColumnSnapPosition.CENTER);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        //to set column dimension in ratio of screen width
        mBoardView.setColumnWidth((int) (metrics.widthPixels / COLUMN_COUNT - ((COLUMN_COUNT - 1) * 10) * metrics.density));
    }

    private void getTaskList() {
        ProgressDialogManager.show(getActivity());
        PersistenceManager pm = PersistenceManager.getInstance();
        SimpleRequests.getTaskList(pm.getSiteUrl(), new GetTaskListCallback() {
            @Override
            public void onGetTaskListCallbackSuccess(TaskListResponse response) {
                ProgressDialogManager.dismiss();
                if (isDetached() || getContext() == null) {
                    return;
                }
                List<TaskProgress> taskList = response.getResponseDictionaryDT().getTaskProgress();
                initColumnLists();
                createColumnsLists(taskList);
                initColumns(getString(R.string.todo), mTodoList, TaskProgress.TaskStatus.TODO.getValue(), 0);
                initColumns(getString(R.string.in_progress), mInProgressList, TaskProgress.TaskStatus.IN_PROGRESS.getValue(), 0);
                initColumns(getString(R.string.done_for_task), mDoneList, TaskProgress.TaskStatus.DONE.getValue(), new Date().getTime() - DAY_IN_MILLIS);
                initColumns(getString(R.string.cancelled), mCancelledList, TaskProgress.TaskStatus.CANCELLED.getValue(), new Date().getTime() - DAY_IN_MILLIS);
                orderAll();
            }

            @Override
            public void onGetTaskListCallbackFailed(StandardResponse reason) {
                ProgressDialogManager.dismiss();

            }
        }, NetworkManager.getInstance(), pm.getTotalRetries(), pm.getRequestTimeout());
    }

    private void initColumnLists() {
        mTodoList = initList(mTodoList);
        mInProgressList = initList(mInProgressList);
        mDoneList = initList(mDoneList);
        mCancelledList = initList(mCancelledList);
    }

    private ArrayList<TaskProgress> initList(ArrayList<TaskProgress> list) {
        if (list == null) {
            list = new ArrayList<>();
        } else {
            list.clear();
        }
        return list;
    }

    private void createColumnsLists(List<TaskProgress> taskList) {
        for (TaskProgress task : taskList) {
            switch (task.getTaskStatus()) {
                case 2:
                    mTodoList.add(task);
                    break;
                case 3:
                    mInProgressList.add(task);
                    break;
                case 4:
                    mDoneList.add(task);
                    break;
                case 5:
                    mCancelledList.add(task);
                    break;
            }
        }
    }

    private void updateTaskStatus(final TaskProgress taskProgress, final int fromColumn, final int fromRow, final int toColumn, final int toRow) {
        ProgressDialogManager.show(getActivity());
        PersistenceManager pm = PersistenceManager.getInstance();
        SimpleRequests.updateTaskStatus(createHistoryObject(taskProgress, toColumn), pm.getSiteUrl(), new UpdateTaskStatusCallback() {
            @Override
            public void onUpdateTaskStatusCallbackSuccess(StandardResponse response) {
                ProgressDialogManager.dismiss();
                //update original list to perform filtering
                mColumnsObjectList.get(fromColumn).getAdapter().getList().remove(taskProgress);
                mColumnsObjectList.get(toColumn).getAdapter().getList().add(taskProgress);
                //reorder
                orderList(mColumnsObjectList.get(fromColumn));
                orderList(mColumnsObjectList.get(toColumn));
            }

            @Override
            public void onUpdateTaskStatusCallbackFailed(StandardResponse reason) {
                ProgressDialogManager.dismiss();
                mBoardView.moveItem(toColumn, toRow, fromColumn, fromRow, false);
            }
        }, NetworkManager.getInstance(), pm.getTotalRetries(), pm.getRequestTimeout());
    }

    private void orderAll() {
        for (ColumnObject columnObject : mColumnsObjectList) {
            orderList(columnObject);
        }
    }

    private void orderList(ColumnObject columnObject) {
        if (isOrderByDate) {
            orderByDate(columnObject.getAdapter().getList());
        } else {
            orderByPriority(columnObject.getAdapter().getList());
        }
        columnObject.getAdapter().getFilter().filter(columnObject.getAdapter().getSearchExpression());
    }

    private void orderByPriority(List<TaskProgress> list) {
        Collections.sort(list, new Comparator<TaskProgress>() {
            @Override
            public int compare(TaskProgress o1, TaskProgress o2) {
                return Integer.valueOf(o2.getTaskPriorityID()).compareTo(o1.getTaskPriorityID());
            }
        });
    }

    private void orderByDate(List<TaskProgress> list) {
        Collections.sort(list, new Comparator<TaskProgress>() {
            @Override
            public int compare(TaskProgress o1, TaskProgress o2) {
                return TimeUtils.convertDateToMillisecond(o2.getTaskStartTimeTarget(), TimeUtils.SQL_T_FORMAT_NO_SECOND)
                        .compareTo(TimeUtils.convertDateToMillisecond(o1.getTaskStartTimeTarget(), TimeUtils.SQL_T_FORMAT_NO_SECOND));
            }
        });
    }

    private TaskHistory createHistoryObject(TaskProgress taskProgress, int toColumn) {
        return new TaskHistory(taskProgress.getTaskID(), mColumnsObjectList.get(toColumn).getId(), taskProgress.getAssignee());
    }

    @Override
    public void onTaskClicked(TaskProgress taskProgress) {
        mListener.onTaskClicked(taskProgress);
    }

    private class ColumnObject {
        TaskColumnAdapter adapter;
        String name;
        private int id;

        public ColumnObject(String name, int id, TaskColumnAdapter adapter) {
            this.adapter = adapter;
            this.name = name;
            this.id = id;
        }

        public TaskColumnAdapter getAdapter() {
            return adapter;
        }

        public String getName() {
            return name;
        }

        public Integer getId() {
            return id;
        }
    }


    public interface TaskBoardFragmentListener{
        void onTaskClicked(TaskProgress taskProgress);

        void onCreateTask();
    }
}