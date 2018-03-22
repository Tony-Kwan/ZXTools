package com.pygzx.zxtools.util.geo;

import com.pygzx.zxtools.util.GeoUtil;
import org.bson.Document;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CircleArea extends AbstractGeoPointArea implements Serializable {

	private static final long serialVersionUID = 1L;

	private double longitude;
	private double latitude;
	private double radius;

	public CircleArea(double longitude, double latitude, double radius) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.radius = radius;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getRadius() {
		return radius;
	}

	@Override
	public String toString() {
		return "CircleArea{" +
			"longitude=" + longitude +
			", latitude=" + latitude +
			", radius=" + radius +
			'}';
	}

	@Override
	public Document makeFilter() {
		return new Document("$geoWithin", new Document("$centerSphere", Arrays.asList(
			Arrays.asList(longitude, latitude), radius / GeoUtil.EARTH_RADIUS
		)));
	}

	@Override
	public boolean includeInternal(GeoPoint point) {
		return GeoUtil.geoDistance(longitude, latitude, point.getLongitude(), point.getLatitude()) <= radius;
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> ret = new HashMap<>();
		ret.put("latitude", latitude);
		ret.put("longitude", longitude);
		ret.put("radius", radius);
		return ret;
	}
}
