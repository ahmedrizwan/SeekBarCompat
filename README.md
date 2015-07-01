# SeekBarCompat
A support library for the material design SeekBar in Android for API 16 and above.

##Screenshot

<img src="https://raw.githubusercontent.com/ahmedrizwan/SeekBarCompat/master/app/src/main/res/drawable/seekbar.png" align="left"  width="448" />
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
##Example
You can set the thumbColor and progressColor in Xml like

```xml
<app.minimize.com.seek_bar_compat.SeekBarCompat
        android:id="@+id/materialSeekBar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:maxHeight="300sp"
        app:progressColor="#AFF123"
        app:thumbColor="#FF4444"/>
```
Note: specifying *maxHeight* fixes the gravity/positioning of thumb and progress-line

Or you can programmitcally change the colors
```java
SeekBarCompat seekBarCompat = (SeekBarCompat) findViewById(R.id.materialSeekBar);
seekBarCompat.setThumbColor(Color.RED);
seekBarCompat.setProgressColor(Color.CYAN);
```

##Download 
Repository available on jCenter
```Gradle
compile 'com.minimize.library:seekbar-compat:0.1.0'
```
