package meh.expo;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import kotlin.reflect.KClass;

fun ClassLoader.find(name: String): Class<*> {
	return XposedHelpers.findClass(name, this)
}

fun Class<*>.field(name: String): Field {
	return XposedHelpers.findField(this, name)
}

fun KClass<*>.field(name: String): Field {
	return XposedHelpers.findField(this.java, name)
}

fun Class<*>.field(type: Class<*>): Field {
	return XposedHelpers.findFirstFieldByExactType(this, type)
}

fun KClass<*>.field(type: Class<*>): Field {
	return XposedHelpers.findFirstFieldByExactType(this.java, type)
}

fun Class<*>.field(type: KClass<*>): Field {
	return XposedHelpers.findFirstFieldByExactType(this, type.java)
}

fun KClass<*>.field(type: KClass<*>): Field {
	return XposedHelpers.findFirstFieldByExactType(this.java, type.java)
}

fun Class<*>.constructor(vararg types: Class<*>): Constructor<*> {
	return XposedHelpers.findConstructorBestMatch(this, *types)
}

fun KClass<*>.constructor(vararg types: Class<*>): Constructor<*> {
	return XposedHelpers.findConstructorBestMatch(this.java, *types)
}

fun Class<*>.constructor(vararg types: KClass<*>): Constructor<*> {
	return XposedHelpers.findConstructorBestMatch(this, *types.map { it.java }.toTypedArray())
}

fun KClass<*>.constructor(vararg types: KClass<*>): Constructor<*> {
	return XposedHelpers.findConstructorBestMatch(this.java, *types.map { it.java }.toTypedArray())
}

fun Class<*>.method(name: String, vararg types: Class<*>): Method {
	return XposedHelpers.findMethodBestMatch(this, name, *types)
}

fun KClass<*>.method(name: String, vararg types: Class<*>): Method {
	return XposedHelpers.findMethodBestMatch(this.java, name, *types)
}

fun Class<*>.method(name: String, vararg types: KClass<*>): Method {
	return XposedHelpers.findMethodBestMatch(this, name, *types.map { it.java }.toTypedArray())
}

fun KClass<*>.method(name: String, vararg types: KClass<*>): Method {
	return XposedHelpers.findMethodBestMatch(this.java, name, *types.map { it.java }.toTypedArray())
}

@Suppress("UNCHECKED_CAST")
fun<T> Class<*>.new(vararg params: Any): T {
	return XposedHelpers.newInstance(this, *params) as T
}

@Suppress("UNCHECKED_CAST")
fun<T> KClass<*>.new(vararg params: Any): T {
	return XposedHelpers.newInstance(this.java, *params) as T
}

fun Method.call(vararg params: Any?): Any? {
	return this.invoke(null, *params)
}

fun Method.apply(obj: Any, vararg params: Any?): Any? {
	return this.invoke(obj, *params)
}
