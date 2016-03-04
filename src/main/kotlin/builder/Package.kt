package meh.expo.builder;

import de.robv.android.xposed.callbacks.XCallback;

import meh.expo.Package as P;

class Package {
	class Load {
		private val _priority: Int;
		private val _body:     (P.Load.Parameter) -> Unit;

		constructor(body: (P.Load.Parameter) -> Unit)
			: this(XCallback.PRIORITY_DEFAULT, body)

		constructor(priority: Int, body: (P.Load.Parameter) -> Unit) {
			_priority = priority;
			_body     = body;
		}

		fun build(): P.Load {
			return object : P.Load(_priority) {
				override fun load(param: P.Load.Parameter) {
					_body(param);
				}
			}
		}
	}
}
