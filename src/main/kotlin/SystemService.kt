package meh.expo;

import android.content.Context;

interface SystemService {
	fun register(secret: String, context: Context, loader: ClassLoader);
}
