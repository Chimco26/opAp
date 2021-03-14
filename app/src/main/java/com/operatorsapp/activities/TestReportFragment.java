package com.operatorsapp.activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.common.request.BaseRequest;
import com.operatorsapp.R;
import com.operatorsapp.adapters.TestReportAdapter;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.server.responses.TestReportsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestReportFragment extends Fragment {

    private TestReportsResponse mTestReport;
    private RecyclerView mRecycler;
    private ProgressBar mProgress;

    public TestReportFragment() {
    }

    public static TestReportFragment newInstance() {
        return new TestReportFragment();
    }

    private void getTestReports() {
        mProgress.setVisibility(View.VISIBLE);
        NetworkManager.getInstance().getTestReports(new BaseRequest(PersistenceManager.getInstance().getSessionId()), new Callback<TestReportsResponse>() {
            @Override
            public void onResponse(Call<TestReportsResponse> call, Response<TestReportsResponse> response) {
                mTestReport = response.body();
                showTestReport();
                mProgress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<TestReportsResponse> call, Throwable t) {
                mProgress.setVisibility(View.GONE);
            }
        });
    }

    private void showTestReport() {
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setAdapter(new TestReportAdapter(getActivity(), mTestReport));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_report, container, false);
        initViews(view);
        getTestReports();
        return view;
    }

    private void initViews(View view) {
        mRecycler = view.findViewById(R.id.FTR_recycler_rv);
        mProgress = view.findViewById(R.id.FTR_progress_pb);
    }
}