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

import android.text.SpannableStringBuilder;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;


import com.hosle.tageditor.Constants;

import java.util.Set;


/**
 * Created by tanjiahao on 17/5/24.
 * Subscribe the delete key event.
 * Mainly to delete the whole tag inside the EditText string
 */

public class RichOnKeyListener implements View.OnKeyListener {

    private Set<String> tagSet;

    public RichOnKeyListener(Set<String> tagSet) {
        this.tagSet = tagSet;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        EditText editText;
        try {
            editText = (EditText) v;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {

            int selectedStart = editText.getSelectionStart();
            SpannableStringBuilder oriEditable = (SpannableStringBuilder) editText.getText();
            int tagLength;

            for (String tagString : tagSet) {
                String processText;
                int processPos;
                int tagPos = -1;
                String itemTag = Constants.TAG_FLAG + tagString + Constants.TAG_FLAG;

                do {
                    processText = oriEditable.subSequence(tagPos == -1 ? 0 : tagPos,oriEditable.length()).toString();
                    processPos = processText.indexOf(itemTag);
                    tagPos = tagPos == -1 ? processPos : tagPos + processPos;
                    tagLength = itemTag.length();

                    if (processPos != -1) {
                        if (selectedStart != 0 && selectedStart > tagPos && selectedStart <= tagPos + tagLength) {
                            StringBuilder newString = new StringBuilder();
                            newString.append(oriEditable.subSequence(0, tagPos))
                                    .append(oriEditable.subSequence(tagPos + tagLength, oriEditable.length()));
                            editText.setText(newString);
                            editText.setSelection(tagPos);
                            return true;
                        } else {
                            tagPos += tagLength;
                        }
                    }
                } while (processPos != -1 && processText.length() >= tagLength);
            }
        }
        return false;
    }
}
