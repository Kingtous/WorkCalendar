package com.kingtous.workcalendar.task;

import android.os.AsyncTask;

import com.kingtous.workcalendar.tools.TimeUtil;

public class TimeRefreshTask extends AsyncTask<Void,Void,Void> {


    public interface OnTimeChangedListener {
        void OnCurrentTimeChanged(String currentTime);
    }

    private OnTimeChangedListener mOnTimeChangeListener;

    public void setOnTimeChangedListener(OnTimeChangedListener listener){
        mOnTimeChangeListener=listener;
    }

    public String getTime(){
        return TimeUtil.getTimeString();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        while (true){
            //没有设置Listener就停止，没用
            if (mOnTimeChangeListener == null){
                return null;
            }
            mOnTimeChangeListener.OnCurrentTimeChanged(getTime());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return null;
            }
        }
    }
}
