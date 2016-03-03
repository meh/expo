package meh.expo;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.IXUnhook;
import java.lang.reflect.Method;

class MethodHook {
	class Parameter(value: XC_MethodHook.MethodHookParam) : meh.expo.Parameter(value) {
		private val _value = value;

		fun method(): Method {
			return _value.method as Method
		}

		fun instance(): Any {
			return _value.thisObject
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

		fun call(): Any? {
			return _value.getResultOrThrowable()
		}
	}

	class Builder {
		private var _before: ((Parameter) -> Unit)? = null;
		private var _after:  ((Parameter) -> Unit)? = null;

		constructor(body: Builder.() -> Unit) {
			this.body();
		}

		fun before(body: (Parameter) -> Unit) {
			_before = body;
		}

		fun after(body: (Parameter) -> Unit) {
			_after = body;
		}

		fun build(): XC_MethodHook {
			return object : XC_MethodHook() {
				override protected fun beforeHookedMethod(param: XC_MethodHook.MethodHookParam) {
					if (_before != null) {
						_before!!(Parameter(param))
					}
				}

				override protected fun afterHookedMethod(param: XC_MethodHook.MethodHookParam) {
					if (_after != null) {
						_after!!(Parameter(param))
					}
				}
			}
		}
	}

	class Unhook(value: XC_MethodHook.Unhook): IXUnhook {
		private val _value = value;

		fun method(): Method {
			return _value.getHookedMethod() as Method
		}

		fun callback(): XC_MethodHook {
			return _value.getCallback();
		}

		override fun unhook() {
			_value.unhook()
		}
	}
}
