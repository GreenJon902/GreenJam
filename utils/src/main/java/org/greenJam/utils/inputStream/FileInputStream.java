package org.greenJam.utils.inputStream;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

// TODO: This could probably be more memory efficient

public class FileInputStream extends StringInputStream  {
	public FileInputStream(File file) throws IOException {
		// Can't try with resource as super needs to be on the first line
		//noinspection resource
		super(Arrays.toString(new java.io.FileInputStream(file).readAllBytes()), file.toString());
	}
}
