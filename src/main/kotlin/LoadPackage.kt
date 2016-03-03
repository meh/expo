package meh.expo;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import android.content.pm.ApplicationInfo;

class Package(value: LoadPackageParam) : Parameter(value) {
	private val _value = value;

	fun name(): String {
		return _value.packageName
	}

	fun process(): String {
		return _value.processName;
	}

	fun loader(): ClassLoader {
		return _value.classLoader
	}

	fun info(): ApplicationInfo {
		return _value.appInfo;
	}

	fun isFirst(): Boolean {
		return _value.isFirstApplication
	}
}

interface LoadPackage : IXposedHookLoadPackage {
	override fun handleLoadPackage(param: LoadPackageParam) {
		loadPackage(Package(param))
	}

	fun loadPackage(param: Package);
}
