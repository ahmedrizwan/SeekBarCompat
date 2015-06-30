package app.minimize.com.seekbarcompatandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



//        SeekBarCompat seekBarCompatCode = (SeekBarCompat) View.inflate(this,R.layout.seekbar_compat, null);
//        seekBarCompatCode.setThumbColor(Color.RED);
//        ((LinearLayout) findViewById(R.id.container)).addView(seekBarCompatCode);

//        SeekBarCompat seekBarCompat = (SeekBarCompat) findViewById(R.id.materialSeekBar);
//        seekBarCompat.setThumbColor(Color.RED);
//        seekBarCompat.setProgressColor(Color.RED);
//
//
//        SeekBarCompat seekBarCompatTwo = (SeekBarCompat) findViewById(R.id.materialSeekBarTwo);
//        seekBarCompatTwo.setThumbColor(Color.BLUE);
////        seekBarCompatTwo.setProgressColor(Color.RED);

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
