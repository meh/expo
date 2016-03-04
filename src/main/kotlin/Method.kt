package meh.expo;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.IXUnhook;

import java.lang.reflect.Method as M;
import java.lang.reflect.Constructor as C;

import meh.expo.builder.Method as Builder;

abstract class Method : XC_MethodHook {
	constructor() : super();
	constructor(priority: Int) : super(priority);

	override protected fun beforeHookedMethod(param: XC_MethodHook.MethodHookParam) {
		before(Parameter(param))
	}

	override protected fun afterHookedMethod(param: XC_MethodHook.MethodHookParam) {
		after(Parameter(param))
	}

	abstract protected fun before(param: Parameter);
	abstract protected fun after(param: Parameter);

	class Parameter(value: XC_MethodHook.MethodHookParam) : meh.expo.Parameter(value) {
		private val _value = value;

		fun method(): M {
			return _value.method as M
		}

		@Suppress("UNCHECKED_CAST")
		fun<T> instance(): T {
			return _value.thisObject as T
		}

		fun arguments(): Array<Any> {
			return _value.args
		}

		fun result(): Any? {
			return _value.getResult()
		}

		fun result(value: Any) {
			_value.setResult(value)
		}

		fun exception(): Throwable? {
			return _value.getThrowable();
		}

		fun exception(value: Throwable) {
			_value.setThrowable(value)
		}

		fun returns(): Any? {
			return _value.getResultOrThrowable()
		}
	}

	class Hook(value: XC_MethodHook.Unhook): meh.expo.Hook {
		private val _value = value;

		fun method(): Method {
			return _value.getHookedMethod() as Method
		}

		fun callback(): XC_MethodHook {
			return _value.getCallback();
		}

		override fun remove() {
			_value.unhook()
		}
	}
}

fun Class<*>.hook(name: String, body: Builder.() -> Unit): Set<Method.Hook> {
	return XposedBridge.hookAllMethods(this, name, Builder(body).build())
		.map { Method.Hook(it) }
		.toSet()
}

fun Class<*>.hook(method: M, body: Builder.() -> Unit): Method.Hook {
	return Method.Hook(XposedBridge.hookMethod(method, Builder(body).build()))
}

fun Class<*>.hook(body: Builder.() -> Unit): Set<Method.Hook> {
	return XposedBridge.hookAllConstructors(this, Builder(body).build())
		.map { Method.Hook(it) }
		.toSet()
}

fun Class<*>.hook(constructor: C<*>, body: Builder.() -> Unit): Method.Hook {
	return Method.Hook(XposedBridge.hookMethod(constructor, Builder(body).build()))
}
