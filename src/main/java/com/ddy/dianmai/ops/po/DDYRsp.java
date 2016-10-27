/*
 * DDYRsp.java
 * Copyright by ddy
 * Description：
 * Modified By：Administrator
 * Modified Time：2015年12月16日
 * Modified Number：
 * Modified Contents：
 */

package com.ddy.dianmai.ops.po;

import com.google.gson.GsonBuilder;



/**
 * 
 * @author  jc
 * @version 2015年12月16日
 * @see DDYRsp
 * @since
 */

public class DDYRsp {
	protected String msg = "";
	protected String sid;
	protected int code;
	protected Object data;
	protected transient String msgPrefix;
	protected transient Object[] params;
	
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public boolean isSuccess(){
		return this.code == CodeEnum.SUCCESS.getValue();
	}
	@Override
	public String toString() {
		
		for(CodeEnum ce:CodeEnum.values()){
			if(ce.getValue() == code){
				if(this.msgPrefix!=null){
					this.msg = String.format(ce.getLabel(), this.msgPrefix);
				}else if(this.params!=null){
					this.msg = String.format(ce.getLabel(), this.params);
				}else {
				    this.msg = ce.getLabel();
				}
				break;
			}
		}
		return this.toJSONString();
	}
	private String toJSONString(){
		return new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().setDateFormat(
        "yyyy-MM-dd HH:mm:ss").create().toJson(this);
	}
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public void setCodeAndMsgPrefix(int code,String msgPrefix){
		this.code = code;
		this.msgPrefix = msgPrefix;
	}
	
	public void setCodeAndParams(int code,Object[] params){
	    this.code = code;
	    this.params = params;
	}
}
