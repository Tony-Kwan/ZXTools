package com.pygzx.zxtools.util.geo;

import org.bson.Document;

import java.util.Map;

public interface Area<T> {
	boolean include(T point);

	Document makeFilter();

	Map<String, Object> toMap();
}
