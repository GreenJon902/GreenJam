package com.greenjon902.greenJam.utils;

/**
 * A wrapper so we can return a null value, while also having the ability to say a function failed without using errors
 * (as slow).
 * @param <T> The type of object we are returning
 */
public class Result<T> {
	public final boolean isOk;
	private final T object;

	public Result(boolean isOk, T object) {
		this.isOk = isOk;
		this.object = object;
	}

	public static <T> Result<T> ok(T object) {
		return new Result<>(true, object);
	}

	public static <T> Result<T> fail() {
		return new Result<>(false, null);
	}

	/**
	 * Returns the object.
	 * @return the object
	 * @throws IllegalStateException If {@link #isOk} is false
	 */
	public T unwrap() throws IllegalStateException {
		if (!isOk) {
			throw new IllegalStateException("Tried to unwrap a failed result");
		}
		return object;
	}
}
