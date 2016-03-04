package meh.expo;

import de.robv.android.xposed.XposedBridge;

class Expo {
	companion object {
		fun log(string: String) {
			XposedBridge.log(string)
		}

		fun log(error: Throwable) {
			XposedBridge.log(error)
		}
	}
}
