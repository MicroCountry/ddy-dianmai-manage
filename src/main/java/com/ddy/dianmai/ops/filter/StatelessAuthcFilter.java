package com.ddy.dianmai.ops.filter;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;

import com.alibaba.fastjson.JSONObject;
import com.ddy.dianmai.ops.po.CodeEnum;
import com.ddy.dianmai.ops.po.DDYRsp;
import com.ddy.dianmai.ops.util.ShiroResponseUtil;

/**
 * 用户会话的Filter
 */
public class StatelessAuthcFilter extends AuthenticatingFilter {
	 public static final String DEFAULT_ERROR_KEY_ATTRIBUTE_NAME = "shiroLoginFailure";

    public static final String DEFAULT_USERNAME_PARAM = "username";
    public static final String DEFAULT_PASSWORD_PARAM = "password";
	public static final String DEFAULT_REMEMBER_ME_PARAM = "rememberMe";
	private String rememberMeParam = DEFAULT_REMEMBER_ME_PARAM;
	private String usernameParam = DEFAULT_USERNAME_PARAM;
    private String passwordParam = DEFAULT_PASSWORD_PARAM;
    private String failureKeyAttribute = DEFAULT_ERROR_KEY_ATTRIBUTE_NAME;
    public StatelessAuthcFilter() {
        setLoginUrl(DEFAULT_LOGIN_URL);
    }

    @Override
    public void setLoginUrl(String loginUrl) {
        String previous = getLoginUrl();
        if (previous != null) {
            this.appliedPaths.remove(previous);
        }
        super.setLoginUrl(loginUrl);
        this.appliedPaths.put(getLoginUrl(), null);
    }
	public String getRememberMeParam() {
        return rememberMeParam;
    }
	public String getUsernameParam() {
        return usernameParam;
    }

    /**
     * Sets the request parameter name to look for when acquiring the username.  Unless overridden by calling this
     * method, the default is <code>username</code>.
     *
     * @param usernameParam the name of the request param to check for acquiring the username.
     */
    public void setUsernameParam(String usernameParam) {
        this.usernameParam = usernameParam;
    }

    public String getPasswordParam() {
        return passwordParam;
    }

    /**
     * Sets the request parameter name to look for when acquiring the password.  Unless overridden by calling this
     * method, the default is <code>password</code>.
     *
     * @param passwordParam the name of the request param to check for acquiring the password.
     */
    public void setPasswordParam(String passwordParam) {
        this.passwordParam = passwordParam;
    }
    /**
     * Sets the request parameter name to look for when acquiring the rememberMe boolean value.  Unless overridden
     * by calling this method, the default is <code>rememberMe</code>.
     * <p/>
     * RememberMe will be <code>true</code> if the parameter value equals any of those supported by
     * {@link org.apache.shiro.web.util.WebUtils#isTrue(javax.servlet.ServletRequest, String) WebUtils.isTrue(request,value)}, <code>false</code>
     * otherwise.
     *
     * @param rememberMeParam the name of the request param to check for acquiring the rememberMe boolean value.
     */
    public void setRememberMeParam(String rememberMeParam) {
        this.rememberMeParam = rememberMeParam;
    }
    public String getFailureKeyAttribute() {
        return failureKeyAttribute;
    }

    public void setFailureKeyAttribute(String failureKeyAttribute) {
        this.failureKeyAttribute = failureKeyAttribute;
    }
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
    	System.out.println("StatelessAuthcFilter-isAccessAllowed");
        //强制为否，必须经过验证
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
    	System.out.println("StatelessAuthcFilter-onAccessDenied");
    	String validate = (String)request.getAttribute("shiroLoginFailure");
    	if("jCaptcha.error".equals(validate)) {
    		DDYRsp rsp = new DDYRsp();
			rsp.setCode(CodeEnum.CODE_ERROR.getValue());
            onLoginFail(response,rsp.toString());
            return false;
    	}
    	BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
		StringBuilder sb = new StringBuilder();
		String temp = null;
		try {
			while ((temp = br.readLine()) != null) {
				sb.append(temp);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(sb.toString());
		String context = sb.toString();
		JSONObject obj = JSONObject.parseObject(context);
		String username =obj.getString("username");
		String password =obj.getString("password");
		this.setUsernameParam(username);
		this.setPasswordParam(password);
		if(StringUtils.isBlank(password)||StringUtils.isBlank(username)){
			System.out.println("string error");
			DDYRsp rsp = new DDYRsp();
			rsp.setCode(CodeEnum.REQUEST_INVILAD.getValue());
			onLoginFail(response,rsp.toString());
			return false;
		}
	    DDYRsp rsp = new DDYRsp();
    	//UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		 AuthenticationToken token = createToken(request, response);
		 Subject subject = getSubject(request, response);
        try {
            subject.login(token);
            System.out.println("subject.login");
        } catch (Exception e) {
            String error = null;
            if( e instanceof UnknownAccountException ){
                error = "用户名/密码错误";
                rsp.setCode(CodeEnum.USERPASS.getValue());
            }else if(e instanceof IncorrectCredentialsException){
                error = "会话过期，请重新登录";
                rsp.setCode(CodeEnum.SESSION_INVILAD.getValue());
            }else if("jCaptcha.error".equals(e)) {
                error = "验证码错误";
                rsp.setCode(CodeEnum.CODE_ERROR.getValue());
            }
            String exceptionClassName = (String)request.getAttribute("shiroLoginFailure");
            if(UnknownAccountException.class.getName().equals(exceptionClassName)) {
                error = "用户名/密码错误";
                rsp.setCode(CodeEnum.USERPASS.getValue());
            }
            System.out.println("error:"+error);
            onLoginFail(response,rsp.toString());
            return false;
        }
        return onLoginSuccess(token, subject, request, response);
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
    
    /**
     * This default implementation merely returns <code>true</code> if the request is an HTTP <code>POST</code>,
     * <code>false</code> otherwise. Can be overridden by subclasses for custom login submission detection behavior.
     *
     * @param request  the incoming ServletRequest
     * @param response the outgoing ServletResponse.
     * @return <code>true</code> if the request is an HTTP <code>POST</code>, <code>false</code> otherwise.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    protected boolean isLoginSubmission(ServletRequest request, ServletResponse response) {
        return (request instanceof HttpServletRequest) && WebUtils.toHttp(request).getMethod().equalsIgnoreCase(POST_METHOD);
    }

    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        String username = this.getUsernameParam();
        String password =this.getPasswordParam();
        return createToken(username, password, request, response);
    }

    protected boolean isRememberMe(ServletRequest request) {
        return WebUtils.isTrue(request, getRememberMeParam());
    }

    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject,
                                     ServletRequest request, ServletResponse response) throws Exception {
        issueSuccessRedirect(request, response);
        //we handled the success redirect directly, prevent the chain from continuing:
        return false;
    }

    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e,
                                     ServletRequest request, ServletResponse response) {
        setFailureAttribute(request, e);
        //login failed, let request continue back to the login page:
        return true;
    }

    protected void setFailureAttribute(ServletRequest request, AuthenticationException ae) {
        String className = ae.getClass().getName();
        request.setAttribute(getFailureKeyAttribute(), className);
    }

    protected String getUsername(ServletRequest request) {
        return this.getUsernameParam();
    }

    protected String getPassword(ServletRequest request) {
        return this.getPasswordParam();
    }
}
