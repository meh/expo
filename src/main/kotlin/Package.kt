package meh.expo;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XCallback;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import android.content.pm.ApplicationInfo;

class Package {
	abstract class Load : XC_LoadPackage {
		constructor() : super();
		constructor(priority: Int) : super(priority);

		override fun handleLoadPackage(param: XC_LoadPackage.LoadPackageParam) {
			load(Parameter(param))
		}

		abstract fun load(param: Parameter);

		class Parameter(value: XC_LoadPackage.LoadPackageParam) : meh.expo.Parameter(value) {
			private val _value = value;

			fun name(): String {
				return _value.packageName
			}

			fun process(): String {
				return _value.processName
			}

			fun loader(): ClassLoader {
				return _value.classLoader
			}

			fun info(): ApplicationInfo {
				return _value.appInfo
			}

			fun isFirst(): Boolean {
				return _value.isFirstApplication
			}
		}
	}

	interface ILoad : IXposedHookLoadPackage {
		override fun handleLoadPackage(param: XC_LoadPackage.LoadPackageParam) {
			load(Load.Parameter(param))
		}

		fun load(param: Load.Parameter);
	}
}
