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

package com.hosle.tageditordemo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hosle.tageditor.Constants;
import com.hosle.tageditor.Utils;
import com.hosle.tageditor.handler.LayoutHandlerImp;
import com.hosle.tageditor.listener.LayoutChangeListener;
import com.hosle.tageditor.listener.RichOnKeyListener;
import com.hosle.tageditor.listener.RichTextWatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by tanjiahao on 17/5/26.
 */
public class MainActivity extends Activity {

    private LinearLayout rootLayout;
    private EditText mRemarksEditText;
    private LinearLayout tagFullContainer;
    private CheckBox switcher;
    private LinearLayout tagBox;
    private LinearLayout tagBarContent;

    private List<String> originalTagList = new ArrayList<>();
    private Set<String> selectedTagSet = new HashSet<>();

    private ViewTreeObserver.OnGlobalLayoutListener mLayoutChangeListener;
    private LayoutHandlerImp layoutHandlerImp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
        //mock data for demo
        createMockData();

        createHandler();
        setUpEditText();
        setUpSwitcher();
        setUpLayoutListener();
    }

    private void bindViews() {
        mRemarksEditText = (EditText) findViewById(R.id.remarks_inputer);
        rootLayout = (LinearLayout) findViewById(R.id.activity_main);
        tagFullContainer = (LinearLayout) findViewById(R.id.tab_full_container);
        tagBox = (LinearLayout) findViewById(R.id.tab_box);
        tagBarContent = (LinearLayout) findViewById(R.id.tab_bar_scroll_container);
        switcher = (CheckBox) findViewById(R.id.switcher);
    }

    private void createHandler() {
        layoutHandlerImp = new LayoutHandlerImp(this, tagFullContainer, switcher, tagBox);
    }

    private void setUpEditText() {
        layoutHandlerImp.attachEditText(mRemarksEditText, new RichOnKeyListener(selectedTagSet),
                new RichTextWatcher(originalTagList,
                        Color.RED,
                        new RichTextWatcher.CallbackKeyword() {
                            @Override
                            public void onUpdate(Set<String> keywordSet) {
                                selectedTagSet.clear();
                                selectedTagSet.addAll(keywordSet);
                            }
                        }));
    }

    private void setUpSwitcher() {
        switcher.setOnCheckedChangeListener(layoutHandlerImp.createSwitcherChangedListener());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        layoutHandlerImp.dispatchTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    private void setUpLayoutListener() {
        mLayoutChangeListener = new LayoutChangeListener(rootLayout, layoutHandlerImp.createKeyboardListener());

        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(mLayoutChangeListener);
    }


    //=========================== mock data for testing=========================
    private void createMockData() {
        String[] testDataSet = new String[]{"Apple", "Banana", "Orange", "Cola", "Sugar","Watermelon"};
        originalTagList = Arrays.asList(testDataSet);
        setTagData(originalTagList);
    }

    private void setTagData(List<String> nameList) {
        LayoutInflater inflater = getLayoutInflater();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(Utils.dip2px(this, 15), 0, Utils.dip2px(this, 10), 0);
        LinearLayout.LayoutParams paramsFull = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsFull.setMargins(Utils.dip2px(this, 15), 0, 0,Utils.dip2px(this, 10));

        View.OnClickListener onTagClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tagString = (String) v.getTag();
                Editable editable = mRemarksEditText.getEditableText();
                int index = mRemarksEditText.getSelectionStart();
                SpannableStringBuilder spanny = new SpannableStringBuilder();
                spanny.append(tagString);

                if (index < 0 || index >= editable.length()) {
                    editable.append(spanny);
                } else {
                    //insert the words in where the cursor is.
                    editable.insert(index, spanny);
                }
            }
        };
        for (String itemTag : nameList) {
            String formatTag = Constants.TAG_FLAG + itemTag + Constants.TAG_FLAG;
            TextView tag = (TextView) inflater.inflate(R.layout.tag_item, null);
            tag.setText(itemTag);
            tag.setTag(formatTag);
            tag.setOnClickListener(onTagClickListener);
            tagBarContent.addView(tag, params);

            TextView tag2 = (TextView) inflater.inflate(R.layout.tag_item, null);
            tag2.setText(itemTag);
            tag2.setTag(formatTag);
            tag2.setOnClickListener(onTagClickListener);
            tagFullContainer.addView(tag2, paramsFull);

        }
    }

    @Override
    protected void onDestroy() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(mLayoutChangeListener);
        } else {
            getWindow().getDecorView().getViewTreeObserver().removeGlobalOnLayoutListener(mLayoutChangeListener);
        }
        super.onDestroy();
    }
}
