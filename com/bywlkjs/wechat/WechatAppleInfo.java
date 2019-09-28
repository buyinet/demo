package com.bywlkjs.wechat;


import org.springframework.stereotype.Service;

import com.bywlkjs.json.JSONObject;
import com.bywlkjs.tool.AesCbcUtil;
import com.bywlkjs.tool.WebSent;

@Service//只有用这个才能和父类用同样spring注入的值
public class WechatAppleInfo extends WechatInfo{
	//微信端传来的get参数
	private String encryptedData;
	private String iv;
	private String code;
	
	@Override   
	String getTradeType() {
		return "JSAPI";
	}
	
	public WechatAppleInfo(){
	}
	
	/**
	 * 将数据提交给微信端，并生成数据
	 * @param encryptedData 得到的get传参
	 * @param iv 得到的get传参
	 * @param code 得到的get传参
	 */
	public int commint(String encryptedData,String iv,String code){
		this.encryptedData=encryptedData;
		this.iv=iv;
		this.code=code;
		return commit();
	}
	
	
	
	/**
	 * 使用前先使用set方法给encryptedData,iv,code赋值<br>
	 * setEncryptedData(String encryptedData)<br>
	 * setIv(String iv)
	 * setCode(String code)
	 */
	@Override
	public int commit(){
		if (code == null || code.length() == 0) {
			this.getStatusJson().put("status",0);
			this.getStatusJson().put("msg","code 不能为空");
			return 0;
		}
		
		if(encryptedData==null || encryptedData.length()==0) {
			this.getStatusJson().put("status",0);
			this.getStatusJson().put("msg","cencryptedData 不能为空");
			return 0;
		}
		
		if(iv==null || iv.length()==0){
			this.getStatusJson().put("status",0);
			this.getStatusJson().put("msg","iv 不能为空");
			return 0;
		}


		String grantType = "authorization_code";
		
		/*发送请求得到用户信息数据*/
		String sr = WebSent.getUrlContent(
				"https://api.weixin.qq.com/sns/jscode2session?"
				+"appid=" + getAppid()
				+ "&secret=" + getSecret()
				+ "&js_code=" + code 
				+"&grant_type="+ grantType);

		JSONObject json = new JSONObject(sr);
		String session_key = json.getString("session_key");
		String openid = json.getString("openid");
		try {
			String result = AesCbcUtil.decrypt(encryptedData, session_key, iv, "UTF-8");
			System.out.println(result);
			
			if (null != result && result.length() > 0) {
				JSONObject userInfoJson=new JSONObject(result);
				this.setUserInfoJson(userInfoJson);
				
				this.setOpenid(userInfoJson.getString("openId"));
				this.setUnionid(userInfoJson.getString("unionId"));
				this.setNickname(userInfoJson.getString("nickName"));
				this.setGender(userInfoJson.getInt("gender"));
				this.setHeadimgurl(userInfoJson.getString("avatarUrl"));
				this.setCountry(userInfoJson.getString("country"));
				this.setProvince(userInfoJson.getString("province"));
				this.setCity(userInfoJson.getString("city"));
				
				this.getStatusJson().put("status", 1);
				this.getStatusJson().put("msg","获取用户信息成功");
				return 1;
			} 
			
				this.getStatusJson().put("status", 0);
				this.getStatusJson().put("msg", "获取用户信息失败");
				return 0;
		} catch (Exception e) {
			this.getStatusJson().put("status", 0);
			this.getStatusJson().put("msg", "获取用户信息失败");
			e.printStackTrace();
		}
		return 0;
	}

	
	public String getEncryptedData() {
		return encryptedData;
	}

	/**
	 * @param encryptedData 微信端传来的get参数
	 */
	public void setEncryptedData(String encryptedData) {
		this.encryptedData = encryptedData;
	}

	public String getIv() {
		return iv;
	}

	/**
	 * @param iv 微信端传来的get参数
	 */
	public void setIv(String iv) {
		this.iv = iv;
	}

	public String getCode() {
		return code;
	}
	
	/**
	 * @param code 微信端传来的get参数
	 */
	public void setCode(String code) {
		this.code = code;
	}

}
