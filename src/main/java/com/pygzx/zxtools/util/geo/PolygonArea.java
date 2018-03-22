package com.pygzx.zxtools.util.geo;

import org.bson.Document;

import java.io.Serializable;
import java.util.*;

public class PolygonArea extends AbstractGeoPointArea implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<GeoPoint> coordinates;

	public PolygonArea(List<GeoPoint> coordinates) {
		this.coordinates = coordinates;
	}

	public List<GeoPoint> getCoordinates() {
		return coordinates;
	}

	@Override
	public String toString() {
		return "PolygonArea{" +
			"coordinates=" + coordinates +
			'}';
	}

	@Override
	public Document makeFilter() {
		List<List<Double>> polygon = new ArrayList<>();
		for (GeoPoint point : coordinates) {
			polygon.add(Arrays.asList(point.getLongitude(), point.getLatitude()));
		}
		return new Document("$geoWithin", new Document("$polygon", polygon));
	}

	@Override
	public boolean includeInternal(GeoPoint point) {
		int j = coordinates.size() - 1;
		boolean oddNodes = false;
		for (int i = 0; i < coordinates.size(); i++) {
			GeoPoint nodeI = coordinates.get(i);
			GeoPoint nodeJ = coordinates.get(j);
			if (point.isLatLngEqual(nodeI) || point.isLatLngEqual(nodeJ))
				return true;
			double slop_A = (point.getLatitude() - nodeI.getLatitude()) / (point.getLongitude() - nodeI.getLongitude());
			double slop_B = (point.getLatitude() - nodeJ.getLatitude()) / (point.getLongitude() - nodeJ.getLongitude());
			if ((nodeI.getLatitude() < point.getLatitude() && nodeJ.getLatitude() >= point.getLatitude())
				|| (nodeJ.getLatitude() < point.getLatitude() && nodeI.getLatitude() >= point.getLatitude())) {
				if (Math.abs(slop_A - slop_B) < GeoPoint.INFS)
					return true;
				if (nodeI.getLongitude() + (point.getLatitude() - nodeI.getLatitude()) / (nodeJ.getLatitude() - nodeI.getLatitude()) * (nodeJ.getLongitude() - nodeI.getLongitude()) < point.getLongitude())
					oddNodes = !oddNodes;
			}
			j = i;
		}
		return oddNodes;
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> ret = new HashMap<>();
		List<List<Double>> list = new ArrayList<>(coordinates.size());
		for (GeoPoint point : coordinates)
			list.add(Arrays.asList(point.getLongitude(), point.getLatitude()));
		ret.put("points", list);
		return ret;
	}
}
