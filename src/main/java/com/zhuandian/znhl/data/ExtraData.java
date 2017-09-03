package com.zhuandian.znhl.data;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.zhuandian.znhl.R;

/**
 * Created by 谢栋 on 2016/6/26.
 */
public class ExtraData extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.extra_data);

        ((TextView) findViewById(R.id.toolbar_title)).setText("有毒气体");  //设置toolbar标题

        //设置左上角按钮并绑定监听事件
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //设置左上角按钮并绑定点击事件
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
