# A Tag Editor with A Smooth Switching Scheme for Keyboard and Panel in Android

>[中文文档](./README_zh.md)

---

This Repository is to solve the flashing problem when switching the keyboard and the customized panel. In addition, an editor that handles the keyword tag is provided.

>Including three main functions:
>
>1. Eliminating the screen frashing when the customized panel and the keyboard switch between each other. The layout of a control bar above the soft keyboard is not affected in the same time.
>2. Hidding the keyboard and the panel by touching outside the `EditText`.
>3. Adding a new function to `EditText` -- the ability to handle the tags, including automatically highlight, adding by click, adding by typing and delete.

## Effects

![][effect_1_gif]![][effect_2_gif]

## Key Features

* Smooth the switch between the keyboard and the panel while the control bar staying at the same position on the screen. The keys are :
	1. Calculate the height of soft keyboard. Refer to [LayoutChangeListener.java][LayoutChangeListener_link].
	2. Switch the soft input mode between ADJUST_ NOTHING and ADJUST_RESIZE. Refer to [LayoutHandlerImp.java][LayoutHandlerImp_link].

* Add a new function that handling tags to the EditText. The keys are :
	1. Overwrite the delete event in `OnKeyListener`interface in order to delete tag by tag instead of by letter. Refer to [RichOnKeyListener.java][RichOnKeyListener_link].
	2. Overwrite `TextWatcher` in order to add and highlight each input keyword as a tag. Refer to [RichTextWatcher.java][RichTextWatcher_link].  
	
## Demo

This demo shows the critical steps when imports the module`libHTagEditor` in your host project.

1. Inflate layout and obtain element references.
2. Create data and item layout of tags.
3. Create all the handler instances. Firstly, to create the main handler`LayoutHandlerImp`. Secondly, to register the listener`RichTextWatcher` for EditText. Thirdly, to register the listener`SwitcherChangedListener` for switch button. Finally, to register the layout listener `LayoutChangeListener` for the global layout.
4. Unregister the layout listener when Activity is destroied.

## License

```

Copyright (C) 2017. Henry Tam (Hosle) 

Contact: hosle@163.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and limitations under the License.
```
 [effect_1_gif]: ./sourcepic/effect_pic1.gif
 [effect_2_gif]: ./sourcepic/effect_pic2.gif
 [LayoutChangeListener_link]: ./libHTagEditor/src/main/java/com/hosle/tageditor/listener/LayoutChangeListener.java
 [LayoutHandlerImp_link]: ./libHTagEditor/src/main/java/com/hosle/tageditor/handler/LayoutHandlerImp.java
 [RichOnKeyListener_link]:./libHTagEditor/src/main/java/com/hosle/tageditor/listener/RichOnKeyListener.java 
 [RichTextWatcher_link]: ./libHTagEditor/src/main/java/com/hosle/tageditor/listener/RichTextWatcher.java 