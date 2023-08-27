package com.greenjon902.greenJam.core.packageLoader.rawConfig;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * A less-functional list of {@link DependencyRawConfig}, but this is able to be adapted by the
 * {@link DependencyInfoAdapter}. This holds all the versions of the same dependency, even if there is only one
 * loaded version.
 */
@JsonAdapter(DependencyList.DependencyInfoAdapter.class)
public class DependencyList implements Iterable<DependencyRawConfig> {
	private final List<DependencyRawConfig> contents;

	public DependencyList(DependencyRawConfig... contents) {
		this.contents = Arrays.asList(contents);
	}

	@NotNull
	@Override
	public Iterator<DependencyRawConfig> iterator() {
		return contents.iterator();
	}

	@Override
	public String toString() {
		return "DependencyList{" + contents + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		if (o instanceof DependencyList that) {
			return Objects.equals(contents, that.contents);
		} else {
			return Objects.equals(contents, o);
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(contents);
	}

	/**
	 * This class helps convert between a {@link Gson} object and a {@link DependencyList}.
	 * It can detect three types of dependency declarations:
	 * <br><br>
	 * <pre>
	 * "&lt;package&gt;": "&lt;version&gt;"
	 * "&lt;package&gt;": { "version": "&lt;version&gt;" }
	 * "&lt;package&gt;": [{ "version": "&lt;version&gt;" }]
	 * </pre>
	 */
	public static class DependencyInfoAdapter extends TypeAdapter<DependencyList> {
		@Override
		public void write(JsonWriter out, DependencyList value) {
			throw new RuntimeException("Write has not been implemented for DependencyItemAdapter");
		}

		@Override
		public DependencyList read(JsonReader in) throws IOException {
			DependencyList dependencyItem;
			dependencyItem = switch (in.peek()) {
				case BEGIN_OBJECT -> new DependencyList(new Gson().getAdapter(DependencyRawConfig.class).read(in));
				case BEGIN_ARRAY -> new DependencyList(new Gson().getAdapter(DependencyRawConfig[].class).read(in));
				case STRING -> new DependencyList(new DependencyRawConfig("", in.nextString()));
				default -> throw new RuntimeException("Unexpected token " + in.peek());
			};
			return dependencyItem;
		}
	}
}
