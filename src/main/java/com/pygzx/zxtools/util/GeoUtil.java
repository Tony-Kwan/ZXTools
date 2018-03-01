package com.pygzx.zxtools.util;

public class GeoUtil {
	public static final double EARTH_RADIUS = 6378100.0;

	public static double geoDistance(Double lng1, Double lat1, Double lng2, Double lat2) {
		Double latA = lat1 * Math.PI / 180.0;
		Double latB = lat2 * Math.PI / 180.0;
		Double lngA = lng1 * Math.PI / 180.0;
		Double lngB = lng2 * Math.PI / 180.0;
		return 2 * EARTH_RADIUS * Math.asin(Math.sqrt(oneMinusCos(latA - latB) + Math.cos(latA) * Math.cos(latB) * (oneMinusCos(lngA - lngB))) / 1.41421356237309504880);
	}

	private static double oneMinusCos(Double x) {
		Double s = Math.sin(x / 2);
		return s * s * 2;
	}

	public static boolean isLegalCoordinate(double lat, double lng) {
		return -90 <= lat && lat <= 90 && -180 <= lng && lng <= 180;
	}
}
