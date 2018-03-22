package com.pygzx.zxtools.util.geo;

import org.apache.commons.lang3.tuple.Triple;
import org.bson.Document;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 行政区区域(用省市区的Adcode表示一个行政区域)
 */
public class AdminArea implements Area<Triple<Integer, Integer, Integer>>, Serializable {

	private static final long serialVersionUID = 1L;

	private List<Integer> adcodeList = Collections.emptyList();

	public AdminArea() {
	}

	public AdminArea(List<Integer> adcodeList) {
		this.adcodeList = adcodeList;
	}

	public List<Integer> getAdcodeList() {
		return adcodeList;
	}

	@Override
	public boolean include(Triple<Integer, Integer, Integer> adcodeTriple) {
		if (adcodeTriple == null) {
			return false;
		}
		if (adcodeTriple.getLeft() != null) {
			if (adcodeList.contains(adcodeTriple.getLeft())) {
				return true;
			}
			if (adcodeTriple.getMiddle() != null) {
				if (adcodeList.contains(adcodeTriple.getMiddle())) {
					return true;
				}
				if (adcodeTriple.getRight() != null) {
					return adcodeList.contains(adcodeTriple.getRight());
				}
			}
		}
		return false;
	}

	@Override
	public Document makeFilter() {
		return new Document("$in", adcodeList);
	}

	@Override
	public Map<String, Object> toMap() {
		return Collections.singletonMap("adcodes", adcodeList);
	}

	@Override
	public String toString() {
		return "AdminArea{" +
			"adcodeList=" + adcodeList +
			'}';
	}
}
