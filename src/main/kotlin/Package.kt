package meh.expo;

import java.util.ArrayList;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XCallback;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import android.content.pm.ApplicationInfo;

import meh.expo.builder.Package as Builder;

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

		class Hook(value: XC_LoadPackage.Unhook): meh.expo.Hook {
			private val _value = value;

			fun callback(): XC_LoadPackage {
				return _value.getCallback();
			}

			override fun remove() {
				_value.unhook()
			}
		}
	}

	interface ILoad : IXposedHookLoadPackage {
		override fun handleLoadPackage(param: XC_LoadPackage.LoadPackageParam) {
			load(Load.Parameter(param))
		}

		fun load(param: Load.Parameter);
	}

	companion object {
		fun on(body: Hooker.() -> Unit): Set<Hook> {
			return Hooker(body).run()
		}

		class Hooker {
			private val _callbacks: ArrayList<Any> = ArrayList();

			constructor(body: Hooker.() -> Unit) {
				this.body();
			}

			fun load(body: (Load.Parameter) -> Unit) {
				_callbacks.add(Builder.Load(body))
			}

			fun load(priority: Int, body: (Load.Parameter) -> Unit) {
				_callbacks.add(Builder.Load(priority, body))
			}

			fun run(): Set<Hook> {
				return _callbacks.map {
					when (it) {
						is Load ->
							Load.Hook(XposedBridge.hookLoadPackage(it))

						else ->
							throw RuntimeException("unreachable")
					}
				}.toSet()
			}
		}
	}
}
