package meh.expo;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;

fun ClassLoader.find(name: String): Class<*> {
	return XposedHelpers.findClass(name, this)
}

fun Class<*>.field(name: String): Field {
	return XposedHelpers.findField(this, name)
}

fun Class<*>.field(type: Class<*>): Field {
	return XposedHelpers.findFirstFieldByExactType(this, type)
}

fun Class<*>.constructor(vararg types: Class<*>): Constructor<*> {
	return XposedHelpers.findConstructorBestMatch(this, *types)
}

fun Class<*>.method(name: String, vararg types: Class<*>): Method {
	return XposedHelpers.findMethodBestMatch(this, name, *types)
}

@Suppress("UNCHECKED_CAST")
fun<T> Class<*>.new(vararg params: Any): T {
	return XposedHelpers.newInstance(this, *params) as T
}
