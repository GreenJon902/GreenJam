package org.greenJam.testUtils;

import org.greenJam.utils.FieldStringWriter;

import java.util.Map;

public abstract class MapValueBase extends FieldStringWriter.Abstract implements FieldStringWriter {
	protected final Map<String, Object> values;

	public MapValueBase(Map<String, Object> values) {
		this.values = values;
	}
}
