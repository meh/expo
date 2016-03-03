package meh.expo;

import de.robv.android.xposed.callbacks.XCallback;
import android.os.Bundle;

open class Parameter(value: XCallback.Param) {
	private val _value = value;

	fun callbacks(): Array<Any> {
		return _value.callbacks
	}

	fun extra(): Bundle {
		return _value.getExtra()
	}

	fun objectExtra(key: String): Any {
		return _value.getObjectExtra(key)
	}

	fun objectExtra(key: String, value: Any) {
		_value.setObjectExtra(key, value)
	}
}
