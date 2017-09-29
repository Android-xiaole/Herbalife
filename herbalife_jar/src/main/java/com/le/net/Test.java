package com.le.net;

import com.sitsmice.herbalife_jar.IDEventHttpManager;
import com.sitsmice.herbalife_jar.MLog;

/**
 * Created by sahara on 2016/4/29.
 */
public class Test {

    public IDEventHttpManager httpManager = new IDEventHttpManager();

    public void net(){
        httpManager.makeRequest("url", false, new IHttpHandler() {
            @Override
            public void start() throws Exception {
//                req.writeForm();//写表单格式
//                req.writeJson();//写json
            }

            @Override
            public void done() throws Exception {
                MLog.e("test","网络请求返回："+res.getString());
            }

            @Override
            public void error(Exception e) throws Exception {//网络请求错误
                super.error(e);
            }
        });
    }
}
