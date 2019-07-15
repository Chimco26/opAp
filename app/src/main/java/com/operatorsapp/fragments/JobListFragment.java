package com.operatorsapp.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.Header;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.PendingJob;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.PendingJobResponse;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.Property;
import com.operatorsapp.R;
import com.operatorsapp.adapters.JobHeadersAdapter;
import com.operatorsapp.adapters.JobHeadersSpinnerAdapter;
import com.operatorsapp.adapters.PendingJobsAdapter;
import com.operatorsapp.adapters.PendingJobsAdapterNew;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.utils.GoogleAnalyticsHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class JobListFragment extends Fragment implements
        JobHeadersAdapter.JobHeadersAdaperListener,
        View.OnClickListener, PendingJobsAdapter.PendingJobsAdapterListener {

    private static final String TAG = JobListFragment.class.getSimpleName();
    public static final String ID = "ID";
    public static final String PRODUCT_CATALOG_ID = "ProductCatalogID";
    public static final String UNITS_TARGET = "UnitsTarget";
    public static final String UNITS_PRODUCED = "UnitsProduced";
    public static final String END_TIME = "EndTime";
    public static final String TIME_LEFT_HR_HOUR = "TimeLeftHrHour";
    private EditText mSearchViewEt;
    private Spinner mHeadersRv;
    private RecyclerView mPendingJobsRv;
    private JobHeadersSpinnerAdapter mHeadersAdapter;
    private PendingJobsAdapterNew mPendingJobsAdapter;
    private PendingJobResponse mPendingJobsResponse;
    private ArrayList<PendingJob> mPendingJobs;
    private List<PendingJob> mPendingJobsNoHeadersFiltered = new ArrayList<>();
    private HashMap<String, Header> mHashMapHeaders;
    private ArrayList<Header> mHeaders;
    private JobListFragmentListener mListener;
    private Header mSelectedHeader;
    private TextView mTitleTv;

    public static JobListFragment newInstance(PendingJobResponse mPendingJobsResponse, ArrayList<PendingJob> mPendingJobs, ArrayList<Header> headers) {

        JobListFragment jobListFragment = new JobListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PendingJobResponse.TAG, mPendingJobsResponse);
        bundle.putParcelableArrayList(PendingJob.TAG, mPendingJobs);
        bundle.putParcelableArrayList(Header.TAG, headers);
        jobListFragment.setArguments(bundle);
        return jobListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Analytics
        new GoogleAnalyticsHelper().trackScreen(getActivity(), "Pending job list");

        if (getArguments() != null) {
            if (getArguments().containsKey(PendingJobResponse.TAG)) {
                mPendingJobsResponse = getArguments().getParcelable(PendingJobResponse.TAG);
            }
            if (getArguments().containsKey(PendingJob.TAG)) {
                mPendingJobs = getArguments().getParcelableArrayList(PendingJob.TAG);
                mPendingJobsNoHeadersFiltered.addAll(mPendingJobs);
            }
            if (getArguments().containsKey(Header.TAG)) {
                mHeaders = getArguments().getParcelableArrayList(Header.TAG);
                sortHeaders();
                mHashMapHeaders = headerListToHashMap(mPendingJobsResponse.getHeaders());
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_job_list, container, false);

        return rootView;
    }//@+id/pending_jobs_tv"/>//you have x pending jobs

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVars(view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof JobListFragmentListener) {
            mListener = (JobListFragmentListener) context;
        }
    }


    private void initVars(View view) {

        mTitleTv = view.findViewById(R.id.pending_jobs_tv);
        if (mPendingJobsResponse != null && mPendingJobsResponse.getPendingJobs() != null) {
            mTitleTv.setText(String.format(Locale.getDefault(), "%s %d %s",
                    getString(R.string.you_have), mPendingJobsResponse.getPendingJobs().size(), getString(R.string.pending_jobs)));
        }
        initTitleViews(view);
        initVarsSearch(view);
        initRecyclerViews(view);
        initListener(view);

        if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            view.findViewById(R.id.AJA_back_btn).setRotationY(180);
        }
    }

    private void initTitleViews(View view) {
        if (mHashMapHeaders != null) {
            if (mHashMapHeaders.get(ID) != null)
                ((TextView) view.findViewById(R.id.FJL_index)).setText(mHashMapHeaders.get(ID).getDisplayName());
            if (mHashMapHeaders.get(PRODUCT_CATALOG_ID) != null)
                ((TextView) view.findViewById(R.id.FJL_catalog)).setText(mHashMapHeaders.get(PRODUCT_CATALOG_ID).getDisplayName());
            if (mHashMapHeaders.get(UNITS_TARGET) != null)
                ((TextView) view.findViewById(R.id.FJL_target)).setText(mHashMapHeaders.get(UNITS_TARGET).getDisplayName());
            if (mHashMapHeaders.get(UNITS_PRODUCED) != null)
                ((TextView) view.findViewById(R.id.FJL_produced)).setText(mHashMapHeaders.get(UNITS_PRODUCED).getDisplayName());
            if (mHashMapHeaders.get(END_TIME) != null)
                ((TextView) view.findViewById(R.id.FJL_end_time)).setText(mHashMapHeaders.get(END_TIME).getDisplayName());
            if (mHashMapHeaders.get(TIME_LEFT_HR_HOUR) != null)
                ((TextView) view.findViewById(R.id.FJL_job_left)).setText(mHashMapHeaders.get(TIME_LEFT_HR_HOUR).getDisplayName());
        }
    }


    private void initVarsSearch(View view) {
        mSearchViewEt = view.findViewById(R.id.AJA_search_et);
        mHeadersRv = view.findViewById(R.id.AJA_search_rv);
        mPendingJobsRv = view.findViewById(R.id.AJA_product_rv);

    }

    private void initRecyclerViews(View view) {

        if (mPendingJobs != null && mPendingJobs.size() > 0) {
            sortHeaders();
            mHeaders.add(0, new Header(getString(R.string.general), 0));
            mHeadersAdapter = new JobHeadersSpinnerAdapter(getActivity(), R.layout.spinner_language_item, mHeaders);
            mHeadersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mHeadersRv.setAdapter(mHeadersAdapter);
            mHeadersRv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                    for (Map.Entry<String, Header> headerEntry : mHashMapHeaders.entrySet()) {
                        mHashMapHeaders.get(headerEntry.getValue().getName()).setSelected(false);
                    }
                    mSelectedHeader = mHeaders.get(position);
                    mHeadersAdapter.setTitle(mHeaders.get(position).getDisplayName());
                    if (!mHeaders.get(position).getDisplayName().equals(getString(R.string.general))) {
                        mHashMapHeaders.get(mHeaders.get(position).getName()).setSelected(true);
                    }
                    filterPendingJobsByHeaders();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }

            });

            mPendingJobs.get(0).setSelected(true);
            RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            mPendingJobsAdapter = new PendingJobsAdapterNew(mPendingJobs, mHashMapHeaders, this, getActivity());
            mPendingJobsRv.setLayoutManager(layoutManager2);
            mPendingJobsRv.setAdapter(mPendingJobsAdapter);
        }
    }

    private void initListener(View view) {

        view.findViewById(R.id.AJA_search_btn).setOnClickListener(this);
        view.findViewById(R.id.AJA_back_btn).setOnClickListener(this);

        mSearchViewEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() < 1 && mHashMapHeaders != null) {
                    for (Map.Entry<String, Header> headerEntry : mHashMapHeaders.entrySet()) {
                        mHashMapHeaders.get(headerEntry.getValue().getName()).setSelected(false);
                    }
                }

                updateRvBySearchResult();

            }
        });
    }

    private void sortHeaders() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mHeaders.sort(new Comparator<Header>() {
                @Override
                public int compare(Header o1, Header o2) {
                    if (o1.getOrder().equals(o2.getOrder())) {
                        return 0;
                    } else if (o1.getOrder() <
                            o2.getOrder()) {
                        return -1;
                    }
                    return 1;
                }
            });
        }
    }

    private HashMap<String, Header> headerListToHashMap(List<Header> headers) {

        HashMap<String, Header> hashMapHeaders = new HashMap<>();

        for (Header header : headers) {

            hashMapHeaders.put(header.getName(), header);
        }

        return hashMapHeaders;
    }

    public void filterPendingJobsByHeaders() {

        boolean isSelectedByUser = false;

        for (Map.Entry<String, Header> headerEntry : mHashMapHeaders.entrySet()) {

            if (headerEntry.getValue().isSelected()) {

                isSelectedByUser = true;
            }
        }

        if (!isSelectedByUser) {

            mPendingJobs.clear();
            mPendingJobs.addAll(mPendingJobsNoHeadersFiltered);
            mPendingJobsAdapter.notifyDataSetChanged();

            return;
        }

        mPendingJobs.clear();

        for (PendingJob pendingJob : mPendingJobsNoHeadersFiltered) {

            for (Property property : pendingJob.getProperties()) {

                if (property.getKey() != null && mHashMapHeaders.get(property.getKey()).isSelected()
                        && property.getValue() != null && mSearchViewEt.getText() != null &&
                        property.getValue().toLowerCase().contains(mSearchViewEt.getText().toString().toLowerCase())) {

                    if (!mPendingJobs.contains(pendingJob)) {
                        mPendingJobs.add(pendingJob);
                    }
                }
            }
        }

        mPendingJobsAdapter.notifyDataSetChanged();
    }


    public void updateRvBySearchResult() {

        mPendingJobs.clear();

        mHeaders.clear();

        if (mPendingJobsResponse != null) {
            for (PendingJob pendingJob : mPendingJobsResponse.getPendingJobs()) {

                for (Property property : pendingJob.getProperties()) {

                    if (property.getValue() != null && property.getValue().toLowerCase().contains(mSearchViewEt.getText().toString().toLowerCase())) {

                        if (!mPendingJobs.contains(pendingJob)) {
                            mPendingJobs.add(pendingJob);
                        }

                        if (!mHeaders.contains(mHashMapHeaders.get(property.getKey())) && mHashMapHeaders.get(property.getKey()) != null) {
                            mHeaders.add(mHashMapHeaders.get(property.getKey()));
                        }
                    }
                }
            }
        }

        mPendingJobsNoHeadersFiltered.clear();
        mPendingJobsNoHeadersFiltered.addAll(mPendingJobs);
        sortHeaders();
        mHeaders.add(0, new Header(getString(R.string.general), 0));
        if (mHeaders.contains(mSelectedHeader)) {
            mHeadersAdapter.setTitle(mSelectedHeader.getDisplayName());
        } else {
            mHeadersAdapter.setTitle(mHeaders.get(0).getDisplayName());
        }
        if (mHeadersAdapter != null) {
            mHeadersAdapter.notifyDataSetChanged();
        }
        if (mPendingJobsAdapter != null) {
            mPendingJobsAdapter.notifyDataSetChanged();
            filterPendingJobsByHeaders();
        }
    }

    @Override
    public void onHeaderSelected(HashMap<String, Header> hashMapHeader) {

        mHashMapHeaders = hashMapHeader;

        filterPendingJobsByHeaders();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.AJA_back_btn:
            case R.id.AJA_title:

                getActivity().onBackPressed();

                break;

        }
    }

    @Override
    public void onPendingJobSelected(PendingJob pendingJob) {
        mListener.onPendingJobSelected(pendingJob);
    }

    public interface JobListFragmentListener {

        void onPendingJobSelected(PendingJob pendingJob);
    }
}
