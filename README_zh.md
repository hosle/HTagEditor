#Android支持标签的文本编辑器与软键盘与面板平滑切换的解决方案

>[README DOC](./README.md)

---

Android中消除软键盘与面板切换时的布局闪动以及提供支持关键词标签的富文本编辑器。

>主要实现三部分功能：

>1. 解决了键盘与扩展面板之间交替切换时布局的闪烁问题。于此同时，键盘上方的控制条布局保持不变。
>2. 点击`EditText`区域以外的地方，收起所有面板。
>3. 使`EditText`提供新功能，支持标签文案的高亮展示、点击添加、输入添加和整体删除。

##效果图
![][effect_1_gif]![][effect_2_gif]

##主要特点与实现关键点

* 控制条在屏幕位置不变的前提下，实现键盘和扩展面板的平滑切换，关键点如下：
  	1. 计算软键盘的高度。参见[LayoutChangeListener.java][LayoutChangeListener_link]。
  	2. 在ADJUST_ NOTHING和ADJUST_RESIZE两种之间切换软键盘的弹出模式。参见[LayoutHandlerImp.java][LayoutHandlerImp_link]。
 
* 在`EditText`中新增添加、删除等的标签编辑功能，关键点如下：
	1. 重写`OnKeyListener`接口的删除键监听方法，实现标签的整体删除，参见[RichOnKeyListener.java][RichOnKeyListener_link]。
	2. 重写`TextWatcher`接口，实现新增标签的匹配与高亮处理，参见[RichTextWatcher.java][RichTextWatcher_link]。

##Demo
Demo展示了引用Module`libHTagEditor`后，还需要在宿主工程完成的工作：

1. 实例化布局并获得元素的引用。
2. 创建扩展面板中标签的数据及单元布局。
3. 实力化处理类，包括实力化主处理类`LayoutHandlerImp`、注册文本编辑框监听器`RichTextWatcher`、注册键盘面板切换按钮事件`SwitcherChangedListener`以及注册布局监听器LayoutChangeListener。
4. 页面销毁时注销布局监听器。

##License
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