package com.greenjon902.greenJam.utils.inputStream;

import com.greenjon902.greenJam.api.InputStream;
import com.greenjon902.greenJam.utils.FieldStringWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Stack;

public class StringInputStream extends FieldStringWriter.Abstract implements InputStream  {
	private final String string;
	private int location = 0;
	private Stack<Integer> locationStack = new Stack<>();
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
	public @NotNull String getAll() {
		return string;
	}

	@Override
	public void push() {
		locationStack.push(location);
	}

	@Override
	public void pop() {
		location = locationStack.pop();
	}
}
