package com.operatorsapp.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.SelectableString;
import com.example.common.StandardResponse;
import com.example.common.callback.CreateTaskCallback;
import com.example.common.task.Task;
import com.example.common.task.TaskFilesResponse;
import com.example.common.task.TaskInfoObject;
import com.example.common.task.TaskObjectForCreateOrEditContent;
import com.example.common.task.TaskObjectsForCreateOrEditResponse;
import com.example.common.task.TaskProgress;
import com.operators.reportrejectnetworkbridge.interfaces.GetTaskFilesCallback;
import com.operators.reportrejectnetworkbridge.interfaces.GetTaskObjectsForCreateCallback;
import com.operatorsapp.R;
import com.operatorsapp.activities.GalleryActivity;
import com.operatorsapp.activities.TaskActivity;
import com.operatorsapp.adapters.GalleryAdapter;
import com.operatorsapp.adapters.SeverityCheckBoxFilterAdapter;
import com.operatorsapp.adapters.TaskInfoObjectSpinnerAdapter;
import com.operatorsapp.adapters.TaskNotesAdapter;
import com.operatorsapp.adapters.TaskStepsAdapter;
import com.operatorsapp.managers.CroutonCreator;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.model.GalleryModel;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.server.requests.CreateTaskNotesRequest;
import com.operatorsapp.server.requests.GetTaskNoteRequest;
import com.operatorsapp.server.responses.TaskNote;
import com.operatorsapp.server.responses.TaskNotesResponse;
import com.example.common.task.TaskStep;
import com.example.common.task.TaskStepResponse;
import com.operatorsapp.utils.InputFilterMinMax;
import com.operatorsapp.utils.KeyboardUtils;
import com.operatorsapp.utils.ShowCrouton;
import com.operatorsapp.utils.SimpleRequests;
import com.operatorsapp.utils.TaskUtil;
import com.operatorsapp.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.common.task.TaskProgress.TaskPriority.VERY_HIGH;
import static com.example.common.task.TaskProgress.TaskStatus.TODO;
import static com.operatorsapp.utils.TimeUtils.ONLY_DATE_FORMAT;
import static com.operatorsapp.utils.TimeUtils.SQL_NO_T_FORMAT_NO_SECOND;
import static com.operatorsapp.utils.TimeUtils.SQL_T_FORMAT_NO_SECOND;

public class TaskDetailsFragment extends Fragment {

    private static final Integer MACHINE_TASK_LEVEL = 3;
    private TaskProgress mTask;
    private TaskObjectForCreateOrEditContent mEditTaskObject;
    private TextView mTitleTv;
    private TextView mDateTv;
    private TextView mAuthorTv;
    private TextView mStartDate;
    private TextView mEndDate;
    private Spinner mAssignSpinner;
    private Spinner mSubjectSpinner;
    private Spinner mStatusSpinner;
    private EditText mDescriptionEt;
    private EditText mTimeHr;
    private EditText mTimeMin;
    private RecyclerView mSeverityRv;
    private RecyclerView mAttachedFilesRv;
    private View mAttachedFilesTv;
    private int initialStatus;
    private TextView mSaveBtn;
    private TaskDetailsFragmentListener mListener;
    private TextView mAssignTv;
    private View mAssignRl;
    private TextView mTaskIdTv;
    private LinearLayout mTaskIdLy;
    private int operatorId;
    private RecyclerView mNotesRv;
    private RecyclerView mTaskStepRv;
    private ArrayList<TaskNote> mTaskNoteList;
    private EditText mNotesAddNewEt;
    private ArrayList<TaskStep> mTaskStepList;
    private EditText mTaskStepAddNewEt;

