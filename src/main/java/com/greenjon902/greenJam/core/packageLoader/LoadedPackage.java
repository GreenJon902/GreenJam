package com.greenjon902.greenJam.core.packageLoader;

import com.greenjon902.greenJam.core.Package;
import org.jetbrains.annotations.NotNull;

/**
 * See {@link LoadedPackageItem}
 */
public class LoadedPackage extends LoadedModule implements Package {
	private final String display_name;
	private final String[] authors;
	private final String description;

	protected LoadedPackage(Builder builder) {
		super(builder);
		display_name = builder.display_name;
		this.authors = builder.authors;
		this.description = builder.description;
	}

	@Override
	public @NotNull String display_name() {
		return display_name;
	}

	@Override
	public @NotNull String[] authors() {
		return authors;
	}

	@Override
	public @NotNull String description() {
		return description;
	}



	public static class Builder extends LoadedModule.Builder {
		private String display_name = "";
		private String[] authors = new String[0];
		private String description = "";

		public Builder display_name(String display_name) {this.display_name = display_name; return this;}
		public Builder authors(String[] authors) {this.authors = authors; return this;}
		public Builder description(String description) {this.description = description; return this;}

		@Override
		public LoadedPackage build() {
			return new LoadedPackage(this);
		}
	}
}
