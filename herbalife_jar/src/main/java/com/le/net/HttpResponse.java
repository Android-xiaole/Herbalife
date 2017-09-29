package com.le.net;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by peng on 2015/8/27.
 *
 */
public class HttpResponse {
	HttpResponse(int sc, Map<String, List<String>> fs, InputStream is) {
		statusCode = sc;
		fields = fs;
		inputStream = is;
	}

	ObjectMapper mapper = new ObjectMapper();
	public int statusCode;
	private Map<String, List<String>> fields;
	public InputStream inputStream;

	public List<String> getHeaders(String key) {
		for (Map.Entry<String, List<String>> entry : fields.entrySet()) {
			if (entry.getKey() == null) {
				continue;
			}
			if (entry.getKey().equals(key)) {
				return entry.getValue();
			}
		}
		return new ArrayList<>();
	}

	private String str;

	public String getString() throws IOException {
		if (str == null) {
			str = "";
			BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream, "UTF8"));
			while(true) {
				String line = rd.readLine();
				if(line != null) {
					str += line + "\n";
				} else {
					break;
				}
			}
//			byte[] buffer = new byte[4 * 1024];
//			StringBuilder sb = new StringBuilder();
//			while (true) {
//				int len = this.inputStream.read(buffer);
//				if (len < 1) {
//					break;
//				}
//				sb.append(new String(buffer, 0, len, "UTF8"));
//
//
//			}
//			this.str = sb.toString();
		}
		return this.str;
	}

	/**
	 * To convert the return value to JSONObject
	 */
//	public com.alibaba.fastjson.JSONObject getJson() throws IOException {
//		return JSON.parseObject(this.getString());
//	}


	/**
	 * If the return value is a JSONObject, will be transformed into a class
	 */
	public <T> T parseJson(Class<T> t) throws IOException {
		return mapper.readValue(this.getString(), t);
//		return JSON.parseObject(this.getString(), t);
	}

	/**
	 * If the return value is a JsonArray, will be converted to a list < Class >
	 */
	public <T> List<T> parseJsonArray(Class<T> t) throws IOException {
		return mapper.readValue(this.getString(), mapper.getTypeFactory().constructCollectionType(List.class, t));
//		return JSON.parseArray(this.getString(), t);
	}
}
