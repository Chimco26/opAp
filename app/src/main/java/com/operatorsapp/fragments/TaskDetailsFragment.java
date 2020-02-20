package com.operatorsapp.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.SelectableString;
import com.example.common.StandardResponse;
import com.example.common.callback.CreateTaskCallback;
import com.example.common.task.Task;
import com.example.common.task.TaskInfoObject;
import com.example.common.task.TaskObjectForCreateOrEditContent;
import com.example.common.task.TaskObjectsForCreateOrEditResponse;
import com.example.common.task.TaskProgress;
import com.operators.reportrejectnetworkbridge.interfaces.GetTaskObjectsForCreateCallback;
import com.operatorsapp.R;
import com.operatorsapp.activities.GalleryActivity;
import com.operatorsapp.adapters.GalleryAdapter;
import com.operatorsapp.adapters.SeverityCheckBoxFilterAdapter;
import com.operatorsapp.adapters.TaskInfoObjectSpinnerAdapter;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.model.GalleryModel;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.SimpleRequests;
import com.operatorsapp.utils.TaskUtil;
import com.operatorsapp.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.text.format.DateUtils.DAY_IN_MILLIS;
import static com.operatorsapp.utils.TimeUtils.ONLY_DATE_FORMAT;
import static com.operatorsapp.utils.TimeUtils.SQL_T_FORMAT_NO_SECOND;

public class TaskDetailsFragment extends Fragment {

    private TaskProgress mTask;
    private TaskObjectForCreateOrEditContent mEditTaskObject;
    private TextView mTitleTv;
    private TextView mDateTv;
    private TextView mAuthorTv;
    private TextView mStartDate;
    private TextView mEndDate;
    private Spinner mLevelSpinner;
    private Spinner mSubjectSpinner;
    private Spinner mStatusSpinner;
    private EditText mDescriptionEt;
    private TextView mTotalTimeTv;
    private RecyclerView mSeverityRv;
    private RecyclerView mAttachedFilesRv;
    private View mAttachedFilesTv;

