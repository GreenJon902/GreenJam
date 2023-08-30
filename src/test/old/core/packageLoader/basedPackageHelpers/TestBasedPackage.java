package com.greenjon902.greenJam.old.core.packageLoader.basedPackageHelpers;

import com.greenjon902.greenJam.api.core.File;
import com.greenjon902.greenJam.api.core.Package;
import com.greenjon902.greenJam.core.packageLoader.LoadedFile;
import com.greenjon902.greenJam.core.packageLoader.LoadedModule;
import com.greenjon902.greenJam.core.packageLoader.LoadedPackage;
import com.greenjon902.greenJam.core.packageLoader.basedPackageHelpers.BasedPackage;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class TestBasedPackage {
	@Test
	public void testPackageCombining() {
		// Actual ------------------------------------------------------------------------------------------------------
		Package a = new LoadedPackage.Builder() {{
			name("CorrectName");
			// Test no description
			authors(Set.of("GreenJon902", "Your Mother"));
			files(Set.of(
					new LoadedFile.Builder() {{
						name("testing123");
					}}.build()
			));
			modules(
					Set.of(
							new LoadedModule.Builder() {{
								name("Module");
								files(Set.of(
										new LoadedFile.Builder() {{
											name("file");
										}}.build(),
										new LoadedFile.Builder() {{
											name("moreFile");
										}}.build()
								));
							}}.build()
					)
			);
		}}.build();

		Package b = new LoadedPackage.Builder() {{
			// Test no name
			description("This should not appear");
			// Test no authors
			modules(
					Set.of(
							new LoadedModule.Builder() {{
								name("Module");
								files(Set.of(
										new LoadedFile.Builder() {{
											name("file");
										}}.build(),
										new LoadedFile.Builder() {{
											name("otherFile");
										}}.build()
								));
							}}.build(),
							new LoadedModule.Builder() {{
								name("SomeOtherModule");
								files(Set.of(
										new LoadedFile.Builder() {{
											name("fileNo1000");
										}}.build()
								));
							}}.build()
					)
			);
		}}.build();

		Package c = new LoadedPackage.Builder() {{
			name("Incorrect Name");
			// Test no description
			authors(Set.of("me??", "GreenJon902"));  // GreenJon902 should only appear once
			modules(
					Set.of(
							new LoadedModule.Builder() {{
								name("Module");
								files(Set.of(
										new LoadedFile.Builder() {{
											name("otherFile");
										}}.build(),
										new LoadedFile.Builder() {{
											name("fileLike7");
										}}.build()
								));
							}}.build()
					)
			);
		}}.build();

		Package actual = new BasedPackage(a, b, c);


		// Expected ----------------------------------------------------------------------------------------------------
		// TODO: Some way to check these files are correct, possible by adding contents?
		Package expected = new LoadedPackage.Builder() {{
			name("CorrectName");
			description("");  // Was never set
			authors(Set.of("GreenJon902", "Your Mother", "me??"));
			files(Set.of(
					new LoadedFile.Builder() {{
						name("testing123");
					}}.build()
			));
			modules(
					Set.of(
							new LoadedModule.Builder() {{
								name("Module");
								files(Set.of(
										new BasedLoadedFile.Builder() {{
											name("file");
											super_(
													new LoadedFile.Builder() {{
														name("file");
													}}.build()
											);
										}}.build(),
										new LoadedFile.Builder() {{
											name("moreFile");
										}}.build(),
										new BasedLoadedFile.Builder() {{
											name("otherFile");
											super_(
													new LoadedFile.Builder() {{
														name("otherFile");
													}}.build()
											);
										}}.build(),
										new LoadedFile.Builder() {{
											name("fileLike7");
										}}.build()
								));
							}}.build(),
							new LoadedModule.Builder() {{
								name("SomeOtherModule");
								files(Set.of(
										new LoadedFile.Builder() {{
											name("fileNo1000");
										}}.build()
								));
							}}.build()
					)
			);
		}}.build();

		// Check ----------------------------------------------------------------------------------------------------
		Assertions.assertEquals(expected, actual);
	}
}

class BasedLoadedFile extends LoadedFile {
	private final @Nullable File super_;

	protected BasedLoadedFile(Builder builder) {
		super(builder);
		this.super_ = builder.super_;
	}

	@Override
	public @Nullable File super_() {
		return super_;
	}

	public static class Builder extends LoadedFile.Builder {
		private @Nullable File super_ = null;

		public void super_(File super_) {
			this.super_ = super_;
		}

		@Override
		public BasedLoadedFile build() {
			return new BasedLoadedFile(this);
		}
	}
}