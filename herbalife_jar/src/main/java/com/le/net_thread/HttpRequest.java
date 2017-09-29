package com.le.net_thread;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sitsmice.herbalife_jar.MLog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by le on 2015/8/27.
 *
 */
public class HttpRequest {
    HttpRequest(PipedInputStream pis) {
       this.pis = pis;
    }
    
    HttpRequest(ByteArrayOutputStream output) {
        this.output = output;
     }
    
    ObjectMapper mapper = new ObjectMapper();

    public String method = "GET";
    
    public Map<String, String> headers = new HashMap();
    
    private PipedInputStream pis;
    
    private PipedOutputStream pos;
    ByteArrayOutputStream output = null;
    
    private long length;
    
    public long getLength() {
    	return length;
    }
    
    public void close() throws IOException {
//    	pos.close();
    	output.close();
    }
    
    public void write(byte[] bs) throws IOException {
//        if(pos == null) {
//            pos = new PipedOutputStream(pis);
//        }
//        if (output == null) {
//        	output= new ByteArrayOutputStream();
//        }
//        pos.write(bs);
        output.write(bs, 0, bs.length);
        length += bs.length;
    }

    public void write(String str) throws IOException {
        write(str.getBytes("UTF8"));
    }

    public void writeForm(Map<String, String> form) throws IOException {
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Range","bytes="+"");
//        headers.put("Cookie", URLEncoder.encode("sign="+ Config.sign_value,"UTF-8"));
        String out = "";
        String prifix = "&";
        for(Map.Entry<String, String> entry : form.entrySet()) {
            if (entry.getValue()==null){
                out += prifix + entry.getKey() + "=" + "";
            }else {
                out += prifix + entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "UTF-8");
            }
        }
        for (Map.Entry<String, String> entry : form.entrySet()) {
            MLog.e("test","writeForm:"+entry.getKey()+":"+entry.getValue()+"\n");
        }
        write(out);
    }

    public void writeJson(Object json) throws IOException {
        headers.put("Content-Type", "application/json; charset=utf-8");
//        write(JSON.toJSONBytes(json));
        byte[] bs = mapper.writeValueAsBytes(json);
        String res = new String(bs);
        MLog.e("test","writeJson:"+res);
        write(bs);
        
    }

    public void writeFile(String key, String fname, InputStream is) throws Exception {
        // TODO: implement writeFile
    	throw new Exception("writeFile has not implemented yet.");
    }

    public static String zhenZE(String keyword) {
        if (keyword!=null) {
            String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };
            for (String key : fbsArr) {
                if (keyword.contains(key)) {
                    keyword = keyword.replace(key, "\\" + key);
                }
            }
        }
        return keyword;
    }
}
