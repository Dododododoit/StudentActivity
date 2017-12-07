package umich.edu.studentactivity;

import android.app.IntentService;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

/**
 * Created by Kaitao on 2017/10/29.
 */

public class DetectService extends IntentService {

    private String Tag = DetectService.class.getSimpleName();
    private Context context;
    public DetectService() {
        super("DetectService");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        //String dataString = workIntent.getDataString();
        return;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Thread t = new Thread(
                new Runnable(){
                    public void run(){
                        while (true){
                            try {
                                TimeUnit.SECONDS.sleep(1);
                                checkScreen();
                            }
                            catch (Exception e){
                                Log.e(Tag, "Error in check screen waiting");
                            }
                        }
                    }
                }
        );
        t.start();


    }

    private void checkScreen(){
        KeyguardManager myKM = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        boolean isPhoneLocked = myKM.inKeyguardRestrictedInputMode();
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Long.toString(System.currentTimeMillis()));
        if(!isPhoneLocked){
            myRef.setValue("False");
            Log.d(Tag, "Student is using his phone!");
        }
        else {
            myRef.setValue("True");
            Log.d(Tag, "Student's phone is locked");
        }

    }
}
