package com.operatorsapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.operatorinfra.Operator;
import com.example.common.StandardResponse;
import com.example.common.UpsertType;
import com.example.common.callback.ErrorObjectInterface;
import com.example.common.callback.GetShiftWorkersCallback;
import com.example.common.callback.SimpleCallback;
import com.example.common.machineData.ShiftOperatorResponse;
import com.example.common.machineData.Worker;
import com.example.common.operator.SaveShiftWorkersRequest;
import com.example.oppapplog.OppAppLogger;
import com.operators.operatorcore.OperatorCore;
import com.operators.operatorcore.interfaces.OperatorForMachineUICallbackListener;
import com.operatorsapp.R;
import com.operatorsapp.activities.DashboardActivity;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.activities.interfaces.SilentLoginCallback;
import com.operatorsapp.adapters.WorkerAdapter;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.interfaces.CroutonRootProvider;
import com.operatorsapp.interfaces.OnStartDragListener;
import com.operatorsapp.interfaces.OperatorCoreToDashboardActivityCallback;
import com.operatorsapp.managers.CroutonCreator;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.ShowCrouton;
import com.operatorsapp.utils.SimpleItemTouchHelperCallback;
import com.operatorsapp.utils.SimpleRequests;
import com.operatorsapp.utils.SoftKeyboardUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.operatorsapp.utils.SimpleRequests.getShiftWorkers;

public class SignInOperatorFragment extends Fragment implements View.OnClickListener, CroutonRootProvider {

    private static final String LOG_TAG = SignInOperatorFragment.class.getSimpleName();
    private static final String SELECTED_OPERATOR = "selected_operator";
    private static final int MAX_WORKERS_LIST_SIZE = 5;
    private EditText mOperatorIdEditText;
    private TextView mSignInButton;

    private OperatorCore mOperatorCore;
    private OperatorCoreToDashboardActivityCallback mOperatorCoreToDashboardActivityCallback;
    private GoToScreenListener mOnGoToScreenListener;
    private OnCroutonRequestListener mOnCroutonRequestListener;
    private View view;
    private ArrayList<Worker> workerItems;
    private RecyclerView mRv;
    private ProgressBar mWorkersProgressBar;
    private View mNoDataTv;
    private WorkerAdapter workersAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private View mSaveBtn;
    private ArrayList<Worker> workerItemsOriginal;
    private Worker mMainWorker;
    private SignInOperatorFragmentListener listener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOperatorCoreToDashboardActivityCallback = (OperatorCoreToDashboardActivityCallback) getActivity();
        if (mOperatorCoreToDashboardActivityCallback != null) {
            mOperatorCore = mOperatorCoreToDashboardActivityCallback.onSignInOperatorFragmentAttached();
        }
        mOnGoToScreenListener = (GoToScreenListener) getActivity();
        mOnCroutonRequestListener = (OnCroutonRequestListener) getActivity();
        mOperatorCore.registerListener(mOperatorForMachineUICallbackListener);
        listener = (SignInOperatorFragmentListener) context;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_operator_sign_in, container, false);
        view = rootView;
