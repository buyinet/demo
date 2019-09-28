package com.bywlkjs.wechat;

import java.util.HashMap;
import java.util.Map;

import com.bywlkjs.json.JSONObject;
import com.bywlkjs.tool.WebSent;
import com.github.wxpay.sdk.WXPayUtil;

public class WechatPay {

	private static WechatPay wechatPay=new WechatPay();

	private JSONObject orderInfoJson;
	private HashMap<String,String> orderInfoMap;

	private JSONObject clientPayJson;
	private HashMap<String,String> clientPayMap;

	private WechatInfo wechatInfo;

	private String appid;
	private String mchid;
	private String openid;
	private String unionid;
	private String tradeType;
	private String totalFee;
	private String clientIp;
	private String body;
	private String attach;
	private String nonceStr;
	private String outTradeNo;
	private String signType;
	private String notifyUrl;
	private String sign;
	
	public WechatPay(){
		orderInfoJson=new JSONObject();
		clientPayJson=new JSONObject();
		orderInfoMap=new HashMap<String, String>();
		clientPayMap=new HashMap<String, String>();
	}
	
	/**
	 * 与 WechatInfo 类的 getWechatPay()方法相同
	 * @param wechatInfo
	 * @return
	 */
	public static WechatPay newWechatPay(WechatInfo wechatInfo) {
		wechatPay.wechatInfo=wechatInfo;
		return wechatPay;
	}
	
	/**
	 * 创建订单<br>
	 * 使用前得用 set 给String clientIp,totalFee,body,attach 注入值<br>
	 * setClientIp(String)<br>
	 * setTotalFee(String)<br>
	 * setBody(String)<br>
	 * setAttach(String)
	 */
	public void createOrders(){
		try {
			//前提参数
			this.appid=this.wechatInfo.getAppid();
			this.mchid=this.wechatInfo.getMchid();
			this.openid=this.wechatInfo.getOpenid();
			this.unionid=this.wechatInfo.getUnionid();
		    //前提参数
		    this.orderInfoMap.put("appid",appid);
		    this.orderInfoMap.put("mch_id",mchid);  
		    this.orderInfoMap.put("openid",openid);
		    //因为在生成签名中不能有null，unionid再没有绑定微信开放平台时时会出现null
		    if(unionid!=null) {
		    	this.orderInfoMap.put("unionid",unionid);
		    }
			
			//交易类型
		    this.tradeType=this.wechatInfo.getTradeType();
		    this.orderInfoMap.put("trade_type", this.tradeType);
		    
			
			//随机字符串
			this.nonceStr=WXPayUtil.generateNonceStr();
			this.orderInfoMap.put("nonce_str",this.nonceStr);
			
			//商户订单号
			this.outTradeNo=PayUtil.getCurrTime()+ PayUtil.getRandomString(5);
			this.orderInfoMap.put("out_trade_no",outTradeNo);
			
			this.signType="MD5";
			this.orderInfoMap.put("sign_type",this.signType);

			//此路径是微信服务器调用支付结果通知路径随意写
			this.notifyUrl=wechatInfo.getNotifyUrl();
			this.orderInfoMap.put("notify_url",this.wechatInfo.getNotifyUrl());
			
			//商品价格
			this.orderInfoMap.put("total_fee",this.totalFee);
			//用户购买后保存的商品名称
			this.orderInfoMap.put("body", this.body);
			//attrach保存的商品名称
			this.orderInfoMap.put("attach",this.attach);
			//终端IP(客户IP)
			this.orderInfoMap.put("spbill_create_ip", this.clientIp);
			
			//生成签名，必须放在最后
			this.sign = WXPayUtil.generateSignature(this.orderInfoMap,this.wechatInfo.getAppkey());
			this.orderInfoMap.put("sign", this.sign);
			
			String xml = WXPayUtil.mapToXml(orderInfoMap);//将所有参数(map)转xml格式
			// 统一下单 https://api.mch.weixin.qq.com/pay/unifiedorder
			String unifiedorder_url 
			="https://api.mch.weixin.qq.com/pay/unifiedorder";
			//发送post请求"统一下单接口"返回预支付id:prepay_id
			String xmlStr
			= WebSent.sendPost(unifiedorder_url, xml);
			
			System.out.println("orderInfoMap\t"+orderInfoMap);
			System.out.println("xmlStr"+xmlStr);
			
			//以下内容是返回前端页面的json数据
			String prepay_id = "";//预支付id
			if (xmlStr.indexOf("SUCCESS") != -1) {  
				Map<String, String> map = WXPayUtil.xmlToMap(xmlStr);  
				prepay_id = (String) map.get("prepay_id");
			}

			/**将 paraMap 的数据保存到 orderProtectedJson(受保护的信息)*/
			for (String key : orderInfoMap.keySet()) {
				this.orderInfoJson.put(key,orderInfoMap.get(key));
			}

			this.clientPayMap.put("appId", this.wechatInfo.getAppid());  
			this.clientPayMap.put("timeStamp", WXPayUtil.getCurrentTimestamp()+"");  
			this.clientPayMap.put("nonceStr", WXPayUtil.generateNonceStr());  
			this.clientPayMap.put("signType", "MD5");  
			//避免前台package关键字冲突,用于获取id
			this.clientPayMap.put("package", "prepay_id=" + prepay_id);//前面必须为package,用于签名
			String paySign = WXPayUtil.generateSignature(this.clientPayMap,this.wechatInfo.getAppkey());  
			this.clientPayMap.put("paySign", paySign);
			
			/**将 payMap 的数据保存到 orderPublicJson(公开的的信息)*/
			for (String key : this.clientPayMap.keySet()) {
				this.clientPayJson.put(key,this.clientPayMap.get(key));
			}
//			System.out.println(clientPayJson);
		} catch (Exception e) {  
			e.printStackTrace();
		} 
	}
	
