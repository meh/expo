package meh.expo;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

class Expo {
	companion object {
		fun log(string: String) {
			XposedBridge.log(string)
		}

		fun log(error: Throwable) {
			XposedBridge.log(error)
		}

		fun find(name: String): Class<*> {
			return XposedHelpers.findClass(name, null)
		}

		fun types(vararg types: Any?): Array<Class<*>> {
			return XposedHelpers.getParameterTypes(*types)
		}
	}
}
