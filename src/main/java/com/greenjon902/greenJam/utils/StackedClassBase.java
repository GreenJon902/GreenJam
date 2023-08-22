package com.greenjon902.greenJam.utils;

import java.util.Stack;

/**
 * The base the class for a stacked class.
 * This is class with attributes that can be accessed via getters and setters, but you can call push on a class to save
 * its state, which can be then be reinstated after other modifications.
 * <p>
 * Implementers should assign each attribute an index in the list and use that in the getters and setters. You must also
 * call the init function with the correct number of attributes.
 */
public class StackedClassBase {
	/**
	 * A stack containing all the previous states of all objects. The objects array will always have the same length and
	 * each index will always equate to the same field.
	 */
	private final Stack<Object[]> stack;

	/**
	 * Create a new instance with each field filled with null.
	 * @param fields The number of fields to use
	 */
	protected StackedClassBase(int fields) {
		this(new Object[fields]);
	}

	/**
	 * Create a new instance with as many fields as supplied in objects, with those being the default values.
	 * @param objects The objects to use as default values
	 */
	protected StackedClassBase(Object... objects) {
		stack = new Stack<>();
		stack.push(objects);
	}

	/**
	 * Saves the current state of the class for reinstation during {@link #pop()}.
	 */
	public void push() {
		Object[] last = stack.peek();
		push(last.clone());
	}

	/**
	 * Saves the current state and then sets the values for each attribute to the values in items.
	 * @param items The items that will be the new attributes.
	 * @return
	 */
	protected void push(Object[] items) {
		if (items.length != length()) {
			throw new IllegalArgumentException("Item being pushed must be of same length, expected: " +
					length() + ", got: " + items.length);
		}
		stack.push(items);
	}

	/**
	 * Reinstates the last state for each attribute. Effectively undoing any modifications to the class.
	 * @return The state before it was overwritten
	 */
	protected Object[] pop() {
		return stack.pop();
	}

	/**
	 * Gets the number of attributes.
	 * @return The number
	 */
	protected int length() {
		return stack.peek().length;
	}

	/**
	 * Gets the attribute with the id in index
	 * @param index The id of that attribute
	 * @return The attribute
	 */
	protected Object get(int index) {
		return stack.peek()[index];
	}

	/**
	 * Gets all attributes in the order of their ids.
	 * @return The attributes
	 */
	protected Object[] get() {
		return stack.peek();
	}

	/**
	 * Updates the attribute with the id in index with the given value. This will be undone if {@link #pop()} is called.
	 * @param index The index to overwrite
	 * @param value The value to set it to.
	 */
	protected void set(int index, Object value) {
		stack.peek()[index] = value;
	}
}
