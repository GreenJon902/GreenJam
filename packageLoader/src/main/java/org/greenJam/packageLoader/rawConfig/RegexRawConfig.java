package org.greenJam.packageLoader.rawConfig;

import java.util.Objects;

public class RegexRawConfig {
	/**
	 * What is used to check if it's correct.
	 */
	public String regex;
	/**
	 * What is used to get the name that will be used.
	 */
	public String substitution;

	public RegexRawConfig(String regex) {
		this(regex, "$1");  // Use 1 as default substitution as that is likely the only group
	}

	public RegexRawConfig(String regex, String substitution) {
		this.regex = regex;
		this.substitution = substitution;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RegexRawConfig that = (RegexRawConfig) o;
		return Objects.equals(regex, that.regex) && Objects.equals(substitution, that.substitution);
	}

	@Override
	public int hashCode() {
		return Objects.hash(regex, substitution);
	}

	@Override
	public String toString() {
		return "RegexRawConfig{" +
				"regex='" + regex + '\'' +
				", substitution='" + substitution + '\'' +
				'}';
	}
}
