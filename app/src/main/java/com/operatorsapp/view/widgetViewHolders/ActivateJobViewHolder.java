package com.operatorsapp.view.widgetViewHolders;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
    private static final String TAG = ActivateJobViewHolder.class.getSimpleName();
    private final int mHeight;
    private final int mWidth;
    private final EditText editText;
    private TextWatcher textWatcher;

    public ActivateJobViewHolder(@NonNull View itemView, int height, int width) {
        super(itemView);
        mHeight = height;
        mWidth = width;
        editText = itemView.findViewById(R.id.HFT_edit_text);
        setSizes((LinearLayout) itemView.findViewById(R.id.widget_parent_layout));
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "afterTextChanged: " + s.toString());
                if(editText.getText().toString().contains("\n")){
                    Log.d(TAG, "onTextChanged: true");
                    String text = s.toString().replace("\n","");
                    text = text.replace("\r","");
                    EventBus.getDefault().post(new ActivateJobEvent(text));
                    editText.setText("");
                }
            }
        };
    }

    public void setData(Widget widget) {
        editText.requestFocus();

        editText.removeTextChangedListener(textWatcher);
        editText.addTextChangedListener(textWatcher);
    }

    private void setSizes(final LinearLayout parent) {
        ViewGroup.LayoutParams layoutParams;
        layoutParams = parent.getLayoutParams();
        layoutParams.height = (int) (mHeight * 0.5);
        layoutParams.width = (int) (mWidth * 0.325);
        parent.requestLayout();

    }
}
