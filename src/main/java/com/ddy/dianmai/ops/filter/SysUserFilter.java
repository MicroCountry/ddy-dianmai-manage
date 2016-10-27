package com.ddy.dianmai.ops.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.springframework.beans.factory.annotation.Autowired;

import com.ddy.dianmai.ops.Constants;
import com.ddy.dianmai.ops.service.UserService;


/**
 *
 * 过滤器
 * PathMatchingFilter 提供了基于Ant 风格的请求路径匹配功能及拦截器参数解析的功能，如
	“roles[admin,user]”自动根据“，”分割解析到一个路径参数配置并绑定到相应的路径
 */
public class SysUserFilter extends PathMatchingFilter {

    @Autowired
    private UserService userService;

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
    	System.out.println("SysUserFilter-onPreHandle");
        String username = (String)SecurityUtils.getSubject().getPrincipal();
        request.setAttribute(Constants.CURRENT_USER, userService.findByUsername(username));
        
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        httpServletRequest.getSession().setAttribute(Constants.CURRENT_USER, username);
        return true;
    }
}
