package com.xware.musicplayer;

import android.os.Bundle;
import android.app.Activity;
import android.widget.ListView;

//import example.com.myapplication.R;

public class LyricActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String s =savedInstanceState.getString("content","");
        Bundle b = getIntent().getExtras();
        if (b != null) {
            String name = b.getString("content");
         /*   String description = b.getString("description");
            String address = b.getString("address");
            String email = b.getString("email");
            String phone = b.getString("phone");
*/

     //       Place place = new Place("0", name, description, address, email, phone);
       //     PlaceContent.addItem(place,getApplicationContext()); //addItem(Place item);
        }
     //   setContentView(R.layout.activity_lyric);


        setContentView(R.layout.songdisplay);
    }

}
