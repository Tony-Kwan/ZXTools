package test.com.pygzx.zxtools.ds;

import com.pygzx.zxtools.ds.SegmentTree;
import com.pygzx.zxtools.function.TernaryOperator;
import com.quickutil.platform.FileUtil;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;

public class SegmentTreeTest {
	private SegmentTree<Integer> sumTree;
	private SegmentTree<Integer> maxTree;

	@Before
	public void before() throws Exception {
		// Update & Sum
		{
			BinaryOperator<Integer> pushUpOp = (l, r) -> l + r;
			BinaryOperator<Integer> updateLazyTarOp = (old, update) -> {
				if (old == null) {
					return update;
				}
				return old + update;
			};
			TernaryOperator<Integer> pushDownOp = (old, lazy, len) -> old + lazy * len;

			List<Integer> initList = Arrays.asList(1, 2, 3, 4, 5);
			sumTree = new SegmentTree<>(initList, pushUpOp, updateLazyTarOp, pushDownOp);
		}

		// Update & Max
		{
			BinaryOperator<Integer> pushUpOp = (l, r) -> Math.max(l.intValue(), r.intValue());
			BinaryOperator<Integer> updateLazyTarOp = (old, update) -> {
				if (old == null) {
					return update;
				}
				return old + update;
			};
			TernaryOperator<Integer> pushDownOp = (old, lazy, len) -> old + lazy;

//			List<Integer> initList = Arrays.asList(1, 2, 3, 4, 5);
//			maxTree = new SegmentTree<>(initList, pushUpOp, updateLazyTarOp, pushDownOp);
			maxTree = new SegmentTree<>(4, 0, pushUpOp, updateLazyTarOp, pushDownOp);
		}
	}

	@After
	public void after() throws Exception {
	}

	@Test
	public void testSum() {
		assertEquals(Integer.valueOf(15), sumTree.query(0, 4));
	}

	@Test
	public void testMax() {
		maxTree.update(0, 1, 1);
		maxTree.update(1, 2, 1);
		maxTree.update(2, 3, 1);
		maxTree.update(0, 3, 10);
//		assertEquals(Integer.valueOf(2), maxTree.query(0, 1));
		System.out.println(maxTree.query(0, 3));
	}
} 
