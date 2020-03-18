package com.operatorsapp.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.operators.reportrejectnetworkbridge.server.response.activateJob.Header;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.PendingJob;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.PendingJobStandardResponse;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.Property;
import com.operatorsapp.R;
import com.operatorsapp.adapters.PendingJobsAdapter;
import com.operatorsapp.adapters.PendingJobsListAdapter;
import com.operatorsapp.utils.GoogleAnalyticsHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class JobListFragment extends Fragment implements
        View.OnClickListener, PendingJobsAdapter.PendingJobsAdapterListener {

    public static final String ID = "ID";
    public static final String PRODUCT_CATALOG_ID = "ProductCatalogID";
    public static final String UNITS_TARGET = "UnitsTarget";
    public static final String UNITS_PRODUCED = "UnitsProduced";
    public static final String END_TIME = "EndTime";
    public static final String TIME_LEFT_HR_HOUR = "TimeLeftHrHour";
    private static final String TAG = JobListFragment.class.getSimpleName();
    private EditText mSearchViewEt;
    private RecyclerView mPendingJobsRv;
    private PendingJobsListAdapter mPendingJobsAdapter;
    private PendingJobStandardResponse mPendingJobsResponse;
    private ArrayList<PendingJob> mPendingJobs;
    private List<PendingJob> mPendingJobsNoHeadersFiltered = new ArrayList<>();
    private HashMap<String, Header> mHashMapHeaders;
//    private ArrayList<Header> headers;
    private JobListFragmentListener mListener;
    private TextView mTitleTv;
    private String[] orderedHederasKey = new String[7];

    public static JobListFragment newInstance(PendingJobStandardResponse mPendingJobsResponse, ArrayList<PendingJob> mPendingJobs, ArrayList<Header> headers) {

        JobListFragment jobListFragment = new JobListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PendingJobStandardResponse.TAG, mPendingJobsResponse);
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
            if (getArguments().containsKey(PendingJobStandardResponse.TAG)) {
                mPendingJobsResponse = getArguments().getParcelable(PendingJobStandardResponse.TAG);
            }
            if (getArguments().containsKey(PendingJob.TAG)) {
                mPendingJobs = getArguments().getParcelableArrayList(PendingJob.TAG);
                mPendingJobsNoHeadersFiltered.addAll(mPendingJobs);
            }
            if (getArguments().containsKey(Header.TAG)) {
                ArrayList<Header> headers = getArguments().getParcelableArrayList(Header.TAG);
                sortHeaders(headers);
                int i = 0;
                int counter = 0;// max 7
                while (counter < 7 && i < headers.size()) {
                    if (headers.get(i).isShowOnHeader()) {
                        orderedHederasKey[counter] = headers.get(i).getName();
                        counter++;
                    }
                    i++;
                }
                mHashMapHeaders = headerListToHashMap(mPendingJobsResponse.getHeaders());
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_job_list, container, false);

        return rootView;
    }//@+id/pending_jobs_tv"/>//you have a pending jobs

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
        initVarsSearch(view);
        initTitleViews(view);
        initRecyclerViews();
        initListener(view);

        if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            view.findViewById(R.id.AJA_back_btn).setRotationY(180);
        }
    }

    private void initTitleViews(View view) {
        if (mHashMapHeaders != null) {
            if (mHashMapHeaders.get(orderedHederasKey[0]) != null) {
                ((TextView) view.findViewById(R.id.FJL_index)).setText(mHashMapHeaders.get(orderedHederasKey[0]).getDisplayName());
                setHeaderSearchViewListener(((EditText) view.findViewById(R.id.FJL_index_search)), mHashMapHeaders.get(orderedHederasKey[0]));
            } else {
                ((TextView) view.findViewById(R.id.FJL_index)).setText("");
                view.findViewById(R.id.FJL_index_search).setVisibility(View.GONE);
            }
            if (mHashMapHeaders.get(orderedHederasKey[1]) != null) {
                ((TextView) view.findViewById(R.id.FJL_catalog)).setText(mHashMapHeaders.get(orderedHederasKey[1]).getDisplayName());
                setHeaderSearchViewListener(((EditText) view.findViewById(R.id.FJL_catalog_search)), mHashMapHeaders.get(orderedHederasKey[1]));
            } else {
                ((TextView) view.findViewById(R.id.FJL_catalog)).setText("");
                view.findViewById(R.id.FJL_catalog_search).setVisibility(View.GONE);
            }
            if (mHashMapHeaders.get(orderedHederasKey[2]) != null) {
                ((TextView) view.findViewById(R.id.FJL_target)).setText(mHashMapHeaders.get(orderedHederasKey[2]).getDisplayName());
                setHeaderSearchViewListener(((EditText) view.findViewById(R.id.FJL_target_search)), mHashMapHeaders.get(orderedHederasKey[2]));
            } else {
                ((TextView) view.findViewById(R.id.FJL_target)).setText("");
                view.findViewById(R.id.FJL_target_search).setVisibility(View.GONE);
            }
            if (mHashMapHeaders.get(orderedHederasKey[3]) != null) {
                ((TextView) view.findViewById(R.id.FJL_produced)).setText(mHashMapHeaders.get(orderedHederasKey[3]).getDisplayName());
                setHeaderSearchViewListener(((EditText) view.findViewById(R.id.FJL_produced_search)), mHashMapHeaders.get(orderedHederasKey[3]));
            } else {
                ((TextView) view.findViewById(R.id.FJL_produced)).setText("");
                view.findViewById(R.id.FJL_produced_search).setVisibility(View.GONE);
            }
            if (mHashMapHeaders.get(orderedHederasKey[4]) != null) {
                ((TextView) view.findViewById(R.id.FJL_end_time)).setText(mHashMapHeaders.get(orderedHederasKey[4]).getDisplayName());
                setHeaderSearchViewListener(((EditText) view.findViewById(R.id.FJL_end_search)), mHashMapHeaders.get(orderedHederasKey[04]));
            } else {
                ((TextView) view.findViewById(R.id.FJL_end_time)).setText("");
                view.findViewById(R.id.FJL_end_search).setVisibility(View.GONE);
            }
            if (mHashMapHeaders.get(orderedHederasKey[5]) != null) {
                ((TextView) view.findViewById(R.id.FJL_job_left)).setText(mHashMapHeaders.get(orderedHederasKey[5]).getDisplayName());
                setHeaderSearchViewListener(((EditText) view.findViewById(R.id.FJL_job_search)), mHashMapHeaders.get(orderedHederasKey[5]));
            } else {
                ((TextView) view.findViewById(R.id.FJL_job_left)).setText("");
                view.findViewById(R.id.FJL_job_search).setVisibility(View.GONE);
            }
            if (mHashMapHeaders.get(orderedHederasKey[6]) != null) {
                ((TextView) view.findViewById(R.id.FJL_image)).setText(mHashMapHeaders.get(orderedHederasKey[6]).getDisplayName());
                setHeaderSearchViewListener(((EditText) view.findViewById(R.id.FJL_image_search)), mHashMapHeaders.get(orderedHederasKey[6]));
            } else {
                ((TextView) view.findViewById(R.id.FJL_image)).setText("");
                view.findViewById(R.id.FJL_image_search).setVisibility(View.GONE);
            }
        }
    }

    private void setHeaderSearchViewListener(EditText searchView, final Header header) {
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                header.setSearchExpression(charSequence.toString());
                updateRvBySearchResult();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    private void initVarsSearch(View view) {
        mSearchViewEt = view.findViewById(R.id.AJA_search_et);
        mPendingJobsRv = view.findViewById(R.id.AJA_product_rv);

    }

    private void initRecyclerViews() {

        if (mPendingJobs != null && mPendingJobs.size() > 0) {
            mPendingJobs.get(0).setSelected(true);
            RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            mPendingJobsAdapter = new PendingJobsListAdapter(mPendingJobs, orderedHederasKey, this, getActivity());
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

                updateRvBySearchResult();

            }
        });
    }

    private void sortHeaders(ArrayList<Header> headers) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            headers.sort(new Comparator<Header>() {
                @Override
                public int compare(Header o1, Header o2) {
                    if (o1.getOrder().equals(o2.getOrder())) {
                        return 0;
                    } else if (o1.getOrder() < o2.getOrder()) {
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

                if (property.getKey() != null && mHashMapHeaders.get(property.getKey()) != null && mHashMapHeaders.get(property.getKey()).isSelected()
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

        if (mPendingJobsResponse != null) {
            outerLoop:
            for (PendingJob pendingJob : mPendingJobsResponse.getPendingJobs()) {

                boolean atLeastOne = false;
                for (Property property : pendingJob.getProperties()) {
                    if (mHashMapHeaders.containsKey(property.getKey()) &&
                            mHashMapHeaders.get(property.getKey()).isShowOnHeader()
                            && property.getValue() != null && property.getValue().toLowerCase().contains(mSearchViewEt.getText().toString().toLowerCase())) {
                        atLeastOne = true;
                        break;
                    }
                }
                if (atLeastOne) {
                    for (Property property : pendingJob.getProperties()) {
                        if ((mHashMapHeaders.containsKey(property.getKey()) &&
                                mHashMapHeaders.get(property.getKey()).isShowOnHeader())) {
                            if (!mHashMapHeaders.get(property.getKey()).getSearchExpression().isEmpty()
                            && (property.getValue() == null || !property.getValue().toLowerCase().contains(mHashMapHeaders.get(property.getKey()).getSearchExpression().toLowerCase()))){
                                continue outerLoop;
                            }
                        }
                    }
                } else {
                    continue outerLoop;
                }
                if (!mPendingJobs.contains(pendingJob)) {
                    mPendingJobs.add(pendingJob);
                }
            }
        }

        mPendingJobsNoHeadersFiltered.clear();
        mPendingJobsNoHeadersFiltered.addAll(mPendingJobs);
        if (mPendingJobsAdapter != null) {
            mPendingJobsAdapter.notifyDataSetChanged();
            filterPendingJobsByHeaders();
        }
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