//        setActionBar();
        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mOperatorIdEditText = view.findViewById(R.id.operator_id_edit_text);
        mSignInButton = view.findViewById(R.id.button_operator_signIn);
        mSaveBtn = view.findViewById(R.id.FOSI_save_btn);
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveShiftWorkers();
            }
        });
        mSignInButton.setClickable(false);
        mOperatorIdEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                OppAppLogger.i(LOG_TAG, "S " + s + " , start " + start + " before, " + before + " count " + count);
                if (getActivity() != null) {

                    if (start + count > 0) {
//                        mSignInButton.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.buttons_selector));
                        mSignInButton.setClickable(true);
                    } else {
//                        mSignInButton.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_bg_disabled));
                        mSignInButton.setClickable(false);

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        initVars(view);
        getShiftWorkersData();

    }

    private void initVars(View view) {
        mWorkersProgressBar = view.findViewById(R.id.FOSI_list_progress);
        mNoDataTv = view.findViewById(R.id.FOSI_no_data_list);
        mRv = view.findViewById(R.id.FOSI_rv);
        mRv.setHasFixedSize(true);
        mRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        workerItems = new ArrayList<>();
        workerItemsOriginal = new ArrayList<>();
        workersAdapter = new WorkerAdapter(workerItems, new OnStartDragListener() {
            @Override
            public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
                mItemTouchHelper.startDrag(viewHolder);
            }
        }, new WorkerAdapter.WorkerAdapterListener() {
            @Override
            public void onRemoveWorker(Worker worker) {
                for (Worker workerOriginal : workerItemsOriginal) {
                    if (workerOriginal.equals(worker)) {
                        if (workerOriginal.getUpsertType() == UpsertType.INSERT.getValue()) {
                            workerItemsOriginal.remove(worker);
                            return;
                        }
                        workerOriginal.setUpsertType(UpsertType.DELETE.getValue());
                        if (workersAdapter.getItemCount() > 1) {
                            workerOriginal.setHeadWorker(false);
                        }
                        return;
                    }
                }
            }
        });
        mRv.setAdapter(workersAdapter);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(workersAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRv);
    }

    private void getShiftWorkersData() {
        PersistenceManager pm = PersistenceManager.getInstance();
        mWorkersProgressBar.setVisibility(View.VISIBLE);
        getShiftWorkers(pm.getSiteUrl(), new GetShiftWorkersCallback() {
            @Override
            public void onGetShiftWorkersSuccess(ShiftOperatorResponse response) {
                mWorkersProgressBar.setVisibility(View.GONE);
                workerItems.clear();
                workerItemsOriginal.clear();
                if (response.getWorkers() != null && response.getWorkers().size() > 0) {
                    response.getWorkers().get(0).setHeadWorker(true);
                }
                workerItems.addAll(response.getWorkers());
                workerItemsOriginal.addAll(response.getWorkers());
                workersAdapter.notifyDataSetChanged();
                if (response.getWorkers() != null && response.getWorkers().size() > 0) {
                    mNoDataTv.setVisibility(View.GONE);
                } else {
                    mNoDataTv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onGetShiftWorkersFailed(StandardResponse reason) {
                mWorkersProgressBar.setVisibility(View.GONE);
                mNoDataTv.setVisibility(View.VISIBLE);
            }
        }, NetworkManager.getInstance(), pm.getTotalRetries(), pm.getRequestTimeout());
    }

    private void saveShiftWorkers() {
        PersistenceManager pm = PersistenceManager.getInstance();
        mWorkersProgressBar.setVisibility(View.VISIBLE);
        SimpleRequests.saveShiftWorkers(new SaveShiftWorkersRequest(pm.getMachineId(),
                pm.getSessionId(), getSubWorkers(), getMainWorker()), pm.getSiteUrl(), new SimpleCallback() {
            @Override
            public void onRequestSuccess(StandardResponse response) {//fix 2.2
                if (isAdded() && getActivity() != null) {
                    mWorkersProgressBar.setVisibility(View.GONE);
                    ShowCrouton.showSimpleCrouton(mOnCroutonRequestListener, getString(R.string.save) + " " +
                            getString(R.string.operator) + " " + getString(R.string.success), CroutonCreator.CroutonType.SUCCESS);
                    if (getMainWorker() != null) {
                        PersistenceManager.getInstance().setOperatorId(getMainWorker().getWorkerID());
                        PersistenceManager.getInstance().setOperatorName(getMainWorker().getWorkerName());
                    }
                    listener.onSaveWorkers();
                }
            }

            @Override
            public void onRequestFailed(StandardResponse reason) {
                if (isAdded() && getActivity() != null) {
                    mWorkersProgressBar.setVisibility(View.GONE);
                    ShowCrouton.showSimpleCrouton(mOnCroutonRequestListener, getString(R.string.save_failed), CroutonCreator.CroutonType.NETWORK_ERROR);
                }
            }
        }, NetworkManager.getInstance(), pm.getTotalRetries(), pm.getRequestTimeout());
    }

    private List<Worker> getSubWorkers() {
        if (workerItems.size() == 0) {
            for (Worker workerOriginal : workerItemsOriginal) {
                if (workerOriginal.isHeadWorker()) {
                    mMainWorker = workerOriginal;
                }
            }
        }
        if (workerItems.size() > 0) {
            for (Worker workerOriginal : workerItemsOriginal) {
                for (Worker worker : workerItems) {
                    if (workerOriginal.getWorkerID().equals(worker.getWorkerID())
                            && workerOriginal.getID() == (worker.getID())) {
                        workerOriginal.setUpsertType(worker.getUpsertType());
                        workerOriginal.setHeadWorker(worker.isHeadWorker());
                        if (workerOriginal.isHeadWorker()) {
                            mMainWorker = workerOriginal;
                        }
                    }
                }
            }
        }
        if (mMainWorker != null) {
            workerItemsOriginal.remove(mMainWorker);
        }
        return workerItemsOriginal;
    }

    private Worker getMainWorker() {
        if (mMainWorker != null) {
            return mMainWorker;
        }
        for (Worker workerOriginal : workerItemsOriginal) {
            if (workerOriginal.isHeadWorker()) {
                mMainWorker = workerOriginal;
            }
        }
        return mMainWorker;
    }

    OperatorForMachineUICallbackListener mOperatorForMachineUICallbackListener = new OperatorForMachineUICallbackListener() {
        @Override
        public void onOperatorDataReceived(Operator operator) {
            removePhoneKeypad();
            mSignInButton.setOnClickListener(SignInOperatorFragment.this);
            if (operator != null) {
                if (operator.getOperatorName().equals("")) {
                    OppAppLogger.d(LOG_TAG, "Operator data receive failed. Reason : Empty operator name ");
                    removePhoneKeypad();
                    ShowCrouton.operatorLoadingErrorCrouton(mOnCroutonRequestListener, getString(R.string.no_worker_found));
                } else {
                    OppAppLogger.d(LOG_TAG, "Operator data received: Operator Id is:" + operator.getOperatorId() + " Operator Name Is: " + operator.getOperatorName());
                    mNoDataTv.setVisibility(View.GONE);
                    Worker worker = new Worker(operator.getOperatorId(), operator.getOperatorName(), UpsertType.INSERT.getValue());
                    workerItems.add(worker);
                    workerItemsOriginal.add(worker);
                    workersAdapter.notifyItemInserted(workerItems.size() - 1);
                }
            } else {
                OppAppLogger.d(LOG_TAG, "Operator data receive failed. Reason : ");
                removePhoneKeypad();
                ShowCrouton.operatorLoadingErrorCrouton(mOnCroutonRequestListener, getString(R.string.no_worker_found));
            }
            dismissProgressDialog();
        }

        @Override
        public void onOperatorDataReceiveFailure(StandardResponse reason) {
            mSignInButton.setOnClickListener(SignInOperatorFragment.this);
            OppAppLogger.d(LOG_TAG, "Operator data receive failed. Reason : " + reason.getError().toString());
            if (reason.getError().getErrorCodeConstant() == ErrorObjectInterface.ErrorCode.Credentials_mismatch && getActivity() != null) {
                ((DashboardActivity) getActivity()).silentLoginFromDashBoard(mOnCroutonRequestListener, new SilentLoginCallback() {
                    @Override
                    public void onSilentLoginSucceeded() {
                        String id = mOperatorIdEditText.getText().toString();
                        OppAppLogger.i(LOG_TAG, "Operator id: " + id);
                        mOperatorCore.getOperatorById(id);
                    }

                    @Override
                    public void onSilentLoginFailed(StandardResponse reason) {
                        ShowCrouton.operatorLoadingErrorCrouton(mOnCroutonRequestListener, reason.getError().toString());
                    }
                });
            } else {
                removePhoneKeypad();
                ShowCrouton.operatorLoadingErrorCrouton(mOnCroutonRequestListener, reason.getError().toString());
            }
            dismissProgressDialog();
        }

        @Override
        public void onSetOperatorSuccess(String operatorId) {
            dismissProgressDialog();
        }

        @Override
        public void onSetOperatorFailed(StandardResponse reason) {
            dismissProgressDialog();
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        mSignInButton.setOnClickListener(null);
        SoftKeyboardUtil.hideKeyboard(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
//            setActionBar();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mSignInButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_operator_signIn: {
                String id = mOperatorIdEditText.getText().toString();
                if (workerItems != null && workerItems.size() == MAX_WORKERS_LIST_SIZE) {
                    ShowCrouton.showSimpleCrouton(mOnCroutonRequestListener, String.format(Locale.getDefault(), "%s %d",
                            getString(R.string.you_cant_add_more_workers_than), MAX_WORKERS_LIST_SIZE), CroutonCreator.CroutonType.NETWORK_ERROR);
                    return;
                }
                if (isNotInList(id)) {
                    ProgressDialogManager.show(getActivity());
                    mOperatorIdEditText.setText(null);
                    OppAppLogger.i(LOG_TAG, "Operator id: " + id);
                    mSignInButton.setOnClickListener(null);
                    mOperatorCore.getOperatorById(id);
                } else {
                    ShowCrouton.showSimpleCrouton(mOnCroutonRequestListener, getString(R.string.already_in_the_perators_list), CroutonCreator.CroutonType.NETWORK_ERROR);
                }
                break;
            }
        }
    }

    private boolean isNotInList(String id) {
        for (Worker worker : workerItems) {
            if (worker.getWorkerID().equals(id)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mOperatorCoreToDashboardActivityCallback != null) {
            mOperatorCoreToDashboardActivityCallback = null;
        }
        mOperatorCore.unregisterListener();
    }

    public void removePhoneKeypad() {
        InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        IBinder binder = view.getWindowToken();
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(binder,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public int getCroutonRoot() {
        return R.id.parent_layouts;
    }

    private void dismissProgressDialog() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (getActivity() != null && !getActivity().isDestroyed()) {
                        ProgressDialogManager.dismiss();
                    }
                }
            });
        }
    }

    public interface SignInOperatorFragmentListener {
        void onSaveWorkers();
    }
}
