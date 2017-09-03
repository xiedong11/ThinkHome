package com.zhuandian.znhl.been;

import cn.bmob.v3.BmobObject;

/**
 * Created by 谢栋 on 2016/8/11.
 */
public class CloudNoteBeen extends BmobObject{

    private String title;
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
