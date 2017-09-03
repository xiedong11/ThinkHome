package com.zhuandian.znhl.feedback;

import cn.bmob.v3.BmobObject;

/**
 * Created by 谢栋 on 2016/6/6.
 */


public class FeedBackZNHLObj extends BmobObject{

    private String call;
    private String feedback;

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getCall() {
        return call;
    }

    public void setCall(String call) {
        this.call = call;
    }
}
