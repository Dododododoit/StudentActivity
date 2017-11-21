package umich.edu.studentactivity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    protected final String TAG = getClass().getSimpleName();
    private Intent intent = null;
    private Intent movementIntent = null;
    private AccessPoint aPoint;
    TextView tv;
    ScrollView sv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aPoint = new AccessPoint(this);
        tv = (TextView) findViewById(R.id.BssidLists);
        tv.setMovementMethod(new ScrollingMovementMethod());
        sv = (ScrollView) findViewById(R.id.scroll);

    }
    @Override
    protected void onStop(){
        super.onStop();
        this.stopService(intent);
    }
    public void printAcPoint(View v){
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        Log.d(TAG, wifiInfo.getBSSID());
    }

    public void RecordSSID(View v){
        EditText editText = (EditText) findViewById(R.id.edit_place);
        String message = editText.getText().toString();
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        aPoint.updateAccessPoints(message, wifiInfo.getBSSID());
        Log.d(TAG, message + wifiInfo.getBSSID());
    }

    public void ShowBssid(View v){
        msg(aPoint.toString());
    }

    public void startDetection(View v){
        intent = new Intent(this, DetectService.class);
        this.startService(intent);
        movementIntent = new Intent(this, MovementDetector.class);
        this.startService(movementIntent);
    }


    private void msg(final String text) {
        runOnUiThread(new Runnable() {
            public void run() {
                tv.setText("");
                tv.append("\n");
                tv.append(text);
                sv.fullScroll(View.FOCUS_DOWN);
            }
        });
        Log.i(TAG, text);
    }

}
