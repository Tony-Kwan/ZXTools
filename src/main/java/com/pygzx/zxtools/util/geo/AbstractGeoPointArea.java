package com.pygzx.zxtools.util.geo;

import java.io.Serializable;

public abstract class AbstractGeoPointArea implements Area<GeoPoint>, Serializable {
	@Override
	public boolean include(GeoPoint point) {
		if (point == null) {
			return false;
		}
		return this.includeInternal(point);
	}

	protected abstract boolean includeInternal(GeoPoint point);
}
