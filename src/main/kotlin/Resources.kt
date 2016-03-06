package meh.expo;

import java.util.ArrayList;

import android.content.res.XResources;
import android.content.res.XModuleResources;
import android.content.res.XResForwarder;
import android.content.res.Resources as Res;

import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import meh.expo.builder.Resources as Builder;

import android.view.View;
import android.graphics.Movie;
import android.graphics.drawable.Drawable;
import android.content.res.ColorStateList;
import android.content.res.Resources.Theme;
import android.content.res.XmlResourceParser;

class Resources(value: XResources) {
	companion object {
		fun replace(id: Int, value: Any) {
			XResources.setSystemWideReplacement(id, value);
		}

		fun replace(name: String, value: Any) {
			XResources.setSystemWideReplacement(name, value);
		}

		fun replace(pkg: String, type: String, name: String, value: Any) {
			XResources.setSystemWideReplacement(pkg, type, name, value);
		}

		fun on(body: Hooker.() -> Unit): Set<Hook> {
			return Hooker(body).run()
		}

		class Hooker {
			private val _callbacks: ArrayList<Pair<Any, Any>> = ArrayList();

			constructor(body: Hooker.() -> Unit) {
				this.body();
			}

			fun layout(id: Int, body: (Layout.Parameter) -> Unit) {
				_callbacks.add(Pair(id, Builder.Layout(body).build()))
			}

			fun layout(name: String, body: (Layout.Parameter) -> Unit) {
				_callbacks.add(Pair(name, Builder.Layout(body).build()))
			}

			fun layout(pkg: String, type: String, name: String, body: (Layout.Parameter) -> Unit) {
				_callbacks.add(Pair(Triple(pkg, type, name), Builder.Layout(body).build()))
			}

			fun run(): Set<Hook> {
				return _callbacks.map {
					val (id, cb) = it;

					when (cb) {
						is Layout -> {
							when (id) {
								is Int ->
									Layout.Hook(XResources.hookSystemWideLayout(id, cb))

								is String ->
									Layout.Hook(XResources.hookSystemWideLayout(id, cb))

								is Triple<*, *, *> ->
									Layout.Hook(XResources.hookSystemWideLayout(id.first!! as String, id.second!! as String, id.third!! as String, cb))

								else ->
									throw RuntimeException("unreachable")
							}
						}

						else ->
							throw RuntimeException("unreachable")
					}
				}.toSet()
			}
		}
	}

	private val _value = value;

	fun directory(): String {
		return _value.getResDir()
	}

	fun owner(construction: Boolean = false): String {
		if (construction) {
			return XResources.getPackageNameDuringConstruction()
		}
		else {
			return _value.getPackageName()
		}
	}

	fun module(path: String): Module {
		return Module(XModuleResources.createInstance(path, _value))
	}

	fun replace(id: Int, value: Forwarder) {
		_value.setReplacement(id, value.into());
	}

	fun replace(id: Int, value: Any) {
		_value.setReplacement(id, value);
	}

	fun replace(name: String, value: Forwarder) {
		_value.setReplacement(name, value.into());
	}

	fun replace(name: String, value: Any) {
		_value.setReplacement(name, value);
	}

	fun replace(pkg: String, type: String, name: String, value: Forwarder) {
		_value.setReplacement(pkg, type, name, value.into());
	}

	fun replace(pkg: String, type: String, name: String, value: Any) {
		_value.setReplacement(pkg, type, name, value);
	}

	fun animation(id: Int): XmlResourceParser {
		return _value.getAnimation(id)
	}

	fun boolean(id: Int): Boolean {
		return _value.getBoolean(id)
	}

	@Suppress("DEPRECATION")
	fun color(id: Int): Int {
		return _value.getColor(id)
	}

	@Suppress("DEPRECATION")
	fun colorState(id: Int): ColorStateList {
		return _value.getColorStateList(id)
	}

	fun dimension(id: Int): Float {
		return _value.getDimension(id)
	}

	fun pixelOffset(id: Int): Int {
		return _value.getDimensionPixelOffset(id)
	}

	fun pixelSize(id: Int): Int {
		return _value.getDimensionPixelSize(id)
	}

	@Suppress("DEPRECATION")
	fun drawable(id: Int): Drawable {
		return _value.getDrawable(id)
	}

	fun drawable(id: Int, theme: Theme): Drawable {
		return _value.getDrawable(id, theme)
	}

	@Suppress("DEPRECATION")
	fun drawable(id: Int, density: Int): Drawable {
		return _value.getDrawableForDensity(id, density)
	}

	fun drawable(id: Int, density: Int, theme: Theme): Drawable {
		return _value.getDrawableForDensity(id, density, theme)
	}

