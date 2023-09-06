package com.greenjon902.greenJam.testUtils;

import com.greenjon902.greenJam.api.core.InputStream;
import com.greenjon902.greenJam.api.core.PackageItem;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * A {@link PackageItem} that acts like a record, but uses the correct equals and hashCode functions.
 */
public class MapValueInputStream extends MapValueBase implements InputStream {
	public MapValueInputStream(Map<String, Object> values) {
		super(values);
	}

	@Override
	public boolean equals(Object o) {
		return equals_(o);
	}

	@Override
	public int hashCode() {
		return hashCode_();
	}

	@Override
	public @NotNull String name() {
		return (String) values.get("name");
	}

	@Override
	public int size() {
		return getAll().length();
	}

	@Override
	public @NotNull String peek(int n) {
		throw new IllegalStateException();
	}

	@Override
	public void skip(int n) {
		throw new IllegalStateException();
	}

	@Override
	public int location() {
		return (int) values.get("location");
	}

	@Override
	public @NotNull String getAll() {
		return (String) values.get("string");
	}

	/**
	 * A subclass that is technically a different class, but apart from that is identical.
	 */
	public static class MapValueInputStream2 extends MapValueInputStream {
		public MapValueInputStream2(Map<String, Object>  values) {
			super(values);
		}
	}
}
