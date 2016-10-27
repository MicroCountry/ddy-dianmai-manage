package com.ddy.dianmai.ops.interceptor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import com.ddy.dianmai.ops.annotation.Operator;
import com.ddy.dianmai.ops.po.OperationLog;
import com.ddy.dianmai.ops.service.OperationLogService;
import com.ddy.dianmai.ops.service.UserService;
import com.ddy.dianmai.ops.util.SpringUtils;
import com.google.gson.Gson;

/**
 * @ClassName OperationInterceptor.java
 * @Description 操作记录拦截器
 * 
 */
@Aspect
public class OperationInterceptor {

	private static OperationLogService operationLogService = SpringUtils.getBean(OperationLogService.class);
	private static UserService userService = SpringUtils.getBean(UserService.class);
	private static Gson gson = new Gson();
	
	
	/*@Before(value = "execution(* com.shiro.service..*.*(..))")
	public void before(JoinPoint joinPoint){
		Signature signature = joinPoint.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method method = methodSignature.getMethod();
		if (method.isAnnotationPresent(Operator.class)) {
			Object[] objs=joinPoint.getArgs();
	        String a = "";
	        for(Object object : objs){
	        	a += object+" , ";
	        }
	        System.out.println(a);
		}
	}*/
	
	@Around(value = "execution(* com.ddy.dianmai.ops.service..*.*(..))")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		Object object = pjp.proceed();
		Signature signature = pjp.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method method = methodSignature.getMethod();
		if (method.isAnnotationPresent(Operator.class)) {
			List<Object> lists = new ArrayList<Object>();
			for (Object object2 : pjp.getArgs()) {
				Class<?> executeClass = object2.getClass();
				if (executeClass.isAnnotationPresent(Operator.class)) {
					lists.add(gson.toJson(object2));
				} else {
					lists.add(object2);
				}
			}
			OperationLog _operatorLog = new OperationLog();
			_operatorLog.setCreateTime(new Date().getTime());
			_operatorLog.setAfterContent(gson.toJson(lists));
			if (method.getAnnotation(Operator.class).story().indexOf(":") > -1) {
				String story = method.getAnnotation(Operator.class).story();
				Operator cc = pjp.getTarget().getClass().getAnnotation(Operator.class);
				if (null != cc) {
					String name = cc.name();
					_operatorLog.setStory(story.replace(":", name));
				} else {
					_operatorLog.setStory(story);
				}
			} else {
				_operatorLog.setStory(method.getAnnotation(Operator.class).story());
			}
			_operatorLog.setUserId(userService.findByUsername((String) SecurityUtils.getSubject().getPrincipal()).getId());
			operationLogService.insert(_operatorLog);
		}
		return object;
	}
}