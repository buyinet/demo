package com.bywlkjs.wechat;

import org.springframework.stereotype.Service;

import com.bywlkjs.json.JSONObject;
import com.bywlkjs.tool.WebSent;

@Service
public class WechatIntranetBrowserInfo extends WechatInfo {
	private String code;

	@Override
	String getTradeType() {
		return "JSAPI";
	}

	/**
	 * 将数据提交给微信端，并生成数据
	 * 
	 * @param code 得到的get传参
	 */
	public int commit(String code) {
		this.code = code;
		return commit();
	}

	/**
	 * 使用前给code使用set方法赋值 setCode(String code);
	 * 
	 * @throws NullPointerException
	 */
	@Override
	public int commit() {
		if (code == null || code == "") {
			this.getStatusJson().put("status", 0);
			this.getStatusJson().put("msg", "code 不能为空");
			return 0;
		}

		String accessTokenJsonStr = WebSent
				.getUrlContent("https://api.weixin.qq.com/sns/oauth2/" + "access_token?appid=" + getAppid() + "&secret="
						+ getSecret() + "&code=" + code + "&grant_type=authorization_code");
		JSONObject accessTokenJson = new JSONObject(accessTokenJsonStr);

		if (accessTokenJson.has("errcode")) {
			/* 获取信息失败时 */
			this.getStatusJson().put("status", accessTokenJson.get("errcode"));
			this.getStatusJson().put("msg", accessTokenJson.get("errmsg"));
			/*
			 * --------------- 示范json --------------- {"Error":"获取信息失败，请刷新重试","Code":"0"}
			 */
			return 0;
		}

		/* 获取信息成功时 */
		/* 这里是绑定过微信开放平台的 */
		String userInfoJsonStr = WebSent.getUrlContent(
				"https://api.weixin.qq.com/sns/" + "userinfo?access_token=" + accessTokenJson.getString("access_token")
						+ "&openid=" + accessTokenJson.getString("openid") + "&lang=zh_CN");

		/*
		 * 如果绑定微信开放平台，可以使用下面这段代码，然后把上面那个注释掉，但是大多情况没必要 userInfoJsonStr =
		 * WebSent.getUrlContent("https://api.weixin.qq.com/sns/"
		 * +"userinfo?access_token=" +accessTokenJson.getString("access_token")
		 * +"&openid=" + accessTokenJson.getString("unionid") + "&lang=zh_CN");
		 */

		/*
		 * ------------------ 示范json -----------------
		 * 
		 * {
		 * "openid":"微信给 每个微信公众平台 的用来识别用户的id【只能再一个公众平台通用】",
		 * "unionid":"微信给 每个开微信开放平台 的用来识别用户的id【只能再一个公众平台通用】"
		 * "nickname":"昵称",
		 * "sex" : 性别（int类型【0代表女，1代表男】）,
		 * "language" : "zh_CN",
		 * "country" : "国家",
		 * "province" : "省份/直辖市",
		 * "city" : "市/直辖市的县区",
		 * "headimgurl" : "头像地址",
		 * "privilege":[],
		 * }
		 * ---- 没有绑定 微信开放平台的公众号 没有 unionid ----
		 */
		
//		System.out.println("微信内浏览器API userInfoJsonStr\t" + userInfoJsonStr);
		this.setUserInfoJson(new JSONObject(userInfoJsonStr));
		this.setOpenid(this.getUserInfoJson().getString("openid")); 
		this.setUnionid(this.getUserInfoJson().getString("unionid"));
		this.setGender(this.getUserInfoJson().getInt("sex"));
		this.setNickname(this.getUserInfoJson().getString("nickname"));
		this.setHeadimgurl(this.getUserInfoJson().getString("headimgurl"));
		this.setCountry(this.getUserInfoJson().getString("country"));
		this.setProvince(this.getUserInfoJson().getString("province"));
		this.setCity(this.getUserInfoJson().getString("city"));
		this.setLanguage(this.getUserInfoJson().getString("language"));

		//告知客户端获取成功
		this.getStatusJson().put("status", "1");
		this.getStatusJson().put("msg", "获取用户信息成功");
		
		// 添加客户端可访问的信息
		JSONObject dataJson=new JSONObject();
		dataJson.put("openid", this.getOpenid());
		dataJson.put("unionid", this.getUnionid());
		dataJson.put("gender", this.getGender());
		dataJson.put("nickname", this.getNickname());
		dataJson.put("headimgurl", this.getHeadimgurl());
		dataJson.put("country", this.getCountry());
		dataJson.put("province", this.getProvince());
		dataJson.put("city", this.getCity());
		dataJson.put("language",this.getLanguage());
		this.getStatusJson().put("data", dataJson);
		
		return 1;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
