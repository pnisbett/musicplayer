package com.xware.util;

import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import static android.content.ContentValues.TAG;


/**
 * Created by paul on 3/15/17.
 */

public  class JsonHandler {


    JsonReader jr;


public static String getValue(String[] keys,String jsonStr){

    if (jsonStr != null) {
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            // Getting JSON Array node
            JSONArray tl = jsonObj.getJSONArray("track_list");
            StringBuilder sb = new StringBuilder();
            // looping through All Contacts
            for (int i = 0; i < tl.length(); i++) {
                JSONObject t = tl.getJSONObject(i);
            //    String id = t.getString("track_id");
             //   String name = t.getString("track_name");

                for (String key:keys){
                    sb.append(key +"-"+t.getString(key));
                    sb.append("------------------");
                }


            }
            return sb.toString();
        } catch (final JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
            return "Json parsing error: " ;
         /*   runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Json parsing error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
            */

        }

    } else {
        Log.e(TAG, "Couldn't get json from server.");
        return "Couldn't get json from server.";
      /*  runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),
                        "Couldn't get json from server. Check LogCat for possible errors!",
                        Toast.LENGTH_LONG).show();
            }
        });*/
    }

   // return "";
}
}

