package com.operatorsapp.fragments;

import android.graphics.Color;
import android.os.Bundle;
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
import com.example.common.task.Task;
import com.operatorsapp.R;
import com.operatorsapp.adapters.TaskColumnAdapter;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.SimpleRequests;
import com.woxthebox.draglistview.BoardView;
import com.woxthebox.draglistview.ColumnProperties;

import java.util.ArrayList;

public class TaskBoardFragment extends Fragment {
    private BoardView mBoardView;

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

        mBoardView = view.findViewById(R.id.FTB_board_view);
        configBoard();
        initBoardListener();
        initColumns();
        initColumns();
    }

    private void initColumns() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        int backgroundColor = ContextCompat.getColor(getContext(), R.color.white);

        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Task("1"));
        tasks.add(new Task("2"));
        tasks.add(new Task("3"));
        tasks.add(new Task("4"));
        tasks.add(new Task("5"));
        TaskColumnAdapter listAdapter = new TaskColumnAdapter(tasks);

        LinearLayout view = new LinearLayout(getActivity());
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50));
        view.setBackgroundColor(getContext().getResources().getColor(R.color.black));
        TextView textView = new TextView(getContext());
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setId(R.id.TCH_count_generated);
        textView.setText("0");
        textView.setTextColor(getContext().getResources().getColor(R.color.white));
        view.addView(textView);

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

    private void initBoardListener() {
        mBoardView.setBoardListener(new BoardView.BoardListener() {
            @Override
            public void onItemDragStarted(int column, int row) {
                Toast.makeText(getActivity(), "Start - column: " + column + " row: " + row, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), "Focused column changed from " + oldColumn + " to " + newColumn, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onColumnDragStarted(int position) {
                Toast.makeText(getContext(), "Column drag started from " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onColumnDragChangedPosition(int oldPosition, int newPosition) {
                Toast.makeText(getContext(), "Column changed from " + oldPosition + " to " + newPosition, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onColumnDragEnded(int position) {
                Toast.makeText(getContext(), "Column drag ended at " + position, Toast.LENGTH_SHORT).show();
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
    }

    private void getTaskList() {
        PersistenceManager pm = PersistenceManager.getInstance();
        SimpleRequests.getTaskList(pm.getSiteUrl(), new GetTaskListCallback() {
            @Override
            public void onGetTaskListCallbackSuccess(Object response) {

            }

            @Override
            public void onGetTaskListCallbackFailed(StandardResponse reason) {

            }
        }, NetworkManager.getInstance(), pm.getTotalRetries(), pm.getRequestTimeout());
    }
}