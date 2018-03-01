package com.pygzx.zxtools.util.geo;

public abstract class AbstractGeoPointArea implements Area<GeoPoint>  {
	@Override
	public boolean include(GeoPoint point) {
		if (point == null) {
			return false;
		}
		return this.includeInternal(point);
	}

	protected abstract boolean includeInternal(GeoPoint point);
}
