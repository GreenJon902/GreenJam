package com.greenjon902.greenJam.core;

/**
 * IntelliJ Templates:
 * <br><br>
 * writeFields:
 * <pre>public void writeFields(StringBuilder sb) {
 * #set ($i = 0)
 * #foreach ($member in $members)
 * #if ($i == 0)
 * sb.append("##
 * #else
 * sb.append(", ##
 * #end
 * #if ($member.string)
 * $member.name='")##
 * #else
 * $member.name=")##
 * #end
 * #if ($member.primitiveArray || $member.objectArray)
 * .append(java.util.Arrays.toString($member.name));
 * #elseif ($member.set)
 * .append(Arrays.toString(${member.name}.stream().sorted(Comparator.comparing(Object::hashCode)).toArray()));
 * #elseif ($member.string)
 * .append($member.accessor).append('\'');
 * #else
 * .append($member.accessor);
 * #end
 * #set ($i = $i + 1)
 * #end
 * sb.append(", ");
 * super.writeFields(sb);
 * }</pre>
 *
 * <br><br>
 * toString:
 * <pre>public java.lang.String toString() {
 * final java.lang.StringBuilder sb = new java.lang.StringBuilder("$classname{");
 * writeFields(sb);
 * sb.append('}');
 * return sb.toString();
 * }</pre>
 */
public interface FieldStringWriter {
	/**
	 * Writes the fields to the string builder. Any unordered items (like sets) should be converted to an appropriate
	 * ordered type and ordered by the hash code, this ensures that two identical items will print identically.
	 */
	void writeFields(StringBuilder builder);
}
