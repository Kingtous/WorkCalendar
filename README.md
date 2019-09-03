### 工作日历(WorkCalendar)

> 这个项目主要是为了方便轮班制用户查询自己哪天上班哪天下班。
>
> 母上、父上大人应该会很开心吧(至少对于我的他们来说是这样)

- 使用QMUI框架构建界面
- 默认4班周期
  - 可在TimeUtil.java中更改需求

- 实时时间显示
- 备忘录功能



**Demo**

详见[Release](https://github.com/Kingtous/WorkCalendar/releases)



开源项目支持：

```
//calendar
implementation 'com.haibin:calendarview:3.6.6'
implementation 'com.qmuiteam:qmui:1.4.0'

//qmui
def qmui_arch_version = '0.6.1'
implementation "com.qmuiteam:arch:$qmui_arch_version"
annotationProcessor "com.qmuiteam:arch-compiler:$qmui_arch_version" // use annotationProcessor if java

//bugly
implementation "com.tencent.bugly:crashreport:latest.release"

// butterknife
implementation 'com.jakewharton:butterknife:8.8.1'
annotationProcessor 'com.jakewharton:butterknife-compiler:8.8.1'

// datepicker
compile 'cn.aigestudio.datepicker:DatePicker:2.2.0'

compile 'com.stephentuso:welcome:1.4.1'
```