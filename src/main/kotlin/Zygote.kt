package meh.expo;

import de.robv.android.xposed.IXposedHookZygoteInit;

class Zygote {
	class Start {
		class Parameter(value: IXposedHookZygoteInit.StartupParam) {
			private val _value = value;

			fun module(): String {
				return _value.modulePath
			}

			fun isPrimary(): Boolean {
				return _value.startsSystemServer
			}
		}
	}

	interface IStart : IXposedHookZygoteInit {
		override fun initZygote(param: IXposedHookZygoteInit.StartupParam) {
			start(Start.Parameter(param))
		}

		fun start(param: Start.Parameter);
	}
}
