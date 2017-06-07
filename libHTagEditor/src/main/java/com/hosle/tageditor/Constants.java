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

package com.hosle.tageditor;

/**
 * Created by tanjiahao on 17/6/1.
 */

public class Constants {
    public static String TAG_FLAG = "#";
    public static char TAG_FLAG_CHAR = '#';

    public static void setTagFlag(char tagFlag) {
        TAG_FLAG = String.valueOf(tagFlag);
        TAG_FLAG_CHAR = tagFlag;
    }
}
