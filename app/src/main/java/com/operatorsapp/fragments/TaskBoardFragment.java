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
import android.widget.Toast;

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
import java.util.List;
import java.util.Locale;

import static androidx.annotation.Dimension.SP;

public class TaskBoardFragment extends Fragment {
    private BoardView mBoardView;
    private ArrayList<TaskProgress> mTodoList;
    private ArrayList<TaskProgress> mInProgressList;
    private ArrayList<TaskProgress> mDoneList;
    private ArrayList<TaskProgress> mCancelledList;

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
        ProgressDialogManager.show(getActivity());
        initVars(view);
        configBoard();
        initBoardListener();
        getTaskList();
    }

    public void initVars(@NonNull View view) {
        mBoardView = view.findViewById(R.id.FTB_board_view);
        ((TextView)view.findViewById(R.id.FTB_title_tv)).setText(String.format(Locale.getDefault(),
                "%s - %d", getString(R.string.task_manager), PersistenceManager.getInstance().getMachineId()));
    }

    private void initColumns(String name, List<TaskProgress> taskProgress) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        int backgroundColor = ContextCompat.getColor(getContext(), R.color.white);

//        initTestList(taskProgress);
        TaskColumnAdapter listAdapter = new TaskColumnAdapter(taskProgress);

        LinearLayout view = initHeaderListView(name, taskProgress);

        ColumnProperties columnProperties = ColumnProperties.Builder.newBuilder(listAdapter)
                .setLayoutManager(layoutManager)
                .setHasFixedItemSize(false)
                .setColumnBackgroundColor(Color.TRANSPARENT)
                .setItemsSectionBackgroundColor(backgroundColor)
                .setHeader(view)
//                .setColumnDrugView(textView)
                .build();

        mBoardView.addColumn(columnProperties);
    }

    @NotNull
    private LinearLayout initHeaderListView(String name, List<TaskProgress> taskProgress) {
        LinearLayout view = new LinearLayout(getContext());
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50));
        view.setOrientation(LinearLayout.HORIZONTAL);
        view.setGravity(Gravity.CENTER_VERTICAL);
        view.setPadding(10, 0, 10, 0);
        view.setBackgroundColor(getContext().getResources().getColor(R.color.black));
        TextView textView = new TextView(getContext());
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setText(name);
        textView.setTextColor(getContext().getResources().getColor(R.color.white));
        textView.setTextSize(SP, 25);
        view.addView(textView);
        View marginView = new View(getContext());
        marginView.setLayoutParams(new LinearLayout.LayoutParams(20, ViewGroup.LayoutParams.MATCH_PARENT));
        view.addView(marginView);
        TextView textView1 = new TextView(getContext());
        textView1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView1.setId(R.id.TCH_count_generated);
        textView1.setText(String.valueOf(taskProgress.size()));
        textView1.setTextColor(getContext().getResources().getColor(R.color.blue2));
        textView1.setTextSize(SP, 25);
        view.addView(textView1);
        return view;
    }

    private void initTestList(List<TaskProgress> taskProgress) {
        taskProgress.clear();
        taskProgress.add(new TaskProgress(1));
        taskProgress.add(new TaskProgress(2));
        taskProgress.add(new TaskProgress(3));
        taskProgress.add(new TaskProgress(4));
        taskProgress.add(new TaskProgress(5));
    }

    private void initBoardListener() {
        mBoardView.setBoardListener(new BoardView.BoardListener() {
            @Override
            public void onItemDragStarted(int column, int row) {
//                Toast.makeText(getActivity(), "Start - column: " + column + " row: " + row, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemDragEnded(int fromColumn, int fromRow, int toColumn, int toRow) {
                if (fromColumn != toColumn || fromRow != toRow) {
                    Toast.makeText(getActivity(), "End - column: " + toColumn + " row: " + toRow, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onItemChangedPosition(int oldColumn, int oldRow, int newColumn, int newRow) {
                Toast.makeText(mBoardView.getContext(), "Position changed - column: " + newColumn + " row: " + newRow, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemChangedColumn(int oldColumn, int newColumn) {
                TextView itemCount1 = (TextView) mBoardView.getHeaderView(oldColumn).findViewById(R.id.TCH_count_generated);
                itemCount1.setText("" + mBoardView.getAdapter(oldColumn).getItemCount());
                TextView itemCount2 = (TextView) mBoardView.getHeaderView(newColumn).findViewById(R.id.TCH_count_generated);
                itemCount2.setText("" + mBoardView.getAdapter(newColumn).getItemCount());
            }

            @Override
            public void onFocusedColumnChanged(int oldColumn, int newColumn) {
//                Toast.makeText(getContext(), "Focused column changed from " + oldColumn + " to " + newColumn, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onColumnDragStarted(int position) {
//                Toast.makeText(getContext(), "Column drag started from " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onColumnDragChangedPosition(int oldPosition, int newPosition) {
//                Toast.makeText(getContext(), "Column changed from " + oldPosition + " to " + newPosition, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onColumnDragEnded(int position) {
//                Toast.makeText(getContext(), "Column drag ended at " + position, Toast.LENGTH_SHORT).show();
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
        PersistenceManager pm = PersistenceManager.getInstance();
        SimpleRequests.getTaskList(pm.getSiteUrl(), new GetTaskListCallback() {
            @Override
            public void onGetTaskListCallbackSuccess(TaskListResponse response) {
                ProgressDialogManager.dismiss();
                List<TaskProgress> taskList = response.getResponseDictionaryDT().getTaskProgress();
                initColumnLists();
                createColumnsLists(taskList);
                initColumns(getString(R.string.todo), mTodoList);
                initColumns(getString(R.string.in_progress), mInProgressList);
                initColumns(getString(R.string.done_for_task), mDoneList);
                initColumns(getString(R.string.cancelled), mCancelledList);
            }

            @Override
            public void onGetTaskListCallbackFailed(StandardResponse reason) {
                ProgressDialogManager.dismiss();

            }
        }, NetworkManager.getInstance(), pm.getTotalRetries(), pm.getRequestTimeout());
    }

    private void initColumnLists() {
        if (mTodoList == null) {
            mTodoList = new ArrayList<>();
        }else {
            mTodoList.clear();
        }
        if (mInProgressList == null) {
            mInProgressList = new ArrayList<>();
        } else {
            mInProgressList.clear();
        }
        if (mDoneList == null) {
            mDoneList = new ArrayList<>();
        }else {
            mDoneList.clear();
        }
        if (mCancelledList == null) {
            mCancelledList = new ArrayList<>();
        }else {
            mCancelledList.clear();
        }
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

    private void updateTaskStatus(TaskHistory taskHistory) {
        ProgressDialogManager.show(getActivity());
        PersistenceManager pm = PersistenceManager.getInstance();
        SimpleRequests.updateTaskStatus(taskHistory, pm.getSiteUrl(), new UpdateTaskStatusCallback() {
            @Override
            public void onUpdateTaskStatusCallbackSuccess(StandardResponse response) {
                ProgressDialogManager.dismiss();
            }

            @Override
            public void onUpdateTaskStatusCallbackFailed(StandardResponse reason) {
                ProgressDialogManager.dismiss();
            }
        }, NetworkManager.getInstance(), pm.getTotalRetries(), pm.getRequestTimeout());
    }
}