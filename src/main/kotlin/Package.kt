package meh.expo;

import java.util.ArrayList;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.callbacks.XCallback;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
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

	abstract class Resources : XC_InitPackageResources {
		constructor() : super();
		constructor(priority: Int) : super(priority);

		override fun handleInitPackageResources(param: XC_InitPackageResources.InitPackageResourcesParam) {
			resources(Parameter(param))
		}

		abstract fun resources(param: Parameter);

		class Parameter(value: XC_InitPackageResources.InitPackageResourcesParam) : meh.expo.Parameter(value) {
			private val _value = value;

			fun owner(): String {
				return _value.packageName
			}

			fun resources(): meh.expo.Resources {
				return meh.expo.Resources(_value.res)
			}
		}

		class Hook(value: XC_InitPackageResources.Unhook): meh.expo.Hook {
			private val _value = value;

			fun callback(): XC_InitPackageResources {
				return _value.getCallback();
			}

			override fun remove() {
				_value.unhook()
			}
		}

	}

	interface IResources : IXposedHookInitPackageResources {
		override fun handleInitPackageResources(param: XC_InitPackageResources.InitPackageResourcesParam) {
			resources(Resources.Parameter(param))
		}

		fun resources(param: Resources.Parameter)
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
				_callbacks.add(Builder.Load(body).build())
			}

			fun load(priority: Int, body: (Load.Parameter) -> Unit) {
				_callbacks.add(Builder.Load(priority, body).build())
			}

			fun resources(body: (Resources.Parameter) -> Unit) {
				_callbacks.add(Builder.Resources(body).build())
			}

			fun resources(priority: Int, body: (Resources.Parameter) -> Unit) {
				_callbacks.add(Builder.Resources(priority, body).build())
			}

			fun run(): Set<Hook> {
				return _callbacks.map {
					when (it) {
						is Load ->
							Load.Hook(XposedBridge.hookLoadPackage(it))

						is Resources ->
							Resources.Hook(XposedBridge.hookInitPackageResources(it))

						else ->
							throw RuntimeException("unreachable")
					}
				}.toSet()
			}
		}
	}
}
