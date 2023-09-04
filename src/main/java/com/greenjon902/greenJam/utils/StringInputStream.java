package com.greenjon902.greenJam.utils;

import com.greenjon902.greenJam.api.core.InputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StringInputStream extends FieldStringWriter.Abstract implements InputStream  {
	private final String string;
	private int location = 0;
	private @Nullable String name;

	public StringInputStream(String string, String name) {
		this.string = string;
		this.name = name;
	}

	public StringInputStream(String string) {
		this(string, null);
	}

	@Override
	public int hashCode() {
		return hashCode_();
	}

	@Override
	public boolean equals(Object obj) {
		return equals_(obj);
	}

	@Override
	public @NotNull String name() {
		return name == null ? "<string>" : name;
	}

	@Override
	public int size() {
		return string.length();
	}

	@Override
	public @NotNull String peek(int n) {
		return string.substring(location, location + n);
	}

	@Override
	public void skip(int n) {
		location += n;
	}

	@Override
	public int location() {
		return location;
	}

	@Override
	public String getAll() {
		return string;
	}
}
