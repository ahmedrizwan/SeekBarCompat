# SeekBarCompat
A support library for the material design SeekBar in Android for API 16 and above.

##Screenshot

On Lollipop (and above)

<img src="https://raw.githubusercontent.com/ahmedrizwan/SeekBarCompat/master/app/src/main/res/drawable/post.png" align="left"  width="448" />
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>

Pre-Lollipop (16 and above)

<img src="https://cloud.githubusercontent.com/assets/4357275/8476938/8b6eb8fc-20df-11e5-8989-f0886d60d5a7.png" align="left"  width="448" />
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
        app:thumbColor="#FF4444"/>
```
In Xml you can also specify *progressBackgroundColor* (optional) - by default the progressBackground is Black.

Note: specifying *maxHeight* fixes the gravity/positioning of thumb and progress-line

####Programmatically
```java
SeekBarCompat seekBarCompat = (SeekBarCompat) findViewById(R.id.materialSeekBar);
seekBarCompat.setThumbColor(Color.RED);
seekBarCompat.setProgressColor(Color.CYAN);
```

##Download 
Repository available on jCenter
```Gradle
compile 'com.minimize.library:seekbar-compat:0.1.2'
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
