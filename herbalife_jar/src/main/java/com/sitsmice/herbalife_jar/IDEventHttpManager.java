package com.sitsmice.herbalife_jar;

import com.le.net.HttpManagerWithCookie;
import com.le.net.HttpResponse;

/**
 * Created by sahara on 2016/4/1.
 */
public class IDEventHttpManager extends HttpManagerWithCookie{

    @Override
    public boolean responseFilter(HttpResponse httpResponse) throws Exception {
        super.responseFilter(httpResponse);
        if (httpResponse.statusCode==401){
                throw new Exception(httpResponse.getString());
        }else if (httpResponse.statusCode >= 300){
            throw new Exception(httpResponse.getString());
        }
        return true;
    }

    @Override
    public void error(Exception e) {
        MLog.e("test","Http error:" + e.getMessage());
        if (e.getMessage()==null){
            JarApplication.showToast("error：空指针");
            return;
        }
        if (e.getMessage().equals("ID不存在")){
            JarApplication.showToast(e.getMessage());
            return;
        }
        if (e.getMessage().equals("Unable to resolve host \"app.idevent.cn\": No address associated with hostname")){
            JarApplication.showToast("无网络，请重试！");
            return;
        }
        try{
        ErrorMsg errorMsg = JarApplication.JsonToObject(e.getMessage(), ErrorMsg.class);
        JarApplication.showToast(errorMsg.message);}
        catch (Exception e1){
//                throw new RuntimeException(e1);
            JarApplication.showToast(e.getMessage()+"（isn`t error message of json）");
        }
    }
}
