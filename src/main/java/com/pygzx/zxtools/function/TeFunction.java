package com.pygzx.zxtools.function;

@FunctionalInterface
public interface TeFunction<T, U, V, R> {
	R apply(T t, U u, V v);
}
