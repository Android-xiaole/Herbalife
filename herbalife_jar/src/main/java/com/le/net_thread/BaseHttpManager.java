package com.le.net_thread;


import com.sitsmice.herbalife_jar.MLog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

	public static String zhPattern = "[\u4e00-\u9fa5]+";
	public HttpURLConnection conn;
	public InputStream is ;

    public Thread makeRequest(final String url,final IHttpHandler callback) {
      final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url1 = new URL(encode(url, "UTF-8"));
					MLog.e("test","接口地址："+url1.toString());
                    conn = (HttpURLConnection)url1.openConnection();
					conn.setConnectTimeout(60000);//
					conn.setReadTimeout(60000);//
                    final ByteArrayOutputStream output = new ByteArrayOutputStream();
                    final HttpRequest httpRequest = new HttpRequest(output);
                    BaseHttpManager.this.requestFilter(httpRequest);

					callback.req = httpRequest;
					// start
					callback.start();

					int responseCode ;
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
							wait0(responseCode, conn, callback, is);
							//done
							callback.done();
						} else {
							is = conn.getErrorStream();
							wait0(responseCode, conn, callback, is);
							throw new Exception(callback.res.getString());
						}
					}catch (Exception e) {
						throw new Exception(e);
					}
                }
                catch (Exception e) {
					callback.error(e);
                }
            }
        });
		return thread;
    }

	public void disconn(){
		if (conn!=null){
			if (is!=null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			conn.disconnect();
		}
	}

    private void wait0(int responseCode, HttpURLConnection conn, IHttpHandler callback, InputStream inputStream) throws Exception {
        Map<String, List<String>> fs = conn.getHeaderFields();
		HttpResponse res = new HttpResponse(responseCode, fs, inputStream);
		BaseHttpManager.this.responseFilter(res);
		callback.httpManager = BaseHttpManager.this;
		callback.res = res;
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
        byte[] bs = pis.toByteArray();
        ops.write(bs, 0, bs.length);
        ops.flush();
    }

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
    
    public abstract boolean requestFilter(HttpRequest httpRequest) throws Exception;
    public abstract boolean responseFilter(HttpResponse httpResponse) throws Exception;
}
