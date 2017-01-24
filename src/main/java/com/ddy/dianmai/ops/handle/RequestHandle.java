package com.ddy.dianmai.ops.handle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ddy.dianmai.web.util.HttpUtil;


public class RequestHandle implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String content = HttpUtil.getRequestContent();
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		 JsonObject jsonObject = null;
		if(StringUtils.isNotBlank(content)){
			 JsonParser parser = new JsonParser();
             JsonElement jsonEle = parser.parse(content);
             jsonObject = jsonEle.getAsJsonObject();
             HttpUtil.setRequestAttribuesDDYData(jsonObject.toString());
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
