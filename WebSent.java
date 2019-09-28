package com.bywlkjs.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import com.bywlkjs.json.JSONObject;


public class WebSent {
	
	public static String getUrlContent(String urlStr) {
		String str1="";
		try {
			URL url = new URL(urlStr);
			URLConnection URLconnection = url.openConnection();
			HttpURLConnection httpConnection = (HttpURLConnection) URLconnection;
			int responseCode = httpConnection.getResponseCode();
			InputStream in = httpConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(in,"UTF-8");
			BufferedReader bufr = new BufferedReader(isr);
			String str="";
			while ((str = bufr.readLine()) != null) {
				str1+=str;
			}
			bufr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str1;
	}
	
	public static JSONObject getUrlContentJson(String urlStr) {
		return new JSONObject(getUrlContent(urlStr));
	}
    /**
     * 向指�? URL 发�?�POST方法的请�?
     * 
     * @param url
     *            发�?�请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式�??
     * @return �?代表远程资源的响应结�?
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连�?
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属�?
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
//            conn.setRequestProperty("user-agent",
//                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (weixin; niubi 1.1; mahuateng NB 5.13;SV1)");
            // 发�?�POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(),"UTF-8"));

            // 发�?�请求参�?
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响�?
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(),"UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发�?? POST 请求出现异常�?"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流�?�输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }    
    
}
