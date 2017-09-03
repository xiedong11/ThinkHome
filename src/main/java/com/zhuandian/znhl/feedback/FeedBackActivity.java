package com.zhuandian.znhl.feedback;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zhuandian.znhl.R;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * Created by 谢栋 on 2016/6/6.
 */
public class FeedBackActivity extends Activity{

    private EditText callEditText;
    private EditText feedbackeEditText;
    private Button submitbButton;
    private SweetAlertDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        ((TextView) findViewById(R.id.toolbar_title)).setText("反馈建议");  //设置toolbar标题

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

        Bmob.initialize(this,"19b4c91a0e602de758817ca0a3b5765c");

        callEditText= (EditText) findViewById(R.id.call);
        feedbackeEditText= (EditText) findViewById(R.id.feedback);
        submitbButton= (Button) findViewById(R.id.submit);

        submitbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String call=callEditText.getText().toString();
                String feedback=feedbackeEditText.getText().toString();

                if(call.equals("")||feedback.equals("")){

                    new SweetAlertDialog(FeedBackActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("请完善反馈信息")
                            .setContentText("请留下您的联系方式及宝贵意见，谢谢 !")
                            .show();
                    return;
                }else {
                    pDialog = new SweetAlertDialog(FeedBackActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("提交中...");
                    pDialog.setCancelable(false);
                    pDialog.show();
                }

                FeedBackZNHLObj feedBackObj = new FeedBackZNHLObj();

                feedBackObj.setCall(call);
                feedBackObj.setFeedback(feedback);

                feedBackObj.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if(e==null){

                        pDialog.cancel();
                        new SweetAlertDialog(FeedBackActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("反馈成功")
                                .setContentText("感谢您的宝贵意见，我们将会做的更好 ！")
                                .show();
                        }else{
                            pDialog.cancel();
                        new SweetAlertDialog(FeedBackActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("确保网路畅通 ？")
                                .setContentText("请重新提交您的反馈，谢谢 !")
                                .show();
                        }
                    }
                });
//                feedBackObj.save(FeedBackActivity.this,  new SaveListener() {
//                    @Override
//                    public void onSuccess() {
////                        Toast.makeText(FeedBackActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
//
//                        pDialog.cancel();
//                        new SweetAlertDialog(FeedBackActivity.this, SweetAlertDialog.SUCCESS_TYPE)
//                                .setTitleText("反馈成功")
//                                .setContentText("感谢您的宝贵意见，我们将会做的更好 ！")
//                                .show();
//
//                    }
//
//                    @Override
//                    public void onFailure(int i, String s) {
//
////                        Toast.makeText(FeedBackActivity.this,"提交失败",Toast.LENGTH_SHORT).show();
//                        pDialog.cancel();
//                        new SweetAlertDialog(FeedBackActivity.this, SweetAlertDialog.ERROR_TYPE)
//                                .setTitleText("确保网路畅通 ？")
//                                .setContentText("请重新提交您的反馈，谢谢 !")
//                                .show();
//                    }
//                });

            }
        });
    }
}
