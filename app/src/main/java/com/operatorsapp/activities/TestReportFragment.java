package com.operatorsapp.activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.common.request.BaseRequest;
import com.operatorsapp.R;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.server.responses.TestReportsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestReportFragment extends Fragment {

    public TestReportFragment() {
    }

    public static TestReportFragment newInstance() {
        return new TestReportFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTestReports();
    }

    private void getTestReports() {
        NetworkManager.getInstance().getTestReports(new BaseRequest(PersistenceManager.getInstance().getSessionId()), new Callback<TestReportsResponse>() {
            @Override
            public void onResponse(Call<TestReportsResponse> call, Response<TestReportsResponse> response) {

            }

            @Override
            public void onFailure(Call<TestReportsResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_report, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {

    }
}