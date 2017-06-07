/*
 * Copyright (C) 2017. Henry Tam (Hosle)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hosle.tageditor.handler;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.hosle.tageditor.Utils;
import com.hosle.tageditor.listener.IKeyboardListener;
import com.hosle.tageditor.listener.KeyboardListenerImp;
import com.hosle.tageditor.listener.RichOnKeyListener;
import com.hosle.tageditor.listener.RichTextWatcher;


/**
 * Created by tanjiahao on 17/6/2.
 *
 * The Mainly Handler to register all the listeners,
 * and implement the callbacks to refresh the layouts
 */

public class LayoutHandlerImp implements ILayoutHandler, ViewInterface {
    private boolean hideInputForContentPanel = false;

    private EditText editText;
    private ViewGroup contentPanel;
    private CheckBox switchButton;
    private ViewGroup parentBox;
    private Activity activity;
    private InputMethodManager imm;

    public LayoutHandlerImp(Activity activity, ViewGroup contentPanel, CheckBox switchButton, ViewGroup parentBox) {
        this.activity = activity;
        this.contentPanel = contentPanel;
        this.switchButton = switchButton;
        this.parentBox = parentBox;
        this.parentBox.setVisibility(View.GONE);
        this.imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

    }

    public void attachEditText(EditText editText, RichOnKeyListener onKeyListener, RichTextWatcher textWatcher) {
        this.editText = editText;
        this.editText.setOnKeyListener(onKeyListener);
        this.editText.addTextChangedListener(textWatcher);
    }

    public IKeyboardListener createKeyboardListener() {
        return new KeyboardListenerImp(activity, contentPanel, this, this);
    }

    public CompoundButton.OnCheckedChangeListener createSwitcherChangedListener() {
        Utils.checkNotNull(editText, "Must Attach EditText to Handler First");
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    setHideInputForContentPanel(true);
                    updateSoftInputMethod(activity, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

                    contentPanel.setVisibility(View.VISIBLE);
                } else {
                    imm.showSoftInput(editText, 0);
                    resetKeyboardShowStatus();
                }
            }
        };
    }

    public void dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isClickInside(parentBox, ev)) {
                //ignore when click inside the box
            } else if (isClickInside(editText, ev)) {
                //show keyboard
                resetKeyboardShowStatus();
                switchButton.setChecked(false);
            } else {
                //click other area to close
                closeAllPannel();
            }
        }
    }

    private void closeAllPannel() {
        updateSoftInputMethod(activity, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        if (isHideInputForContentPanel()) {
            //close when the keyboard is hidden
            parentBox.setVisibility(View.GONE);
        } else {
            //close when the keyboard is appeared
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    private boolean isClickInside(View v, MotionEvent event) {
        if (v != null) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();

            return event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom;
        }
        return false;
    }

    public void resetKeyboardShowStatus() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updateSoftInputMethod(activity, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                contentPanel.setVisibility(View.GONE);
            }
        }, 500);
    }

    @Override
    public void updateViewStatus(boolean isRootBoxVisible, boolean isContentPanelVisible, boolean isSwitcherSelected) {
        this.contentPanel.setVisibility(isContentPanelVisible ? View.VISIBLE : View.GONE);
        this.switchButton.setChecked(isSwitcherSelected);
        parentBox.setVisibility(isRootBoxVisible ? View.VISIBLE : View.GONE);
    }

    public boolean isHideInputForContentPanel() {
        return hideInputForContentPanel;
    }

    public void setHideInputForContentPanel(boolean hideInputForContentPanel) {
        this.hideInputForContentPanel = hideInputForContentPanel;
    }

    @Override
    public void updateSoftInputMethod(Activity activity, int softInputMode) {
        if (!activity.isFinishing()) {
            WindowManager.LayoutParams params = activity.getWindow()
                    .getAttributes();
            if (params.softInputMode != softInputMode) {
                params.softInputMode = softInputMode;
                activity.getWindow().setAttributes(params);
            }
        }
    }
}
