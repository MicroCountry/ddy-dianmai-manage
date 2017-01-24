/**
 * Copyright (c) 2005-2012 https://github.com/zhangkaitao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.ddy.dianmai.ops.filter;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.OnlineSession;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import com.ddy.dianmai.ops.Constants;
import com.ddy.dianmai.ops.po.CodeEnum;
import com.ddy.dianmai.ops.po.DDYRsp;

public class OnlineSessionFilter extends AccessControlFilter {

    /**
     * 强制退出后重定向的地址
     */
    private String forceLogoutUrl;

    private SessionDAO sessionDAO;

    public String getForceLogoutUrl() {
        return forceLogoutUrl;
    }

    public void setForceLogoutUrl(String forceLogoutUrl) {
        this.forceLogoutUrl = forceLogoutUrl;
    }

    public void setSessionDAO(SessionDAO sessionDAO) {
        this.sessionDAO = sessionDAO;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
    	System.out.println("OnlineSessionFilter-isAccessAllowed");
        Subject subject = getSubject(request, response);
        if (subject == null || subject.getSession(false) == null) {
            return true;
        }
        Session session = sessionDAO.readSession(subject.getSession().getId());
        if (session != null && session instanceof OnlineSession) {
            OnlineSession onlineSession = (OnlineSession) session;
            request.setAttribute(Constants.ONLINE_SESSION, onlineSession);

            if (onlineSession.getStatus() == OnlineSession.OnlineStatus.force_logout) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
    	System.out.println("OnlineSessionFilter-onAccessDenied");
        Subject subject = getSubject(request, response);
        if (subject != null) {
            subject.logout();
        }
        saveRequestAndRedirectToLogin(request, response);
        return true;
    }


    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
    	System.out.println("OnlineSessionFilter-redirectToLogin");
    	DDYRsp rsp = new DDYRsp();
        rsp.setCode(CodeEnum.RETRY_LOGIN.getValue());
        onLoginFail(response, rsp.toString());
    	//WebUtils.issueRedirect(request, response, getForceLogoutUrl());
    }
    //登录失败时默认返回401状态码
    private void onLoginFail(ServletResponse response,String error) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        String type = "application/json";
        String content = error/*ShiroResponseUtil.getAuthcFailResponse()*/;
        try {
            httpResponse.setContentType(type + ";charset=UTF-8");
            httpResponse.setHeader("Pragma", "No-cache");
            httpResponse.setHeader("Cache-Control", "no-cache");
            httpResponse.setDateHeader("Expires", 0);
            httpResponse.getWriter().write(content);
            httpResponse.getWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
