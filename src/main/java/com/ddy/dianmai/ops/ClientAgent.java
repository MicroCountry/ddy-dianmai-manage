package com.ddy.dianmai.ops;


import java.io.Serializable;

/**
 * Package: me.j360.shiro.appclient.servlet.shiro.dto
 * User: min_xu
 * Date: 16/5/13 下午4:03
 * 说明：
 */
//表达式：appName/version/buildVersion/osNumber/osVersion/deviceModel/deviceUUID
public class ClientAgent implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String appName;
    private String version;
    private int buildVersion;
    private String osNumber;
    private String osVersion;
    private String deviceModel;
    private String deviceUUID;
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public int getBuildVersion() {
		return buildVersion;
	}
	public void setBuildVersion(int buildVersion) {
		this.buildVersion = buildVersion;
	}
	public String getOsNumber() {
		return osNumber;
	}
	public void setOsNumber(String osNumber) {
		this.osNumber = osNumber;
	}
	public String getOsVersion() {
		return osVersion;
	}
	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}
	public String getDeviceModel() {
		return deviceModel;
	}
	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}
	public String getDeviceUUID() {
		return deviceUUID;
	}
	public void setDeviceUUID(String deviceUUID) {
		this.deviceUUID = deviceUUID;
	}
    
}