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

package com.hosle.tageditor.listener;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.hosle.tageditor.handler.ILayoutHandler;
import com.hosle.tageditor.handler.ViewInterface;

/**
 * Created by tanjiahao on 17/6/2.
 * Define what to do in case of the keyboard is shown or hidden.
 */

public class KeyboardListenerImp implements IKeyboardListener {

    private Activity activity;
    private ViewInterface viewInterface;
    private ILayoutHandler handler;
    private View contentPanel;
    private int keyboardHeight = 0;

    public KeyboardListenerImp(Activity activity, View contentPanel,ViewInterface viewInterface, ILayoutHandler handler) {
        this.activity = activity;
        this.viewInterface = viewInterface;
        this.contentPanel = contentPanel;
        this.handler = handler;
    }

    @Override
    public void onDisplay(int keyboardheight) {
        if (0 == keyboardHeight) {
            keyboardHeight = keyboardheight;
        }
        if (handler.isHideInputForContentPanel()) {
            handler.updateSoftInputMethod(activity, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }

        ViewGroup.LayoutParams params = contentPanel.getLayoutParams();
        if (params.height < keyboardHeight) {
            params.height = keyboardHeight;
            contentPanel.setLayoutParams(params);
        }
        viewInterface.updateViewStatus(true,false,false);
        handler.setHideInputForContentPanel(false);
    }

    @Override
    public void onhide() {
        if (handler.isHideInputForContentPanel()) {
            viewInterface.updateViewStatus(true,true,true);
        }else {
            viewInterface.updateViewStatus(false,false,false);
        }
    }

}
