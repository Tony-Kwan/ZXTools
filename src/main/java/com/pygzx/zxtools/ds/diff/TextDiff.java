package com.pygzx.zxtools.ds.diff;

import com.pygzx.zxtools.ds.diff.impl.MyersDiff;

import java.util.List;

public class TextDiff extends MyersDiff<String> {

	private static final TextWrapper defaultWrapper = new DefaultTextWrapper();
	private static final TextWrapper insertWrapper = new InsertTextWrapper();
	private static final TextWrapper deleteWrapper = new DeleteTextWrapper();

	public static TextDiff of(List<String> src, List<String> dst) {
		return new TextDiff(src, dst);
	}

	public TextDiff(List<String> src, List<String> dst) {
		super(src, dst);
	}

	public String printDiff() {
		List<DiffBlock<String>> result = getDiff();
		StringBuilder sb = new StringBuilder();
		TextWrapper textWrapper;
		for (DiffBlock<String> block : result) {
			textWrapper = (block.type == DiffBlock.Type.INSERT) ? insertWrapper : (block.type == DiffBlock.Type.DELETE) ? deleteWrapper : defaultWrapper;
			for (String s : block.getContent()) {
				sb.append(textWrapper.wrap(s)).append('\n');
			}
		}
		return sb.toString();
	}

	private interface TextWrapper {
		String wrap(String content);
	}

	private static class InsertTextWrapper implements TextWrapper {
		@Override
		public String wrap(String content) {
			return "\033[1;32m+ " + content + "\033[0m";
		}
	}

	private static class DeleteTextWrapper implements TextWrapper {
		@Override
		public String wrap(String content) {
			return "\033[1;31m- " + content + "\033[0m";
		}
	}

	private static class DefaultTextWrapper implements TextWrapper {
		@Override
		public String wrap(String content) {
			return "  " + content;
		}
	}
}
