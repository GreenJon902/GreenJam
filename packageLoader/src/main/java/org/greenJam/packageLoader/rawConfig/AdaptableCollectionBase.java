package org.greenJam.packageLoader.rawConfig;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.IntFunction;

/**
 * A less-functional list of {@link T}, but this is able to be adapted by the {@link AdapterBase}.
 * @implNote We cannot implement list, as then Gson won't use our adapter
 */
public abstract class AdaptableCollectionBase<T, C extends Collection<T>> implements Iterable<T> {
	private final C contents;

	public AdaptableCollectionBase(C contents) {
		this.contents = contents;
	}

	@NotNull
	@Override
	public Iterator<T> iterator() {
		return contents.iterator();
	}

	@Override
	public String toString() {
		return "BetterAdaptableList{" +
				Arrays.toString(contents.stream().sorted(Comparator.comparing(Object::hashCode)).toArray()) +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		if (o instanceof AdaptableCollectionBase<?,?> that) {
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
	 * Checks if the list is empty.
	 * @return True if it is empty
	 */
	public boolean isEmpty() {
		return contents.isEmpty();
	}

	/**
	 * Gets the list turns it into an array.
	 * @return The array
	 */
	public T[] toArray(IntFunction<T[]> generator) {
		return contents.toArray(generator);
	}

	/**
	 * This class helps convert between a {@link Gson} object and a {@link AdaptableCollectionBase} holding T.
	 */
	public abstract class AdapterBase extends TypeAdapter<AdaptableCollectionBase<T, C>> {
		@Override
		public void write(JsonWriter out, AdaptableCollectionBase<T, C> value) {
			throw new RuntimeException("Write has not been implemented for DependencyItemAdapter");
		}
	}
}
