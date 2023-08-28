package com.greenjon902.greenJam.core.packageLoader.basedPackageHelpers;

import com.greenjon902.greenJam.api.core.PackageItem;
import com.greenjon902.greenJam.utils.FieldStringWriter;

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
