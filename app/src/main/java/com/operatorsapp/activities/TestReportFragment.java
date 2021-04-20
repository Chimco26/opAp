package com.operatorsapp.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.request.BaseRequest;
import com.google.gson.Gson;
import com.operatorsapp.R;
import com.operatorsapp.adapters.TestReportAdapter;
import com.operatorsapp.fragments.JobListFragment;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.server.responses.TestReportColumn;
import com.operatorsapp.server.responses.TestReportRow;
import com.operatorsapp.server.responses.TestReportsResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

public class TestReportFragment extends Fragment implements View.OnClickListener {

    private TestReportsResponse mTestReport;
    private RecyclerView mRecycler;
    private ProgressBar mProgress;
    private TextView mClearFilter;
    private ImageView mSearchIconIv;
    private EditText mSearchEt;
    private Gson mGson;
    private HashMap<String, String> mFilterMap = new HashMap<>();
    private TestReportsResponse mTestReportFiltered;
    private LinearLayout mHeaderRowLil;
    private LinearLayout mHeaderRowFilterLil;
    private Handler mHandlerFilter = new Handler();
    private TestReportFragmentListener mListener;

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
                setHeaders();
                filterData();
                mProgress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<TestReportsResponse> call, Throwable t) {
                mProgress.setVisibility(View.GONE);
            }
        });
    }


    private void showTestReport() {
        if (mFilterMap == null || mFilterMap.isEmpty()) {
            mFilterMap = new HashMap<>();
            for (TestReportColumn column : mTestReport.getColumns()) {
                mFilterMap.put(column.getFieldName(), "");
            }
        }
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setAdapter(new TestReportAdapter(mTestReportFiltered, new TestReportAdapter.TestReportAdapterListener() {
            @Override
            public void onReportClicked(TestReportRow row) {
                mListener.onReportSelected(row.getID());
            }

            @Override
            public TextView getCustomTextView(int size) {
                return TestReportFragment.this.getCustomTextView(size);
            }
        }));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test_report, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initViews(view);
        getTestReports();
        super.onViewCreated(view, savedInstanceState);
    }

    private void initViews(View view) {
        mRecycler = view.findViewById(R.id.FTR_recycler_rv);
        mHeaderRowLil = view.findViewById(R.id.test_report_item_lil);
        mHeaderRowFilterLil = view.findViewById(R.id.test_report_item_filter_lil);
        mClearFilter = view.findViewById(R.id.FTR_clear_filters_tv);
        mSearchIconIv = view.findViewById(R.id.FTR_search_iv);
        mSearchEt = view.findViewById(R.id.FTR_search_et);
        mProgress = view.findViewById(R.id.FTR_progress_pb);

        mClearFilter.setOnClickListener(this);
        mSearchIconIv.setOnClickListener(this);

        mSearchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterData();
            }
        });
    }

    private void filterData() {
        mGson = new Gson();
        if (mFilterMap.isEmpty()) {
            for (TestReportColumn column : mTestReport.getColumns()) {
                mFilterMap.put(column.getFieldName(), "");
            }
            mTestReportFiltered = mTestReport;
        } else {
            mTestReportFiltered = new TestReportsResponse();
            mTestReportFiltered.setColumns(mTestReport.getColumns());
            for (TestReportRow row : mTestReport.getRows()) {
                try {
                    JSONObject json = new JSONObject(mGson.toJson(row));
                    boolean isAdd = true;
                    boolean isSearchAdd = false;
                    for (String filter : mFilterMap.keySet()) {
                        if (json.has(filter) && mFilterMap.get(filter).length() > 0 && !json.get(filter).toString().contains(mFilterMap.get(filter))) {
                            isAdd = false;
                            break;
                        } else if (mSearchEt.getText().toString().length() == 0 || (json.has(filter) && json.get(filter).toString().contains(mSearchEt.getText().toString()))) {
                            isSearchAdd = true;
                        }
                    }
                    if (isAdd && isSearchAdd) {
                        mTestReportFiltered.addRow(row);
                    }
                } catch (JSONException e) {
                }
            }
        }

        checkFilter();
        showTestReport();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.FTR_search_iv:
                filterData();
                break;
            case R.id.FTR_clear_filters_tv:
                clearFilter();
                break;
        }
    }

    private void clearFilter() {
        for (String key : mFilterMap.keySet()) {
            mFilterMap.put(key, "");
        }
        for(int i = 0; i < mHeaderRowFilterLil.getChildCount(); i++){
            if (mHeaderRowFilterLil.getChildAt(i) instanceof EditText){
                ((EditText) mHeaderRowFilterLil.getChildAt(i)).setText("");
            }
        }
        mSearchEt.setText("");
        mClearFilter.setBackgroundColor(getResources().getColor(R.color.white_five));

        filterData();
    }

    private void checkFilter() {
        if (mSearchEt.getText().toString().isEmpty()) {
            for (String filter : mFilterMap.keySet()) {
                if (mFilterMap.containsKey(filter) && !mFilterMap.get(filter).isEmpty()) {
                    mClearFilter.setBackgroundColor(getResources().getColor(R.color.blue1));
                    return;
                }
            }
            mClearFilter.setBackgroundColor(getResources().getColor(R.color.white_five));
        } else {
            mClearFilter.setBackgroundColor(getResources().getColor(R.color.blue1));
        }
    }

    private void setHeaders() {
        mHeaderRowFilterLil.removeAllViews();
        mHeaderRowFilterLil.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mHeaderRowLil.removeAllViews();
        mHeaderRowLil.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        for (TestReportColumn column : mTestReport.getColumns()) {

            TextView tv = getCustomTextView(mTestReport.getColumns().size());
            tv.setTextSize(COMPLEX_UNIT_SP, 18);
            tv.setText(column.getDisplayHName());
            mHeaderRowLil.addView(tv);

            EditText et = getCustomEditText(getCellWidth(mTestReport.getColumns().size()));
            et.setTextSize(COMPLEX_UNIT_SP, 18);
            et.setText(mFilterMap.get(column.getFieldName()));
            mHeaderRowFilterLil.addView(et);
            et.addTextChangedListener(setTextWatcher(column.getFieldName()));
        }
    }

    private TextView getCustomTextView(int size) {
        TextView tv = new TextView(getContext());
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(getCellWidth(size), 50);
        params.setMargins(5, 2, 5, 2);
        tv.setLayoutParams(params);
        tv.setTextSize(COMPLEX_UNIT_SP, 14);
        tv.setTextColor(Color.BLACK);
        tv.setMaxLines(2);

        tv.setEllipsize(TextUtils.TruncateAt.END);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setGravity(Gravity.CENTER);
        return tv;
    }

    private int getCellWidth(int size) {
        if (size > 0 && getView() != null) {
            return (getView().getWidth() - 200) / size;
        }
        return 200;
    }

    private EditText getCustomEditText(int cellWidth) {
        EditText et = new EditText(getContext());
        float density = getResources().getDisplayMetrics().density;
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(cellWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins((int) (2 * density), 0, (int) (2 * density), 0);
        et.setLayoutParams(params);
        et.setTextSize(COMPLEX_UNIT_SP, 16);
        et.setTextColor(Color.BLACK);
        et.setBackground(getResources().getDrawable(R.drawable.box_grey_stroke));
        et.setGravity(Gravity.CENTER);
        return et;
    }

    private TextWatcher setTextWatcher(final String fieldName) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mFilterMap.put(fieldName, s.toString());
                mHandlerFilter.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        filterData();
                    }
                }, 1000);
            }
        };
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof JobListFragment.JobListFragmentListener) {
            mListener = (TestReportFragmentListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface TestReportFragmentListener {
        void onReportSelected(int id);
    }
}