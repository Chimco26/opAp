package com.operatorsapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.operatorsapp.R;
import com.operatorsapp.server.responses.TestReportColumn;
import com.operatorsapp.server.responses.TestReportRow;
import com.operatorsapp.server.responses.TestReportsResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

public class TestReportAdapter extends RecyclerView.Adapter<TestReportAdapter.ViewHolder> {

    private static final int TYPE_ROW = 2222;
    private static final int TYPE_HEADER = 1111;
    private final TestReportsResponse mTestReport;
    private final Gson mGson;
    private final FragmentActivity activity;
    Timer filterDelayTimer;
    Handler mHandlerFilter;
    private HashMap<String, String> mFilterMap;

    public TestReportAdapter(FragmentActivity activity, TestReportsResponse testReport) {
        mGson = new Gson();
        this.activity = activity;
        filterDelayTimer = new Timer();
        mHandlerFilter = new Handler();
        mFilterMap = new HashMap<>();
        mTestReport = filterData(testReport);
    }

    private TestReportsResponse filterData(TestReportsResponse testReport) {

        if (mFilterMap.isEmpty()){
            return testReport;
        }else {
            TestReportsResponse testReportFiltered = new TestReportsResponse();
            testReportFiltered.setColumns(testReport.getColumns());
            for (TestReportRow row : testReport.getRows()) {
                try {
                    JSONObject json = new JSONObject(mGson.toJson(row));
                    boolean isAdd = true;
                    for (String filter : mFilterMap.keySet()) {
                        if (!json.has(filter) || !json.get(filter).toString().contains(mFilterMap.get(filter))){
                            isAdd = false;
                            break;
                        }
                    }
                    if (isAdd)
                        testReportFiltered.addRow(row);
                } catch (JSONException e) { }
            }
            return testReportFiltered;
        }
    }

    private TextWatcher setTextWatcher(final String fieldName) {
        mFilterMap.put(fieldName, "");
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mHandlerFilter.removeCallbacksAndMessages(null);
//                filterDelayTimer.cancel();
            }

            @Override
            public void afterTextChanged(Editable s) {
                mFilterMap.put(fieldName, s.toString());
                mHandlerFilter.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                notifyDataSetChanged();
                            }
                        });
                    }
                }, 1000);
//                filterDelayTimer = new Timer();
//                filterDelayTimer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        notifyDataSetChanged();
//                    }
//                }, 1000);
            }
        };
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.test_report_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER){
            setHeaders(holder);
        }else {
            setRow(holder, position);
        }
    }

    private void setRow(ViewHolder holder, int position) {
        holder.itemRowFilter.setVisibility(View.GONE);
        holder.itemRow.removeAllViews();
        TestReportRow row = mTestReport.getRows().get(position - 1); /// -1 because position 0 is headers
        try {
            JSONObject json = new JSONObject(mGson.toJson(row));
            for (TestReportColumn column : mTestReport.getColumns()) {
                TextView tv = getCustomTextView(holder.itemView.getContext());
                String str = "";
                if (json.has(column.getFieldName()))
                    str = String.valueOf(json.get(column.getFieldName()));
                tv.setText(str);
                holder.itemRow.addView(tv);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setHeaders(ViewHolder holder) {
        holder.itemRowFilter.setVisibility(View.VISIBLE);
        holder.itemRow.removeAllViews();

        for (TestReportColumn column : mTestReport.getColumns()){

            TextView tv = getCustomTextView(holder.itemView.getContext());
            tv.setTextSize(COMPLEX_UNIT_SP,18);
            tv.setText(column.getDisplayHName());
            holder.itemRow.addView(tv);

            EditText et = getCustomEditText(holder.itemView.getContext());
            tv.setTextSize(COMPLEX_UNIT_SP,18);
            tv.setText(column.getDisplayHName());
            holder.itemRowFilter.addView(et);
            et.addTextChangedListener(setTextWatcher(column.getFieldName()));
        }
        holder.itemRow.invalidate();
    }

    private EditText getCustomEditText(Context context) {
        EditText et = new EditText(context);
        et.setLayoutParams(new ViewGroup.LayoutParams(200, 80));
        et.setPadding(15,5,15,5);
        et.setTextSize(COMPLEX_UNIT_SP,16);
        et.setTextColor(Color.DKGRAY);
        et.setBackgroundColor(Color.WHITE);
        et.setEllipsize(TextUtils.TruncateAt.END);
        et.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        et.setGravity(Gravity.CENTER);
        return et;
    }

    private TextView getCustomTextView(Context context) {
        TextView tv = new TextView(context);
        tv.setLayoutParams(new ViewGroup.LayoutParams(200, 80));
        tv.setPadding(10,5,10,5);
        tv.setTextSize(COMPLEX_UNIT_SP,16);
        tv.setTextColor(Color.BLACK);
        tv.setEllipsize(TextUtils.TruncateAt.END);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setGravity(Gravity.CENTER);
        return tv;
    }

    @Override
    public int getItemCount() {
        if (mTestReport.getColumns() != null) {
            return mTestReport.getRows().size() + 1;
        }else {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_ROW;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout itemRow;
        public final LinearLayout itemRowFilter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemRow = itemView.findViewById(R.id.test_report_item_lil);
            itemRowFilter = itemView.findViewById(R.id.test_report_item_filter_lil);
        }
    }
}
