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

import android.graphics.Color;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;

import com.hosle.tageditor.Constants;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by tanjiahao on 17/5/24.
 * An implementation of TextWatcher,
 * in order to
 * change the tags' color
 * update the list of selected tag in callback
 */

public class RichTextWatcher implements TextWatcher {
    private static final int DEFAULT_TXT_COLOR = Color.parseColor("#333333");

    private List<String> nameList;
    private Set<String> resultKeyWordSet = new HashSet<>();
    private int textColor = DEFAULT_TXT_COLOR;
    private CallbackKeyword callback;

    public RichTextWatcher(List<String> tagList, int keywordColor, CallbackKeyword callback) {
        this.nameList = tagList;
        this.textColor = keywordColor;
        this.callback = callback;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        SpannableStringBuilder spannableString = (SpannableStringBuilder) s;
        int startPos = -1, endPos;
        char curChar;
        String keyword;
        setUpColorSpan(s);
        resultKeyWordSet.clear();
        for (int i = 0; i < s.length(); i++) {
            curChar = s.charAt(i);
            endPos = i ;
            if (curChar == Constants.TAG_FLAG_CHAR) {
                keyword = spannableString.subSequence(startPos == -1 ? i : startPos + 1, endPos).toString();
                if (isFlagChanged(startPos, i)
                        && nameList.contains(keyword)) {
                    s.setSpan(new ForegroundColorSpan(textColor), startPos, endPos + 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    resultKeyWordSet.add(keyword);
                }
                startPos = i;
            }
        }
        if(null!=callback){
            callback.onUpdate(resultKeyWordSet);
        }

    }

    private boolean isFlagChanged(int startPos,int curPos){
        return startPos < curPos && startPos > -1;
    }

    private void setUpColorSpan(Editable editable){
        editable.setSpan(new ForegroundColorSpan(DEFAULT_TXT_COLOR),0,editable.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
    }
    public int getTextColor() {
        return textColor;
    }

    public interface CallbackKeyword{
        void onUpdate(Set<String> keywordSet);
    }

}