    public static TaskDetailsFragment newInstance(TaskProgress taskProgress) {
        TaskDetailsFragment taskDetailsFragment = new TaskDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TaskProgress.TAG, taskProgress);
        taskDetailsFragment.setArguments(bundle);
        return taskDetailsFragment;
    }

    @Override
    public void onAttach(Context context) {
        if (context instanceof TaskDetailsFragmentListener) {
            mListener = (TaskDetailsFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement TaskDetailsFragmentListener");
        }
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null && getArguments().containsKey(TaskProgress.TAG)) {
            mTask = (TaskProgress) getArguments().get(TaskProgress.TAG);
        }
        operatorId = PersistenceManager.getInstance().getOperatorDBId();
        if (operatorId == 0) {
            operatorId = PersistenceManager.getInstance().getUserId();
        }
        if (mTask == null) {
            mTask = new TaskProgress();
            mTask.setHistoryCreateDate(TimeUtils.getDate(new Date().getTime(), SQL_T_FORMAT_NO_SECOND));
            mTask.setTaskCreateUser(operatorId);
            String operatorName = PersistenceManager.getInstance().getOperatorName();
            if (operatorName == null || operatorName.isEmpty()) {
                operatorName = PersistenceManager.getInstance().getUserName();
            }
            mTask.setCreateUserName(operatorName);
        } else {
            initialStatus = mTask.getTaskStatus();
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
        mTaskIdTv = view.findViewById(R.id.FTD_task_id_tv);
        mTaskIdLy = view.findViewById(R.id.FTD_task_id_ly);
        mDateTv = view.findViewById(R.id.FTD_date_tv);
        mAuthorTv = view.findViewById(R.id.FTD_open_by_tv);
        mStartDate = view.findViewById(R.id.FTD_start_date_edit_tv);
        mEndDate = view.findViewById(R.id.FTD_end_date_edit_tv);
        mStatusSpinner = view.findViewById(R.id.FTD_status_spinner);
        mSubjectSpinner = view.findViewById(R.id.FTD_subject_spinner);
        mAssignSpinner = view.findViewById(R.id.FTD_assign_spinner);
        mAssignTv = view.findViewById(R.id.FTD_assign_tv);
        mAssignRl = view.findViewById(R.id.FTD_assign_rl);
        mDescriptionEt = view.findViewById(R.id.FTD_description_et);
        TextView mDescriptionTitleTv = view.findViewById(R.id.FTD_description_tv);
        mDescriptionTitleTv.setText(Html.fromHtml(getString(R.string.description) + "<font color='red'>*</font>"), TextView.BufferType.SPANNABLE);
        mTimeHr = view.findViewById(R.id.FTD_time_hr_et);
        mTimeMin = view.findViewById(R.id.FTD_time_min_et);
        mSeverityRv = view.findViewById(R.id.FTD_severity_rv);
        mTaskStepRv = view.findViewById(R.id.FTD_sub_task_rv);
        mTaskStepAddNewEt = view.findViewById(R.id.FTD_sub_task_add_new_et);
        mNotesRv = view.findViewById(R.id.FTD_task_notes_rv);
        mNotesAddNewEt = view.findViewById(R.id.FTD_task_note_add_new_et);
        mAttachedFilesRv = view.findViewById(R.id.FTD_attached_files_rv);
        mAttachedFilesTv = view.findViewById(R.id.FTD_attached_files_tv);
        mSaveBtn = view.findViewById(R.id.FTD_add_task_btn);
    }

    private void initView(TaskProgress task, TaskObjectForCreateOrEditContent editTaskObject) {
        mAuthorTv.setText(task.getCreateUserName());
        if (task.getTaskID() == 0) {
            mTaskIdLy.setVisibility(View.GONE);
            mSaveBtn.setText(getString(R.string.add_task));
            mTitleTv.setText(getString(R.string.add_new_task));
            mDateTv.setText(TimeUtils.getDate(new Date().getTime(), ONLY_DATE_FORMAT));
            initStatusSpinner(editTaskObject.getStatus(), TODO.getValue(), getResources().getColor(R.color.grey1));
            mStatusSpinner.setEnabled(false);
            initSeverity(editTaskObject.getPriority(), TaskProgress.TaskPriority.MEDIUM.getValue(), true);
            initSubjectSpinner(editTaskObject.getSubjects(), editTaskObject.getSubjects().get(0).getID(), 0);
            initAssignSpinner("", getResources().getColor(R.color.grey1));
            mAssignSpinner.setEnabled(false);
            initStartAndEndTimeViews();
            initTotalTime(task);
            mAttachedFilesTv.setVisibility(View.GONE);
        } else {
            mSaveBtn.setText(getString(R.string.save));
            mTitleTv.setText(getString(R.string.edit_task));
            mDescriptionEt.setText(task.getText());
            mDateTv.setText(TimeUtils.getDate(TimeUtils.convertDateToMillisecond(task.getTaskCreateDate(),
                    SQL_T_FORMAT_NO_SECOND), ONLY_DATE_FORMAT));
            mTaskIdTv.setText(String.valueOf(task.getTaskID()));
            mTaskIdLy.setVisibility(View.VISIBLE);
            if (task.getTaskStartTimeTarget() != null && !task.getTaskStartTimeTarget().isEmpty()
                    && !task.getTaskStartTimeTarget().equals("0")) {
                mStartDate.setText(TimeUtils.getDate(TimeUtils.convertDateToMillisecond(task.getTaskStartTimeTarget(),
                        SQL_T_FORMAT_NO_SECOND), SQL_NO_T_FORMAT_NO_SECOND));
            }
            if (task.getTaskEndTimeTarget() != null && !task.getTaskEndTimeTarget().isEmpty()
                    && !task.getTaskEndTimeTarget().equals("0")) {
                mEndDate.setText(TimeUtils.getDate(TimeUtils.convertDateToMillisecond(task.getTaskEndTimeTarget(),
                        SQL_T_FORMAT_NO_SECOND), SQL_NO_T_FORMAT_NO_SECOND));
            }
            getTaskNotes();
            getTaskSteps();
            getTaskFiles(task.getTaskID());
            initTotalTime(task);
            initAssignSpinner(task.getAssigneeDisplayName(), getResources().getColor(R.color.grey1));
            mAssignSpinner.setEnabled(false);
            if (task.getTaskCreateUser() != operatorId) {
                disableEditText(mDescriptionEt);
                disableEditText(mTimeMin);
                disableEditText(mTimeHr);
                initSeverity(editTaskObject.getPriority(), task.getTaskPriorityID(), false);
                List<TaskInfoObject> status = editTaskObject.getStatus();
                if (task.getTaskStatus() != TaskProgress.TaskStatus.CANCELLED.getValue()) {
                    status = removeIdFromInfoObjectList(editTaskObject.getStatus(), TaskProgress.TaskStatus.CANCELLED.getValue());
                }
                initStatusSpinner(status, task.getTaskStatus(), 0);
                initSubjectSpinner(editTaskObject.getSubjects(), task.getSubjectId(), getResources().getColor(R.color.grey1));
                mSubjectSpinner.setEnabled(false);
            } else {
                initSeverity(editTaskObject.getPriority(), task.getTaskPriorityID(), true);
                initStatusSpinner(editTaskObject.getStatus(), task.getTaskStatus(), 0);
                initSubjectSpinner(editTaskObject.getSubjects(), task.getSubjectId(), 0);
                initStartAndEndTimeViews();
            }
        }
    }

    private List<TaskInfoObject> removeIdFromInfoObjectList(List<TaskInfoObject> status, int statusToRemove) {
        ArrayList<TaskInfoObject> toRemove = new ArrayList<>();
        for (TaskInfoObject taskInfoObject : status) {
            if (taskInfoObject.getID() == statusToRemove) {
                toRemove.add(taskInfoObject);
            }
        }
        status.removeAll(toRemove);
        return status;
    }

    private void disableEditText(EditText editText) {
        editText.setEnabled(false);
        editText.setFocusable(false);
    }

    private TaskInfoObject getMachineLevel(List<TaskInfoObject> level) {
        for (TaskInfoObject taskInfoObject : level) {
            if (taskInfoObject.getID() == 3) {
                return taskInfoObject;
            }
        }
        return new TaskInfoObject(3, getString(R.string.machine), getString(R.string.machine));
    }


    private void initStartAndEndTimeViews() {
        final long[] start = new long[1];
        final long[] end = new long[1];
        if (!mTask.getTaskStartTimeTarget().isEmpty()) {
            start[0] = TimeUtils.convertDateToMillisecond(mTask.getTaskStartTimeTarget(), SQL_T_FORMAT_NO_SECOND);
            mStartDate.setText(TimeUtils.getDate(start[0], SQL_NO_T_FORMAT_NO_SECOND));
        } else {
            start[0] = new Date().getTime();
        }

        if (!mTask.getTaskEndTimeTarget().isEmpty()) {
            end[0] = TimeUtils.convertDateToMillisecond(mTask.getTaskEndTimeTarget(), SQL_T_FORMAT_NO_SECOND);
            mEndDate.setText(TimeUtils.getDate(
                    end[0], SQL_NO_T_FORMAT_NO_SECOND));
        } else {
            end[0] = new Date().getTime();
        }

        mStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(start[0]));
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(), R.style.TimePickerTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calendar.set(Calendar.YEAR, i);
                        calendar.set(Calendar.MONTH, i1);
                        calendar.set(Calendar.DAY_OF_MONTH, i2);

                        TimePickerDialog.OnTimeSetListener myTimeListener = null;
                        final TimePickerDialog.OnTimeSetListener finalMyTimeListener = myTimeListener;
                        myTimeListener = new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                if (view.isShown()) {
                                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                    calendar.set(Calendar.MINUTE, minute);
                                    start[0] = calendar.getTime().getTime();
                                    mTask.setTaskStartTimeTarget(TimeUtils.getDate(start[0], SQL_T_FORMAT_NO_SECOND));
                                    mStartDate.setText(TimeUtils.getDate(TimeUtils.convertDateToMillisecond(mTask.getTaskStartTimeTarget(),
                                            SQL_T_FORMAT_NO_SECOND), SQL_NO_T_FORMAT_NO_SECOND));
                                }
                            }
                        }

                        ;
                        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), R.style.TimePickerTheme, myTimeListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                        timePickerDialog.setTitle(String.format("%s :",

                                getString(R.string.choose_hour)));
                        timePickerDialog.show();

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                if (!mTask.getTaskEndTimeTarget().

                        isEmpty()) {
                    datePickerDialog.getDatePicker().setMaxDate(end[0]);
                }
                datePickerDialog.getDatePicker().

                        setMinDate(new Date().

                                getTime());
                datePickerDialog.show();
            }
        });
        mEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(end[0]));
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(), R.style.TimePickerTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calendar.set(Calendar.YEAR, i);
                        calendar.set(Calendar.MONTH, i1);
                        calendar.set(Calendar.DAY_OF_MONTH, i2);

                        TimePickerDialog.OnTimeSetListener myTimeListener = null;
                        final TimePickerDialog.OnTimeSetListener finalMyTimeListener = myTimeListener;
                        myTimeListener = new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                if (view.isShown()) {
                                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                    calendar.set(Calendar.MINUTE, minute);
                                    end[0] = calendar.getTime().getTime();
                                    mTask.setTaskEndTimeTarget(TimeUtils.getDate(end[0], SQL_T_FORMAT_NO_SECOND));
                                    mEndDate.setText(TimeUtils.getDate(TimeUtils.convertDateToMillisecond(mTask.getTaskEndTimeTarget(),
                                            SQL_T_FORMAT_NO_SECOND), SQL_NO_T_FORMAT_NO_SECOND));

                                }
                            }
                        };
                        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), R.style.TimePickerTheme, myTimeListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                        timePickerDialog.setTitle(String.format("%s :", getString(R.string.choose_hour)));
                        timePickerDialog.show();
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(start[0]);
                datePickerDialog.show();
            }
        });
    }


    private void initAssignSpinner(final String assign, int color) {
        final List<TaskInfoObject> levels = new ArrayList<>();
        levels.add(new TaskInfoObject(assign));
        final TaskInfoObjectSpinnerAdapter dataAdapter = new TaskInfoObjectSpinnerAdapter(getActivity(), R.layout.base_spinner_item, levels, color);
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_custom);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAssignSpinner.setAdapter(dataAdapter);
        mAssignSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mTask.setTaskLevel(levels.get(adapterView.getSelectedItemPosition()).getID());
                dataAdapter.setTitle(adapterView.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initSubjectSpinner(final List<TaskInfoObject> subjects, final int subjectId, int color) {
        mTask.setSubjectId(subjectId);
        final TaskInfoObjectSpinnerAdapter dataAdapter = new TaskInfoObjectSpinnerAdapter(getActivity(), R.layout.base_spinner_item, subjects, color);
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_custom);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSubjectSpinner.setAdapter(dataAdapter);
        mSubjectSpinner.post(new Runnable() {
            @Override
            public void run() {
                mSubjectSpinner.setSelection(getInfoObjectSelectedPosition(subjects, subjectId));
                mSubjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        dataAdapter.setTitle(adapterView.getSelectedItemPosition());
                        mTask.setSubjectTrans(subjects.get(adapterView.getSelectedItemPosition()).getName());
                        mTask.setSubjectId(subjects.get(adapterView.getSelectedItemPosition()).getID());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        });

    }

    private void initStatusSpinner(List<TaskInfoObject> status, final int taskStatus, int color) {
        mTask.setTaskStatus(taskStatus);
        status = removeIdFromInfoObjectList(status, TaskProgress.TaskStatus.OPEN.getValue());
        final TaskInfoObjectSpinnerAdapter dataAdapter = new TaskInfoObjectSpinnerAdapter(getActivity(), R.layout.base_spinner_item, status, color);
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_custom);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mStatusSpinner.setAdapter(dataAdapter);
        final List<TaskInfoObject> finalStatus = status;
        mStatusSpinner.post(new Runnable() {
            @Override
            public void run() {
                mStatusSpinner.setSelection(getInfoObjectSelectedPosition(finalStatus, taskStatus));
                mStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        int selectedId = finalStatus.get(adapterView.getSelectedItemPosition()).getID();
                        if (selectedId == TaskProgress.TaskStatus.DONE.getValue()){
                            for (TaskStep step : ((TaskStepsAdapter)mTaskStepRv.getAdapter()).getTaskSteps()) {
                                if (step.IsOpen){
                                    ShowCrouton.showSimpleCrouton((TaskActivity) getActivity(), getString(R.string.step_task_not_finished), CroutonCreator.CroutonType.ALERT_DIALOG);
                                    return;
                                }
                            }
                        }
                        dataAdapter.setTitle(adapterView.getSelectedItemPosition());
                        mTask.setTaskStatus(selectedId);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        });
    }

    private int getInfoObjectSelectedPosition(List<TaskInfoObject> list, int id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getID() == id) {
                return i;
            }
        }
        return 0;
    }

    private TaskInfoObject getInfoObjectById(List<TaskInfoObject> list, int id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getID() == id) {
                return list.get(i);
            }
        }
        return new TaskInfoObject(id);
    }

    private void initTotalTime(TaskProgress task) {
        if (task.getEstimatedExecutionTime() > 0) {

            int hours = (int) (task.getEstimatedExecutionTime() / 60);
            mTimeHr.setText(String.valueOf(hours));
            mTimeMin.setText(String.valueOf(((int)task.getEstimatedExecutionTime()) -  (hours * 60)));

//            int hours = ((int) task.getEstimatedExecutionTime());
//            int min = (int) (((task.getEstimatedExecutionTime()) - ((int) task.getEstimatedExecutionTime()) + 0.001) * 100);
//            mTimeHr.setText(String.valueOf(hours));
//            mTimeMin.setText(String.valueOf(min));
        }
        mTimeMin.setFilters(new InputFilter[]{new InputFilterMinMax(0, 59)});
    }

    private void setNotesRecycler() {
        mNotesRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mNotesRv.setAdapter(new TaskNotesAdapter(mTaskNoteList != null ? mTaskNoteList : new ArrayList<TaskNote>()));
    }

    private void setTaskStepsRecycler() {
        mTaskStepRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTaskStepRv.setAdapter(new TaskStepsAdapter(mTaskStepList != null ? mTaskStepList : new ArrayList<TaskStep>()));
    }

    private void initAttachFiles(TaskFilesResponse taskFiles) {

        if (taskFiles == null || taskFiles.getResponseDictionaryDT() == null
                || taskFiles.getResponseDictionaryDT().getTaskFiles() == null || taskFiles.getResponseDictionaryDT().getTaskFiles().isEmpty()) {
            mAttachedFilesTv.setVisibility(View.GONE);
            return;
        } else {
            mAttachedFilesTv.setVisibility(View.VISIBLE);
        }
        final ArrayList<String> files = new ArrayList<>();
        final ArrayList<GalleryModel> galleryModels = new ArrayList<>();

        for (TaskFilesResponse.TaskFiles filePath : taskFiles.getResponseDictionaryDT().getTaskFiles()) {
            files.add(filePath.getFilePath());
        }

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

    private void initSeverity(List<TaskInfoObject> severities, final int selectedId, boolean editable) {
        mTask.setTaskPriorityID(selectedId);
        severities = removeIdFromInfoObjectList(severities, VERY_HIGH.getValue());
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
        view.findViewById(R.id.FTD_sub_task_add_new_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTaskStep();
            }
        });
        view.findViewById(R.id.FTD_task_note_add_new_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNoteToTask();
            }
        });
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
                Task task = buildTask(mTask);
                if (checkMandatoryFilled(task)) {
                    createTask(task);
                } else {
                    ShowCrouton.showSimpleCrouton((TaskActivity) getActivity(),
                            getString(R.string.you_need_to_complete_all_mandatory_fields),
                            CroutonCreator.CroutonType.CREDENTIALS_ERROR);
                }
            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                KeyboardUtils.closeKeyboard(getActivity());
                return true;
            }
        });
    }

    private void addTaskStep() {
        mTaskStepList = mTaskStepList == null ? new ArrayList<TaskStep>() : mTaskStepList;
        if (mTask.getTaskID() == 0) {
            mTaskStepList.add(new TaskStep(0, 0, 0, mTaskStepAddNewEt.getText().toString(), true));
        }else {
            mTaskStepList.add(new TaskStep(0, mTask.getTaskID(), mTask.getHistoryID(), mTaskStepAddNewEt.getText().toString(), true));
        }

        setTaskStepsRecycler();
        mTaskStepAddNewEt.setText("");
    }

    private void addNoteToTask() {
        int commentId = 0; //pass comment id for update comment. not in use for now.
        if (mTaskNoteList == null) mTaskNoteList = new ArrayList<>();
        TaskNote note = new TaskNote(0, mTask.getTaskID(), mTask.getHistoryID(), mNotesAddNewEt.getText().toString(),
                         TimeUtils.getDateFromFormat(new Date(), TimeUtils.SQL_T_FORMAT), PersistenceManager.getInstance().getOperatorName());
        mTaskNoteList.add(note);
        mNotesAddNewEt.setText("");
        setNotesRecycler();
    }

    private boolean checkMandatoryFilled(Task task) {
        return task.getCreateUser() != 0 && task.getHistoryUserID() != null
                && task.getSubject() != 0
                && task.getText() != null && !task.getText().isEmpty();
    }


    private Task buildTask(TaskProgress taskProgress) {
        Task task = new Task();
        task.setID(taskProgress.getTaskID());
        task.setHistoryID(taskProgress.getHistoryID());
        task.setHistoryUserID(String.valueOf(operatorId));
        task.setCreateUser(taskProgress.getTaskCreateUser());
        task.setCreateUserName(taskProgress.getCreateUserName());
        task.setSubject(taskProgress.getSubjectId());
        task.setText(mDescriptionEt.getText().toString());
        task.setTaskLevel(MACHINE_TASK_LEVEL);
        task.setTaskLevelObjectID(PersistenceManager.getInstance().getMachineId());
        task.setPriority(taskProgress.getTaskPriorityID());
        task.setAssignee(taskProgress.getAssignee());
        task.setStartTimeTarget(taskProgress.getTaskStartTimeTarget().replace("T", " "));
        task.setEndTimeTarget(taskProgress.getTaskEndTimeTarget().replace("T", " "));
        task.setEstimatedExecutionTime(getTotalTime());
        if (taskProgress.getTaskStatus() != initialStatus) {
            task.setStatus(taskProgress.getTaskStatus());
        } else {
            task.setStatus(0);
        }
        return task;
    }

    private double getTotalTime() {
        double totalTime;
        try {
            totalTime = (Integer.parseInt(mTimeHr.getText().toString()) * 60)  + (Integer.parseInt(mTimeMin.getText().toString()));
        } catch (NumberFormatException e) {
            totalTime = 0;
        }
        return totalTime;
    }

    private void createTask(Task task) {
        PersistenceManager pm = PersistenceManager.getInstance();
        ProgressDialogManager.show(getActivity());
        task.setTaskSteps(((TaskStepsAdapter)mTaskStepRv.getAdapter()).getTaskSteps());
        SimpleRequests.createOrUpdateTask(task, pm.getSiteUrl(), new CreateTaskCallback() {
            @Override
            public void onCreateTaskCallbackSuccess(StandardResponse response) {
                ProgressDialogManager.dismiss();
                postNotesForTask(response.getLeaderRecordID());
//                ShowCrouton.showSimpleCrouton((TaskActivity) getActivity(), getString(R.string.success), CroutonCreator.CroutonType.SUCCESS);
//                mListener.onUpdate();
            }

            @Override
            public void onCreateTaskCallbackFailed(StandardResponse reason) {
                ProgressDialogManager.dismiss();
                String error = reason.getError().getErrorDesc();
                ShowCrouton.showSimpleCrouton((TaskActivity) getActivity(), error, CroutonCreator.CroutonType.CREDENTIALS_ERROR);
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

    private void getTaskSteps(){
        GetTaskNoteRequest request = new GetTaskNoteRequest(PersistenceManager.getInstance().getSessionId(), mTask.getTaskID());
        NetworkManager.getInstance().getTaskSteps(request, new Callback<TaskStepResponse>() {
            @Override
            public void onResponse(Call<TaskStepResponse> call, Response<TaskStepResponse> response) {
                if (response.body() != null && response.body().isNoError()) {
                    mTaskStepList = response.body().getTaskStepList();
                    setTaskStepsRecycler();
                }
            }

            @Override
            public void onFailure(Call<TaskStepResponse> call, Throwable t) {

            }
        });

    }

    private void getTaskNotes(){
        GetTaskNoteRequest request = new GetTaskNoteRequest(PersistenceManager.getInstance().getSessionId(), mTask.getTaskID());
        NetworkManager.getInstance().getTaskNotes(request, new Callback<TaskNotesResponse>() {
            @Override
            public void onResponse(Call<TaskNotesResponse> call, Response<TaskNotesResponse> response) {
                if (response.body() != null && response.body().isNoError()){
                    mTaskNoteList = response.body().getTaskNoteList();
                    setNotesRecycler();
                }else {
                    String msg = getString(R.string.error_rest);
                    if (response.body() != null && !response.body().isNoError()){
                        msg = response.body().getError().getErrorDesc();
                    }
                    onFailure(call, new Throwable(msg));
                }
            }

            @Override
            public void onFailure(Call<TaskNotesResponse> call, Throwable t) {

            }
        });
    }

    private void postNotesForTask(int taskId) {
        for (TaskNote taskNote : mTaskNoteList) {
            if (taskNote.getmNoteId() == 0) {
                final int[] requestCounter = {0};
                CreateTaskNotesRequest request = new CreateTaskNotesRequest(PersistenceManager.getInstance().getSessionId(), taskId, taskNote.getmNoteId(), mTask.getHistoryID(), taskNote.getmNoteText());
                NetworkManager.getInstance().postNotesForNewTask(request, new Callback<StandardResponse>() {
                    @Override
                    public void onResponse(Call<StandardResponse> call, Response<StandardResponse> response) {
                        requestCounter[0]++;
                        if (requestCounter[0] == mTaskNoteList.size()){
                            ShowCrouton.showSimpleCrouton((TaskActivity) getActivity(), getString(R.string.success), CroutonCreator.CroutonType.SUCCESS);
                            mListener.onUpdate();
                        }
                    }

                    @Override
                    public void onFailure(Call<StandardResponse> call, Throwable t) {
                        requestCounter[0]++;
                        if (requestCounter[0] == mTaskNoteList.size()){
                            mListener.onUpdate();
                        }
                    }
                });
            }
        }
    }

    private void getTaskFiles(int taskID) {
        PersistenceManager pm = PersistenceManager.getInstance();
        SimpleRequests.getTaskFiles(taskID, pm.getSiteUrl(), new GetTaskFilesCallback() {
            @Override
            public void onGetTaskFilesCallbackSuccess(TaskFilesResponse response) {
                initAttachFiles(response);
            }

            @Override
            public void onGetTaskFilesCallbackFailed(StandardResponse reason) {
                mAttachedFilesTv.setVisibility(View.GONE);
            }
        }, NetworkManager.getInstance(), pm.getTotalRetries(), pm.getRequestTimeout());
    }


    public interface TaskDetailsFragmentListener {
        void onUpdate();
    }

}
