package org.greenJam.utils.inputStream;

public class NullInputStream extends StringInputStream {
	public NullInputStream() {
		super("", "null");
	}
}