	/**
	  * 创建订单
	  * @param clientIp 客户端ip
	  * @param totalFee 金额(以分为单位)
	  * @param bodyAttach body(付款后的商品名称)
	  * @param attach 的设置保存着的商品名称
	  */
	public void createOrders(String totalFee,String body,String attach,String clientIp){
		this.totalFee=totalFee;
		this.body=body;
		this.attach=attach;
		this.clientIp=clientIp;
		createOrders();
	}
	
	public static JSONObject queryOrder(){
		return null;
	}
	public JSONObject getOrderInfoJson() {
		return orderInfoJson;
	}
	public JSONObject getClientPayJson() {
		return clientPayJson;
	}
	public void setWechatInfo(WechatInfo wechatInfo) {
		this.wechatInfo = wechatInfo;
	}
	public WechatInfo getWechatInfo() {
		return wechatInfo;
	}
	public static WechatPay getWechatPay() {
		return wechatPay;
	}

	
	public static void setWechatPay(WechatPay wechatPay) {
		WechatPay.wechatPay = wechatPay;
	}

	public HashMap<String, String> getOrderInfoMap() {
		return orderInfoMap;
	}

	public void setOrderInfoMap(HashMap<String, String> orderInfoMap) {
		this.orderInfoMap = orderInfoMap;
	}

	public HashMap<String, String> getClientPayMap() {
		return clientPayMap;
	}

	public void setClientPayMap(HashMap<String, String> clientPayMap) {
		this.clientPayMap = clientPayMap;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMchid() {
		return mchid;
	}

	public void setMchid(String mchid) {
		this.mchid = mchid;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public void setOrderInfoJson(JSONObject orderInfoJson) {
		this.orderInfoJson = orderInfoJson;
	}

	public void setClientPayJson(JSONObject clientPayJson) {
		this.clientPayJson = clientPayJson;
	}
	
	
}
