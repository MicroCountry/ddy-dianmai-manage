/*
 * CodeEnum.java
 * Copyright by ddy
 * Description：
 * Modified By：Administrator
 * Modified Time：2015年12月18日
 * Modified Number：
 * Modified Contents：
 */

package com.ddy.dianmai.ops.po;

/**
 * 
 * @author  jc
 * @version 2015年12月18日
 * @see CodeEnum
 * @since
 */

public enum CodeEnum {
	SUCCESS(0,"SUCCESS"),LOGERROR(-1,"登录错误:%s%%"),SESSION_INVILAD(2,"会话失效，请重新登录。")
	,LOGIN_FAIL(3,"登录失败。"),REQUEST_INVILAD(4,"请求无效"),CODE_ERROR(5,"验证码错误"),USERPASS(6,"用户名或者密码错误");//add by wanggm 2016-04-25
	private int value;
	private String label;
	private CodeEnum(int value,String label) {
		this.value = value;
		this.label = label;
	}
	public int getValue(){
		return this.value;
	}
	public void setValue(int value){
		this.value = value;
	}
	public String getLabel() {
		return label;
	}
}
