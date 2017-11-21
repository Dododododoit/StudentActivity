package umich.edu.studentactivity;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.List;

/**
 * Created by Kaitao on 2017/11/6.
 * reference: https://stackoverflow.com/questions/14574879/how-to-detect-movement-of-an-android-device
 *            https://code.tutsplus.com/tutorials/how-to-recognize-user-activity-with-activity-recognition--cms-25851
 */

public class MovementDetector extends IntentService implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private String TAG = DetectService.class.getSimpleName();
    public GoogleApiClient googleApiClient = null;
    private Context context;
    public MovementDetector() {
        super("MovementDetector");
    }


    public void onConnected(Bundle bundle) {
        Intent intent = new Intent(this, MovementDetector.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(googleApiClient, 1000, pendingIntent);
        Log.d(TAG, "Connected");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.e(TAG, "ConnectionFailed");
        GoogleApiAvailability apiAvail = GoogleApiAvailability.getInstance();
        int errorCode = apiAvail.isGooglePlayServicesAvailable(this);
        if (errorCode == ConnectionResult.SUCCESS) {
            Log.d(TAG,"GooglePlayService passed");
        } else {
            Log.d(TAG,"GooglePlayService not available, please install or update");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        googleApiClient = new GoogleApiClient.Builder(context)
                        .addApi(ActivityRecognition.API)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .build();
        Log.d(TAG, "Connect");
        googleApiClient.connect();

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG,  "Intent got" );
        if(ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            handleDetectedActivities( result.getProbableActivities() );
        }
    }

    private void handleDetectedActivities(List<DetectedActivity> probableActivities) {
        for( DetectedActivity activity : probableActivities ) {
            switch( activity.getType() ) {
                case DetectedActivity.IN_VEHICLE: {
                    Log.d(TAG,  "ActivityRecogition"+ "In Vehicle: " + activity.getConfidence() );
                    break;
                }
                case DetectedActivity.ON_BICYCLE: {
                    Log.d(TAG,  "ActivityRecogition"+ "On Bicycle: " + activity.getConfidence() );
                    break;
                }
                case DetectedActivity.ON_FOOT: {
                    Log.d(TAG,  "ActivityRecogition"+ "On Foot: " + activity.getConfidence() );
                    break;
                }
                case DetectedActivity.RUNNING: {
                    Log.d(TAG,  "ActivityRecogition"+ "Running: " + activity.getConfidence() );
                    break;
                }
                case DetectedActivity.STILL: {
                    Log.d(TAG, "ActivityRecogition"+ "Still: " + activity.getConfidence() );
                    break;
                }
                case DetectedActivity.TILTING: {
                    Log.d(TAG,  "ActivityRecogition"+ "Tilting: " + activity.getConfidence() );
                    break;
                }
                case DetectedActivity.WALKING: {
                    Log.d(TAG, "ActivityRecogition"+ "Walking: " + activity.getConfidence() );
                    break;
                }
                case DetectedActivity.UNKNOWN: {
                    Log.d(TAG, "ActivityRecogition"+ "Unknown: " + activity.getConfidence() );
                    break;
                }
            }
        }
    }

}
