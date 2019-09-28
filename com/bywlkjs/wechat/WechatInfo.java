package com.bywlkjs.wechat;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.bywlkjs.json.JSONObject;

@Component
public abstract class WechatInfo {
	@Value("#{wechatInfoProperties.appid}")
	private String appid;
	@Value("#{wechatInfoProperties.secret}")
	private String secret;
	@Value("#{wechatInfoProperties.mchid}")
	private String mchid;
	@Value("#{wechatInfoProperties.appkey}")
	private String appkey;
	@Value("#{wechatInfoProperties.notifyUrl}")
	private String notifyUrl;//微信服务器调用支付结果通知路径
	
	private JSONObject statusJson;//状态json
	private JSONObject userInfoJson;//用户信息
	

	//微信平台获取的openid,即微信内网页获取到的openid,微信小程序获取到的openId
	private String openid;
	//微信平台获取的unionid,即微信内网页获取到的unionid,微信小程序获取到的unionId
	private String unionid;
	//用户的昵称，即微信内网页获取到的nickname,微信小程序获取到的nickName
	private String nickname;
	//用户的性别，即微信内网页获取到的sex,微信小程序获取到的gender
	private int gender;
	//用户的头像，即微信内网页获取到的sex,微信小程序获取到的gender
	private String headimgurl;
	private String country;//国家
	private String province;//省/直辖市
	private String city;//市/直辖市的县区
	private String language;//用户微信使用的语言

	public WechatInfo(){
		statusJson=new JSONObject();
		userInfoJson=new JSONObject();
	}
	/**
	 * @例
	 * 微信小程序：JSAPI
	 * NATIVES支付：Natives
	 * @return 支付类型
	 * */
	abstract String getTradeType();
	
	/**
	 * @throws Exception 
	 * @ 将数据提交给微信后台，并生成数据
	 */
	abstract int commit();
	
