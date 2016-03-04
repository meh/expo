package meh.expo;

import de.robv.android.xposed.callbacks.IXUnhook;

interface Hook : IXUnhook {
	override fun unhook() {
		remove()
	}

	fun remove();
}
