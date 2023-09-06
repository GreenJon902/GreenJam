package com.greenjon902.greenJam.utils;

public class NullInputStream extends StringInputStream {
	public NullInputStream() {
		super("", "null");
	}
}
