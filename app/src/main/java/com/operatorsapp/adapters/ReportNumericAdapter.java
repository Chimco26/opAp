package com.operatorsapp.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.text.Editable;
import android.text.Html;
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
import com.operatorsapp.interfaces.OnKeyboardManagerListener;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.view.SingleLineKeyboard;

import java.util.List;
import java.util.Locale;

public class ReportNumericAdapter extends PagerAdapter {
    private final OnKeyboardManagerListener mKeyboardListener;
    private List<ActiveJob> mActiveJobs;
    private boolean isReject;

    public ReportNumericAdapter(List<ActiveJob> activeJobs, boolean isRejects, OnKeyboardManagerListener onKeyboardManagerListener) {
        mActiveJobs = activeJobs;
        this.isReject = isRejects;
        mKeyboardListener = onKeyboardManagerListener;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(collection.getContext());
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.item_report_numeric, collection, false);
        initView(layout, position);
        collection.addView(layout);
        return layout;
    }

    private void initView(ViewGroup itemView, final int position) {

        final Context context = itemView.getContext();
        TextView title = itemView.findViewById(R.id.IRN_title);
        TextView boxTitle = itemView.findViewById(R.id.IRN_edit_number_title_tv);
        final EditText editNumericEt = itemView.findViewById(R.id.IRN_edit_number_et);
        RadioGroup radioGroup = itemView.findViewById(R.id.IRN_radio_group);
        ((TextView)itemView.findViewById(R.id.IRN_edit_unit_btn)).setText(PersistenceManager.getInstance().getTranslationForKPIS().getKPIByName("units"));
        initTitle(title, boxTitle, position, context);
//        setText(String.valueOf(mActiveJobs.get(position).getEditedValue()), position, editNumericEt, context);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mActiveJobs.get(position).setUnit((checkedId == R.id.IRN_edit_unit_btn));
            }
        });
//        itemView.findViewById(R.id.IRN_edit_weight_btn).callOnClick();
        textChangeListener(editNumericEt, position, context);
        editNumericEt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int inType = editNumericEt.getInputType(); // backup the input type
                editNumericEt.setInputType(InputType.TYPE_NULL); // disable soft input
                editNumericEt.onTouchEvent(event); // call native handler
                editNumericEt.setInputType(inType); // restore input type
                setKeyBoard(editNumericEt, new String[]{".", "-"});
                return false; // consume touch event
            }
        });
    }

    private void setKeyBoard(final EditText editText, String[] complementChars) {
        if (mKeyboardListener != null) {
            mKeyboardListener.onOpenKeyboard(editText.getContext(), new SingleLineKeyboard.OnKeyboardClickListener() {
                @Override
                public void onKeyboardClick(String text) {
                    editText.setText(text);
                }
            }, editText.getText().toString(), complementChars);
        }
    }

    private void textChangeListener(@NonNull final EditText editText, final int position, final Context context) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                setText(s, position, editText, context);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void setText(CharSequence s, int position, @NonNull EditText editText, Context context) {
        mActiveJobs.get(position).setEdited(false);
        try {
            float value = Float.parseFloat(String.valueOf(s));
            mActiveJobs.get(position).setEditedValue((int) value);
            if (mActiveJobs.get(position).getJoshUnitsProducedOK() != null
                    && value <= Float.parseFloat(mActiveJobs.get(position).getJoshUnitsProducedOK())
                    && value > 0) {
                mActiveJobs.get(position).setEdited(true);
                editText.setTextColor(context.getResources().getColor(R.color.black));
                if (isReject) {
                    mActiveJobs.get(position).setReportValue(value * -1);
                } else {
                    mActiveJobs.get(position).setReportValue(value);
                }
            } else {
                editText.setTextColor(context.getResources().getColor(R.color.red_line));
            }
        } catch (NumberFormatException e) {
            editText.setTextColor(context.getResources().getColor(R.color.red_line));
        }
    }

    private void initTitle(@NonNull TextView textView, TextView boxTitle, int position, Context context) {
        if (isReject) {
            String txt = context.getResources().getString(R.string.setup_good_units_dialog_text);
            txt = txt.replace(context.getResources().getString(R.string.placeholder1), PersistenceManager.getInstance().getTranslationForKPIS().getKPIByName("GoodUnits"));

            textView.setText(Html.fromHtml(String.format(Locale.getDefault(), "%s <b>%s</b> %s %s <b>%s</b>.<br /> %s",
                    context.getString(R.string.on_the_setup_you_produced),
                    mActiveJobs.get(position).getJobUnitsProducedOK(),
                    context.getString(R.string.rejects),
                    context.getString(R.string.from_the),
                    mActiveJobs.get(position).getProductName(),
                    txt)));
            boxTitle.setText(context.getString(R.string.good_units));
        } else {
            textView.setText(Html.fromHtml(String.format(Locale.getDefault(), "%s <b>%s</b> %s %s <b>%s</b>.<br /> %s",
                    context.getString(R.string.on_the_setup_you_produced),
                    mActiveJobs.get(position).getJobUnitsProducedOK(),
                    context.getString(R.string.good_units),
                    context.getString(R.string.from_the),
                    mActiveJobs.get(position).getProductName(),
                    context.getString(R.string.setup_rejects_dialog_text))));
            boxTitle.setText(context.getString(R.string.rejects));
        }
    }

    @Override
    public void destroyItem(@NonNull ViewGroup collection, int position, @NonNull Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        if (mActiveJobs != null) {
            return mActiveJobs.size();
        } else return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
