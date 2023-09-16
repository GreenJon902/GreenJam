package com.greenjon902.greenJam.api;

public interface InterfaceComparable {
	/**
	 * See {@link #equals_(Object, boolean)}
	 */
	default boolean equals_(Object o) {
		return equals_(o, false);
	}

	/**
	 * The equals implementation.
	 * @param o The other object
	 * @param sameClass Do we require the class to be the same
	 * @return True if it is equals
	 */
	boolean equals_(Object o, boolean sameClass);

	/**
	 * The hashCode implementation.
	 * @return the hash code
	 */
	int hashCode_();

}
