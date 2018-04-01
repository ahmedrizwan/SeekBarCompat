package app.minimize.com.seekbarcompatandroid

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import app.minimize.com.seek_bar_compat.SeekBarCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val seekBarCompat = findViewById<View>(R.id.materialSeekBar) as SeekBarCompat
        seekBarCompat.setThumbColor(Color.MAGENTA)
        seekBarCompat.setProgressBackgroundColor(Color.TRANSPARENT)
        seekBarCompat.setProgressColor(Color.RED)
        seekBarCompat.progress = 30
        seekBarCompat.setThumbAlpha(0)

        val seekBarCompatTwo = findViewById<View>(R.id.materialSeekBarTwo) as SeekBarCompat
        seekBarCompatTwo.setProgressBackgroundColor(Color.RED)
        seekBarCompatTwo.progress = 50
        seekBarCompatTwo.isEnabled = false
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }
}
