package com.ddy.dianmai.ops.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import com.ddy.dianmai.ops.exception.ScheduleException;
import com.ddy.dianmai.ops.exception.ShiroException;

/**
 * @ClassName HandlerExceptionInterceptor.java
 * @Description 控制器异常处理器
 *
 */
public class HandlerExceptionInterceptor extends AbstractHandlerExceptionResolver{
	
	private final Logger console = LoggerFactory.getLogger(this.getClass());
	
	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {		
		System.out.println("HandlerExceptionInterceptor-doResolveException");
		String msgInfo = "操作失败!!";
		String viewAame = "error/msg-error";
		ExceptionResponse exceptionResponse = ExceptionResponse.from(ex);
		ModelAndView modelAndView = new ModelAndView(viewAame);
		if(ex instanceof DuplicateKeyException){
			msgInfo = ex.getMessage();  //唯一约束冲突
		}else if(ex instanceof ScheduleException){
			msgInfo = ex.getMessage();  //定时任务操作出错
		}else if(ex instanceof DataIntegrityViolationException){
			msgInfo = ex.getMessage();  //非空错误
		}else if(ex instanceof UnauthorizedException){
			msgInfo = "没有权限 "+ex.getMessage();  //没有权限
		}else if(ex instanceof ShiroException){
			msgInfo = ex.getMessage();  //系统错误
		}else{
			viewAame = "controller-error";
			ex.printStackTrace();
		}
		console.info(ex.getMessage());
		modelAndView.addObject("message", msgInfo);
		modelAndView.addObject("error",exceptionResponse);
		return modelAndView;
	}

}
