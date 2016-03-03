package meh.expo;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

fun log(string: String) {
	XposedBridge.log(string)
}

fun log(error: Throwable) {
	XposedBridge.log(error)
}

fun ClassLoader.find(name: String): Class<*> {
	return XposedHelpers.findClass(name, this)
}

fun Class<*>.field(name: String): Field {
	return XposedHelpers.findField(this, name)
}

fun Class<*>.field(type: Class<*>): Field {
	return XposedHelpers.findFirstFieldByExactType(this, type)
}

fun Class<*>.method(name: String, vararg types: Class<*>): Method {
	return XposedHelpers.findMethodBestMatch(this, name, *types)
}

fun Class<*>.hook(method: Method, body: MethodHook.Builder.() -> Unit): MethodHook.Unhook {
	return MethodHook.Unhook(XposedBridge.hookMethod(method,
		MethodHook.Builder(body).build()))
}

fun Class<*>.hook(name: String, body: MethodHook.Builder.() -> Unit): Set<MethodHook.Unhook> {
	return XposedBridge.hookAllMethods(this, name,
		MethodHook.Builder(body).build()).map { MethodHook.Unhook(it) }.toSet();
}

fun types(vararg types: Any): Array<Class<*>> {
	return XposedHelpers.getParameterTypes(*types)
}
