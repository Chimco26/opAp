package com.operatorsapp.interfaces;

import android.content.Context;

import com.operatorsapp.view.SingleLineKeyboard;

public interface OnKeyboardManagerListener {

    void onOpenKeyboard(Context context, SingleLineKeyboard.OnKeyboardClickListener listener, String text, String[] complementChars);

    void onCloseKeyboard();
}
