package com.pygzx.zxtools.util.geo;

public class GeoPoint {
	public static final double INFS = 1e-8;

	private double longitude;
	private double latitude;

	public GeoPoint(double longitude, double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	@Override
	public String toString() {
		return "GeoPoint{" +
			"latitude=" + latitude +
			", longitude=" + longitude +
			'}';
	}

	boolean isLatLngEqual(GeoPoint point) {
		return Math.abs(longitude - point.longitude) < INFS && Math.abs(latitude - point.latitude) < INFS;
	}
}
