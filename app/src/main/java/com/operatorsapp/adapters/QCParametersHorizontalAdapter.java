package com.operatorsapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.common.QCModels.SamplesDatum;
import com.example.common.QCModels.TestSampleFieldsDatum;
import com.operatorsapp.R;

import java.util.ArrayList;
import java.util.List;

import static com.operatorsapp.adapters.QCSamplesMultiTypeAdapter.LAST;

public class QCParametersHorizontalAdapter extends RecyclerView.Adapter<QCParametersHorizontalAdapter.ViewHolder> {

    private Integer samples;
    private ArrayList<TestSampleFieldsDatum> list;

    public QCParametersHorizontalAdapter(Integer samples, List<TestSampleFieldsDatum> testSampleFieldsData) {
        list = (ArrayList<TestSampleFieldsDatum>) testSampleFieldsData;
        this.samples = samples;
        this.samples = 3;
        updateMinusLastColumn();
    }


    @NonNull
    @Override
    public QCParametersHorizontalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new QCParametersHorizontalAdapter.ViewHolder(inflater.inflate(R.layout.item_qc_paramters_horizontal, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull final QCParametersHorizontalAdapter.ViewHolder viewHolder, final int position) {

        viewHolder.mTitleTv.setText(list.get(position).getLName());
        viewHolder.mRv.setLayoutManager(new LinearLayoutManager(viewHolder.mRv.getContext()));
        viewHolder.mRv.setAdapter(new QCSamplesMultiTypeAdapter(list.get(position).getInputType(), (ArrayList<SamplesDatum>) list.get(position).getSamplesData()));

    }

    public void updateMinusLastColumn() {
        TestSampleFieldsDatum testSampleFieldsDatum = new TestSampleFieldsDatum();

        testSampleFieldsDatum.setSamplesData(new ArrayList<SamplesDatum>());
        testSampleFieldsDatum.setInputType(LAST);

        for (int i = 0; i < samples; i++) {
            testSampleFieldsDatum.getSamplesData().add(new SamplesDatum());
        }
        list.add(testSampleFieldsDatum);
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        } else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTitleTv;
        private final RecyclerView mRv;

        ViewHolder(View itemView) {
            super(itemView);

            mTitleTv = itemView.findViewById(R.id.IQCPH_title);
            mRv = itemView.findViewById(R.id.IQCPH_rv);
        }

    }

    public interface QCParametersHorizontalAdapterListener {
        void onItemCheck();
    }
}
