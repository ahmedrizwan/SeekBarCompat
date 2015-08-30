package app.minimize.com.seekbarcompatandroid;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;

import app.minimize.com.seek_bar_compat.SeekBarCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SeekBarCompat seekBarCompat = (SeekBarCompat) findViewById(R.id.materialSeekBar);
        seekBarCompat.setThumbColor(Color.MAGENTA);
        seekBarCompat.setProgressBackgroundColor(Color.TRANSPARENT);
        seekBarCompat.setProgressColor(Color.RED);
        seekBarCompat.setProgress(30);
        seekBarCompat.setThumbAlpha(0);

        SeekBar normalSeekBar = (SeekBar) findViewById(R.id.normalSeekbar);
        normalSeekBar.setEnabled(false);

        SeekBarCompat seekBarCompatTwo = (SeekBarCompat) findViewById(R.id.materialSeekBarTwo);
        seekBarCompatTwo.setProgressBackgroundColor(Color.RED);
        seekBarCompatTwo.setEnabled(false);
        seekBarCompatTwo.setEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
