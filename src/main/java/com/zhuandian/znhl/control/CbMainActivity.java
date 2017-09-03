package com.zhuandian.znhl.control;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import com.zhuandian.znhl.R;

public class CbMainActivity extends Activity implements  android.view.GestureDetector.OnGestureListener
{
    //定义手势检测器实例
    GestureDetector detector;
     
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.light);
        //创建手势检测器
        detector = new GestureDetector(this,this); 
    }
 
    //将该activity上的触碰事件交给GestureDetector处理
    public boolean onTouchEvent(MotionEvent me){
        return detector.onTouchEvent(me);
    }
     
    @Override
    public boolean onDown(MotionEvent arg0) {
        return false;
    }
 
    /**
     * 滑屏监测
     * 
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
            float velocityY) {
        float minMove = 120;         //最小滑动距离
        float minVelocity = 200;      //最小滑动速度
        float beginX = e1.getX();
        float endX = e2.getX();
        float beginY = e1.getY();
        float endY = e2.getY();

        if(beginX-endX>minMove&&Math.abs(velocityX)>minVelocity){   //左滑
            Log.i("xie",(int)velocityX+"左滑");
            Toast.makeText(this, (int)velocityX + "左滑", Toast.LENGTH_SHORT).show();
        }else if(endX-beginX>minMove&&Math.abs(velocityX)>minVelocity){   //右滑
            Log.i("xie",(int)velocityX+"右滑");
            Toast.makeText(this,velocityX+"右滑",Toast.LENGTH_SHORT).show();
        }else if(beginY-endY>minMove&&Math.abs(velocityY)>minVelocity){   //上滑
            Log.i("xie",(int)velocityX+"上滑");
            Toast.makeText(this,velocityX+"上滑",Toast.LENGTH_SHORT).show();
        }else if(endY-beginY>minMove&&Math.abs(velocityY)>minVelocity){   //下滑
            Log.i("xie",(int)velocityX+"下滑");
            Toast.makeText(this,velocityX+"下滑",Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    @Override
    public void onShowPress(MotionEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onSingleTapUp(MotionEvent arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onLongPress(MotionEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float velocityX,
            float velocityY) {
     
        return false;
    }
 
}