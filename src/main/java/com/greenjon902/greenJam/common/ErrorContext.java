package com.greenjon902.greenJam.common;

import com.greenjon902.greenJam.errorConditions.*;

import java.util.ArrayList;
import java.util.HashMap;

public class ErrorContext {
	HashMap<String, ArrayList<String>> error_assignations = new HashMap<>();
	HashMap<String, ErrorCondition> conditions = new HashMap<>();
	HashMap<Tuple.Two<String, String>, String> messages = new HashMap<>();

	public void assignError(String group, String condition_group, String message) {
		if (!error_assignations.containsKey("group")) {
			error_assignations.put(group, new ArrayList<>());
		}
		error_assignations.get(group).add(condition_group);
		messages.put(new Tuple.Two<>(group, condition_group), message);
	}

	private void checkNotMade(String group) {
		if (conditions.containsKey(group)) {
			Errors.errorConditions_conditionAlreadyExists(group);
		}
	}

	public void createReturningNull(String group) {
		checkNotMade(group);
		conditions.put(group, new ReturningNullErrorCondition());
	}

	public void createNextIs(String group, String string) {
		checkNotMade(group);
		conditions.put(group, new NextIsErrorCondition(string));
	}

	public void createNot(String group, String other_group) {
		checkNotMade(group);
		conditions.put(group, new NotErrorCondition(other_group));
	}

	public void createAnd(String group, String group1, String group2) {
		checkNotMade(group);
		conditions.put(group, new AndErrorCondition(group1, group2));
	}

	public void createEndOfFile(String group) {
		checkNotMade(group);
		conditions.put(group, new EndOfFileErrorCondition());
	}

	public ErrorCondition getError(String other_group) {
		return conditions.get(other_group);
	}

	public boolean hasErrorAssignationFor(String other_group) {
		return error_assignations.containsKey(other_group);
	}

	public String[] getErrorAssignations(String other_group) {
		return error_assignations.get(other_group).toArray(String[]::new);
	}

	public String getMessage(String group, String error_group) {
		return messages.get(new Tuple.Two<>(group, error_group));
	}
}
