package com.operatorsapp.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.TooltipCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.common.QCModels.SamplesDatum;
import com.example.common.QCModels.TestSampleFieldsDatum;
import com.operatorsapp.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.common.QCModels.TestDetailsResponse.FIELD_TYPE_LAST;


public class QCParametersHorizontalAdapter extends RecyclerView.Adapter<QCParametersHorizontalAdapter.ViewHolder> {

    private final QCSamplesMultiTypeAdapter.QCSamplesMultiTypeAdapterListener mQcSamplesMultiTypeAdapterListener;
    private int itemWidth = 120;
    private Integer samples;
    private ArrayList<TestSampleFieldsDatum> list;
    private boolean isEditMode = true;

    public QCParametersHorizontalAdapter(Integer samples, List<TestSampleFieldsDatum> testSampleFieldsData,
                                         QCSamplesMultiTypeAdapter.QCSamplesMultiTypeAdapterListener qcSamplesMultiTypeAdapterListener,
                                         int widthPixels, boolean isEditMode) {
        list = (ArrayList<TestSampleFieldsDatum>) testSampleFieldsData;
        mQcSamplesMultiTypeAdapterListener = qcSamplesMultiTypeAdapterListener;
        this.samples = samples;
        updateMinusLastColumn();
        if (widthPixels > 0) {
            itemWidth = widthPixels / list.size();
        }
        this.isEditMode = isEditMode;
    }


    @NonNull
    @Override
    public QCParametersHorizontalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new QCParametersHorizontalAdapter.ViewHolder(inflater.inflate(R.layout.item_qc_paramters_horizontal, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull final QCParametersHorizontalAdapter.ViewHolder viewHolder, final int position) {

        if (list.get(position).isFailed()){
            (viewHolder).mTitleTv.setTextColor(viewHolder.itemView.getResources().getColor(R.color.red_line_alpha));
        }else if (list.get(position).getRequiredField() && list.get(position).getAllowEntry() && isEditMode){
            (viewHolder).mTitleTv.setTextColor(viewHolder.itemView.getResources().getColor(R.color.blue1));
        }else {
            (viewHolder).mTitleTv.setTextColor(viewHolder.itemView.getResources().getColor(R.color.black));
        }
        TooltipCompat.setTooltipText(viewHolder.mTitleTv, list.get(position).getLName());
        viewHolder.mTitleTv.setText(list.get(position).getLName());
        viewHolder.mRv.setLayoutManager(new LinearLayoutManager(viewHolder.mRv.getContext()));
        viewHolder.mRv.setAdapter(new QCSamplesMultiTypeAdapter(list.get(position).getFieldType(),
                list.get(position), mQcSamplesMultiTypeAdapterListener, isEditMode));

    }

    public void updateMinusLastColumn() {
        TestSampleFieldsDatum testSampleFieldsDatum = new TestSampleFieldsDatum();

        testSampleFieldsDatum.setSamplesData(new ArrayList<SamplesDatum>());
        testSampleFieldsDatum.setFieldType(FIELD_TYPE_LAST);

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
            ViewGroup.LayoutParams params = itemView.getLayoutParams();
            params.width = itemWidth;
            itemView.setLayoutParams(params);
        }

    }
}