    public static TaskDetailsFragment newInstance(TaskProgress taskProgress) {
        TaskDetailsFragment taskDetailsFragment = new TaskDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TaskProgress.TAG, taskProgress);
        taskDetailsFragment.setArguments(bundle);
        return taskDetailsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null && getArguments().containsKey(TaskProgress.TAG)) {
            mTask = (TaskProgress) getArguments().get(TaskProgress.TAG);
        }
        if (mTask == null) {
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
        mAuthorTv = view.findViewById(R.id.FTD_open_by_tv);
        mStartDate = view.findViewById(R.id.FTD_start_date_edit_tv);
        mEndDate = view.findViewById(R.id.FTD_end_date_edit_tv);
        mStatusSpinner = view.findViewById(R.id.FTD_status_spinner);
        mSubjectSpinner = view.findViewById(R.id.FTD_subject_spinner);
        mLevelSpinner = view.findViewById(R.id.FTD_level_spinner);
        mDescriptionEt = view.findViewById(R.id.FTD_description_spinner);
        mTotalTimeTv = view.findViewById(R.id.FTD_total_time_tv);
        mSeverityRv = view.findViewById(R.id.FTD_severity_rv);
        mAttachedFilesRv = view.findViewById(R.id.FTD_attached_files_rv);
        mAttachedFilesTv = view.findViewById(R.id.FTD_attached_files_tv);
    }

    private void initView(TaskProgress task, TaskObjectForCreateOrEditContent editTaskObject) {
        if (task.getSubjectTrans() == null) {
            mTitleTv.setText(getString(R.string.add_new_task));
            mDateTv.setText(TimeUtils.getDate(new Date().getTime(), ONLY_DATE_FORMAT));
            mAuthorTv.setText(PersistenceManager.getInstance().getOperatorName());
            initStatusSpinner(editTaskObject.getStatus(), 0);
            initSubjectSpinner(editTaskObject.getSubjects());
//            initLevelSpinner(editTaskObject.getLevel());
            initStartAndEndTimeViews();
            initTotalTime(task);
            initSeverity(editTaskObject.getPriority(), TaskProgress.TaskStatus.TODO.getValue(), true);
            mAttachedFilesTv.setVisibility(View.GONE);
        } else {
            mDescriptionEt.setEnabled(false);
            mDescriptionEt.setFocusable(false);
            mTitleTv.setText(getString(R.string.edit_task));
            mDateTv.setText(TimeUtils.getDate(TimeUtils.convertDateToMillisecond(task.getTaskCreateDate(),
                    SQL_T_FORMAT_NO_SECOND), ONLY_DATE_FORMAT));
            mStartDate.setText(TimeUtils.getDate(TimeUtils.convertDateToMillisecond(task.getTaskStartTimeTarget(),
                    SQL_T_FORMAT_NO_SECOND), ONLY_DATE_FORMAT));
            mEndDate.setText(TimeUtils.getDate(TimeUtils.convertDateToMillisecond(task.getTaskEndTimeTarget(),
                    SQL_T_FORMAT_NO_SECOND), ONLY_DATE_FORMAT));
            mAuthorTv.setText(String.valueOf(task.getTaskCreateUser()));
            initSeverity(editTaskObject.getPriority(), task.getTaskStatus(), false);
            initAttachFiles();
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
        final long start;
        final long end;
        if (mTask.getTaskStartTimeTarget() != null) {
            start = TimeUtils.convertDateToMillisecond(mTask.getTaskStartTimeTarget(), SQL_T_FORMAT_NO_SECOND);
            mStartDate.setText(TimeUtils.getDate(
                    start, ONLY_DATE_FORMAT));
        } else {
            start = new Date().getTime();
            mStartDate.setText(TimeUtils.getDate(start, ONLY_DATE_FORMAT));
        }

        if (mTask.getTaskEndTimeTarget() != null) {
            end = TimeUtils.convertDateToMillisecond(mTask.getTaskEndTimeTarget(), SQL_T_FORMAT_NO_SECOND);
            mEndDate.setText(TimeUtils.getDate(
                    end, ONLY_DATE_FORMAT));
        } else {
            end = new Date().getTime();
            mEndDate.setText(TimeUtils.getDate(end, ONLY_DATE_FORMAT));
        }

        mStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(start));
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
                        initTotalTime(mTask);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(end);
                datePickerDialog.show();
            }
        });
        mEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(end));
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
                        initTotalTime(mTask);
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
                dataAdapter.setTitle(adapterView.getSelectedItemPosition());
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
                dataAdapter.setTitle(adapterView.getSelectedItemPosition());
                mTask.setSubjectTrans(subjects.get(adapterView.getSelectedItemPosition()).getName());
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
                dataAdapter.setTitle(adapterView.getSelectedItemPosition());
                mTask.setTaskStatus(selectedId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initTotalTime(TaskProgress task) {
        long start = TimeUtils.convertDateToMillisecond(task.getTaskStartTimeTarget(), SQL_T_FORMAT_NO_SECOND);
        long end = TimeUtils.convertDateToMillisecond(task.getTaskEndTimeTarget(), SQL_T_FORMAT_NO_SECOND);

        long total = 0;
        total = (end - start) / DAY_IN_MILLIS;
        total = total % DAY_IN_MILLIS;

        mTotalTimeTv.setText(String.format(Locale.getDefault(), "%d%s", total, getString(R.string.days)));

    }

    private void initAttachFiles() {

        final ArrayList<String> files = new ArrayList<>();
        files.add("https://www.ets.org/Media/Tests/GRE/pdf/gre_research_validity_data.pdf");

        files.add(files.get(0));
        files.add(files.get(0));
        files.add(files.get(0));

        files.add(3, "http://personal.psu.edu/hyw5138/mini.jpg");

        files.add(files.get(3));
        files.add(files.get(3));
        files.add(files.get(3));
        files.add(files.get(3));
        files.add(files.get(3));

        final ArrayList<GalleryModel> galleryModels = new ArrayList<>();

        for (String s : files) {

            galleryModels.add(new GalleryModel(s, false));
        }

        GalleryAdapter adapter = new GalleryAdapter(galleryModels, new GalleryAdapter.GalleryAdapterListener() {
            @Override
            public void onImageClick(GalleryModel galleryModel) {
                startGalleryActivity(files, "Task files");
            }

            @Override
            public void onPdfClick(GalleryModel galleryModel) {
                startGalleryActivity(files, "Task files");
            }
        }, getContext(), R.layout.item_task_gallery);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        mAttachedFilesRv.setLayoutManager(layoutManager);

        mAttachedFilesRv.setAdapter(adapter);
    }

    private void startGalleryActivity(List<String> fileUrl, String name) {

        if (fileUrl != null && fileUrl.size() > 0) {

            Intent galleryIntent = new Intent(getActivity(), GalleryActivity.class);

            galleryIntent.putExtra(GalleryActivity.EXTRA_FILE_URL, (ArrayList<String>) fileUrl);

            galleryIntent.putExtra(GalleryActivity.EXTRA_RECIPE_FILES_TITLE, name);

//            mGalleryIntent.putExtra(GalleryActivity.EXTRA_RECIPE_PDF_FILES, mPdfList);

            startActivityForResult(galleryIntent, GalleryActivity.EXTRA_GALLERY_CODE);

        }
    }

    private void initSeverity(final List<TaskInfoObject> severities, final int selectedId, boolean editable) {
        final ArrayList<SelectableString> severitiesObjects;
        severitiesObjects = initSeveritiesSelectableString(severities, selectedId);
        mSeverityRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mSeverityRv.setHasFixedSize(true);
        SeverityCheckBoxFilterAdapter severitiesAdapter = new SeverityCheckBoxFilterAdapter(severitiesObjects, new SeverityCheckBoxFilterAdapter.SeverityCheckBoxFilterAdapterListener() {
            @Override
            public void onItemCheck(SelectableString selectableString) {
                mTask.setTaskPriorityID(Integer.parseInt(selectableString.getId()));
            }
        }, false, true, editable);
        mSeverityRv.setAdapter(severitiesAdapter);
    }

    private ArrayList<SelectableString> initSeveritiesSelectableString(List<TaskInfoObject> editTaskObject, int selectedId) {
        ArrayList<SelectableString> selectableStrings = new ArrayList<>();
        for (TaskInfoObject taskInfoObject : editTaskObject) {
            SelectableString selectableString = new SelectableString(TaskUtil.getPriorityName(taskInfoObject.getID(), getContext()),
                    false, String.valueOf(taskInfoObject.getID()), TaskUtil.getPriorityColor(taskInfoObject.getID(), getContext()));
            if (selectedId == taskInfoObject.getID()) {
                selectableString.setSelected(true);
            }
            selectableStrings.add(selectableString);

        }
        return selectableStrings;
    }

    private void initListener(View view) {
        view.findViewById(R.id.FTD_close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
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

    private Task buildTask(TaskProgress taskProgress) {
        Task task = new Task();
//        task.setText(mDescriptionEt.getText().toString());
//        task.setCreateUser(mDescriptionEt.getText().toString());
//        task.setPriority(taskProgress.getTaskPriorityID());
//        task.setSubject(taskProgress.get);
        return task;
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
                removeOpenStatus(mEditTaskObject.getStatus());
                initView(mTask, mEditTaskObject);
            }

            @Override
            public void onGetTaskObjectsForCreateCallbackFailed(StandardResponse reason) {
                ProgressDialogManager.dismiss();
            }
        }, NetworkManager.getInstance(), pm.getTotalRetries(), pm.getRequestTimeout());
    }

    private void removeOpenStatus(List<TaskInfoObject> status) {
        for (TaskInfoObject taskInfoObject : status) {
            if (taskInfoObject.getID() == 1) {
                status.remove(taskInfoObject);
                return;
            }
        }
    }

}
