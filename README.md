expo
====
Writing XPosed modules with Kotlin.

Usage
-----
Download the latest XPosed bridge from [XDA][1] and put it in `libs/`.

Add the [jitpack.io][2] repository to your `build.gradle`:
```groovy
repositories { 
  ...
  maven { url "https://jitpack.io" }
}
```

Add expo as a dependency to your `build.gradle`:
```groovy
dependencies {
  ...
  compile 'com.github.meh:expo:master'
}
```

Add the XPosed bridge as a **provided** dependency to your `build.gradle`:
```groovy
dependencies {
  ...
	provided fileTree(dir: 'libs', include: ['XposedBridgeApi-*.jar'])
}
```

[1]: http://forum.xda-developers.com/showthread.php?t=3034811
[2]: http://jitpack.io
