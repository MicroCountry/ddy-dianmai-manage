package com.ddy.dianmai.ops.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @ClassName Operator.java
 * @Description 标记操作记录
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.METHOD,ElementType.TYPE})  
public @interface Operator {

	public String story() default "";//执行方法故事
	public String name() default "";//类名
	public boolean operatorClass() default true;//是否是标记日志类
}
