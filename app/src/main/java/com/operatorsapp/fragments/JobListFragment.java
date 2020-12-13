package com.operatorsapp.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.server.responses.JobForTest;
import com.operatorsapp.server.responses.MaterialForTest;
import com.operatorsapp.utils.GoogleAnalyticsHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class JobListFragment extends Fragment implements
        View.OnClickListener, PendingJobsAdapter.PendingJobsAdapterListener {

    private static final String TAG = JobListFragment.class.getSimpleName();
    private static final String GLOBAL_SEARCH_FIELD = "GLOBAL_SEARCH_FIELD";
    private static final String IS_FOR_QC_NO_MACHINE = "IS_FOR_QC_NO_MACHINE";
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
    private String[] orderedHeadersKey = new String[7];
    private HashMap<String, String> mSavedFilters;
    private TextView mClearFiltersTv;
    private ArrayList<PendingJob> mJobForTestList;
    private boolean isQcFromJob = false;
    private boolean isQcFromMaterial = false;
    private ArrayList<JobForTest> mJobForTestListOrigin;
    private ArrayList<MaterialForTest> mMaterialsForTestListOrigin;

    public static JobListFragment newInstance(PendingJobStandardResponse mPendingJobsResponse, ArrayList<PendingJob> mPendingJobs, ArrayList<Header> headers) {

        JobListFragment jobListFragment = new JobListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PendingJobStandardResponse.TAG, mPendingJobsResponse);
        bundle.putParcelableArrayList(PendingJob.TAG, mPendingJobs);
        bundle.putParcelableArrayList(Header.TAG, headers);
        jobListFragment.setArguments(bundle);
        return jobListFragment;
    }

    public static JobListFragment newInstance(ArrayList<JobForTest> jobForTestList) {
        JobListFragment jobListFragment = new JobListFragment();
        jobListFragment.setDataForQcTest(jobForTestList);
        return jobListFragment;
    }

    public static JobListFragment newInstance(ArrayList<MaterialForTest> materialForTestList, int junkParam) {
        JobListFragment jobListFragment = new JobListFragment();
        jobListFragment.setMaterialsForQcTest(materialForTestList);
        return jobListFragment;
    }

    private void setMaterialsForQcTest(ArrayList<MaterialForTest> materialForTestList) {
        mMaterialsForTestListOrigin = new ArrayList<>();
        mMaterialsForTestListOrigin.addAll(materialForTestList);
        isQcFromMaterial = true;
    }

    private void setDataForQcTest(ArrayList<JobForTest> jobForTestList) {
        mJobForTestListOrigin = new ArrayList<>();
        // TODO: 30/11/2020 DEV is A Mess, to many results (20,000)
        if (PersistenceManager.getInstance().getSiteName().equals("DEV")) {
            for (int i = 0; i < 100; i++) {

//                if (jobForTestList.get(i).getId() == 20157 || mJobForTestListOrigin.size() < 50) {
//                }
                mJobForTestListOrigin.add(jobForTestList.get(i));
            }
        }else {
            mJobForTestListOrigin.addAll(jobForTestList);
        }
        isQcFromJob = true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Analytics
        new GoogleAnalyticsHelper().trackScreen(getActivity(), "Pending job list");

        mSavedFilters = PersistenceManager.getInstance().getSavedPendingJobFilters();

        if (isQcFromJob || isQcFromMaterial){
            setHeadersForQc();
        }else if (getArguments() != null) {
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
                        orderedHeadersKey[counter] = headers.get(i).getName();
                        counter++;
                    }
                    i++;
                }
                mHashMapHeaders = headerListToHashMap(mPendingJobsResponse.getHeaders());
            }
        }
    }

    private void setHeadersForQc() {
        ArrayList<Header> headers = new ArrayList<>();
        if (isQcFromJob) {
            orderedHeadersKey = getResources().getStringArray(R.array.pending_job_headers_for_qc_array);
        }else{
            orderedHeadersKey = getResources().getStringArray(R.array.material_headers_for_qc_array);
        }
        for (int i = 0; i< orderedHeadersKey.length; i++) {
            Header header = new Header(orderedHeadersKey[i], i);
            header.setName(orderedHeadersKey[i]);
            header.setShowOnHeader(true);
            headers.add(header);
        }

        mPendingJobs = new ArrayList<>();
        mJobForTestList = new ArrayList<>();
        if (isQcFromJob) {
            for (JobForTest job : mJobForTestListOrigin) {
                PendingJob pj = new PendingJob();
                pj.setID(job.getId());
                pj.setProperties(job.getProperties(getActivity()));
                mPendingJobs.add(pj);
            }
        }else {
            for (MaterialForTest test : mMaterialsForTestListOrigin) {
                PendingJob pj = new PendingJob();
                pj.setID(test.getId());
                pj.setProperties(test.getProperties(getActivity()));
                mPendingJobs.add(pj);
            }
        }
        mJobForTestList.addAll(mPendingJobs);
        mHashMapHeaders = headerListToHashMap(headers);
        mPendingJobsNoHeadersFiltered.addAll(mPendingJobs);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_job_list, container, false);
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
        checkFiltersIfCleared();

        if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            view.findViewById(R.id.AJA_back_btn).setRotationY(180);
        }
    }

    private void initTitleViews(View view) {
        if (mHashMapHeaders != null) {
            if (mHashMapHeaders.get(orderedHeadersKey[0]) != null) {
                ((TextView) view.findViewById(R.id.FJL_index)).setText(mHashMapHeaders.get(orderedHeadersKey[0]).getDisplayName());
                setHeaderSearchViewListener(((EditText) view.findViewById(R.id.FJL_index_search)), mHashMapHeaders.get(orderedHeadersKey[0]));
            } else {
//                ((TextView) view.findViewById(R.id.FJL_index)).setText("");
                view.findViewById(R.id.FJL_index_search_lil).setVisibility(View.GONE);
            }
            if (mHashMapHeaders.get(orderedHeadersKey[1]) != null) {
                ((TextView) view.findViewById(R.id.FJL_catalog)).setText(mHashMapHeaders.get(orderedHeadersKey[1]).getDisplayName());
                setHeaderSearchViewListener(((EditText) view.findViewById(R.id.FJL_catalog_search)), mHashMapHeaders.get(orderedHeadersKey[1]));
            } else {
//                ((TextView) view.findViewById(R.id.FJL_catalog)).setText("");
                view.findViewById(R.id.FJL_catalog_lil).setVisibility(View.GONE);
            }
            if (mHashMapHeaders.get(orderedHeadersKey[2]) != null) {
                ((TextView) view.findViewById(R.id.FJL_target)).setText(mHashMapHeaders.get(orderedHeadersKey[2]).getDisplayName());
                setHeaderSearchViewListener(((EditText) view.findViewById(R.id.FJL_target_search)), mHashMapHeaders.get(orderedHeadersKey[2]));
            } else {
//                ((TextView) view.findViewById(R.id.FJL_target)).setText("");
                view.findViewById(R.id.FJL_target_search_lil).setVisibility(View.GONE);
            }
            if (mHashMapHeaders.get(orderedHeadersKey[3]) != null) {
                ((TextView) view.findViewById(R.id.FJL_produced)).setText(mHashMapHeaders.get(orderedHeadersKey[3]).getDisplayName());
                setHeaderSearchViewListener(((EditText) view.findViewById(R.id.FJL_produced_search)), mHashMapHeaders.get(orderedHeadersKey[3]));
            } else {
//                ((TextView) view.findViewById(R.id.FJL_produced)).setText("");
                view.findViewById(R.id.FJL_produced_search_lil).setVisibility(View.GONE);
            }
            if (mHashMapHeaders.get(orderedHeadersKey[4]) != null) {
                ((TextView) view.findViewById(R.id.FJL_end_time)).setText(mHashMapHeaders.get(orderedHeadersKey[4]).getDisplayName());
                setHeaderSearchViewListener(((EditText) view.findViewById(R.id.FJL_end_search)), mHashMapHeaders.get(orderedHeadersKey[04]));
            } else {
//                ((TextView) view.findViewById(R.id.FJL_end_time)).setText("");
                view.findViewById(R.id.FJL_end_search_lil).setVisibility(View.GONE);
            }
            if (mHashMapHeaders.get(orderedHeadersKey[5]) != null) {
                ((TextView) view.findViewById(R.id.FJL_job_left)).setText(mHashMapHeaders.get(orderedHeadersKey[5]).getDisplayName());
                setHeaderSearchViewListener(((EditText) view.findViewById(R.id.FJL_job_search)), mHashMapHeaders.get(orderedHeadersKey[5]));
            } else {
//                ((TextView) view.findViewById(R.id.FJL_job_left)).setText("");
                view.findViewById(R.id.FJL_job_search_lil).setVisibility(View.GONE);
            }
            if (mHashMapHeaders.get(orderedHeadersKey[6]) != null) {
                ((TextView) view.findViewById(R.id.FJL_image)).setText(mHashMapHeaders.get(orderedHeadersKey[6]).getDisplayName());
                setHeaderSearchViewListener(((EditText) view.findViewById(R.id.FJL_image_search)), mHashMapHeaders.get(orderedHeadersKey[6]));
            } else {
//                ((TextView) view.findViewById(R.id.FJL_image)).setText("");
                view.findViewById(R.id.FJL_image_search_lil).setVisibility(View.GONE);
            }

            if (orderedHeadersKey.length > 7 && mHashMapHeaders.get(orderedHeadersKey[7]) != null) {
                ((TextView) view.findViewById(R.id.FJL_extra_1)).setText(mHashMapHeaders.get(orderedHeadersKey[7]).getDisplayName());
                setHeaderSearchViewListener(((EditText) view.findViewById(R.id.FJL_search_extra_1)), mHashMapHeaders.get(orderedHeadersKey[7]));
            } else {
//                ((TextView) view.findViewById(R.id.FJL_extra_1)).setText("");
                view.findViewById(R.id.FJL_search_extra_1_lil).setVisibility(View.GONE);
            }if (orderedHeadersKey.length > 8 && mHashMapHeaders.get(orderedHeadersKey[8]) != null) {
                ((TextView) view.findViewById(R.id.FJL_extra_2)).setText(mHashMapHeaders.get(orderedHeadersKey[8]).getDisplayName());
                setHeaderSearchViewListener(((EditText) view.findViewById(R.id.FJL_search_extra_2)), mHashMapHeaders.get(orderedHeadersKey[8]));
            } else {
//                ((TextView) view.findViewById(R.id.FJL_extra_2)).setText("");
                view.findViewById(R.id.FJL_search_extra_2_lil).setVisibility(View.GONE);
            }if (orderedHeadersKey.length > 9 && mHashMapHeaders.get(orderedHeadersKey[9]) != null) {
                ((TextView) view.findViewById(R.id.FJL_extra_3)).setText(mHashMapHeaders.get(orderedHeadersKey[9]).getDisplayName());
                setHeaderSearchViewListener(((EditText) view.findViewById(R.id.FJL_search_extra_3)), mHashMapHeaders.get(orderedHeadersKey[9]));
            } else {
//                ((TextView) view.findViewById(R.id.FJL_extra_3)).setText("");
                view.findViewById(R.id.FJL_search_extra_3_lil).setVisibility(View.GONE);
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
                mSavedFilters.put(header.getName(), charSequence.toString());
                PersistenceManager.getInstance().setSavedPendingJobFilters(mSavedFilters);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkFiltersIfCleared();
            }
        });

        if (mSavedFilters.containsKey(header.getName())){
            searchView.setText(mSavedFilters.get(header.getName()));
        }else {
            searchView.setText("");
        }
    }


    private void initVarsSearch(View view) {
        mSearchViewEt = view.findViewById(R.id.AJA_search_et);
        mPendingJobsRv = view.findViewById(R.id.AJA_product_rv);
        mClearFiltersTv = view.findViewById(R.id.AJA_clear_filters_tv);

    }

    private void initRecyclerViews() {

        if (mPendingJobs != null && mPendingJobs.size() > 0) {
            mPendingJobs.get(0).setSelected(true);
            RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            mPendingJobsAdapter = new PendingJobsListAdapter(mPendingJobs, orderedHeadersKey, this, getActivity());
            mPendingJobsRv.setLayoutManager(layoutManager2);
            mPendingJobsRv.setAdapter(mPendingJobsAdapter);
        }
    }

    private void initListener(View view) {

        view.findViewById(R.id.AJA_search_btn).setOnClickListener(this);
        view.findViewById(R.id.AJA_back_btn).setOnClickListener(this);
        mClearFiltersTv.setOnClickListener(this);

        mSearchViewEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mSavedFilters.put(GLOBAL_SEARCH_FIELD, s.toString());
                PersistenceManager.getInstance().setSavedPendingJobFilters(mSavedFilters);
                updateRvBySearchResult();
                checkFiltersIfCleared();
            }
        });
        if (mSavedFilters.containsKey(GLOBAL_SEARCH_FIELD)){
            mSearchViewEt.setText(mSavedFilters.get(GLOBAL_SEARCH_FIELD));
        }
    }

    private void checkFiltersIfCleared() {
        boolean isFilterEmpty = true;
        for (String key : mSavedFilters.keySet()) {
            if (!mSavedFilters.get(key).isEmpty()){
                isFilterEmpty = false;
                break;
            }
        }

        if (isFilterEmpty && mSearchViewEt.getText().toString().isEmpty()) {
            mClearFiltersTv.setBackgroundColor(getResources().getColor(R.color.white_five));
            mClearFiltersTv.setOnClickListener(null);
        }else {
            mClearFiltersTv.setBackgroundColor(getResources().getColor(R.color.blue1));
            mClearFiltersTv.setOnClickListener(JobListFragment.this);
        }
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
        if (mPendingJobsResponse != null || ((isQcFromMaterial || isQcFromJob) && mJobForTestList != null)) {
            List<PendingJob> jobList = (isQcFromJob || isQcFromMaterial) ? mJobForTestList : mPendingJobsResponse.getPendingJobs();
            outerLoop:
            for (PendingJob pendingJob : jobList) {

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
            case R.id.AJA_clear_filters_tv:
                mSavedFilters = new HashMap<>();
                PersistenceManager.getInstance().setSavedPendingJobFilters(mSavedFilters);
                mSearchViewEt.setText("");
//                initVarsSearch(getView());
                initTitleViews(getView());
                initRecyclerViews();
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
