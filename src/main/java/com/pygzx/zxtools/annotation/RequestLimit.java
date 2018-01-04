package com.pygzx.zxtools.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequestLimit {
	/**
	 * 间隔时间内允许访问次数, 默认5次
	 */
	int value() default 5;

	/**
	 * 间隔时间(单位:秒), 默认60秒
	 */
	int interval() default 60;
}
