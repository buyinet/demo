package com.bywlkjs.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 *	读取非String类型的json
 * @author 不一网络科技社
 */
public class JSONRead {
	
	/**
	 * 
	 * @param is
	 * @param charsetName
	 * @return
	 */
	public static JSONObject inputStreamToJson(InputStream is,String charsetName){
		StringBuilder sb=new StringBuilder();
		try {
			
			InputStreamReader isr = new InputStreamReader(is,charsetName);
			int d=-1;
			while((d=isr.read())!=-1) {
				sb.append((char)d);
			}
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new JSONObject(sb+"");
	}
	
	
}
