package com.operatorsapp.adapters;


import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.operators.jobsinfra.Header;
import com.operatorsapp.R;
import com.operatorsapp.fragments.interfaces.OnJobSelectedCallbackListener;
import com.operatorsapp.model.CurrentJob;
import com.operatorsapp.utils.TimeUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JobsRecyclerViewAdapter extends RecyclerView.Adapter<JobsRecyclerViewAdapter.ViewHolder> implements Filterable {
    private static final String ID = "ID";
    private List<Header> mHeaderList;
    private List<HashMap<String, Object>> mJobsList;
    private List<HashMap<String, Object>> mJobsFilter;
    private List<CurrentJob> mCurrentJobs = new ArrayList<>();
    private OnJobSelectedCallbackListener mOnJobSelectedCallbackListener;
    private JobFilter mFilter = new JobFilter();

    public JobsRecyclerViewAdapter(OnJobSelectedCallbackListener onJobSelectedCallbackListener, List<Header> headerList, List<HashMap<String, Object>> jobsDataList) {
        mOnJobSelectedCallbackListener = onJobSelectedCallbackListener;
        mHeaderList = headerList;
        mJobsList = jobsDataList;
        mJobsFilter = jobsDataList;

    }

    @Override
    public JobsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.job_recycler_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final JobsRecyclerViewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        BigDecimal bigDecimal = new BigDecimal(mJobsList.get(position).get(ID).toString());
        int jobId = bigDecimal.intValue();
        holder.mJobRowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnJobSelectedCallbackListener.onJobSelected(mCurrentJobs.get(position));
            }
        });

        // TODO: 22-Nov-16 CHECK THIS!!!!!
        String[] fieldValues = new String[5];
        String[] headers = new String[5];
        if (mHeaderList.size() > 0) {

            for (int i = 0; i < 5; i++) {
                if (i < mHeaderList.size()) {
                    if (mJobsList == null || mJobsList.get(position) == null || TextUtils.isEmpty(mHeaderList.get(i).getFieldName()) || mJobsList.get(position).get(mHeaderList.get(i).getFieldName()) == null || TextUtils.isEmpty(mJobsList.get(position).get(mHeaderList.get(i).getFieldName()).toString())) {
                        fieldValues[i] = "- -";
                        headers[i] = "- -";
                    } else {
                        headers[i] = mHeaderList.get(i).getFieldName();
                        switch (mHeaderList.get(i).getDisplayType()) {
                            case "date": {
                                fieldValues[i] = TimeUtils.getDateForJob(mJobsList.get(position).get(mHeaderList.get(i).getFieldName()).toString());
                                break;
                            }
                            case "num": {
                                try {
                                    String doubleFormat = mJobsList.get(position).get(mHeaderList.get(i).getFieldName()).toString();
                                    Double doubleValue = Double.parseDouble(doubleFormat);

                                    // Check if the num value is double or integer.
                                    if (doubleValue % 1 > 0)
                                        fieldValues[i] = doubleValue.toString();
                                    else
                                        fieldValues[i] = ((Long) Math.round(doubleValue)).toString();

                                } catch (Exception e) {
                                    fieldValues[i] = mJobsList.get(position).get(mHeaderList.get(i).getFieldName()).toString();
                                }
                                break;
                            }
                            case "text": {
                                fieldValues[i] = mJobsList.get(position).get(mHeaderList.get(i).getFieldName()).toString();
                                break;
                            }
                        }
                    }
                } else {
                    fieldValues[i] = "- -";
                    headers[i] = "- -";
                }
            }

            mCurrentJobs.add(new CurrentJob(headers, fieldValues[0], fieldValues[1], fieldValues[2], fieldValues[3], fieldValues[4], jobId));
            initRowsHeader(holder, fieldValues[0], fieldValues[1], fieldValues[2], fieldValues[3], fieldValues[4]);
        }
    }

    private void initRowsHeader(ViewHolder holder, String text1, String text2, String text3, String text4, String text5) {
        holder.mFirstTextView.setText(text1);
        holder.mSecondTextView.setText(text2);
        holder.mThirdTextView.setText(text3);
        holder.mFourthTextView.setText(text4);
        holder.mFifthTextView.setText(text5);
    }

    @Override
    public int getItemCount() {
        return mJobsList.size();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mJobRowLayout;
        private TextView mFirstTextView;
        private TextView mSecondTextView;
        private TextView mThirdTextView;
        private TextView mFourthTextView;
        private TextView mFifthTextView;

        public ViewHolder(View view) {
            super(view);
            mJobRowLayout = (LinearLayout) view.findViewById(R.id.job_row_layout);
            mFirstTextView = (TextView) view.findViewById(R.id.text_view_job_id);
            mSecondTextView = (TextView) view.findViewById(R.id.adapter_text_view_product_name);
            mThirdTextView = (TextView) view.findViewById(R.id.adapter_text_view_ERP);
            mFourthTextView = (TextView) view.findViewById(R.id.adapter_text_planned_start);
            mFifthTextView = (TextView) view.findViewById(R.id.adapter_text_view_number_of_units);
        }
    }

    private class JobFilter extends Filter {


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();
            if (constraint.length() > 1) {

                String[] textToFilter = constraint.toString().split("#@#");


                ArrayList<HashMap<String, Object>> filterJobs = new ArrayList<>();

                for (HashMap<String, Object> objectHashMap : mJobsFilter) {


                    if ((objectHashMap.get(mHeaderList.get(0).getFieldName()) == null || objectHashMap.get(mHeaderList.get(0).getFieldName()).toString().contains(textToFilter[0].substring(0, textToFilter[0].length() - 1)))
                            && (objectHashMap.get(mHeaderList.get(1).getFieldName()) == null || objectHashMap.get(mHeaderList.get(1).getFieldName()).toString().contains(textToFilter[1].substring(0, textToFilter[1].length() - 1)))
                            && (objectHashMap.get(mHeaderList.get(2).getFieldName()) == null || objectHashMap.get(mHeaderList.get(2).getFieldName()).toString().contains(textToFilter[2].substring(0, textToFilter[2].length() - 1)))
                            && (objectHashMap.get(mHeaderList.get(3).getFieldName()) == null || objectHashMap.get(mHeaderList.get(3).getFieldName()).toString().contains(textToFilter[3].substring(0, textToFilter[3].length() - 1)))
                            && (objectHashMap.get(mHeaderList.get(4).getFieldName()) == null || objectHashMap.get(mHeaderList.get(4).getFieldName()).toString().contains(textToFilter[4].substring(0, textToFilter[4].length() - 1))))

                        filterJobs.add(objectHashMap);

                }


                results.values = filterJobs;

                results.count = filterJobs.size();

            } else

            {

                results.values = mJobsFilter;

                results.count = mJobsFilter.size();
            }
            return results;

        }


        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            mJobsList = (List<HashMap<String, Object>>) results.values;

            notifyDataSetChanged();
        }

    }
}
