package com.greenjon902.greenJam.core.packageLoader.basedPackageHelpers;

import com.greenjon902.greenJam.api.File;
import com.greenjon902.greenJam.api.Module;
import com.greenjon902.greenJam.api.PackageItem;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * See {@link BasedPackage}.
 */
public class BasedModule extends BasedPackageItem implements Module {
	private final String name;
	private final Set<File> files;
	private final Set<Module> modules;

	/**
	 * Create a new based module.
	 * @param modules The modules to combine, where lower indexes means higher precedence
	 */
	public BasedModule(Module... modules) {
		name = modules[0].name();
		files = mergeFiles(modules);
		this.modules = mergeModules(modules);
	}

	/**
	 * Recursively merge the submodules, while keeping the desired highest precedence.
	 * @param modules The modules to combine, where lower indexes means higher precedence
	 * @return The merged modules set
	 */
	private Set<Module> mergeModules(Module... modules) {

		// If we get all versions of the same module, then we can combine them
		HashMap<String, List<Module>> nameToSubModules = new HashMap<>();

		for (Module module : modules) {
			for (Module subModule : module.modules()) {

				String subModuleName = subModule.name();
				if (!nameToSubModules.containsKey(subModuleName)) {
					nameToSubModules.put(subModuleName, new ArrayList<>());
				}
				nameToSubModules.get(subModuleName).add(subModule);
			}
		}

		// Now flatten the module lists into BasedModules
		Set<Module> set = new HashSet<>();

		for (String subModuleName : nameToSubModules.keySet()) {
			List<Module> subModules = nameToSubModules.get(subModuleName);

			if (subModules.size() == 1) {  // No point making it based if it has no others
				set.add(subModules.get(0));
			} else {
				set.add(new BasedModule(subModules.toArray(Module[]::new)));
			}
		}

		return set;
	}

	/**
	 * Merge the files of all these modules, by choosing the file with the highest precedence, and then setting its
	 * super file (if it doesn't have one already).
	 * @param modules The modules to combine, where lower indexes means higher precedence
	 * @return The merged file set
	 */
	private Set<File> mergeFiles(Module... modules) {
		Set<File> set = new HashSet<>();

		// Reverse modules as we want lowest precedence first, as that is needed to become super file as BasedFiles are
		// not mutable
		List<Module> reversedModules = new ArrayList<>(List.of(modules));
		Collections.reverse(reversedModules);

		for (Module module : reversedModules) {
			for (File file : module.files()) {

				File oldFile;
				if ((oldFile = containsByName(set, file.name())) != null) {  // Already in the set so must be overwritten

					if (file.super_() == null) {  // No super file already, so put one in
						file = new BasedFile(file, oldFile);
					} // Else: already has a super file, so we can forget oldFile
					boolean r = set.remove(oldFile); // We are overwriting so remove
					assert r;  // Make sure it worked
				}
				set.add(file);
			}
		}

		return set;
	}

	private <T extends PackageItem> T containsByName(Set<T> set, String name) {
		for (T item : set) {
			if (item.name().equals(name)) {
				return item;
			}
		}
		return null;
	}

	@Override
	public @NotNull Set<Module> modules() {
		return modules;
	}

	@Override
	public @NotNull Set<File> files() {
		return files;
	}

	@Override
	public @NotNull String name() {
		return name;
	}
}
