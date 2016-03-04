package meh.expo.builder;

import de.robv.android.xposed.callbacks.XCallback;
import meh.expo.Resources as R;

class Resources {
	class Layout {
		private val _priority: Int;
		private val _body:     (R.Layout.Parameter) -> Unit;

		constructor(body: (R.Layout.Parameter) -> Unit)
			: this(XCallback.PRIORITY_DEFAULT, body)

		constructor(priority: Int, body: (R.Layout.Parameter) -> Unit) {
			_priority = priority;
			_body     = body;
		}

		fun build(): R.Layout {
			return object : R.Layout(_priority) {
				override fun layout(param: R.Layout.Parameter) {
					_body(param);
				}
			}
		}
	}
}
