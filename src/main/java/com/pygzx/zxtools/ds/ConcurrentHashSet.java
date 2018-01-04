package com.pygzx.zxtools.ds;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A ConcurrentHashSet.
 *
 * Offers same concurrency as ConcurrentHashMap but for a Set
 */
public class ConcurrentHashSet<E> extends AbstractSet<E> implements ConcurrentSet<E> {

	private final ConcurrentMap<E, Object> theMap;

	private static final Object DUMMY = new Object();

	public ConcurrentHashSet() {
		theMap = new ConcurrentHashMap<>();
	}

	@Override
	public int size() {
		return theMap.size();
	}

	@Override
	public Iterator<E> iterator() {
		return theMap.keySet().iterator();
	}

	@Override
	public boolean isEmpty() {
		return theMap.isEmpty();
	}

	@Override
	public boolean add(final E o) {
		return theMap.put(o, ConcurrentHashSet.DUMMY) == null;
	}

	@Override
	public boolean contains(final Object o) {
		return theMap.containsKey(o);
	}

	@Override
	public void clear() {
		theMap.clear();
	}

	@Override
	public boolean remove(final Object o) {
		return theMap.remove(o) == ConcurrentHashSet.DUMMY;
	}

	@Override
	public boolean addIfAbsent(final E o) {
		Object obj = theMap.putIfAbsent(o, ConcurrentHashSet.DUMMY);

		return obj == null;
	}

}
