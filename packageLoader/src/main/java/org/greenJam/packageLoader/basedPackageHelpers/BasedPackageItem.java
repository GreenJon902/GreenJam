package org.greenJam.packageLoader.basedPackageHelpers;

import org.greenJam.api.PackageItem;
import org.greenJam.api.FieldStringWriter;

public abstract class BasedPackageItem extends FieldStringWriter.Abstract  implements PackageItem {
	@Override
	public boolean equals(Object obj) {
		return equals_(obj);
	}

	@Override
	public int hashCode() {
		return hashCode_();
	}
}
