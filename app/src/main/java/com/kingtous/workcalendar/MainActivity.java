package com.kingtous.workcalendar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.haibin.calendarview.CalendarView;
import com.kingtous.workcalendar.activity.BaseActivity;
import com.kingtous.workcalendar.tools.mMonthView;
import com.qmuiteam.qmui.arch.QMUIActivity;
import com.qmuiteam.qmui.widget.QMUITopBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.topbar)
    QMUITopBar topbar;
    @BindView(R.id.calendar)
    CalendarView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initEvent();
    }

    @Override
    protected void initView() {
        super.initView();
        initTopBar(getString(R.string.app_name));

        calendar.setMonthView(mMonthView.class);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
    }
}
