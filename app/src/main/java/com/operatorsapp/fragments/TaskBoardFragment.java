package com.operatorsapp.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.common.StandardResponse;
import com.example.common.callback.GetTaskListCallback;
import com.example.common.task.TaskHistory;
import com.example.common.task.TaskListResponse;
import com.example.common.task.TaskProgress;
import com.example.common.utils.TimeUtils;
import com.operators.reportrejectnetworkbridge.interfaces.UpdateTaskStatusCallback;
import com.operatorsapp.R;
import com.operatorsapp.adapters.TaskColumnAdapter;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.SimpleRequests;
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

public class TaskBoardFragment extends Fragment {
    private BoardView mBoardView;
    private ArrayList<TaskProgress> mTodoList;
    private ArrayList<TaskProgress> mInProgressList;
    private ArrayList<TaskProgress> mDoneList;
    private ArrayList<TaskProgress> mCancelledList;
    private ArrayList<ColumnObject> mColumnsObjectList = new ArrayList<>();
    private boolean isOrderByDate = true;

    public static TaskBoardFragment newInstance() {
        return new TaskBoardFragment();
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
        initVars(view);
        initParams();
        configBoard();
        initBoardListener();
        getTaskList();
    }

    private void initParams() {
        isOrderByDate = PersistenceManager.getInstance().getTasksOrderByDate();
//        ArrayList<Integer> integers = new ArrayList<Integer>();
//        integers.add(1);
//        PersistenceManager.getInstance().setTaskFilterPriorityToShow(integers);
//        PersistenceManager.getInstance().setTaskFilterOnlyCritical(true);
    }

    public void initVars(@NonNull View view) {
        mBoardView = view.findViewById(R.id.FTB_board_view);
        ((TextView) view.findViewById(R.id.FTB_title_tv)).setText(String.format(Locale.getDefault(),
                "%s - %d", getString(R.string.task_manager), PersistenceManager.getInstance().getMachineId()));
        view.findViewById(R.id.FTB_close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });
    }

    private void initColumns(String name, List<TaskProgress> taskProgress, int id, long minDateToShow) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        int backgroundColor = ContextCompat.getColor(getContext(), R.color.white);

        LinearLayout view = initHeaderListView(name);

        TaskColumnAdapter listAdapter = new TaskColumnAdapter(taskProgress, minDateToShow, view);

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
        mBoardView.setColumnWidth((int) (metrics.widthPixels / 4 - 30 * metrics.density));
    }

    private void getTaskList() {
        ProgressDialogManager.show(getActivity());
        PersistenceManager pm = PersistenceManager.getInstance();
        SimpleRequests.getTaskList(pm.getSiteUrl(), new GetTaskListCallback() {
            @Override
            public void onGetTaskListCallbackSuccess(TaskListResponse response) {
                ProgressDialogManager.dismiss();
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
            orderByDate(columnObject.getAdapter().getListFiltered());
        } else {
            orderByPriority(columnObject.getAdapter().getListFiltered());
        }
        columnObject.getAdapter().getFilter().filter("");
    }

    private void orderByPriority(List<TaskProgress> list) {
        if (list.size() > 0) {
            Collections.sort(list, new Comparator<TaskProgress>() {
                @Override
                public int compare(TaskProgress o1, TaskProgress o2) {
                    return Integer.valueOf(o2.getTaskPriorityID()).compareTo(o1.getTaskPriorityID());
                }
            });
        }
    }

    private void orderByDate(List<TaskProgress> list) {
        if (list.size() > 0) {
            Collections.sort(list, new Comparator<TaskProgress>() {
                @Override
                public int compare(TaskProgress o1, TaskProgress o2) {
                    return TimeUtils.convertDateToMillisecond(o2.getTaskStartTimeTarget(), TimeUtils.SQL_T_FORMAT_NO_SECOND)
                            .compareTo(TimeUtils.convertDateToMillisecond(o1.getTaskStartTimeTarget(), TimeUtils.SQL_T_FORMAT_NO_SECOND));
                }
            });
        }
    }

    private TaskHistory createHistoryObject(TaskProgress taskProgress, int toColumn) {
        return new TaskHistory(taskProgress.getTaskID(), mColumnsObjectList.get(toColumn).getId(), taskProgress.getAssignee());
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
}