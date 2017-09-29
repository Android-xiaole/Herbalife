package com.le.net_thread;

/**
 * Created by peng on 2015/8/27.
 *   
 */
public abstract class IHttpHandler {
	public BaseHttpManager httpManager;
	public HttpRequest req;
	public HttpResponse res;
    public abstract void start() throws Exception;
    public abstract void done() throws Exception;
    public void error(Exception e){}
}
