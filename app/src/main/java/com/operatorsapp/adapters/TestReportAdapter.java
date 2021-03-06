package com.operatorsapp.adapters;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.operatorsapp.R;
import com.operatorsapp.server.responses.TestReportColumn;
import com.operatorsapp.server.responses.TestReportRow;
import com.operatorsapp.server.responses.TestReportsResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class TestReportAdapter extends RecyclerView.Adapter<TestReportAdapter.ViewHolder> {

    private static final int TYPE_ROW = 2222;
    private static final int TYPE_HEADER = 1111;
    private final TestReportsResponse mTestReport;
    private final Gson mGson;
    private final TestReportAdapterListener mListener;
    Handler mHandlerFilter;

    public TestReportAdapter(TestReportsResponse testReport, TestReportAdapterListener listener) {
        mGson = new Gson();
        mHandlerFilter = new Handler();
        mTestReport = testReport;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.test_report_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setRow(holder, position);
    }

    private void setRow(ViewHolder holder, int position) {
        holder.itemRowFilter.setVisibility(View.GONE);
        holder.itemRow.removeAllViews();
        holder.itemLy.setBackgroundColor(ContextCompat.getColor(holder.itemLy.getContext(), R.color.grey_transparent));
        TestReportRow row = mTestReport.getRows().get(position); /// -1 because position 0 is headers
        try {
            JSONObject json = new JSONObject(mGson.toJson(row));
            for (TestReportColumn column : mTestReport.getColumns()) {
                TextView tv = mListener.getCustomTextView(mTestReport.getColumns().size());
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

    @Override
    public int getItemCount() {
        if (mTestReport.getColumns() != null) {
            return mTestReport.getRows().size();
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
        private final View itemLy;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemRow = itemView.findViewById(R.id.test_report_item_lil);
            itemRowFilter = itemView.findViewById(R.id.test_report_item_filter_lil);
            itemLy = itemView.findViewById(R.id.TRI_item_ly);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onReportClicked(mTestReport.getRows().get(getAdapterPosition()));
                }
            });
        }
    }

    public interface TestReportAdapterListener {
        void onReportClicked(TestReportRow row);
        TextView getCustomTextView(int size);
    }
}
