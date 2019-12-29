package com.operatorsapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.operatorinfra.Operator;
import com.example.common.StandardResponse;
import com.example.common.callback.ErrorObjectInterface;
import com.example.common.callback.GetShiftWorkersCallback;
import com.example.common.machineData.ShiftOperatorResponse;
import com.example.common.machineData.Worker;
import com.example.oppapplog.OppAppLogger;
import com.google.gson.Gson;
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
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.ShowCrouton;
import com.operatorsapp.utils.SimpleItemTouchHelperCallback;
import com.operatorsapp.utils.SoftKeyboardUtil;

import java.util.ArrayList;

import static com.operatorsapp.utils.SimpleRequests.getShiftWorkers;

public class SignInOperatorFragment extends Fragment implements View.OnClickListener, CroutonRootProvider {

    private static final String LOG_TAG = SignInOperatorFragment.class.getSimpleName();
    private static final String SELECTED_OPERATOR = "selected_operator";
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
                //todo save request and (close or next fragment?)
            }
        });
        mOperatorIdEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                OppAppLogger.getInstance().i(LOG_TAG, "S " + s + " , start " + start + " before, " + before + " count " + count);
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
        workersAdapter = new WorkerAdapter(workerItems, new OnStartDragListener() {
            @Override
            public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
                mItemTouchHelper.startDrag(viewHolder);
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
                workerItems.addAll(response.getWorkers());
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

    OperatorForMachineUICallbackListener mOperatorForMachineUICallbackListener = new OperatorForMachineUICallbackListener() {
        @Override
        public void onOperatorDataReceived(Operator operator) {
            removePhoneKeypad();
            if (operator != null) {
                if (operator.getOperatorName().equals("")) {
                    OppAppLogger.getInstance().d(LOG_TAG, "Operator data receive failed. Reason : Empty operator name ");
                    removePhoneKeypad();
                    ShowCrouton.operatorLoadingErrorCrouton(mOnCroutonRequestListener, "No operator found");
                } else {
                    OppAppLogger.getInstance().d(LOG_TAG, "Operator data received: Operator Id is:" + operator.getOperatorId() + " Operator Name Is: " + operator.getOperatorName());

                    SelectedOperatorFragment selectedOperatorFragment = new SelectedOperatorFragment();
                    Bundle bundle = new Bundle();
                    Gson gson = new Gson();
                    String jobString = gson.toJson(operator, Operator.class);
                    bundle.putString(SELECTED_OPERATOR, jobString);

                    selectedOperatorFragment.setArguments(bundle);
                    mOnGoToScreenListener.goToFragment(selectedOperatorFragment, true, true);
                }
            } else {
                OppAppLogger.getInstance().d(LOG_TAG, "Operator data receive failed. Reason : ");
                removePhoneKeypad();
                ShowCrouton.operatorLoadingErrorCrouton(mOnCroutonRequestListener, "No operator found");
            }
            dismissProgressDialog();
        }

        @Override
        public void onOperatorDataReceiveFailure(StandardResponse reason) {
            OppAppLogger.getInstance().d(LOG_TAG, "Operator data receive failed. Reason : " + reason.getError().toString());
            if (reason.getError().getErrorCodeConstant() == ErrorObjectInterface.ErrorCode.Credentials_mismatch && getActivity() != null) {
                ((DashboardActivity) getActivity()).silentLoginFromDashBoard(mOnCroutonRequestListener, new SilentLoginCallback() {
                    @Override
                    public void onSilentLoginSucceeded() {
                        String id = mOperatorIdEditText.getText().toString();
                        OppAppLogger.getInstance().i(LOG_TAG, "Operator id: " + id);
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
                ProgressDialogManager.show(getActivity());
                String id = mOperatorIdEditText.getText().toString();
                mOperatorIdEditText.setText(null);
                OppAppLogger.getInstance().i(LOG_TAG, "Operator id: " + id);
                mOperatorCore.getOperatorById(id);
                //todo check with request befor add to list
                workerItems.add(new Worker(id));
                workersAdapter.notifyItemInserted(workerItems.size() - 1);
                break;
            }
        }
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
                    ProgressDialogManager.dismiss();
                }
            });
        }
    }
}
