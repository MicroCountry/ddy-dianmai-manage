package com.ddy.dianmai.ops.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ddy.dianmai.ops.po.CodeEnum;
import com.ddy.dianmai.ops.po.DDYRsp;

@Controller
@RequestMapping(produces="text/plain;charset=UTF-8")
public class LoginController {

    @RequestMapping(value = "/login")
    @ResponseBody
    public String showLoginForm(HttpServletRequest req, ModelMap model) {
        String exceptionClassName = (String)req.getAttribute("shiroLoginFailure");
        String error = null;
        DDYRsp rsp = new DDYRsp();
        if(UnknownAccountException.class.getName().equals(exceptionClassName)) {
            error = "用户名/密码错误";
        } else if(IncorrectCredentialsException.class.getName().equals(exceptionClassName)) {
            error = "用户名/密码错误";
        } else if(exceptionClassName != null) {
            error = "其他错误：" + exceptionClassName;
        }
        String kickout = req.getParameter("kickout");
        String forceLogout = req.getParameter("forceLogout");
        if(StringUtils.hasText(kickout)){
        	error = "您的账号已在其他地方登录";
        }
        if(StringUtils.hasText(forceLogout)) {
        	error = "您已经被管理员强制退出，请重新登录";
    	}
        System.out.println("login:error:"+error);
        if(error!=null)
        	rsp.setCodeAndParams(CodeEnum.LOGERROR.getValue(),new Object[]{error});
        return rsp.toString();
    }

}
