package com.ddy.dianmai.ops.expand;

import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

/**
 * 
 * @ClassName RewriteFormAuthenticationFilter.java
 * @Description <p>重写FormAuthenticationFilter的  onLoginSuccess 方法<br/>
 * 				自定义登录成功时的跳转页面
 */
public class ExpandFormAuthenticationFilter extends FormAuthenticationFilter {

	private transient final Log logger = LogFactory.getLog(ExpandFormAuthenticationFilter.class);
	
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response)throws Exception {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
	    HttpServletResponse httpServletResponse = (HttpServletResponse) response;

	    if (!"XMLHttpRequest".equalsIgnoreCase(httpServletRequest.getHeader("X-Requested-With")) || request.getParameter("ajax") == null) { //不是ajax请求
	        //issueSuccessRedirect(request, response);  //默认成功地址"/"
	    	//更改登录成功的默认地址
	    	String successUrl = httpServletRequest.getContextPath() + this.getSuccessUrl();
	    	httpServletResponse.sendRedirect(successUrl);
	    	logger.info("登录成功跳转："+successUrl);
	    } else {
	        httpServletResponse.setCharacterEncoding("UTF-8");
	        PrintWriter out = httpServletResponse.getWriter();
	        out.println("{success:true,message:'登入成功'}");
	        out.flush();
	        out.close();
	    }
	    /*if (!"XMLHttpRequest".equalsIgnoreCase(httpServletRequest.getHeader("X-Requested-With")) || request.getParameter("ajax") == null) { // 不是ajax请求
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + this.getSuccessUrl());
        } else {
            httpServletRequest.getRequestDispatcher("/login/timeout/success").forward(httpServletRequest, httpServletResponse);
        }*/
	    
	    return false;
	}
}
