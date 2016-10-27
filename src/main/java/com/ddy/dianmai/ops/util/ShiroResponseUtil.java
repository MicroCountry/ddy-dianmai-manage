package com.ddy.dianmai.ops.util;


import com.ddy.dianmai.ops.po.CodeEnum;
import com.ddy.dianmai.ops.po.DDYRsp;


/**
 * Package: me.j360.shiro.appclient.servlet.shiro.util
 * User: min_xu
 * Date: 16/4/20 下午5:37
 * 说明：
 */
public class ShiroResponseUtil {

    public static String getAuthcFailResponse(){
    	DDYRsp rsp = new DDYRsp();
    	rsp.setCode(CodeEnum.SESSION_INVILAD.getValue());
        return rsp.toString();
    }

    public static  String getLoginFailResponse(){
    	DDYRsp rsp = new DDYRsp();
    	rsp.setCode(CodeEnum.LOGIN_FAIL.getValue());
        return rsp.toString();
    }

    public static  String getWhiteListFailResponse(){
    	DDYRsp rsp = new DDYRsp();
    	rsp.setCode(CodeEnum.REQUEST_INVILAD.getValue());
        return rsp.toString();
    }

}
