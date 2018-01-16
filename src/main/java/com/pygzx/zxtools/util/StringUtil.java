package com.pygzx.zxtools.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class StringUtil {

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	public static List<Integer> str2IntList(String str, String splitRegex) {
		if (isEmpty(str)) {
			return Collections.emptyList();
		}
		return Arrays.asList(str.split(splitRegex)).stream()
			.map(s -> {
				try {
					return Integer.parseInt(s);
				} catch (Exception e) {
					return null;
				}
			})
			.filter(Objects::nonNull)
			.collect(Collectors.toList());
	}
}
