package com.ddy.dianmai.ops.filter;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.session.Session;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import com.ddy.dianmai.ops.Constants;
import com.ddy.dianmai.ops.po.CodeEnum;
import com.ddy.dianmai.ops.po.DDYRsp;


/**
 * @ClassName ForceLogoutFilter.java
 * @Description 强制退出拦截器
 *
 */
public class ForceLogoutFilter extends AccessControlFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
    	System.out.println("ForceLogoutFilter-isAccessAllowed");
        Session session = getSubject(request, response).getSession(false);
        if(session == null) {
            return true;
        }
        return session.getAttribute(Constants.SESSION_FORCE_LOGOUT_KEY) == null;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
    	System.out.println("ForceLogoutFilter-onAccessDenied");
        try {
            getSubject(request, response).logout();//强制退出
        } catch (Exception e) {
        }

        String loginUrl = getLoginUrl() + (getLoginUrl().contains("?") ? "&" : "?") + "forceLogout=1";
        //WebUtils.issueRedirect(request, response, loginUrl);
        DDYRsp rsp = new DDYRsp();
        rsp.setCode(CodeEnum.RETRY_LOGIN.getValue());
        onLoginFail(response, rsp.toString());
        return false;
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
