package com.kingtous.workcalendar.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.kingtous.workcalendar.R;
import com.qmuiteam.qmui.arch.QMUIActivity;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;

public class BaseActivity extends QMUIActivity {

    protected QMUITopBar mTopBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QMUIStatusBarHelper.translucent(this);
    }

    protected void initView(){
        mTopBar = findViewById(R.id.topbar);
    }

    protected void initTopBar(String title) {
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_still, R.anim.slide_out_right);
            }
        });
        mTopBar.setTitle(title);
    }


    protected void initEvent(){

    }

    protected void gotoActivity(Class<?> cls){
        Intent intent=new Intent(this,cls);
        startActivity(intent);
    }


}
