package com.operatorsapp.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.common.StandardResponse;
import com.example.common.callback.CreateTaskCallback;
import com.example.common.task.Task;
import com.example.common.task.TaskInfoObject;
import com.example.common.task.TaskObjectForCreateOrEditContent;
import com.example.common.task.TaskObjectsForCreateOrEditResponse;
import com.example.common.task.TaskProgress;
import com.operators.reportrejectnetworkbridge.interfaces.GetTaskObjectsForCreateCallback;
import com.operatorsapp.R;
import com.operatorsapp.adapters.TaskInfoObjectSpinnerAdapter;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.SimpleRequests;
import com.operatorsapp.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.operatorsapp.utils.TimeUtils.ONLY_DATE_FORMAT;
import static com.operatorsapp.utils.TimeUtils.SQL_T_FORMAT_NO_SECOND;

public class TaskDetailsFragment extends Fragment {

    private TaskProgress mTask;
    private TaskObjectForCreateOrEditContent mEditTaskObject;
    private TextView mTitleTv;
    private TextView mDateTv;
    private TextView mAutorTv;
    private TextView mStartDate;
    private TextView mEndDate;
    private Spinner mLevelSpinner;
    private Spinner mSubjectSpinner;
    private Spinner mStatusSpinner;

    public static TaskDetailsFragment newInstance(TaskProgress taskProgress) {
        TaskDetailsFragment taskDetailsFragment = new TaskDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TaskProgress.TAG, taskProgress);
        taskDetailsFragment.setArguments(bundle);
        return new TaskDetailsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null && getArguments().containsKey(TaskProgress.TAG)) {
            mTask = (TaskProgress) getArguments().get(TaskProgress.TAG);
        }
        if (mTask == null){
            mTask = new TaskProgress();
        }
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVars(view);
        initListener(view);
        getTaskObjectsForCreateOrEdit();
    }

    private void initVars(View view) {
        mTitleTv = view.findViewById(R.id.FTD_title_tv);
        mDateTv = view.findViewById(R.id.FTD_date_tv);
        mAutorTv = view.findViewById(R.id.FTD_open_by_tv);
        mStartDate = view.findViewById(R.id.FTD_start_date_edit_tv);
        mEndDate = view.findViewById(R.id.FTD_end_date_edit_tv);
        mStatusSpinner = view.findViewById(R.id.FTD_status_spinner);
        mSubjectSpinner = view.findViewById(R.id.FTD_subject_spinner);
        mLevelSpinner = view.findViewById(R.id.FTD_level_spinner);
    }

    private void initView(TaskProgress task, TaskObjectForCreateOrEditContent editTaskObject) {
        if (task == null) {
            mTitleTv.setText(getString(R.string.add_new_task));
            mDateTv.setText(TimeUtils.getDate(new Date().getTime(), ONLY_DATE_FORMAT));
            mAutorTv.setText(PersistenceManager.getInstance().getOperatorName());
            initStatusSpinner(editTaskObject.getStatus(), 0);
            initSubjectSpinner(editTaskObject.getSubjects());
            initLevelSpinner(editTaskObject.getLevel());
            initStartAndEndTimeViews();
        } else {
            mTitleTv.setText(task.getSubjectTrans());
            mDateTv.setText(TimeUtils.getDate(TimeUtils.convertDateToMillisecond(task.getTaskCreateDate(),
                    SQL_T_FORMAT_NO_SECOND), ONLY_DATE_FORMAT));
            mStartDate.setText(TimeUtils.getDate(TimeUtils.convertDateToMillisecond(task.getTaskStartTimeTarget(),
                    SQL_T_FORMAT_NO_SECOND), ONLY_DATE_FORMAT));
            mEndDate.setText(TimeUtils.getDate(TimeUtils.convertDateToMillisecond(task.getTaskEndTimeTarget(),
                    SQL_T_FORMAT_NO_SECOND), ONLY_DATE_FORMAT));
            mAutorTv.setText(String.valueOf(task.getTaskCreateUser()));
            initSeverity(task, editTaskObject);
            initAttachFiles(task);
            initTotalTime(task);
            initStatusSpinner(editTaskObject.getStatus(), task.getTaskStatus());
            List<TaskInfoObject> subjects = new ArrayList<>();
            subjects.add(new TaskInfoObject(task.getSubjectTrans()));
            initSubjectSpinner(subjects);
            List<TaskInfoObject> levels = new ArrayList<>();
            levels.add(new TaskInfoObject(String.valueOf(task.getTaskLevel())));
            initLevelSpinner(levels);
        }
    }

    private void initStartAndEndTimeViews() {
        mStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(), R.style.TimePickerTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calendar.set(Calendar.YEAR, i);
                        calendar.set(Calendar.MONTH, i1);
                        calendar.set(Calendar.DAY_OF_MONTH, i2);
                        mTask.setTaskStartTimeTarget(TimeUtils.getDate(calendar.getTime().getTime(), SQL_T_FORMAT_NO_SECOND));
                        mStartDate.setText(TimeUtils.getDate(
                                TimeUtils.convertDateToMillisecond(mTask.getTaskStartTimeTarget(), SQL_T_FORMAT_NO_SECOND),
                                ONLY_DATE_FORMAT));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        mEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(), R.style.TimePickerTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calendar.set(Calendar.YEAR, i);
                        calendar.set(Calendar.MONTH, i1);
                        calendar.set(Calendar.DAY_OF_MONTH, i2);
                        mTask.setTaskEndTimeTarget(TimeUtils.getDate(calendar.getTime().getTime(), SQL_T_FORMAT_NO_SECOND));
                        mEndDate.setText(TimeUtils.getDate(
                                TimeUtils.convertDateToMillisecond(mTask.getTaskEndTimeTarget(), SQL_T_FORMAT_NO_SECOND),
                                ONLY_DATE_FORMAT));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
    }

    private void initLevelSpinner(final List<TaskInfoObject> level) {
        final TaskInfoObjectSpinnerAdapter dataAdapter = new TaskInfoObjectSpinnerAdapter(getActivity(), R.layout.base_spinner_item, level);
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_custom);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLevelSpinner.setAdapter(dataAdapter);
        mLevelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mTask.setTaskLevel(level.get(adapterView.getSelectedItemPosition()).getID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initSubjectSpinner(final List<TaskInfoObject> subjects) {
        final TaskInfoObjectSpinnerAdapter dataAdapter = new TaskInfoObjectSpinnerAdapter(getActivity(), R.layout.base_spinner_item, subjects);
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_custom);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSubjectSpinner.setAdapter(dataAdapter);
        mSubjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mTask.setTaskLevel(subjects.get(adapterView.getSelectedItemPosition()).getID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initStatusSpinner(final List<TaskInfoObject> status, final int taskStatus) {
        final TaskInfoObjectSpinnerAdapter dataAdapter = new TaskInfoObjectSpinnerAdapter(getActivity(), R.layout.base_spinner_item, status);
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_custom);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mStatusSpinner.setAdapter(dataAdapter);
        mStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int selectedId = status.get(adapterView.getSelectedItemPosition()).getID();
                if (selectedId != taskStatus) {
                    mTask.setTaskLevel(status.get(adapterView.getSelectedItemPosition()).getID());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initTotalTime(TaskProgress task) {

    }

    private void initAttachFiles(TaskProgress task) {

    }

    private void initSeverity(TaskProgress task, TaskObjectForCreateOrEditContent editTaskObject) {

    }

    private void initListener(View view) {
        view.findViewById(R.id.FTD_close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });
        view.findViewById(R.id.FTD_add_task_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTask(buildTask(mTask));
            }
        });
    }

    private Task buildTask(TaskProgress task) {
        return null;
    }

    private void createTask(Task task) {
        PersistenceManager pm = PersistenceManager.getInstance();
        ProgressDialogManager.show(getActivity());
        SimpleRequests.createOrUpdateTask(task, pm.getSiteUrl(), new CreateTaskCallback() {
            @Override
            public void onCreateTaskCallbackSuccess(StandardResponse response) {
                ProgressDialogManager.dismiss();
            }

            @Override
            public void onCreateTaskCallbackFailed(StandardResponse reason) {
                ProgressDialogManager.dismiss();
            }
        }, NetworkManager.getInstance(), pm.getTotalRetries(), pm.getRequestTimeout());
    }

    private void getTaskObjectsForCreateOrEdit() {
        PersistenceManager pm = PersistenceManager.getInstance();
        ProgressDialogManager.show(getActivity());
        SimpleRequests.getTaskObjectsForCreateOrEdit(pm.getSiteUrl(), new GetTaskObjectsForCreateCallback() {
            @Override
            public void onGetTaskObjectsForCreateCallbackSuccess(TaskObjectsForCreateOrEditResponse response) {
                ProgressDialogManager.dismiss();
                mEditTaskObject = response.getResponseDictionaryDT();
                initView(mTask, mEditTaskObject);
            }

            @Override
            public void onGetTaskObjectsForCreateCallbackFailed(StandardResponse reason) {
                ProgressDialogManager.dismiss();
            }
        }, NetworkManager.getInstance(), pm.getTotalRetries(), pm.getRequestTimeout());
    }

}
