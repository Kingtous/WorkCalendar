package com.kingtous.workcalendar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.kingtous.workcalendar.activity.mWelcomeActivity;
import com.kingtous.workcalendar.base.BaseActivity;
import com.kingtous.workcalendar.task.TimeRefreshTask;
import com.kingtous.workcalendar.tools.CodeDef;
import com.kingtous.workcalendar.tools.TimeUtil;
import com.qmuiteam.qmui.alpha.QMUIAlphaTextView;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundLinearLayout;
import com.stephentuso.welcome.WelcomeHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.topbar)
    QMUITopBar topbar;
    @BindView(R.id.calendar)
    CalendarView calendar;
    @BindView(R.id.msg_main_text)
    QMUIAlphaTextView msgMainText;

    TimeRefreshTask timeRefreshTask;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CodeDef.UPDATE_TITLE:
                    if (msgMainText != null) {
                        topbar.setSubTitle((String) msg.obj);
                    }
            }
        }
    };
    @BindView(R.id.btn_change_date)
    QMUIRoundButton btnChangeYear;
    @BindView(R.id.btn_back_date)
    QMUIRoundButton btnBackDate;
    @BindView(R.id.ll_calendar_pannel)
    QMUIRoundLinearLayout llCalendarPannel;

    Button btn_setting;

    WelcomeHelper welcomeScreen;
    @BindView(R.id.msgWorkStatus)
    QMUIAlphaTextView msgWorkStatus;
    @BindView(R.id.msg_sticky_notes)
    QMUIAlphaTextView msgStickyNotes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        welcomeScreen = new WelcomeHelper(this, mWelcomeActivity.class);
        startActivity(new Intent(this, mWelcomeActivity.class));
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initEvent();
    }

    @Override
    protected void initView() {
        super.initView();
        initTopBar(getString(R.string.app_name));
        topbar.addRightTextButton("重新设置", CodeDef.SETTINGS);
        btn_setting = (Button) findViewById(CodeDef.SETTINGS);
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QMUIDialog.MessageDialogBuilder builder=new QMUIDialog.MessageDialogBuilder(MainActivity.this);
                builder.setTitle("重新设置工作日");
                builder.setMessage("请确认是否重新设置工作日(不会清除备忘录)？");
                builder.addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(final QMUIDialog dialog, int index) {
                        //先清除原先的设置
                        SharedPreferences sharedPreferences=getSharedPreferences(CodeDef.WORK_SETTING,Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.remove(CodeDef.YEAR);
                        editor.remove(CodeDef.MONTH);
                        editor.remove(CodeDef.DAY);
                        editor.apply();
                        final QMUITipDialog tipDialog= new QMUITipDialog.Builder(MainActivity.this)
                                .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                                .setTipWord("重置成功").create();
                        tipDialog.show();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                tipDialog.dismiss();
                                dialog.dismiss();
                                quickSetup();
                            }
                        },1500);
                    }
                });
                builder.addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
        calendar.setWeekStarWithMon();
        msgMainText.setText(TimeUtil.getNowTime());
        msgMainText.setChangeAlphaWhenPress(true);
        String year = String.valueOf(calendar.getCurYear());
        String month = String.valueOf(calendar.getCurMonth());
        String day = String.valueOf(calendar.getCurDay());
        msgMainText.setText(TimeUtil.getChineseYMD(year, month, day));
        msgWorkStatus.setText(TimeUtil.getWorkStatus(this,year+'-'+month+'-'+day));
    }

    void quickSetup(){
        QMUIDialog.MessageDialogBuilder builder=new QMUIDialog.MessageDialogBuilder(this);
        builder.setTitle(R.string.msg_quick_setting);
        builder.setMessage(R.string.msg_force_setting);
        builder.setCancelable(false);
        builder.setCanceledOnTouchOutside(false);
        builder.addAction("确定", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                dialog.dismiss();
                querySunWorkTime();
            }
        });
        builder.addAction("退出", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                finish();
            }
        });
        builder.create().show();
    }



    void querySunWorkTime(){
        final QMUIDialog.CheckableDialogBuilder builder=new QMUIDialog.CheckableDialogBuilder(this);
        builder.setTitle("任选本月上白班的日子");
        int maxdays=TimeUtil.getMonthOfDay(calendar.getCurYear(),calendar.getCurMonth());
        ArrayList<String> day_index=new ArrayList<>();
        for (int i=1;i<=maxdays;i++){
            day_index.add(String.valueOf(i));
        }
        final String[] day_str_index = day_index.toArray(new String[day_index.size()]);
        builder.addItems(day_str_index, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setCancelable(false);
        builder.setCanceledOnTouchOutside(false);
        builder.addAction("确定", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(final QMUIDialog dialog, int index) {
                int day = Integer.parseInt(day_str_index[builder.getCheckedIndex()]);
                SharedPreferences sharedPreferences=getSharedPreferences(CodeDef.WORK_SETTING,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putInt(CodeDef.YEAR,calendar.getCurYear());
                editor.putInt(CodeDef.MONTH,calendar.getCurMonth());
                editor.putInt(CodeDef.DAY,day);
                editor.apply();
                updateDate(calendar.getCurYear(),calendar.getCurMonth(),calendar.getCurDay());
                final QMUITipDialog tipDialog= new QMUITipDialog.Builder(MainActivity.this)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                        .setTipWord("保存成功").create();
                tipDialog.show();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tipDialog.dismiss();
                        dialog.dismiss();
                    }
                },1500);
            }
        });
        builder.addAction("退出", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                finish();
            }
        });
        builder.create().show();
    }

    void updateDate(int _year,int _month,int _day){
        // 注意，不改变日历！
        String year = String.format("%02d",_year);
        String month = String.format("%02d",_month);
        String day = String.format("%02d",_day);
        msgMainText.setText(TimeUtil.getChineseYMD(year, month, day));
        msgWorkStatus.setText(TimeUtil.getWorkStatus(MainActivity.this,year+'-'+month+'-'+day));
        SharedPreferences sharedPreferences=getSharedPreferences(CodeDef.WORK_SETTING, Context.MODE_PRIVATE);
        sharedPreferences.getString(year+'-'+month+'-'+day,getString(R.string.NO_SETTING));
        msgStickyNotes.setText(sharedPreferences.getString(year+'-'+month+'-'+day,getString(R.string.NO_CONTENT)));
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        calendar.setOnCalendarSelectListener(new CalendarView.OnCalendarSelectListener() {
            @Override
            public void onCalendarOutOfRange(Calendar calendar) {

            }

            @Override
            public void onCalendarSelect(Calendar calendar, boolean isClick) {
                updateDate(calendar.getYear(),calendar.getMonth(),calendar.getDay());
            }
        });
        timeRefreshTask = new TimeRefreshTask();
        timeRefreshTask.setOnTimeChangedListener(new TimeRefreshTask.OnTimeChangedListener() {
            @Override
            public void OnCurrentTimeChanged(String currentTime) {
                Message msg = new Message();
                msg.what = CodeDef.UPDATE_TITLE;
                msg.obj = currentTime;
                mHandler.sendMessage(msg);
//                Log.d("时间：", "发送更新至Handler");
            }
        });

        btnBackDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.scrollToCurrent(true);
            }
        });

        btnChangeYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final QMUIDialog.EditTextDialogBuilder builder=new QMUIDialog.EditTextDialogBuilder(MainActivity.this);
                builder.setTitle("请输入年份");
                builder.setDefaultText(String.valueOf(calendar.getCurYear()));
                builder.addAction("确定", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                try {
                                    int year=Integer.parseInt(builder.getEditText().getText().toString());
                                    if (year>=2000 && year <=2100){
                                        calendar.scrollToCalendar(year,1,1,true);
                                        // year-01-01
                                        updateDate(year,1,1);
                                        final QMUITipDialog tipDialog=new QMUITipDialog.Builder(MainActivity.this)
                                                .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                                                .setTipWord("修改成功").create();
                                        tipDialog.show();
                                        mHandler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                tipDialog.dismiss();
                                            }
                                        },1500);
                                    }
                                    else {
                                        throw new NumberFormatException("年份只支持2000至2100");
                                    }
                                    dialog.dismiss();
                                }catch (NumberFormatException e){
                                    final QMUITipDialog tipDialog=new QMUITipDialog.Builder(MainActivity.this)
                                            .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                                            .setTipWord(e.getMessage()).create();
                                    tipDialog.show();
                                    mHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            tipDialog.dismiss();
                                        }
                                    },1500);
                                }
                            }
                        });
                builder.addAction("取消", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        });
                QMUIDialog dialog=builder.create();
                dialog.show();
            }
        });

        // 备忘录
        msgStickyNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final QMUIDialog.EditTextDialogBuilder builder =new QMUIDialog.EditTextDialogBuilder(MainActivity.this);
                builder.setTitle("请输入文本");
                builder.addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        String content = builder.getEditText().getText().toString();
                        SharedPreferences sharedPreferences=getSharedPreferences(CodeDef.WORK_SETTING,Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        String content_index = String.format("%02d",calendar.getSelectedCalendar().getYear())+'-'+String.format("%02d",calendar.getSelectedCalendar().getMonth())+'-'+String.format("%02d",calendar.getSelectedCalendar().getDay());

                        QMUITipDialog.Builder tipBuilder= new QMUITipDialog.Builder(MainActivity.this);
                        if (content.equals("")){
                            //删除
                            editor.remove(content_index);
                            tipBuilder.setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO);
                            tipBuilder.setTipWord("已删除记录");
                        }
                        else {
                            editor.putString(content_index,content);
                            tipBuilder.setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS);
                            tipBuilder.setTipWord("添加成功");
                        }
                        editor.apply();
                        dialog.dismiss();
                        updateDate(calendar.getSelectedCalendar().getYear(),calendar.getSelectedCalendar().getMonth(),calendar.getSelectedCalendar().getDay());
                        final QMUITipDialog tipDialog=tipBuilder.create();
                        tipDialog.show();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                tipDialog.dismiss();
                            }
                        },1500);
                    }
                });
                builder.addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                });
                QMUIDialog dialog=builder.create();
                builder.getEditText().setText(getSharedPreferences(CodeDef.WORK_SETTING,Context.MODE_PRIVATE)
                        .getString(String.format("%02d",calendar.getSelectedCalendar().getYear())+'-'+String.format("%02d",calendar.getSelectedCalendar().getMonth())+'-'+String.format("%02d",calendar.getSelectedCalendar().getDay()),""));
                dialog.show();
            }
        });
        // 执行
        timeRefreshTask.execute();

        // 检查配置
        if (getSharedPreferences(CodeDef.WORK_SETTING,Context.MODE_PRIVATE).getInt(CodeDef.YEAR,0) == 0){
            quickSetup();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        welcomeScreen.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeRefreshTask.cancel(true);
    }

    private long firstTime=System.currentTimeMillis();

    @Override
    protected void doOnBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            firstTime = secondTime;
        } else {
            finish();
        }
    }
}
