package com.le.net;

import android.os.Handler;
import android.os.Message;

/**
 * Created by peng on 2015/8/27.
 *   
 */
public abstract class IHttpHandler {
	public BaseHttpManager httpManager;
	
	public Handler hander = new Handler() {
		public void handleMessage(Message msg) {
			try{
				switch(msg.what) {
				case 1:
					// start
					IHttpHandler.this.start();
					remote.sendEmptyMessage(1);
					break;
				case 2:
					// done
					if (httpManager.responseFilter(res)) {
						IHttpHandler.this.done();
					}else {
						throw new Exception("未登录");
					}

					remote.sendEmptyMessage(2);
					break;
				case 3:
					// error from manager
//					IHttpHandler.this.error((Exception)msg.obj);
					throw (Exception)msg.obj;
				}
			}catch(Exception e) {
				try {
					IHttpHandler.this.error(e);
				} catch (Exception e1) {
					Message msg1 = Message.obtain();
					msg1.obj = e1;
					msg1.what = 3;
					remote.sendMessage(msg1);
				}
			}
		};
	};
	public Handler remote;
	public HttpRequest req;
	public HttpResponse res;
    public abstract void start() throws Exception;
    public abstract void done() throws Exception;
    public void error(Exception e) throws Exception {
    	throw e;
    }
}
