package com.pygzx.zxtools.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AccessLog {
	enum Type {
		GET("get"),
		ADD("add"),
		UPDATE("update"),
		REMOVE("remove"),
		EXPORT("export"),
		LOGIN("login"),
		LOGOUT("logout");

		String type;
		Type(String type) {
			this.type = type;
		}

		@Override
		public String toString() {
			return type;
		}
	}

	/**
	 * 操作类型
	 */
	Type type();

	/**
	 * 操作描述
	 */
	String description() default "";

	/**
	 * 不写入数据库的敏感字段的名称列表
	 * eg: "password", "pwd"
	 */
	String[] ignoredParams() default {};
}
