package org.greenJam.api;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

// TODO: Figure out what error is thrown and how end of stream is detected

public interface InputStream extends InterfaceComparable, FieldStringWriter {
	/**
	 * Gets the name of this InputStream.
	 * @return The character amount
	 */
	@NotNull String name();

	/**
	 * Gets the amount of characters in this {@link InputStream}.
	 * @return The character amount
	 */
	int size();

	/**
	 * Gets the next "n" characters and returns them.
	 * @param n The number of characters to get
	 * @return The characters
	 */
	@NotNull String peek(int n);

	/**
	 * Skips the next "n" characters.
	 * @param n The number of characters to get
	 */
	void skip(int n);

	/**
	 * Gets the next "n" characters and returns them, in the process consuming them
	 * @param n The number of characters to get
	 * @return The characters
	 */
	default @NotNull String next(int n) {
		String string = peek(n);
		skip(n);
		return string;
	}

	/**
	 * Gets the current location.
	 * @return The location
	 */
	int location();

	/**
	 * Gets the entire contents of this stream from start to finish.
	 * @return The entire contents
	 */
	@NotNull String getAll();

	@Override
	default void writeFields(StringBuilder sb) {
		sb.append("name=").append(name());
		sb.append(", location=").append(location());
		sb.append(", size=").append(size());
	}

	@Override
	default boolean equals_(Object o, boolean sameClass) {
		if (this == o) return true;
		if (sameClass && (this.getClass() != o.getClass())) return false;
		if (!(o instanceof InputStream that)) return false;
		// Compare location and size first, as that is most likely to be different and fastest to check
		return Objects.equals(location(), that.location()) && Objects.equals(size(), size()) &&
				Objects.equals(name(), that.name()) && Objects.equals(getAll(), that.getAll());
	}

	@Override
	default int hashCode_() {
		return Objects.hash(name(), location(), getAll());
	}

	/**
	 * Saves the location, so it can be reverted to later using a {@link #pop}.
	 */
	void push();

	/**
	 * Reverts the location to what it was when it was pushed.
	 */
	void pop();

	/**
	 * Consume if the next characters are equal to value, this will also fail if the end of file is reached.
	 * @param value The value to check
	 * @return Whether it was consumed
	 */
	default boolean consumeIf(String value) {
		if (location() + value.length() <= size() && peek(value.length()).equals(value)) {
			skip(value.length());  // Consume it
			return true;
		}
		return false;
	}

	/**
	 * Check if we can read the next n characters.
	 * @param n The number of characters to check
	 * @return The result of the check.
	 */
	default boolean hasNext(int n) {
		return location() + n <= size();
	}

	/**
	 * Set the location to the value at "i".
	 * @param i The value to go to
	 */
	void seek(int i);

	/**
	 * Does the same as {@link #pop()} but without changing any values.
	 */
	default void popKeep() {
		int location = location();
		pop();
		seek(location);
	}
}
