package com.pygzx.zxtools.ds;

import com.pygzx.zxtools.function.TeFunction;

import java.util.Collections;
import java.util.List;
import java.util.function.BinaryOperator;

public class SegmentTree<T> {
	private static class Node<T> {
		int leftBound;
		int rightBound;
		Node<T> left;
		Node<T> right;
		T value;
		T lazy;

		int mid() {
			return (this.leftBound + this.rightBound) / 2;
		}

		int length() {
			return rightBound - leftBound + 1;
		}
	}

	private Node<T> root;
	private BinaryOperator<T> pushUpOp;                 // (l, r) -> p
	private BinaryOperator<T> updateLazyTarOp;          // (old, update) -> newLazy
	private TeFunction<T, T, Integer, T> pushDownOp;    // (old, lazy, length) -> newValue
	private int nodeCount = 0;

	public SegmentTree(int len, T initValue, BinaryOperator<T> pushUpOp, BinaryOperator<T> updateLazyTarOp, TeFunction<T, T, Integer, T> pushDownOp) {
		this(Collections.nCopies(len, initValue), pushUpOp, updateLazyTarOp, pushDownOp);
	}

	public SegmentTree(List<T> initList, BinaryOperator<T> pushUpOp, BinaryOperator<T> updateLazyTarOp, TeFunction<T, T, Integer, T> pushDownOp) {
		if (initList == null) {
			throw new NullPointerException("initList == null");
		}
		if (initList.isEmpty()) {
			throw new IndexOutOfBoundsException("initList isEmpty");
		}
		if (pushUpOp == null || updateLazyTarOp == null || pushDownOp == null) {
			throw new NullPointerException("pushUpOp == null || updateLazyTarOp == null || pushDownOp == null");
		}
		this.pushUpOp = pushUpOp;
		this.updateLazyTarOp = updateLazyTarOp;
		this.pushDownOp = pushDownOp;
		this.root = build(0, initList.size() - 1, initList);
	}

	private Node<T> build(int l, int r, List<T> initList) {
		this.nodeCount++;
		Node<T> node = new Node<>();
		node.leftBound = l;
		node.rightBound = r;
		node.lazy = null;

		if (l == r) {
			node.value = initList.get(l);
			return node;
		}
		int mid = node.mid();
		node.left = build(l, mid, initList);
		node.right = build(mid + 1, r, initList);
		pushUp(node);
		return node;
	}

	private void pushUp(Node<T> node) {
		node.value = this.pushUpOp.apply(node.left.value, node.right.value);
	}

	private void pushDown(Node<T> node) {
		if (node.lazy != null) {
			node.left.lazy = this.updateLazyTarOp.apply(node.left.lazy, node.lazy);
			node.right.lazy = this.updateLazyTarOp.apply(node.right.lazy, node.lazy);
			node.left.value = this.pushDownOp.apply(node.left.value, node.left.lazy, node.left.length());
			node.right.value = this.pushDownOp.apply(node.right.value, node.right.lazy, node.right.length());
			node.lazy = null;
		}
	}

	private void update0(Node<T> node, int l, int r, T value) {
		if (l == node.leftBound && node.rightBound == r) {
			node.lazy = this.updateLazyTarOp.apply(node.lazy, value);
			node.value = this.pushDownOp.apply(node.value, node.lazy, node.length());
			return;
		}
		pushDown(node);
		int mid = node.mid();
		if (r <= mid) {
			update0(node.left, l, r, value);
		} else if (l > mid) {
			update0(node.right, l, r, value);
		} else {
			update0(node.left, l, mid, value);
			update0(node.right, mid + 1, r, value);
		}
		pushUp(node);
	}

	public void update(int l, int r, T value) {
		if (l < 0 || r > this.root.rightBound) {
			throw new IndexOutOfBoundsException();
		}
		update0(this.root, l, r, value);
	}

	private T query0(Node<T> node, int l, int r) {
		if (l == node.leftBound && node.rightBound == r) {
			return node.value;
		}
		pushDown(node);
		int mid = node.mid();
		if (r <= mid) {
			return query0(node.left, l, r);
		} else if (l > mid) {
			return query0(node.right, l, r);
		} else {
			T leftR = query0(node.left, l, mid);
			T rightR = query0(node.right, mid + 1, r);
			return this.pushUpOp.apply(leftR, rightR);
		}
	}

	public T query(int l, int r) {
		if (l < 0 || r > this.root.rightBound) {
			throw new IndexOutOfBoundsException();
		}
		return query0(this.root, l, r);
	}

	public int size() {
		return this.nodeCount;
	}
}
