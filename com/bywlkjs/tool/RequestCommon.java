package com.bywlkjs.tool;

import javax.servlet.http.HttpServletRequest;

public class RequestCommon {
	HttpServletRequest request;

	public RequestCommon(HttpServletRequest request){
		this.request=request;
	}
	
	/**�����û��豸*/
	public String toClientDevice(){
		String device=
				request.getHeader("user-agent");

		return 	device.substring(device.indexOf("(")+1
				,device.indexOf(")"));
	}
	
	/**�����û�ip*/
	public String toClientIp() {
		String ip = request.getHeader("x-forwarded-for");
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getRemoteAddr();
	    }
	    if(ip.indexOf(",")!=-1){
	    	String[] ips = ip.split(",");
	    	ip = ips[0].trim();
	    }
	    return ip;
	}

	
	/**�����û��豸*/
	public static String toClientDevice(HttpServletRequest request){
		String device=
				request.getHeader("user-agent");

		return 	device.substring(device.indexOf("(")+1
				,device.indexOf(")"));
	}
	
	/**�����û�ip*/
	public static String toClientIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getRemoteAddr();
	    }
	    if(ip.indexOf(",")!=-1){
	    	String[] ips = ip.split(",");
	    	ip = ips[0].trim();
	    }
	    return ip;
	}
	
}
