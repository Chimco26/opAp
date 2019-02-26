package com.operatorsapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.operators.activejobslistformachineinfra.ActiveJob;
import com.operatorsapp.R;
import com.operatorsapp.view.FocusableRecycleView;
import com.operatorsapp.view.SingleLineKeyboard;
import com.operatorsapp.view.widgetViewHolders.NumericViewHolder;

import java.util.List;
import java.util.Locale;

public class ReportNumericAdapter extends FocusableRecycleView.Adapter<ReportNumericAdapter.ViewHolder> {
    private final NumericViewHolder.OnKeyboardManagerListener mKeyboardListener;
    private List<ActiveJob> mActiveJobs;
    private boolean isReject;

    public ReportNumericAdapter(List<ActiveJob> activeJobs, boolean isRejects, NumericViewHolder.OnKeyboardManagerListener onKeyboardManagerListener) {
        mActiveJobs = activeJobs;
        this.isReject = isRejects;
        mKeyboardListener = onKeyboardManagerListener;
    }

    @NonNull
    @Override
    public ReportNumericAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new ReportNumericAdapter.ViewHolder(inflater.inflate(R.layout.item_report_numeric, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull final ReportNumericAdapter.ViewHolder holder, final int position) {
        final Context context = holder.itemView.getContext();
        initTitle(holder, position, context);
        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mActiveJobs.get(position).setUnit((checkedId == R.id.IRN_edit_unit_btn));
            }
        });
        textChangeListener(holder, position, context);
        holder.editNumericEt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int inType = holder.editNumericEt.getInputType(); // backup the input type
                holder.editNumericEt.setInputType(InputType.TYPE_NULL); // disable soft input
                holder.editNumericEt.onTouchEvent(event); // call native handler
                holder.editNumericEt.setInputType(inType); // restore input type
                setKeyBoard(holder.editNumericEt, new String[]{".", "-"});
                return false; // consume touch event
            }
        });
    }

    private void setKeyBoard(final EditText editText, String[] complementChars) {
        if (mKeyboardListener != null) {
            mKeyboardListener.onOpenKeyboard(new SingleLineKeyboard.OnKeyboardClickListener() {
                @Override
                public void onKeyboardClick(String text) {
                    editText.setText(text);
                }
            }, editText.getText().toString(), complementChars);
        }
    }

    private void textChangeListener(@NonNull final ViewHolder holder, final int position, final Context context) {
        holder.editNumericEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mActiveJobs.get(position).setEdited(false);
                try {
                    float value = Float.parseFloat(String.valueOf(s));
                    if (value <= Float.parseFloat(mActiveJobs.get(position).getJoshUnitsProducedOK())
                            && value > 0) {
                        mActiveJobs.get(position).setEdited(true);
                        holder.editNumericEt.setTextColor(context.getResources().getColor(R.color.black));
                        if (isReject) {
                            mActiveJobs.get(position).setReportValue(value * -1);
                        } else {
                            mActiveJobs.get(position).setReportValue(value);
                        }
                    } else {
                        holder.editNumericEt.setTextColor(context.getResources().getColor(R.color.red_line));
                    }
                } catch (NumberFormatException e) {
                    holder.editNumericEt.setTextColor(context.getResources().getColor(R.color.red_line));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void initTitle(@NonNull ViewHolder holder, int position, Context context) {
        if (isReject) {
            holder.title.setText(String.format(Locale.getDefault(), "%s %s - %s %s",
                    context.getString(R.string.product), mActiveJobs.get(position).getProductName(),
                    mActiveJobs.get(position).getJobUnitsProducedOK(),
                    context.getString(R.string.rejects)));
        } else {
            holder.title.setText(String.format(Locale.getDefault(), "%s %s - %s %s",
                    context.getString(R.string.product), mActiveJobs.get(position).getProductName(),
                    mActiveJobs.get(position).getJobUnitsProducedOK(),
                    context.getString(R.string.good_units)));
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private EditText editNumericEt;
        private RadioGroup radioGroup;

        ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.IRN_title);
            editNumericEt = itemView.findViewById(R.id.IRN_edit_number_et);
            radioGroup = itemView.findViewById(R.id.IRN_radio_group);

        }

    }

    @Override
    public int getItemCount() {
        if (mActiveJobs != null) {
            return mActiveJobs.size();
        } else return 0;
    }
}
