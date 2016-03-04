package meh.expo.builder;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XCallback;

import meh.expo.Method as M;

class Method {
	private var _before: ((M.Parameter) -> Unit)? = null;
	private var _after:  ((M.Parameter) -> Unit)? = null;

	constructor(body: Method.() -> Unit) {
		this.body();
	}

	var priority: Int = XCallback.PRIORITY_DEFAULT;

	fun before(body: (M.Parameter) -> Unit) {
		_before = body;
	}

	fun after(body: (M.Parameter) -> Unit) {
		_after = body;
	}

	fun build(): M {
		return object : M(priority) {
			override protected fun before(param: M.Parameter) {
				if (_before != null) {
					_before!!(param)
				}
			}

			override protected fun after(param: M.Parameter) {
				if (_after != null) {
					_after!!(param)
				}
			}
		}
	}
}