	fun fraction(id: Int, base: Int, pbase: Int): Float {
		return _value.getFraction(id, base, pbase)
	}

	fun integer(id: Int): Int {
		return _value.getInteger(id)
	}

	fun integerArray(id: Int): IntArray {
		return _value.getIntArray(id)
	}

	fun layout(id: Int): XmlResourceParser {
		return _value.getLayout(id)
	}

	fun movie(id: Int): Movie {
		return _value.getMovie(id)
	}

	fun quantity(id: Int, quantity: Int): CharSequence {
		return _value.getQuantityText(id, quantity)
	}

	fun stringArray(id: Int): Array<String> {
		return _value.getStringArray(id)
	}

	fun text(id: Int): CharSequence {
		return _value.getText(id)
	}

	fun text(id: Int, def: CharSequence): CharSequence {
		return _value.getText(id, def)
	}

	fun textArray(id: Int): Array<CharSequence> {
		return _value.getTextArray(id)
	}

	fun xml(id: Int): XmlResourceParser {
		return _value.getXml(id)
	}

	fun add(id: Int, res: Res): Int {
		return _value.addResource(res, id)
	}

	abstract class Layout : XC_LayoutInflated {
		constructor() : super();
		constructor(priority: Int) : super(priority);

		override fun handleLayoutInflated(param: XC_LayoutInflated.LayoutInflatedParam) {
			layout(Parameter(param))
		}

		abstract fun layout(param: Parameter);

		class Parameter(value: XC_LayoutInflated.LayoutInflatedParam) : meh.expo.Parameter(value) {
			private val _value = value;

			fun view(): View {
				return _value.view
			}

			fun names(): Names {
				return Names(_value.resNames)
			}

			fun variant(): String {
				return _value.variant
			}

			fun resource(): Resources {
				return Resources(_value.res)
			}
		}

		class Hook(value: XC_LayoutInflated.Unhook): meh.expo.Hook {
			private val _value = value;

			fun callback(): XC_LayoutInflated {
				return _value.getCallback()
			}

			fun directory(): String {
				return _value.getResDir()
			}

			fun id(): Int {
				return _value.getId()
			}

			override fun remove() {
				_value.unhook()
			}
		}
	}

	fun on(body: Hooker.() -> Unit): Set<Hook> {
		return Hooker(body).run()
	}

	inner class Hooker {
		private val _callbacks: ArrayList<Pair<Any, Any>> = ArrayList();

		constructor(body: Hooker.() -> Unit) {
			this.body();
		}

		fun layout(id: Int, body: (Layout.Parameter) -> Unit) {
			_callbacks.add(Pair(id, Builder.Layout(body).build()))
		}

		fun layout(name: String, body: (Layout.Parameter) -> Unit) {
			_callbacks.add(Pair(name, Builder.Layout(body).build()))
		}

		fun layout(pkg: String, type: String, name: String, body: (Layout.Parameter) -> Unit) {
			_callbacks.add(Pair(Triple(pkg, type, name), Builder.Layout(body).build()))
		}

		fun run(): Set<Hook> {
			return _callbacks.map {
				val (id, cb) = it;

				when (cb) {
					is Layout -> {
						when (id) {
							is Int ->
								Layout.Hook(_value.hookLayout(id, cb))

							is String ->
								Layout.Hook(_value.hookLayout(id, cb))

							is Triple<*, *, *> ->
								Layout.Hook(_value.hookLayout(id.first!! as String, id.second!! as String, id.third!! as String, cb))

							else ->
								throw RuntimeException("unreachable")
						}
					}

					else ->
						throw RuntimeException("unreachable")
				}
			}.toSet()
		}
	}

	class Names(value: XResources.ResourceNames) {
		private val _value = value;

		fun id(): Int {
			return _value.id
		}

		fun pkg(): String {
			return _value.pkg
		}

		fun name(full: Boolean = false): String {
			if (full) {
				return _value.fullName;
			}
			else {
				return _value.name
			}
		}

		fun type(): String {
			return _value.type;
		}

		fun equals(pkg: String, name: String, type: String, id: Int): Boolean {
			return _value.equals(pkg, name, type, id)
		}
	}

	class Forwarder(value: XResForwarder) {
		private val _value = value;

		fun resources(): Res {
			return _value.getResources()
		}
		
		fun id(): Int {
			return _value.getId()
		}

		fun into(): XResForwarder {
			return _value
		}
	}

	class Module(value: XModuleResources) {
		private val _value = value;

		fun forward(id: Int): Forwarder {
			return Forwarder(_value.fwd(id))
		}
	}
}
