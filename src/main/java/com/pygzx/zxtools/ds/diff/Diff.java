package com.pygzx.zxtools.ds.diff;

import java.util.List;

public interface Diff<T> {

	List<DiffBlock<T>> getDiff();


}
