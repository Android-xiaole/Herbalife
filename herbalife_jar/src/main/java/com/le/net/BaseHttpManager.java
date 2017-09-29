package com.le.net;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.sitsmice.herbalife_jar.MLog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by le on 2015/8/27.
 *
 */
public abstract class BaseHttpManager {
	private static String zhPattern = "[\u4e00-\u9fa5]+";

	public static String encode(String str, String charset) throws UnsupportedEncodingException {
		Pattern p = Pattern.compile(zhPattern);
		Matcher m = p.matcher(str);
		StringBuffer b = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(b, URLEncoder.encode(m.group(0), charset));
		}
		m.appendTail(b);
		return b.toString();
	}
	
    public Thread makeRequest(final String url,boolean starNow,final IHttpHandler callback) {
      final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url1 = new URL(encode(url, "UTF-8"));
					MLog.e("test","接口地址："+url1.toString());
                    final HttpURLConnection conn = (HttpURLConnection)url1.openConnection();
					conn.setConnectTimeout(10000);//连接超时
					conn.setReadTimeout(10000);//读取超时
//                    final PipedInputStream pis0 = new PipedInputStream();
                    final ByteArrayOutputStream output = new ByteArrayOutputStream();
                    final HttpRequest httpRequest = new HttpRequest(output);
                    BaseHttpManager.this.requestFilter(httpRequest);

                    callback.req = httpRequest;
					if (Looper.myLooper()==null){
						Looper.prepare();
					}
//					MLog.e("test", "当前线程Looper对象:" + Looper.myLooper().toString() + ":" + url);
                    final Handler remote = new Handler(Looper.myLooper()) {
						Looper looper = Looper.myLooper();
                    	@Override
                    	public void handleMessage(Message msg) {
                    		// TODO Auto-generated method stub
							switch (msg.what) {
							
							case 1:
								// start
								int responseCode = 0;
								InputStream is = null;
								try {
		                    		sendHeaders(conn, httpRequest.headers);
		                            if (httpRequest.getLength() > 0) {
		                            	httpRequest.close();
		                            	sendBody(conn, httpRequest, output);
		                            } else {
		                            	conn.setRequestMethod(httpRequest.method);
		                            	conn.setDoOutput(false);
		                            }
		                            responseCode = conn.getResponseCode();
		                            if (responseCode < 300) {
		                            	is = conn.getInputStream();
		                            } else {
		                            	is = conn.getErrorStream();
		                            }
		                            wait0(responseCode, conn, callback, is);
	                    		}/*catch(IOException e) {
	                    			if(e.getMessage().equals("No authentication challenges found")) {
	                            		// 401
	                            		try {
	                            			BaseHttpManager.this.authenticationError();
	                            		}catch (Exception e1) {
	                            			BaseHttpManager.this.error(e1);
	                            		}
	                            		
	                            	} else {
	                            		
//	                            		BaseHttpManager.this.error(e);
//	                            		Message msg1 = Message.obtain();
//	                            		msg1.what = 3;
//		                    			msg1.obj = e;
//		                    			callback.hander.sendMessage(msg1);
		                    			try {
		                    				wait0(500, conn, callback, conn.getErrorStream());
		                    			} catch (Exception e1) {
		                    				Message msg2 = Message.obtain();
		                            		msg2.what = 3;
			                    			msg2.obj = e1;
			                    			callback.hander.sendMessage(msg2);
		                    			}
	                            	}
	                    		}*/catch (Exception e) {
//	                    			BaseHttpManager.this.error(e);
	                    			Message msg1 = Message.obtain();
                            		msg1.what = 3;
	                    			msg1.obj = e;
	                    			callback.hander.sendMessage(msg1);
	                    		}
								break;
							case 2:
								// done
								looper.quit();
//								looper = null;
								break;
							case 3:
								// error
								BaseHttpManager.this.error((Exception) msg.obj);
								looper.quit();
//								looper = null;
								break;
							default:
								break;
							}
                    	}
                    };
                    callback.remote = remote;
//                    remote.sendEmptyMessage(1);
                    callback.hander.sendEmptyMessage(1);
                    Looper.loop();
                }/*catch(IOException e) {
                	if(e.getMessage().equals("No authentication challenges found")) {
                		// 401
                		try {
                			BaseHttpManager.this.authenticationError();
                		}catch (Exception e1) {
                			BaseHttpManager.this.error(e1);
                		}
                		
                	} else {
                		BaseHttpManager.this.error(e);
                	}
                }*/

                catch (Exception e) {
//					if (e.getMessage().equals("InterruptedException")){
//						Thread.currentThread().stop();//终止线程
//						MLog.e("test","BaseHttpManager error:"+e.getMessage()+"-线程被终止");
//					}
                    BaseHttpManager.this.error(e);
                }
            }
        });
		if (starNow){
			thread.start();
		}
		return thread;
    }

    private void wait0(int responseCode, HttpURLConnection conn, IHttpHandler callback, InputStream inputStream) throws Exception {
    	//int responseCode = conn.getResponseCode();
        Map<String, List<String>> fs = conn.getHeaderFields();
        long len = -1;
        for (Map.Entry<String, List<String>> entry : fs.entrySet()) {
			if (entry.getKey() == null) {
				continue;
			}
			if (entry.getKey().equals("Content-Length")) {
				len = Long.parseLong(entry.getValue().get(0));
				break;
			}
		}
        PipedInputStream pis = null;
//        if(len != 0) {
        	pis = new PipedInputStream();
//        	InputStream inputStream = conn.getInputStream();
            PipedOutputStream pos = new PipedOutputStream(pis);
            
            HttpResponse res = new HttpResponse(responseCode, fs, pis);
//			BaseHttpManager.this.responseFilter(res);
//            if(!BaseHttpManager.this.responseFilter(res)){
//                return;
//            }
            callback.httpManager = BaseHttpManager.this;
            callback.res = res;
            callback.hander.sendEmptyMessage(2);
            
            
            byte[] buffer = new byte[4 * 1024];
            if (len == 0) {
            	pos.close();
            } else if (len > 0) {
            	// given length
            	while(true) {
            		int rd = inputStream.read(buffer);
            		if(rd < 1) {
            			// over
            			if(len > 0) {
            				throw new IOException("EOF");
            			}
            			break;
            		}
            		pos.write(buffer, 0, rd);
            		len -= rd;
            		if(len < 0) {
            			throw new IOException("Httpbody is larger than Content-Length");
            		} else if(len == 0) {
            			pos.close();
            			break;
            		}
            	}
            } else if (len < 0) {
            	// not given length
            	while(true) {
            		int rd = inputStream.read(buffer);
            		if(rd < 1) {
            			// over
            			pos.close();
            			break;
            		}
            		pos.write(buffer, 0, rd);
            	}
            }
            pos.close();
//        } else {
//        	HttpResponse res = new HttpResponse(responseCode, fs, pis);
////            if(!BaseHttpManager.this.responseFilter(res)){
////                return;
////            }
//            callback.httpManager = BaseHttpManager.this;
//            callback.res = res;
//            callback.hander.sendEmptyMessage(2);
//        }
    }
    
    private static void sendHeaders(HttpURLConnection conn, Map<String, String> headers) {
    	for (Map.Entry<String, String> header : headers.entrySet()) {
			conn.setRequestProperty(header.getKey(), header.getValue());
		}
    }
    
    private static void sendBody(HttpURLConnection conn, HttpRequest req, ByteArrayOutputStream pis) throws IOException, InterruptedException {
    	if(req.method.equals("GET")) {
            req.method = "POST";
        }
    	conn.setRequestMethod(req.method);
    	long len0 = req.getLength();
    	conn.setRequestProperty("Content-Length", "" + len0);
    	conn.setDoOutput(true);
    	
    	
        OutputStream ops = conn.getOutputStream();
        
//        byte[] buffer = new byte[4 * 1024];
//        
//        while(true) {
//        	int len = pis.read(buffer);
//        	
//        	if(len < 1) {
//        		break;
//        	}
//        	String temp = new String(buffer, "UTF8");
//        	ops.write(buffer, 0, len);
//        }
        byte[] bs = pis.toByteArray();
        ops.write(bs, 0, bs.length);
        ops.flush();
    }

    public abstract boolean requestFilter(HttpRequest httpRequest) throws Exception;
    public abstract boolean responseFilter(HttpResponse httpResponse) throws Exception;
    
//    public abstract void authenticationError() throws Exception;
    public abstract void error(Exception e);
}
