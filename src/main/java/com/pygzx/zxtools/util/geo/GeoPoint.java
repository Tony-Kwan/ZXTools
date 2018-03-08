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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		GeoPoint point = (GeoPoint) o;

		if (Double.compare(point.longitude, longitude) != 0) {
			return false;
		}
		return Double.compare(point.latitude, latitude) == 0;
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		temp = Double.doubleToLongBits(longitude);
		result = (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(latitude);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
}
