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

import android.graphics.Rect;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

/**
 * Created by tanjiahao on 17/6/1.
 *
 * To calculate the height of keyboard
 * which equals the difference when root layout changed
 */

public class LayoutChangeListener implements ViewTreeObserver.OnGlobalLayoutListener {
    private ViewGroup rootLayout;
    private IKeyboardListener keyboardListener;

    private int lastHeightDiff = 0;

    public LayoutChangeListener(ViewGroup rootLayout, IKeyboardListener keyboardListener) {
        this.rootLayout = rootLayout;
        this.keyboardListener = keyboardListener;
    }

    @Override
    public void onGlobalLayout() {
        Rect r = new Rect();
        rootLayout.getWindowVisibleDisplayFrame(r);

        int screenHeight = rootLayout.getRootView().getHeight();
        int heightDiff = screenHeight - r.bottom;

        if (heightDiff != getLastHeightDiff()) {
            setLastHeightDiff(heightDiff);
            if (heightDiff > screenHeight / 3) {
                keyboardListener.onDisplay(heightDiff);
            } else {
                keyboardListener.onhide();
            }
        }
    }

    private int getLastHeightDiff() {
        return lastHeightDiff;
    }

    private void setLastHeightDiff(int lastHeightDiff) {
        this.lastHeightDiff = lastHeightDiff;
    }
}
