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
import android.widget.EditText;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.Header;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.PendingJob;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.PendingJobResponse;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.Property;
import com.operatorsapp.R;
import com.operatorsapp.adapters.JobHeadersAdapter;
import com.operatorsapp.adapters.PendingJobsAdapter;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.managers.PersistenceManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobListFragment extends Fragment implements
        JobHeadersAdapter.JobHeadersAdaperListener,
        View.OnClickListener, PendingJobsAdapter.PendingJobsAdapterListener {


    private static final String TAG = JobListFragment.class.getSimpleName();
    private EditText mSearchViewEt;
    private RecyclerView mHeadersRv;
    private RecyclerView mPendingJobsRv;
    private JobHeadersAdapter mHeadersAdapter;
    private PendingJobsAdapter mPendingJobsAdapter;
    private PendingJobResponse mPendingJobsResponse;
    private ArrayList<PendingJob> mPendingJobs;
    private List<PendingJob> mPendingJobsNoHeadersFiltered = new ArrayList<>();
    private HashMap<String, Header> mHashMapHeaders;
    private ArrayList<Header> mHeaders;
    private JobListFragmentListener mListener;

    public static JobListFragment newInstance(PendingJobResponse mPendingJobsResponse, ArrayList<PendingJob> mPendingJobs, ArrayList<Header> headers) {

        JobListFragment jobListFragment = new JobListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PendingJobResponse.TAG, mPendingJobsResponse);
        bundle.putParcelableArrayList(PendingJob.TAG, mPendingJobs);
        bundle.putParcelableArrayList(Header.TAG, headers);
        return jobListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Analytics
        OperatorApplication application = (OperatorApplication) getActivity().getApplication();
        Tracker mTracker = application.getDefaultTracker();
        PersistenceManager pm = PersistenceManager.getInstance();
        mTracker.setClientId("machine id: " + pm.getMachineId());
        mTracker.setAppVersion(pm.getVersion() + "");
        mTracker.setHostname(pm.getSiteName());
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

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
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVars(view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof JobListFragmentListener){
            mListener = (JobListFragmentListener) context;
        }
    }


    private void initVars(View view) {


        initVarsSearch(view);
        initRecyclerViews(view);
        initListener(view);

        if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            view.findViewById(R.id.AJA_back_btn).setRotationY(180);
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
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            mHeadersAdapter = new JobHeadersAdapter(mHeaders, mHashMapHeaders, this, getActivity());
            mHeadersRv.setLayoutManager(layoutManager);
            mHeadersRv.setAdapter(mHeadersAdapter);

            mPendingJobs.get(0).setSelected(true);
            RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            mPendingJobsAdapter = new PendingJobsAdapter(mPendingJobs, mHashMapHeaders, this, getActivity());
            mPendingJobsRv.setLayoutManager(layoutManager2);
            mPendingJobsRv.setAdapter(mPendingJobsAdapter);
        }
    }

    private void initListener(View view) {

        view.findViewById(R.id.AJA_search_btn).setOnClickListener(this);

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

                        if (!mHeaders.contains(mHashMapHeaders.get(property.getKey())))
                            mHeaders.add(mHashMapHeaders.get(property.getKey()));
                    }
                }
            }
        }

        mPendingJobsNoHeadersFiltered.clear();
        mPendingJobsNoHeadersFiltered.addAll(mPendingJobs);
        sortHeaders();
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

            case R.id.AJA_search_btn:

                break;

        }
    }

    @Override
    public void onPendingJobSelected(PendingJob pendingJob) {
        mListener.onPendingJobSelected(pendingJob);
    }

    public interface JobListFragmentListener{

        void onPendingJobSelected(PendingJob pendingJob);
    }
}
