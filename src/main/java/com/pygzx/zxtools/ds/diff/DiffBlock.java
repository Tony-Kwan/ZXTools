package com.pygzx.zxtools.ds.diff;

import java.util.List;

public class DiffBlock<T> {
	public enum Type {

		MOVE,

		INSERT,

		DELETE
	}

	Type type;
	List<T> content;

	public DiffBlock(Type type, List<T> content) {
		this.type = type;
		this.content = content;
	}

	public Type getType() {
		return type;
	}

	public List<T> getContent() {
		return content;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("DiffBlock{");
		sb.append("type=").append(type);
		sb.append(", content=").append(content);
		sb.append('}');
		return sb.toString();
	}
}
