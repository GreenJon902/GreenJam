package com.greenjon902.greenJam.core.packageLoader.rawConfig;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * A less-functional list of {@link T}, but this is able to be adapted by the {@link AdapterBase}.
 */
public abstract class AdaptableListBase<T> implements Iterable<T> {
	// Cannot implement list, as then Gson won't use our adapter

	private final List<T> contents;

	public AdaptableListBase(T... contents) {
		this.contents = Arrays.asList(contents);
	}

	@NotNull
	@Override
	public Iterator<T> iterator() {
		return contents.iterator();
	}

	@Override
	public String toString() {
		return "BetterAdaptableList{" + contents + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		if (o instanceof AdaptableListBase<?> that) {
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
	 * This class helps convert between a {@link Gson} object and a {@link AdaptableListBase} holding T.
	 */
	public abstract class AdapterBase extends TypeAdapter<AdaptableListBase<T>> {
		@Override
		public void write(JsonWriter out, AdaptableListBase<T> value) {
			throw new RuntimeException("Write has not been implemented for DependencyItemAdapter");
		}
	}
}
