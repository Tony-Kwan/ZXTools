package com.pygzx.zxtools.ds.diff.impl;

import com.pygzx.zxtools.ds.DefaultHashMap;
import com.pygzx.zxtools.ds.diff.Diff;
import com.pygzx.zxtools.ds.diff.DiffBlock;

import java.util.*;
import java.util.stream.Collectors;

public class MyersDiff<T> implements Diff<T> {

	private List<T> src = Collections.emptyList();
	private List<T> dst = Collections.emptyList();

	private int N = 0;
	private int M = 0;

	private MyersDiff() {}

	public MyersDiff(List<T> src, List<T> dst) {
		if (src != null) {
			this.src = new ArrayList<>(src);
		}
		if (dst != null) {
			this.dst = new ArrayList<>(dst);
		}
		N = this.src.size();
		M = this.dst.size();
	}

	public List<T> getSrc() {
		return src;
	}

	public List<T> getDst() {
		return dst;
	}

	@Override
	public List<DiffBlock<T>> getDiff() {
		List<Snake> snakeList = getSnakeList();
		Optional<Snake> optSnake = snakeList.stream().max(Comparator.comparing(snake -> snake.d));
		if (!optSnake.isPresent()) {
			return Collections.emptyList();
		}
		Snake endSnake = optSnake.get();
		int d = endSnake.d;
		List<Point> path = new ArrayList<>(d);
		Point p = new Point(N, M);
		Map<Integer, List<Snake>> snakeListMap = snakeList.stream().collect(Collectors.groupingBy(snake -> snake.d));
		for (int i = d; i >= 0; i--) {
			List<Snake> sl = snakeListMap.get(i);
			for (Snake snake : sl) {
				if (snake.end.equals(p)) {
					path.add(snake.end);
					if (!snake.end.equals(snake.mid)) {
						for (int j = snake.end.x - 1; j > snake.mid.x; j--) {
							path.add(new Point(j, snake.end.y - (snake.end.x - j)));
						}
						path.add(snake.mid);
					}
					p = snake.start;
					break;
				}
			}
		}

		List<DiffBlock<T>> result = new ArrayList<>(path.size());
		Point p1 = path.get(path.size() - 1), p2;
		List<T> itemList = new ArrayList<>();
		DiffBlock.Type t1 = null, t2 = null;
		T value;
		for (int i = path.size() - 2; i >= 0; i--) {
			p2 = path.get(i);
			if (p2.x > p1.x && p2.y > p1.y) { //MOVE
				t2 = DiffBlock.Type.MOVE;
				value = src.get(p2.x - 1);
			} else if (p2.x > p1.x) { //DELETE
				t2 = DiffBlock.Type.DELETE;
				value = src.get(p2.x - 1);
			} else { //INSERT: p2.y > p1.y
				t2 = DiffBlock.Type.INSERT;
				value = dst.get(p2.y - 1);
			}
			p1 = p2;

			if (t1 == null) {
				t1 = t2;
			}
			if (t1 != t2) {
				result.add(new DiffBlock<>(t1, itemList));
				itemList = new ArrayList<>();
				t1 = t2;
			}
			itemList.add(value);
		}
		if (t2 != null) {
			result.add(new DiffBlock<>(t2, itemList));
		}
		return result;
	}

	private List<Snake> getSnakeList() {
		int max = N + M;
		int v[] = new int[(max * 2) + 1];
		Arrays.fill(v, Integer.MAX_VALUE);
		v[max + 1] = 0;
		int xStart, yStart, xMid, yMid, xEnd, yEnd;
		List<Snake> snakeList = new ArrayList<>(max);
		boolean stop = false;
		for (int d = 0; d <= max && !stop; d++) {
			for (int k = -d; k <= d; k += 2) {
				int kk = k + max;
				if (k == -d || (k != d && v[kk - 1] < v[kk + 1])) {
					xStart = v[kk + 1];
					yStart = xStart - (k + 1);
					xMid = xStart;
				} else {
					xStart = v[kk - 1];
					yStart = xStart - (k - 1);
					xMid = xStart + 1;
				}
				yMid = xMid - k;

				xEnd = xMid;
				yEnd = yMid;
				while (xEnd < N
					&& yEnd < M
					&& src.get(xEnd).hashCode() == dst.get(yEnd).hashCode()
					&& src.get(xEnd).equals(dst.get(yEnd))) {
					xEnd++;
					yEnd++;
				}
				v[kk] = xEnd;
				snakeList.add(new Snake(
					d,
					new Point(xStart, yStart),
					new Point(xMid, yMid),
					new Point(xEnd, yEnd)
				));

				if (xEnd >= N && yEnd >= M) {
					stop = true;
					break;
				}
			}
		}
		return snakeList;
	}

	private static class Snake {
		private int d;
		private Point start;
		private Point mid;
		private Point end;

		public Snake(int d, Point start, Point mid, Point end) {
			this.d = d;
			this.start = start;
			this.mid = mid;
			this.end = end;
		}

		@Override
		public String toString() {
			final StringBuffer sb = new StringBuffer("Snake{");
			sb.append("d=").append(d);
			sb.append(", start=").append(start);
			sb.append(", mid=").append(mid);
			sb.append(", end=").append(end);
			sb.append('}');
			return sb.toString();
		}
	}

	private static class Point {
		private int x;
		private int y;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Point point = (Point) o;
			return x == point.x &&
				y == point.y;
		}

		@Override
		public int hashCode() {
			return com.google.common.base.Objects.hashCode(x, y);
		}

		@Override
		public String toString() {
			final StringBuffer sb = new StringBuffer("Point{");
			sb.append("x=").append(x);
			sb.append(", y=").append(y);
			sb.append('}');
			return sb.toString();
		}
	}
}
