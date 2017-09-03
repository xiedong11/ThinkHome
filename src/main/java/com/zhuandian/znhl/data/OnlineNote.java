package com.zhuandian.znhl.data;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zhuandian.znhl.R;
import com.zhuandian.znhl.been.CloudNoteBeen;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by 谢栋 on 2016/8/6.
 */
public class OnlineNote extends Activity implements View.OnClickListener {


    private SweetAlertDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onlinenote);

        ((TextView) findViewById(R.id.toolbar_title)).setText("云端记事");  //设置toolbar标题

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

        //初始化SDK
        Bmob.initialize(this, "19b4c91a0e602de758817ca0a3b5765c");

        initView();
    }

    private void initView() {
//        findViewById(R.id.clear).setOnClickListener(this);
        findViewById(R.id.commit).setOnClickListener(this);
        findViewById(R.id.scandata).setOnClickListener(this);
        findViewById(R.id.edcontent).setOnClickListener(this);
        findViewById(R.id.content).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.commit:
                saveData();
                break;

            case R.id.scandata:
                scanData();
                break;
//            case R.id.clear :
//                clearData();


        }

    }

    /**
     * 删除数据库中的所有数据
     */
    private void clearData() {
        BmobBatch batch =new BmobBatch();



//批量删除
        List<BmobObject> persons2 = new ArrayList<BmobObject>();
        CloudNoteBeen p2 = new CloudNoteBeen();
        p2.delete();
        p2.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {

            }
        });
////        p2.setObjectId("9af452ebd");
////        persons2.add(p2);
//        batch.deleteBatch(persons2);
////执行批量操作
//        batch.doBatch(new QueryListListener<BatchResult>(){
//
//            @Override
//            public void done(List<BatchResult> results, BmobException e) {
//                if(e==null){
//                    //返回结果的results和上面提交的顺序是一样的，请一一对应
//                    for(int i=0;i<results.size();i++){
//                        BatchResult result= results.get(i);
//                        if(result.isSuccess()){//只有批量添加才返回objectId
////                            log("第"+i+"个成功："+result.getObjectId()+","+result.getUpdatedAt());
//                        }else{
//                            BmobException error= result.getError();
////                            log("第"+i+"个失败："+error.getErrorCode()+","+error.getMessage());
//                        }
//                    }
//                }else{
//                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
//                }
//            }
//        });
    }

    /**
     * 从云数据库获取数据，显示在textview上
     */
    private void scanData() {

        pDialog = new SweetAlertDialog(OnlineNote.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("正在同步数据到本地...");
        pDialog.setCancelable(false);
        pDialog.show();
        CloudNoteBeen cloudNoteBeen = new CloudNoteBeen();
        BmobQuery<CloudNoteBeen> query = new BmobQuery<CloudNoteBeen>();
        //查询playerName叫“比目”的数据
        //        query.addWhereEqualTo("playerName", "比目");
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);
        //执行查询方法
        query.findObjects(new FindListener<CloudNoteBeen>() {
            @Override
            public void done(List<CloudNoteBeen> object, BmobException e) {

                String s = "";

                if (e == null) {
//                    toast("查询成功：共"+object.size()+"条数据。");
                    for (CloudNoteBeen cloudNoteBeen : object) {

                        String content = cloudNoteBeen.getContent();
                        //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
                        String time = cloudNoteBeen.getCreatedAt();

                        s += time + "\n" + content + "\n\n\n";
                        Log.i("xie", s);
//                        ((TextView)findViewById(R.id.content)).setText(s);
                    }
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
                Log.i("xied", s);
                ((TextView) findViewById(R.id.content)).setText(s);
                pDialog.cancel();
                new SweetAlertDialog(OnlineNote.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("好的嘛")
                        .setContentText("数据同步成功！")
                        .show();
            }
        });


    }

    /**
     * 存入用户输入的内容到数据库
     */
    private void saveData() {

        pDialog = new SweetAlertDialog(OnlineNote.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("正在同步本地数据到云端...");
        pDialog.setCancelable(false);
        pDialog.show();
        String content = ((EditText) findViewById(R.id.edcontent)).getText().toString();

        CloudNoteBeen cloudNoteBeen = new CloudNoteBeen();
        cloudNoteBeen.setContent(content);


        cloudNoteBeen.save(new SaveListener<String>() {



            @Override
            public void done(String s, BmobException e) {
                if (e == null) {

                    pDialog.cancel();
                    new SweetAlertDialog(OnlineNote.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("好的嘛")
                            .setContentText("云端数据同步完成 ！")
                            .show();
                    ((TextView) findViewById(R.id.content)).setText("");
                    ((EditText) findViewById(R.id.edcontent)).setText("");
                }

            }
        });
    }
}
