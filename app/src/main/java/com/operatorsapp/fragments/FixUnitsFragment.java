package com.operatorsapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.operators.activejobslistformachineinfra.ActiveJobsListForMachine;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.ShowDashboardCroutonListener;
import com.operatorsapp.adapters.ActiveJobsSpinnerAdapter;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.interfaces.CroutonRootProvider;
import com.operatorsapp.managers.PersistenceManager;

import static com.operatorsapp.fragments.ReportProductionFragment.CURRENT_JOB_LIST_FOR_MACHINE;
import static com.operatorsapp.fragments.ReportProductionFragment.CURRENT_PRODUCT_ID;
import static com.operatorsapp.fragments.ReportProductionFragment.CURRENT_SELECTED_POSITION;

public class FixUnitsFragment extends BackStackAwareFragment implements View.OnClickListener, CroutonRootProvider {

    private ShowDashboardCroutonListener mDashboardCroutonListener;
    private OnCroutonRequestListener mOnCroutonRequestListener;
    private View mActiveJobsProgressBar;
    private int mCurrentProductId;
    private ActiveJobsListForMachine mActiveJobsListForMachine;
    private int mSelectedPosition;
    private Integer mJobId;
    private Spinner mJobsSpinner;

    public static FixUnitsFragment newInstance(int currentProductId, ActiveJobsListForMachine activeJobsListForMachine, int selectedPosition) {
        FixUnitsFragment fragment = new FixUnitsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(CURRENT_PRODUCT_ID, currentProductId);
        bundle.putParcelable(CURRENT_JOB_LIST_FOR_MACHINE, activeJobsListForMachine);
        bundle.putInt(CURRENT_SELECTED_POSITION, selectedPosition);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ShowDashboardCroutonListener) {
            mDashboardCroutonListener = (ShowDashboardCroutonListener) getActivity();
        }

        if (context instanceof OnCroutonRequestListener) {
            mOnCroutonRequestListener = (OnCroutonRequestListener) getActivity();
        }

    }

    @Override
    public void onDetach() {
        mOnCroutonRequestListener = null;
        mDashboardCroutonListener = null;
        super.onDetach();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        setActionBar();

        return inflater.inflate(R.layout.fragment_fix_cycle_unit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //        final InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        //        inputMethodManager.showSoftInput(mUnitsCounterTextView, InputMethodManager.SHOW_IMPLICIT);

        if (getActivity() != null) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }

        mActiveJobsProgressBar = view.findViewById(R.id.active_jobs_progressBar);
//        getActiveJobs();

        if (getArguments() != null) {
            mCurrentProductId = getArguments().getInt(CURRENT_PRODUCT_ID);
            mActiveJobsListForMachine = getArguments().getParcelable(CURRENT_JOB_LIST_FOR_MACHINE);
            mSelectedPosition = getArguments().getInt(CURRENT_SELECTED_POSITION);

            if (mActiveJobsListForMachine != null && mActiveJobsListForMachine.getActiveJobs() != null
                    && mActiveJobsListForMachine.getActiveJobs().get(mSelectedPosition) != null) {
                mJobId = mActiveJobsListForMachine.getActiveJobs().get(mSelectedPosition).getJobID();
            }

        }


        TextView mProductIdTextView = view.findViewById(R.id.report_cycle_id_text_view);

        mProductIdTextView.setText(String.valueOf(mCurrentProductId));

        ((TextView)view.findViewById(R.id.FRCU_units_tv)).setText(PersistenceManager.getInstance().getTranslationForKPIS().getKPIByName("Units"));
        String txt = getResources().getString(R.string.report_cycle_nunits);
        txt = txt.replace(getResources().getString(R.string.placeholder1), PersistenceManager.getInstance().getTranslationForKPIS().getKPIByName("Units"));
        ((TextView)view.findViewById(R.id.FRCU_units_subtitle_tv)).setText(txt);

        mJobsSpinner = view.findViewById(R.id.report_job_spinner);

        initJobsSpinner();
        disableSpinnerProgressBar();
    }

    private void disableSpinnerProgressBar() {
        mActiveJobsProgressBar.setVisibility(View.GONE);
    }

    private void initJobsSpinner() {
        if (getActivity() != null) {
            if (mActiveJobsListForMachine != null && mActiveJobsListForMachine.getActiveJobs() != null) {
                mJobsSpinner.setVisibility(View.VISIBLE);
                final ActiveJobsSpinnerAdapter activeJobsSpinnerAdapter = new ActiveJobsSpinnerAdapter(getActivity(), R.layout.active_jobs_spinner_item, mActiveJobsListForMachine.getActiveJobs());
                activeJobsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mJobsSpinner.setAdapter(activeJobsSpinnerAdapter);
                mJobsSpinner.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.T12_color), PorterDuff.Mode.SRC_ATOP);
                mJobsSpinner.setSelection(mSelectedPosition);
                mJobsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        activeJobsSpinnerAdapter.setTitle(position);
                        mJobId = mActiveJobsListForMachine.getActiveJobs().get(position).getJobID();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    protected void setActionBar() {
        if (getActivity() != null) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setHomeButtonEnabled(false);
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setDisplayShowTitleEnabled(false);
                actionBar.setDisplayShowCustomEnabled(true);
                actionBar.setDisplayUseLogoEnabled(true);
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                // rootView null
                @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.report_cycle_unit_action_bar, null);
                String txt = getResources().getString(R.string.report_cycle_units);
                txt = txt.replace(getResources().getString(R.string.placeholder1), PersistenceManager.getInstance().getTranslationForKPIS().getKPIByName("GoodUnits"));
                ((TextView)view.findViewById(R.id.report_cycle_unit_actionbar_units_tv)).setText(txt);
                LinearLayout buttonClose = view.findViewById(R.id.close_image);
                buttonClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                    FragmentManager fragmentManager = getFragmentManager();
//                    if(fragmentManager != null)
//                    {
//                        fragmentManager.popBackStack();
//                    }
                        getActivity().onBackPressed();
                    }
                });
                actionBar.setCustomView(view);
            }
        }
    }

    @Override
    public int getCroutonRoot() {
        return R.id.top_layout;
    }
}