	/**
	 * 生成付款类
	 * 与 WechatPay.newWechat(WechatInfo) 用途相同
	 * @return 付款类
	 */
	public WechatPay getWechatPay(){
		WechatPay wechatPay=new WechatPay();
		wechatPay.setWechatInfo(this);
		return wechatPay;
	}
	
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public String getMchid() {
		return mchid;
	}
	public void setMchid(String mchid) {
		this.mchid = mchid;
	}
	public String getAppkey() {
		return appkey;
	}
	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}
	
	/**得先使用之类中的 commit(参数不定) 方法，
	 * 就可以查询微信信息 */
	public JSONObject getUserInfoJson() {
		return userInfoJson;
	}
	public void setUserInfoJson(JSONObject userInfoJson) {
		this.userInfoJson = userInfoJson;
	}
	
	/**微信服务器调用支付结果通知路径*/
	public String getNotifyUrl() {
		return notifyUrl;
	}
	/**微信服务器调用支付结果通知路径*/
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	

	public JSONObject getStatusJson() {
		return statusJson;
	}
	public void setStatusJson(JSONObject statusJson) {
		this.statusJson = statusJson;
	}

	/**
	 * 	@从JSON中获取的key
	 * 	微信内页面: openid<br>
	 * 	微信小程序: openId
	 * 
	 * @return openid 从<b>微信平台返回的用户信息</b>中获取的<b>openid</b><br>
	 * (微信平台返回的用户信息为JSON格式)
	 */
	public String getOpenid() {
		return openid;
	}
	
	
	/**
	 * 	@从JSON中获取的key
	 * 	微信内页面: unionid<br>
	 * 	微信小程序: unionId
	 * 
	 * @return unionid 从<b>微信平台返回的用户信息</b>中获取的<b>unionid/unionId</b><br>
	 * (微信平台返回的用户信息为JSON格式)
	 */
	public String getUnionid() {
		return unionid;
	}
	
	/**
	 * 	@从JSON中获取的key
	 * 	微信内页面: nickname<br>
	 * 	微信小程序: nickName
	 * 
	 * @return nickname 从<b>微信平台返回的用户信息</b>中获取的<b>nickname/nickName</b><br>
	 * (微信平台返回的用户信息为JSON格式)
	 */	
	public String getNickname() {
		return nickname;
	}
	
	/**
	 * 	@从JSON中获取的key
	 * 	微信内页面: sex<br>
	 * 	微信小程序: gender
	 * 
	 * @return gender 从<b>微信平台返回的用户信息</b>中获取的<b>sex/gender</b><br>
	 * (微信平台返回的用户信息为JSON格式)
	 */		
	public int getGender() {
		return gender;
	}
	
	/**
	 * 	@从JSON中获取的key
	 * 	微信内页面: headimgurl<br>
	 * 	微信小程序: avatarUrl
	 * 
	 * @return headimgurl 从<b>微信平台返回的用户信息</b>中获取的<b>headimgurl/avatarUrl</b><br>
	 * (微信平台返回的用户信息为JSON格式)
	 */
	public String getHeadimgurl() {
		return headimgurl;
	}

	/**
	 * 	@从JSON中获取的key
	 * 	微信内页面: country<br>
	 * 	微信小程序: country
	 * 
	 * @return country 从<b>微信平台返回的用户信息</b>中获取的<b>country</b><br>
	 * (微信平台返回的用户信息为JSON格式)
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * 	@从JSON中获取的key
	 * 	微信内页面: province<br>
	 * 	微信小程序: province
	 * 
	 * @return province 从<b>微信平台返回的用户信息</b>中获取的<b>province</b><br>
	 * (微信平台返回的用户信息为JSON格式)
	 */
	public String getProvince() {
		return province;
	}
	/**
	 * 	@从JSON中获取的key
	 * 	微信内页面: city<br>
	 * 	微信小程序: city
	 * 
	 * @return city 从<b>微信平台返回的用户信息</b>中获取的<b>city</b><br>
	 * (微信平台返回的用户信息为JSON格式)
	 */
	public String getCity() {
		return city;
	}

	/**
	 * 	@从JSON中获取的key
	 * 	微信内页面: language<br>
	 * 	微信小程序: language
	 * 
	 * @return language 从<b>微信平台返回的用户信息</b>中获取的<b>language</b><br>
	 * (微信平台返回的用户信息为JSON格式)
	 */
	public String getLanguage() {
		return language;
	}
	
	/**
	 * 	@从JSON中获取的key
	 * 	微信内页面: openid<br>
	 * 	微信小程序: openId
	 * 
	 * @param openid 从<b>微信平台返回的用户信息</b>中获取的<b>openid</b><br>
	 * (微信平台返回的用户信息为JSON格式)
	 */
	public void setOpenid(String openid) {
		this.openid = openid;
	}

	/**
	 * 	@从JSON中获取的key
	 * 	微信内页面: unionid<br>
	 * 	微信小程序: unionId
	 * 
	 * @param unionid 从<b>微信平台返回的用户信息</b>中获取的<b>nickname/nickName</b><br>
	 * (微信平台返回的用户信息为JSON格式)
	 */	
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	
	/**
	 * 	@从JSON中获取的key
	 * 	微信内页面: nickname<br>
	 * 	微信小程序: nickName
	 * 
	 * @param nickname 从<b>微信平台返回的用户信息</b>中获取的<b>nickname/nickName</b><br>
	 * (微信平台返回的用户信息为JSON格式)
	 */	
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	

	/**
	 * 	@从JSON中获取的key
	 * 	微信内页面: sex<br>
	 * 	微信小程序: gender
	 * 
	 * @param gender 从<b>微信平台返回的用户信息</b>中获取的<b>sex/gender</b><br>
	 * (微信平台返回的用户信息为JSON格式)
	 */
	public void setGender(int gender) {
		this.gender = gender;
	}
	

	/**
	 * 	@从JSON中获取的key
	 * 	微信内页面: headimgurl<br>
	 * 	微信小程序: headimgurl
	 * 
	 * @param headimgurl 从<b>微信平台返回的用户信息</b>中获取的<b>headimgurl/avatarUrl</b><br>
	 * (微信平台返回的用户信息为JSON格式)
	 */
	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
	

	/**
	 * 	@从JSON中获取的key
	 * 	微信内页面: country<br>
	 * 	微信小程序: country
	 * 
	 * @param country 从<b>微信平台返回的用户信息</b>中获取的<b>country</b><br>
	 * (微信平台返回的用户信息为JSON格式)
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	
	
	/**
	 * 	@从JSON中获取的key
	 * 	微信内页面: province<br>
	 * 	微信小程序: province
	 * 
	 * @param province 从<b>微信平台返回的用户信息</b>中获取的<b>province</b><br>
	 * (微信平台返回的用户信息为JSON格式)
	 */
	public void setProvince(String province) {
		this.province = province;
	}

	/**
	 * 	@从JSON中获取的key
	 * 	微信内页面: city<br>
	 * 	微信小程序: city
	 * 
	 * @param city 从<b>微信平台返回的用户信息</b>中获取的<b>city</b><br>
	 * (微信平台返回的用户信息为JSON格式)
	 */
	public void setCity(String city) {
		this.city = city;
	}
	
	/**
	 * 	@从JSON中获取的key
	 * 	微信内页面: language<br>
	 * 	微信小程序: language
	 * 
	 * @param language 从<b>微信平台返回的用户信息</b>中获取的<b>language</b><br>
	 * (微信平台返回的用户信息为JSON格式)
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

}
/*	{"openId":"olqVW49Zp5LzfXcwzFxZit4KAZPM",
 * "nickName":"等灯瞪等灯",
 * "gender":1,
 * "language":"zh_CN",
 * "city":"Shangrao",
 * "province":"Jiangxi",
 * "country":"China",
 * "avatarUrl":""
 * "watermark":{"timestamp":1564573063,"appid":"wxdd5cf71b08b4fec5"}}
 */
/*
 * {"country":"中国",
 * "province":"江西",
 * "city":"上饶",
 * "openid":"or4wa6EAnpvWd9DA-mIWIdIADIFo",
 * "sex":1,
 * "nickname":"等灯瞪等灯",
 * "headimgurl":"",
 * unionid:""
	"language":"zh_CN",
	"privilege":[],
	}
 */
