package com.test;

import com.alibaba.fastjson.JSONObject;

public class TestJson {
	public static void main(String[] args) {
		JSONObject obj = JSONObject.parseObject("{\"username\":\"admin\",\"password\":\"123456\"}");
		String username =obj.getString("username");
		String password =obj.getString("password");
		System.out.println(username);
		System.out.println(password);
	}
}
