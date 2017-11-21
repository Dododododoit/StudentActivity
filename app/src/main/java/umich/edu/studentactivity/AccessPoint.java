package umich.edu.studentactivity;

import android.content.Context;
import android.nfc.Tag;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import static junit.framework.Assert.assertTrue;

/**
 * Created by Kaitao on 2017/11/20.
 */

public class AccessPoint {
    String TAG= this.getClass().getSimpleName();
    Map<String, String> AccessPoints;
    final String FILENAME = "bssid.txt";
    Context context;

    public AccessPoint(Context _context){
        context = _context;
        AccessPoints = new HashMap<>();
        initialAccessPoints();
    }


    @Override
    public String toString(){
        String pts = "";
        for (Map.Entry<String, String> entry : AccessPoints.entrySet()) {
            pts += entry.getKey();
            pts += " ";
            pts += entry.getValue();
            pts += "\n";
        }
        return pts;
    }



    public void initialAccessPoints(){
        try {
            String line = null;
            FileInputStream fis = context.openFileInput(FILENAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            while ((line = bufferedReader.readLine()) != null) {
                String[] elements = line.split(" ");
                AccessPoints.put(elements[0], elements[1]);
            }
        }
        catch (Exception e){
            Log.d(TAG, "Error in initializing AccessPoints");
        }
    }




    public void updateAccessPoints(String location, String bssid){
        for(String Bssid: AccessPoints.values()){
            if(Bssid.equals(bssid)){
                return;
            }
        }
        while(AccessPoints.containsKey(location)){
            location += "\'";
        }
        AccessPoints.put(location, bssid);
        WriteToFile(location + " "+ bssid);
    }




    private  void WriteToFile(String s) {
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(FILENAME, Context.MODE_APPEND);
            outputStream.write(s.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
