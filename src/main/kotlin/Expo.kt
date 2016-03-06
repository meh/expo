package meh.expo;

import java.util.Random;
import java.lang.reflect.Field;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import android.content.Context;
import android.os.IBinder;

object Expo {
	fun log(string: String) {
		XposedBridge.log(string)
	}

	fun log(error: Throwable) {
		XposedBridge.log(error)
	}

	fun find(name: String): Class<*> {
		return XposedHelpers.findClass(name, null)
	}

	fun types(vararg types: Any?): Array<Class<*>> {
		return XposedHelpers.getParameterTypes(*types)
	}

	fun service(name: String, obj: SystemService): String {
		val secret = "%x".format(Random().nextLong());

		find("android.app.ActivityThread")
			.hook("systemMain") {
				after {
					Thread.currentThread().getContextClassLoader().let { loader ->
						loader.find("com.android.server.am.ActivityManagerService")
							.hook {
								after {
									fun context(instance: Any): Context {
										var manager: Class<*>? = instance.javaClass;
										var field:   Field?    = null;

										while (manager != null && field == null) {
											try {
												field = manager.field("mContext");
											}
											catch (e: NoSuchFieldException) {
												manager = manager.getSuperclass();
											}
										}

										field!!
										field.setAccessible(true);

										return field.get(instance) as Context;
									}

									loader.find("android.os.ServiceManager")
										.method("addService", String::class, IBinder::class, Boolean::class)
										.call("user.${name}", it.instance(), true);

									obj.register(secret, context(it.instance()), loader);
								}
							}
					}
				}
			}

		return secret;
	}
}
