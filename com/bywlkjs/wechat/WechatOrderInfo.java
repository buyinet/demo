package com.bywlkjs.wechat;

import java.util.HashMap;
import java.util.Map;

import com.bywlkjs.json.JSONObject;
import com.bywlkjs.json.XML;
import com.bywlkjs.tool.WebSent;
import com.github.wxpay.sdk.WXPayUtil;

public class WechatOrderInfo {
	
	

	/**
	 * 查询订单状态
	 * 返回json格式
	 * 
	 *  {
	 * "transaction_id":"交易单号",
	 * "nonce_str":"调起支付时成生成的随机字符串",
	 * "trade_state":"支付是否成功",
	 * "openid":"",
	 * "sign":"",
	 * "return_msg":"OK",
	 * "fee_type":"CNY",
	 * "mch_id":"1520680451",
	 * "cash_fee":4,
	 * "out_trade_no":"",
	 * "appid":"wx8b090c77b904afe4",
	 * "total_fee":支付金额/以“分”为单位,
	 * "trade_state_desc":"支付成功",
	 * "trade_type":"JSAPI",
	 * "result_code":"SUCCESS",
	 * "attach":"",
	 * "time_end":"最后执行时间",
	 * "is_subscribe":"Y",
	 * "return_code":"SUCCESS"
	 * "bank_type":"",
	 * }
	 * 
	 * @return 订单状态 */
	public static JSONObject queryOrderJson
	(String appid,String mchid,String appKey,String out_trade_no,String nonce_str)
	{
			try {
			Map<String, String> paraMap = new HashMap<String, String>();
		    paraMap.put("appid", appid);
		    paraMap.put("mch_id", mchid);
		    paraMap.put("out_trade_no",out_trade_no);
		    paraMap.put("nonce_str",nonce_str);
		    //这里重新生成签名
		    paraMap.put("sign",WXPayUtil.generateSignature(paraMap,appKey));
			//将所有参数(map)转xml格式
			String xml;
				xml = WXPayUtil.mapToXml(paraMap);
				
			String xmlStr = WebSent.sendPost
					("https://api.mch.weixin.qq.com/pay/orderquery"
					, xml);
			
			//将xml转换成json，更便于操作
			JSONObject json=XML.toJSONObject(xmlStr);
			JSONObject resultJson=json.getJSONObject("xml");
			
		    return resultJson;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	
	
}
