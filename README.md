# SeekBarCompat
A support library for the material design SeekBar in Android for API 14 and above.

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-SeekBarCompat-green.svg?style=flat)](https://android-arsenal.com/details/1/2084)


##Screenshot

On APIs 14 and above - Seekbars would look something like

<img src="https://raw.githubusercontent.com/ahmedrizwan/SeekBarCompat/master/app/src/main/res/drawable/post.png" align="left"  width="448" />
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>



##Example
The SeekBarCompat picks up the PrimaryColor by default, but you can set custom colors for SeekBar thumb and progress too, both in xml and programmatically.
####Xml 

```xml
<app.minimize.com.seek_bar_compat.SeekBarCompat
        android:id="@+id/materialSeekBar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:maxHeight="300sp"
        app:progressColor="#AFF123"
        app:progressBackgroundColor="#000"
        app:thumbColor="#FF4444"/>
```
**Note: Add maxHeight attribute in xml - it fixes the thumb gravity issues on APIs < 21**

In Xml you can also specify *progressBackgroundColor* - by default the progressBackgroundColor (progress-line color) is Black.

####Programmatically
```java
SeekBarCompat seekBarCompat = (SeekBarCompat) findViewById(R.id.materialSeekBar);
seekBarCompat.setThumbColor(Color.RED);
seekBarCompat.setProgressColor(Color.CYAN);
seekBarCompat.setProgressBackgroundColor(Color.BLUE); 
seekBarCompat.setThumbAlpha(128); //You can also set thumb alpha (programmatically) 
```

##Download 
Repository available on jCenter

*If the dependency fails to resolve, add this to your project repositories*
```Gradle
repositories {
    maven {
        url  "http://dl.bintray.com/ahmedrizwan/maven" 
    }
}
```
```Gradle
compile 'com.minimize.library:seekbar-compat:0.2.3'
```

##License 
```
Copyright 2015 Ahmed Rizwan

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
