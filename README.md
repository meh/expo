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
  compile 'com.github.meh:expo:SNAPSHOT'
}
```

Add the XPosed bridge as a **provided** dependency to your `build.gradle`:
```groovy
dependencies {
  ...
	provided fileTree(dir: 'libs', include: ['XposedBridgeApi-*.jar'])
}
```

Example
-------
Red clock example from the XPosed Bridge tutorial using expo.

```kotlin
package meh.expo.example;

import meh.expo.*;
import android.graphics.Color;
import android.widget.TextView;

class Example : Package.ILoad {
  override fun load(param: Package.Load.Parameter) {
    if (param.name() != "com.android.systemui") {
      return;
    }

    param.loader().find("com.android.systemui.statusbar.policy.Clock").hook("updateClock") {
      after {
        it.instance<TextView>().let {
          it.setText("${it.getText()} :)")
          it.setTextColor(Color.RED);
        }
      }
    }
  }
}
```

[1]: http://forum.xda-developers.com/showthread.php?t=3034811
[2]: http://jitpack.io
