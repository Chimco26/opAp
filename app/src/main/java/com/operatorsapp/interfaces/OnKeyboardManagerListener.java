package com.operatorsapp.interfaces;

import com.operatorsapp.view.SingleLineKeyboard;

public interface OnKeyboardManagerListener {

    void onOpenKeyboard(SingleLineKeyboard.OnKeyboardClickListener listener, String text, String[] complementChars);

    void onCloseKeyboard();
}
