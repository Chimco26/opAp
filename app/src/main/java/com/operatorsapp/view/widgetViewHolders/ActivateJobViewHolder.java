package com.operatorsapp.view.widgetViewHolders;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.operators.machinedatainfra.models.Widget;
import com.operatorsapp.R;
import com.operatorsapp.model.event.ActivateJobEvent;

import org.greenrobot.eventbus.EventBus;

public class ActivateJobViewHolder extends RecyclerView.ViewHolder {
    private final int mHeight;
    private final int mWidth;
    private final EditText editText;

    public ActivateJobViewHolder(@NonNull View itemView, int height, int width) {
        super(itemView);
        mHeight = height;
        mWidth = width;
        editText = itemView.findViewById(R.id.HFT_edit_text);
        setSizes((LinearLayout) itemView.findViewById(R.id.widget_parent_layout));
    }
    public void setData(Widget widget){
        editText.requestFocus();
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE){
                    EventBus.getDefault().post(new ActivateJobEvent(editText.getText().toString()));
                }
                return false;
            }
        });
    }

    private void setSizes(final LinearLayout parent) {
        ViewGroup.LayoutParams layoutParams;
        layoutParams = parent.getLayoutParams();
        layoutParams.height = (int) (mHeight * 0.5);
        layoutParams.width = (int) (mWidth * 0.325);
        parent.requestLayout();

    }
}
