package tf_web.jp.bluespp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import tf_web.jp.bluespp.bluetooth.Spp;
import tf_web.jp.bluespp.bluetooth.SppListener;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    //デバイス SPP の テスト用
    private Spp spp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spp = new Spp(getApplicationContext(),sppListener);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //USER_ID 書き込む
        Button btnWriteUserID = (Button) findViewById(R.id.btnWriteUserID);
        btnWriteUserID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d(TAG, "USER_ID");
                Toast.makeText(getApplicationContext(),"USER_ID",Toast.LENGTH_SHORT).show();
                spp.writeUserID();
            }
        });

        //ADVERTISE 書き込む
        Button btnWriteAdvertise = (Button) findViewById(R.id.btnWriteAdvertise);
        btnWriteAdvertise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d(TAG, "ADVERTISE");
                Toast.makeText(getApplicationContext(),"ADVERTISE",Toast.LENGTH_SHORT).show();
                spp.writeAdvertise();
            }
        });
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
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

    /** SPP イベントの通知を受け取る
     *
     */
    private SppListener sppListener = new SppListener(){

        @Override
        public void onError(String message) {
            Log.d(TAG,message);
            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
        }
    };
}
