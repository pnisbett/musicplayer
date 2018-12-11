package com.xware.musicplayer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;


//import android.support.v4.app.ActivityCompat;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import android.view.View.OnFocusChangeListener;
import com.xware.musicplayer.MusicService.MusicBinder;
import com.xware.util.HttpHandler;
import com.xware.util.JsonHandler;

import static android.R.attr.button;
import static android.R.attr.colorPrimary;
import static android.R.attr.filter;
import static android.R.attr.id;
import static android.R.attr.key;
import static android.R.attr.mipMap;
import static android.R.attr.name;
import static android.R.attr.onClick;
import static android.R.attr.track;
import static android.R.attr.windowSoftInputMode;
import static android.content.ContentValues.TAG;
//import static android.support.v4.app.ActivityCompatApi23.requestPermissions;
import static android.support.v4.content.PermissionChecker.checkSelfPermission;
import static android.view.View.GONE;
import static com.xware.musicplayer.R.id.action_settings;
import static com.xware.musicplayer.R.id.controllerview;
import static com.xware.musicplayer.R.id.keyboardview;
import static com.xware.musicplayer.R.id.lastSearch;
import static com.xware.musicplayer.R.id.mainview;
import static com.xware.musicplayer.R.id.searchButton;
import static com.xware.musicplayer.R.id.song_list;
import static com.xware.musicplayer.R.id.song_title;
import static com.xware.musicplayer.R.id.txtLyric;
import static com.xware.util.JsonHandler.getValue;

import com.xware.util.*;
// import android.support.v7.app.AppCompatActivity;

// public class MainActivity extends AppCompatActivity implements MediaPlayerControl {
public class MainActivity extends Activity implements MediaPlayerControl {
    private ArrayList<Song> songList;
    private ListView songView;
    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound = false;
    private MusicController controller;
    private boolean paused = false, playbackPaused = false;
    RetrieveFeedTask rt;
    static final int myREAD_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 666;
    EditText emailText;
    TextView responseView;
    ProgressBar progressBar;
    static final String API_KEY = "USE_YOUR_OWN_API_KEY";
    static final String API_URL = "https://api.fullcontact.com/v2/person.json?";
    String songArtist = "";
    String songTitle = "";
    enum filters {a,sh} ;
    private String filter ="a";
    private boolean searchmode = false ;
    static int count =1;
    //controls
    RadioButton rbLyrics ;
    RadioButton rbSonglist;
    RadioButton rbSearchArtist;
    RadioButton rbSearchSong ;
    Button bSearch ;
    EditText txtSearch;
    EditText txtSearch1;
    EditText txtSearch2;
    RadioGroup r;
    ListView lvSonglist;
    TextView lyric;
    TextView tvLastSearch;
    public MainActivity() {

    }
    protected void toggleView() {

        boolean t = false;
        int i = r.getCheckedRadioButtonId();

        //   InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        // imm.hideSoftInputFromWindow(l.getWindowToken(), 0);
        // l.setText("rb id ="+i +"\n"+"viewvisible="+View.VISIBLE+"\n"+"view gone= "+View.GONE);

        if (rbSonglist.isChecked()) {
         //   controller.setVisibility(View.VISIBLE);
     //       if (! controller.isShowing())
       //         controller.show(0);
            lvSonglist.setVisibility(View.VISIBLE);

            lyric.setVisibility(View.GONE);
            rbSearchArtist.setVisibility(View.VISIBLE);
            rbSearchSong.setVisibility(View.VISIBLE);
            bSearch.setVisibility(View.VISIBLE);
            txtSearch.setVisibility(View.VISIBLE);
            controller.setVisibility(View.VISIBLE);
         
        } else {
         //   controller.setVisibility(View.GONE);
        //    controller.hide();
            lvSonglist.setVisibility(View.GONE);
            lyric.setVisibility(View.VISIBLE);
            rbSearchArtist.setVisibility(View.GONE);
            rbSearchSong.setVisibility(View.GONE);
            bSearch.setVisibility(View.GONE);
            txtSearch.setVisibility(View.GONE);
            tvLastSearch.setVisibility(View.GONE);
            controller.setVisibility(View.INVISIBLE);
        }

    }

    protected void toggleSearch() {

        boolean t = false;

        RadioGroup r = (RadioGroup) findViewById(R.id.rg2);
       //  ListView lv = (ListView) findViewById(R.id.song_list);

        int i = r.getCheckedRadioButtonId();
        RadioButton rbSearchArtist = (RadioButton) findViewById(R.id.radioButton3);
    ;
      // txtSearch = (EditText) findViewById(R.id.txtSearch);
    //    String txt=txtSearch.getText().toString();
        //   InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        // imm.hideSoftInputFromWindow(l.getWindowToken(), 0);
        // l.setText("rb id ="+i +"\n"+"viewvisible="+View.VISIBLE+"\n"+"view gone= "+View.GONE);
     //   this.filter="";
     //   if (!txt.equals("")){
        if (rbSearchArtist.isChecked()) {
            this.filter="a";
        //    lvSonglist.setVisibility(View.VISIBLE);

        } else {
            this.filter="s";

        //    lvSonglist.setVisibility(View.GONE);

        //    l.setVisibility(View.VISIBLE);
        }
    //    }

    }
  /*  public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
*/

    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        r = (RadioGroup) findViewById(R.id.rg);
        lvSonglist = (ListView) findViewById(R.id.song_list);
        rbLyrics = (RadioButton) findViewById(R.id.radioButton1);
        rbSonglist = (RadioButton) findViewById(R.id.radioButton2);
        rbSearchArtist = (RadioButton) findViewById(R.id.radioButton3);
        rbSearchSong = (RadioButton) findViewById(R.id.radioButton4);
        bSearch = (Button) findViewById(R.id.searchButton);
        txtSearch = (EditText) findViewById(R.id.txtSearch);
      //  txtSearch1=(EditText) findViewById(R.id.txtSearch1);
      //  txtSearch2=(EditText) findViewById(R.id.txtSearch2);
        lyric = (TextView) findViewById(R.id.txtLyric);
        tvLastSearch= (TextView)findViewById(lastSearch);
        r = (RadioGroup) findViewById(R.id.rg);
        lvSonglist = (ListView) findViewById(R.id.song_list);
        rbLyrics = (RadioButton) findViewById(R.id.radioButton1);
        rbSonglist = (RadioButton) findViewById(R.id.radioButton2);
        rbSearchArtist = (RadioButton) findViewById(R.id.radioButton3);
        rbSearchSong = (RadioButton) findViewById(R.id.radioButton4);
        bSearch = (Button) findViewById(R.id.searchButton);


        lyric = (TextView) findViewById(R.id.txtLyric);
        tvLastSearch= (TextView)findViewById(lastSearch);

        setController();


   //     tsearch.onWindowFocusChanged();
     txtSearch.setOnTouchListener(new View.OnTouchListener() {


         @Override
         public boolean onTouch(View v, MotionEvent event) {
         /*    controller.hide();
             txtSearch2.setText("txtSearch onTouchListener called onTouch");
             if (v.hasFocus())
                 txtSearch.setText("ontouch clickdd has focusr");
             else
                 txtSearch.setText("notfocused ontouch clicked lost focus");
                 */
//txtSearch.setTextColor(colorPrimary);
             //  }
             //      if (v.isSelected() ) {
      /*       if (v.hasFocus()){
                 //                     controller.setVisibility(View.INVISIBLE);

                 //  if (controller.isShowing())
                 //     if (controller != null)
                 //       controller.hide();
                 //    controller.setVisibility(View.INVISIBLE);
                 int count2 = count % 2;
                 count++;
                 tsearch.setText("has focus "+count2);

               InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                 View v1= findViewById(R.id.keyboardview);
                 imm.showSoftInput(v1, 0);
                 controller.hide();
                 // imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                 v.setBackgroundColor(Color.RED);


             } else {
                 //       controller.setVisibility(View.VISIBLE);
                 //   controller.show(0);
                 v.setBackgroundColor(Color.CYAN);
                 InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                 imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                 controller.setVisibility(View.VISIBLE);
                 tsearch.setText("notfocused");
             }
            // Context context = v.getContext();
*/

        /*     if (event.getAction() == MotionEvent.ACTION_DOWN ) {
               //  controller.hide();

                 InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

                 View v1= findViewById(R.id.keyboardview);
             //    if (imm.isActive() == false)
                     imm.showSoftInput(v1, 0);



                 return true;
             }
             */
             return false;
         }
     });


    /*    //     tsearch.onWindowFocusChanged();
        tvLastSearch.setOnTouchListener(new View.OnTouchListener() {

        /*    @Override
            public boolean onTouch(View view, MotionEvent event) {
       //         controller.hide();

            if (event.getAction() == MotionEvent.ACTION_DOWN ) {
               //  controller.hide();

                 InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

                 View v1= findViewById(R.id.keyboardview);
             //    if (imm.isActive() == false)
                     imm.showSoftInput(v1, 0);



                 return true;
             }

                return false;
            }

        });
*/
        txtSearch.setOnFocusChangeListener(new OnFocusChangeListener() {

              @Override
              public void   onFocusChange(View v, boolean hasFocus) {

                     //   txtSearch1.setText("txtSearch onFocusChangeListener called onFocusChange");

                /*      if (v.hasFocus())
                          txtSearch.setText("fuck android "+ ((android.graphics.drawable.ColorDrawable)txtSearch.getBackground()).getColor());
                      else
                          txtSearch.setText("notfocused top");
                  */
                   //   if (v.hasFocus() != hasFocus)
                     //     Toast.makeText(,"onFocusChAnge() v.ahsFocus IS NOT EQUAL TO HAS FOCUS ",Toast.LENGTH_LONG);
                     //     txtSearch1.setText("onFocusChAnge() v.ahsFocus IS NOT EQUAL TO HAS FOCUS ");
Log.e("search change !!!"," Search Text "+txtSearch.getText()+ " that was the text in search ");

                      if (hasFocus){
                          //                     controller.setVisibility(View.INVISIBLE);

                        //  if (controller.isShowing())
                     //     if (controller != null)
                       //       controller.hide();
                         //    controller.setVisibility(View.INVISIBLE);
                          int count2 = count % 2;
                          count++;
                    //      txtSearch2.setText("tsearch has focus "+count2);
                          txtSearch.setText("");
                          InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

                         View v1= findViewById(R.id.keyboardview);
                         imm.showSoftInput(v1, 0);
//                          controller.hide();

                          v.setBackgroundColor(Color.WHITE);
                          controller.setVisibility(View.INVISIBLE);

                      } else {
                          //       controller.setVisibility(View.VISIBLE);
                          //   controller.show(0);
                          v.setBackgroundColor(Color.CYAN);
                          InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                          imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                           controller.setVisibility(View.VISIBLE);
                      //    txtSearch2.setText("txtSearch notfocused");
                      }
                      // Context context = v.getContext();


              }
        }

        );

        StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(old)
                .permitDiskWrites()
                .build());



        lyric.setVisibility(View.INVISIBLE);
        //  l.setEnabled(false);
        //   InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        //  imm.hideSoftInputFromWindow(l.getWindowToken(), 0);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Button btnlyric = (Button) findViewById(R.id.lyricButton);

        btnlyric.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Context context = v.getContext();
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
             //       if (controller != null)
               //       controller.setVisibility(View.INVISIBLE);
                }
                Log.i(" paths", "base context path " + getBaseContext() + "");




                // Perform action on click
                toggleView();




            }
        });

        // Search functionality


       // int i = r.getCheckedRadioButtonId();
        RadioButton rbSearchArtist = (RadioButton) findViewById(R.id.radioButton3);
        RadioButton rbSearchSong= (RadioButton) findViewById(R.id.radioButton4);
        Button btnSearch = (Button) findViewById(searchButton);
        songView = (ListView) findViewById(song_list);
        songList = new ArrayList<Song>();

      btnSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                songList.clear();
                songList.trimToSize();
                Context context = v.getContext();
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
             //   TextView searchTerm = (TextView) findViewById(R.id.txtSearch);
                String searchTerm=txtSearch.getText().toString() ;
                Log.i(" paths", "base context path " + getBaseContext() + "");


                searchmode=true;

                // Perform action on click
                toggleSearch();
                if (filter.equals("a")){

                    getSongListFiltered(searchTerm,null);
                    Collections.sort(songList, new Comparator<Song>() {
                        public int compare(Song a, Song b) {
                            return a.getTitle().compareTo(b.getTitle());
                        }
                    });

                    SongAdapter songAdt = new SongAdapter(getBaseContext(), songList);
                    songView.setAdapter(songAdt);
                //    setController();

                }
                else if (filter.equals("s")){
                    getSongListFiltered(null,searchTerm);
                    Collections.sort(songList, new Comparator<Song>() {
                        public int compare(Song a, Song b) {
                            return a.getTitle().compareTo(b.getTitle());
                        }
                    });

                    SongAdapter songAdt = new SongAdapter(getBaseContext(), songList);
                    songView.setAdapter(songAdt);
                 //   setController();

                }

                else{
                    getSongList();
                    Collections.sort(songList, new Comparator<Song>() {
                        public int compare(Song a, Song b) {
                            return a.getTitle().compareTo(b.getTitle());
                        }
                    });

                    SongAdapter songAdt = new SongAdapter(getBaseContext(), songList);
                    songView.setAdapter(songAdt);
                 //   setController();

                }
                TextView tvLastSearch= (TextView)findViewById(lastSearch);
                tvLastSearch.setBackgroundColor(Color.CYAN);
                tvLastSearch.setTextColor(Color.BLACK);
                tvLastSearch.setText(searchTerm+"");
                txtSearch.setText("");
                tvLastSearch.setVisibility(View.VISIBLE);
                tvLastSearch.requestFocus();



            }
        });


       if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

           ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                   MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            // Should we show an explanation?
 /*           if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }
*/


            return;
        }


        getSongList();
        Collections.sort(songList, new Comparator<Song>() {
            public int compare(Song a, Song b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });
        SongAdapter songAdt = new SongAdapter(this, songList);
        songView.setAdapter(songAdt);
        songView.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (v.hasFocus()) {
                    if (songView.getSelectedView() != null)
                    songView.getSelectedView().setBackgroundColor(Color.parseColor("#ff6d00"));
                 //   v.setBackgroundColor(Color.parseColor("#ff6d00"));
                }
             //   else
              ///      v.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

            }
        });
    setController();

    //end of onCreate()

    }

  /*   @Override
     public void onRequestPermissionsResult(int requestCode,
                                            String permissions[], int[] grantResults) {
         switch (requestCode) {
             case myREAD_EXTERNAL_STORAGE: {
                 // If request is cancelled, the result arrays are empty.
                 if (grantResults.length > 0
                         && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                     // permission was granted, yay! Do the
                     // contacts-related task you need to do.
                     setContentView(R.layout.activity_main);
                     songView = (ListView)findViewById(R.id.song_list);
                     songList = new ArrayList<Song>();
                     getSongList();
                     Collections.sort(songList, new Comparator<Song>(){
                         public int compare(Song a, Song b){
                             return a.getTitle().compareTo(b.getTitle());
                         }
                     });
                     SongAdapter songAdt = new SongAdapter(this, songList);
                     songView.setAdapter(songAdt);
                     setController();

                 } else {

                     // permission denied, boo! Disable the
                     // functionality that depends on this permission.
                 }
                 return;
             }

             // other 'case' lines to check for other
             // permissions this app might request
         }

     }
     */

    private boolean checkMatch(String a,String b , boolean rev){
        int count =0;
        int alength =0 ;
        int blength = 0 ;
        if (a != null && b !=null && a.length()> 0 && b.length()> 0 ) {
            alength = a.length();

            blength = b.length();
        }
        else
            return false;
        Double duba = alength * .66;
        int mosta =duba.intValue();
        String sa =a.toLowerCase().replaceAll(" ","").replaceAll("-","").replaceAll("_","");
        String sb =b.toLowerCase().replaceAll(" ","").replaceAll("-","").replaceAll("_","");
        String sb2 =sb;
        int begin=2;
        try {
            if (sb.length() > 3 && sb.substring(0, 3).equals("the"))
                sb = sb.substring(2);
            if (sa.length() > 3 && sa.substring(0, 3).equals("the"))
                sa = sa.substring(2);
            if (sa.startsWith(sb.substring(0, 2).toLowerCase())) {
                for (int i = 0; i < a.length() - 1; i++) {
                    if (i < blength - 1)
                        if (sa.charAt(i) == sb.charAt(i))
                            count++;
                }
                if (count >= mosta)
                    return true;
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage()+ " a= "+sa+ "  sb ="+sb);

        }

        String ra=strReverse(sa);
        String rb=strReverse(sb);
        System.out.println(" ra = " +ra);

                System.out.println("rb= "+rb);

        if ( rev ) { // must set rev to false to avoid endless loop!!!!!
            checkMatch(ra,rb, false);
        }


        return false;
    }
    private String strReverse(String s){
        StringBuilder sb = new StringBuilder();
     //   sb.append("s = "+s);
        try{
        for (int i=s.length()-1;i> 0;--i){
         //   sb.append("s.length = " +s.length());
            sb.append(s.charAt(i));
   //         System.out.println(" i is " +s.charAt(i));

        }
        }
        catch (Exception e){
            System.out.println(e.getMessage()+ "ERROR =" + e.getMessage() +" STRING IS "+ s);

        }
      //  System.out.println(" STRING REVERSE !!!!!!"+sb.toString());
        return sb.toString();

    }
    public void songPicked(View view) {
        musicSrv.setSong(Integer.parseInt(view.getTag().toString()));
        musicSrv.playSong();
        String ls = musicSrv.getLyric();
        songTitle = musicSrv.getCurSong();
        songArtist = musicSrv.getCurArtist();
        //      responseView = (TextView) findViewById(R.id.responseView);
        //     emailText = (EditText) findViewById(R.id.emailText);
        //    progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //   TextView l=(TextView)findViewById(R.id.txtLyric);

        //   l.setText(ls);
        TextView lastSearch=(TextView)findViewById(R.id.lastSearch);
        lastSearch.setVisibility(View.VISIBLE);
        lastSearch.setText(songTitle);
        rt = null;
        rt =
                new RetrieveFeedTask();
        rt.execute();

        if (playbackPaused) {
         //   setController();
            controller.setVisibility(View.VISIBLE);

            playbackPaused = false;
        }

      //  setController();
        // controller.setVisibility DOES NOTHINGco

        controller.setVisibility(View.VISIBLE);
   //     controller.setAnchorView(findViewById(controllerview));
        controller.show(0);
    }
    private void setController() {
        //set the controller up
        if (controller== null)
          controller = new MusicController(this);
       // controller.setAnchorView(findViewById(controllerview));
        // LayoutParams lp = new LayoutParams();
        //lp.
       // controller.setLayoutParams();
        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });
        controller.setMediaPlayer(this);
     //   controller.setAnchorView(findViewById(mainview));
        controller.setAnchorView(findViewById(controllerview));
        controller.setEnabled(true);
   //     controller.setVisibility(View.VISIBLE);
      //  if (! controller.isShowing())
       //     controller.show(0);
    }

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicBinder binder = (MusicBinder) service;
            //get service
            musicSrv = binder.getService();
            //pass list
            musicSrv.setList(songList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
            // musicConnection.
        }
    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        //   if(bound && !binding){
        //      binding = true;
        unbindService(musicConnection);
        // }
        musicSrv = null;
        super.onDestroy();
    }

    public void getSongList() {
        //retrieve song info
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                songList.add(new Song(thisId, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());
        }

    }
    public void getSongListFiltered(String artist,String songf) {
        //retrieve song info
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                if (artist != null) {
                    if (checkMatch(artist, thisArtist,true))
                        songList.add(new Song(thisId, thisTitle, thisArtist));
                }
                else{
                    if(checkMatch(songf, thisTitle,true))
                        songList.add(new Song(thisId, thisTitle, thisArtist));

                }
            }
            while (musicCursor.moveToNext());
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //menu item selected
        switch (item.getItemId()) {
            case R.id.action_shuffle:
                musicSrv.setShuffle();
                break;
            case R.id.action_end:
                stopService(playIntent);
                musicSrv = null;
                System.exit(0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public int getDuration() {
        if (musicSrv != null && musicBound && musicSrv.isPng())
            return musicSrv.getDur();
        else return 0;
    }


    @Override
    public int getCurrentPosition() {
        if (musicSrv != null && musicBound && musicSrv.isPng())
            return musicSrv.getPosn();
        else return 0;
    }

    @Override
    public void pause() {
        musicSrv.pausePlayer();
        playbackPaused = true;

        //paulnew
      //  controller.setSelected(true);
    }

    @Override
    public void seekTo(int pos) {
        musicSrv.seek(pos);
    }

    @Override
    public void start() {
        musicSrv.playSong();
     //   musicSrv.go();
    }

    @Override
    public boolean isPlaying() {
        if (musicSrv != null && musicBound)
            return musicSrv.isPng();
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    /*
       @Override
       public boolean canPause() {
           return false;
       }

       @Override
       public boolean canSeekBackward() {
           return false;
       }

       @Override
       public boolean canSeekForward() {
           return false;
       }
    */
    @Override
    public int getAudioSessionId() {
        return 0;
    }

    //play next
    private void playNext() {
        musicSrv.playNext();
        if (playbackPaused) {
            setController();
            playbackPaused = false;
        }
        controller.show(0);
    }

    //playprev
    private void playPrev() {
        musicSrv.playPrev();
        if (playbackPaused) {
            setController();
            playbackPaused = false;
        }
        controller.show(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (paused) {
            setController();
            paused = false;
        }
    }

    @Override
    protected void onStop() {
        //paul new
       controller.hide();

        controller.setSelected(false);
        super.onStop();
    }


    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        paused = true;
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
            //    progressBar.setVisibility(View.VISIBLE);
            //   responseView.setText("bob");
            TextView l = (TextView) findViewById(R.id.txtLyric);

            l.setText("bob is waiting...");
        }

        String prepText(String s) {
            return s.replace(' ', '+');


        }

        protected String doInBackground(Void... urls) {
            //  String email = emailText.getText().toString();
            // Do some validation here
            //    if(android.os.Debug.isDebuggerConnected())
            //  android.os.Debug.waitForDebugger();
            //  android.os.Debug.waitForDebugger();
            try {
       /* Different Lyric Providers and api keys
       *
       * used in getTrackSearchData
       * and getLyricSearchData
       * */
          /*      String apikey = "86c87a879ef9074845e49bef93351d12";

                String az = "http://azlyrics.com/lyrics/" + songArtist + "/" + songTitle + ".html";
                String lolo = "http://api.lololyrics.com/0.5/getLyric?artist=" + songArtist + "&track=" + songTitle + "";
                if (az.substring(0, 2).toLowerCase().equals("the"))
                    az = az.substring(2);
                String sl = "http://api.chartlyrics.com/apiv1.asmx/SearchLyric?artist=" + songArtist + "&song=" + songTitle + "";
                String sld = "http://api.chartlyrics.com/apiv1.asmx/SearchLyricDirect?artist=" + songArtist + "&song=" + songTitle + "";

                String mm = "http://api.musixmatch.com/ws/1.1/?apikey=" + apikey + "&q_track=" + songTitle + "&q_artist=" + songArtist;
                mm = "http://api.musixmatch.com/ws/1.1/track.search?apikey=86c87a879ef9074845e49bef93351d12&q_track=" + songTitle + "&q_artist=" + songArtist + "&page_size=10";
                mm = "http://api.musixmatch.com/ws/1.1/track.search?apikey="+apikey+"&q_track=" + songTitle + "&q_artist=" + songArtist + "&page_size=10";

*/


                String result = getTrackSearchData();
                String firstresult = getValuelast(result);
                String resfiltered =getTrackValueFiltered(result);
                if(resfiltered.equals("-4")) {
                    Integer id = getTrackValue(result);
                    String vl = getValuelast(result);
                    vl = getLyricSearchData(id);
                    String lyrics = getLyricValue(vl);
                  //  String firstresult = getValuelast(result);
                    return firstresult + "\n" + lyrics;
                }
                else
                    return "filtered"+ "\n"+firstresult+ "\n" +resfiltered;


            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return " this music service sucks ass!" + e.getMessage();
                //             return null;
            }
            //   return "nothing happened" ;
        }

        protected String getTrackSearchData() {

            String mm = "http://api.musixmatch.com/ws/1.1/track.search?apikey=86c87a879ef9074845e49bef93351d12&q_track=" + songTitle + "&q_artist=" + songArtist + "&page_size=10";
            String apikey =  "34b8bba02482d8369f31edf0d82ff1f4";
             mm = "http://api.musixmatch.com/ws/1.1/track.search?apikey="+apikey+"&q_track=" + songTitle + "&q_artist=" + songArtist + "&page_size=10";

            try {
                URL url = new URL(mm);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        if ((line.indexOf("track_id") > 0) || (line.indexOf("track_name") > 0))
                            stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    //   String [] keys= {"track_id","track_name"};
                    //   String v =JsonHandler.getValue(keys,stringBuilder.toString());

                    return stringBuilder.toString();
                    //  return v ;


                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage(), e);
                    return " this music service sucks ass!" + e.getMessage();
                    //             return null;
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return " this music service sucks ass!" + e.getMessage();

            }


        }


        protected String getLyricSearchData(Integer i) {
      //      String mm = "http://api.musixmatch.com/ws/1.1/?track.lyrics.get?apikey=86c87a879ef9074845e49bef93351d12&track_id=" + id;
                   String apikey =  "34b8bba02482d8369f31edf0d82ff1f4";
          //  mm = "http://api.musixmatch.com/ws/1.1/track.search?apikey="+apikey+"&q_track=" + songTitle + "&q_artist=" + songArtist + "&page_size=10";
            String mm = "http://api.musixmatch.com/ws/1.1/track.lyrics.get?apikey="+apikey+"&track_id=" + i;

            try {
                URL url = new URL(mm);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        //      if ((line.indexOf("track_id")>0)  ||(line.indexOf("track_name")>0))
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();


                    return stringBuilder.toString();
                    //  return v ;


                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage(), e);
                    return " this music service sucks ass!" + e.getMessage();
                    //             return null;
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return " this music service sucks ass!" + e.getMessage();
                //             return null;
            }
            //  return "";

        }

        protected void onPostExecute(String response) {
            if (response == null) {
                response = "THERE WAS AN ERROR";
            }
            //    progressBar.setVisibility(View.GONE);
            Log.i("INFO", response);
            //   responseView.setText(response);
            TextView l = (TextView) findViewById(R.id.txtLyric);
            String[] keys = {"track_id", "track_name"};
         //   String res = getValue(keys, response);
          //  l.setText("bob has waited ..." + response);
            l.setText(response);
            // TODO: check this.exception
            // TODO: do something with the feed


        }

        public Integer getTrackValue(String jsonStr) {
            JSONObject jsonObj = null;
            JSONObject jb = null;
            JSONObject jb1 = null;
            Integer t3 = 0;
            if (jsonStr != null) {
                try {

                    jb = new JSONObject(jsonStr);


                    jsonObj = jb.getJSONObject("message");
                    jb1 = jsonObj.getJSONObject("body");


                    if (jb1 == null)
                        return -1;
                    StringBuilder sb = new StringBuilder();
                    JSONArray tl = jb1.getJSONArray("track_list");
                    if (tl != null) {
                        JSONObject t = tl.getJSONObject(0);
                        String stid = t.toString();// (String)t.get("track_id");
                        JSONObject t2 = t.getJSONObject("track");
                        t3 = t2.getInt("track_id");
                        if (t3 != null)
                            return t3;
                    } else
                        return -2;


                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    return -3;//"jb.toString()
                    //   return "Json parsing error: " + e.getMessage();


                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                return -4; //"Couldn't get json from server.";

            }
            return -5;

        }
        public String getTrackValueFiltered(String jsonStr) {
            JSONObject jsonObj = null;
            JSONObject jb = null;
            JSONObject jb1 = null;
            Integer t3 = 0;
            if (jsonStr != null) {
                try {

                    jb = new JSONObject(jsonStr);


                    jsonObj = jb.getJSONObject("message");
                    jb1 = jsonObj.getJSONObject("body");


                    if (jb1 == null)
                        return "jb is null";
                    StringBuilder sb = new StringBuilder();
                    String ls;
                    JSONArray tl = jb1.getJSONArray("track_list");
                    if (tl != null) {
                        JSONObject deflt = tl.getJSONObject(0);
                        JSONObject dft2 = deflt.getJSONObject("track");
                        t3 = dft2.getInt("track_id");
                        String trackname = dft2.getString("track_name");
                        String artist = dft2.getString("artist_name");
                        //let's see if we can do better...
                        for (int i = 0; i < tl.length(); i++) {
                            JSONObject t = tl.getJSONObject(i);
                            //    String vl= getValuelast(result);

                            String stid = t.toString();// (String)t.get("track_id");
                            JSONObject t2 = t.getJSONObject("track");
                            t3 = t2.getInt("track_id");
                            trackname = t2.getString("track_name");
                            artist = t2.getString("artist_name");
                            String ret="";
                            if(artist.equals(songArtist)&&trackname.equals(songTitle)){
                                ret=getLyricSearchData(t3) ;


                            }

                            if (!ret.equals("")) {
                                ls = getLyricSearchData(t3);
                                String lv =getLyricValue(ls);
                                return lv ;
                            }

                        }
                        ls = getLyricSearchData(t3);
                        String lv =getLyricValue(ls);
                        return lv ;
                    } else
                        return "-4 ";


                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    return "-4";//"jb.toString()
                    //   return "Json parsing error: " + e.getMessage();


                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                return "-4"; //"Couldn't get json from server.";

            }
           // return "-5";

        }

        public String getLyricValue(String jsonStr) {
            JSONObject jsonObj = null;
            JSONObject jb = null;
            JSONObject jb1 = null;
            Integer t3 = 0;
            String stid = "bogus no lyrics found";
            if (jsonStr != null) {
                try {

                    jb = new JSONObject(jsonStr);


                    jsonObj = jb.getJSONObject("message");
                    jb1 = jsonObj.getJSONObject("body");
                    JSONObject t = jb1.getJSONObject("lyrics");

                    if (jb1 == null)
                        return "jb is fucking null";
                    //     StringBuilder sb = new StringBuilder();
                    //      JSONArray tl = jb1.getJSONArray("");
                    //   if (tl != null) {
                        // JSONObject t2 = t.getJSONObject("lyrics_body");

                    stid = t.getString("lyrics_body") ; //t2.toString();// (String)t.get("track_id");

                    //         JSONObject t2 = t.getJSONObject("track");
                    //      t3 = t2.getInt("track_id");
                    //    if (t3 != null)
                    //      return t3;
                    //}
                    // else
                    //   return -2;


                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    return "get lyric value " + e.getMessage();
                    //   return "Json parsing error: " + e.getMessage();


                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                return "Couldn't get json from server.";

            }
            return stid;

        }

      //  public String getValuelast(String[] keys, String jsonStr) {
            public String getValuelast( String jsonStr) {
            JSONObject jsonObj = null;
            JSONObject jb = null;
            JSONObject jb1 = null;
            if (jsonStr != null) {
                try {

                    jb = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    //  JSONArray jr = new JSONArray(jsonStr);
                    jsonObj = jb.getJSONObject("message");
                    jb1 = jsonObj.getJSONObject("body");
                    //       jb1= jb.getJSONObject("body");
                    //    JSONArray st = jsonObj.getJSONArray("body");
                    JSONArray tl = jb1.getJSONArray("track_list");
                    StringBuilder sb = new StringBuilder();
                    // looping through All disabled jsu treturning first record fiund
                //    for (int i = 0; i < tl.length(); i++) {
                    //    JSONObject t = tl.getJSONObject(i);
                    JSONObject t = tl.getJSONObject(0);
                        String stid = t.toString();// (String)t.get("track_id");
                        JSONObject t2 = t.getJSONObject("track");
                        Integer t3 = t2.getInt("track_id");
                        //    String id = t.getString("track_id");
                        String trackname = t2.getString("track_name");
                    String artist = t2.getString("artist_name");
                    String album = t2.getString( "album_name");
                        sb.append(trackname +" - "+ artist+" "+album);
                        sb.append("\n");
                        sb.append(t3.toString());
                        sb.append("\n");
                        //    for (String key:keys){
                        //  if ( t.getString(key) !=null)
                        //   sb.append(key +"- "+ t.getString(key));

                        //         sb.append(key +"- "+ i ); //t.getString(key));
                        //        sb.append("\n");
                        sb.append("------------------");
                        sb.append("\n");
                        //     }


               //     }
                    if (tl != null)
                        return sb.toString();
             /*  */
                    if (jb1 == null)
                        //  return jb.toString();
                        // else
                        return "jb is null";
                    else return
                            jsonObj.toString();
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    return "get value last()  android" + e.getMessage();
                    //   return "Json parsing error: " + e.getMessage();
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
}

//  doCorrectStuffThatWritesToDisk();
// StrictMode.setThreadPolicy(old);
// Here, thisActivity is the current activity
       /*
       if (ContextCompat.checkSelfPermission(this,
               Manifest.permission.READ_EXTERNAL_STORAGE)
               != PackageManager.PERMISSION_GRANTED) {

           // Should we show an explanation?
           if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                   Manifest.permission.READ_EXTERNAL_STORAGE)) {

               // Show an explanation to the user *asynchronously* -- don't block
               // this thread waiting for the user's response! After the user
               // sees the explanation, try again to request the permission.

           } else {

               // No explanation needed, we can request the permission.

               ActivityCompat.requestPermissions(this,
                       new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                       myREAD_EXTERNAL_STORAGE);

               // myREAD_EXTERNAL_STORAGE is an
               // app-defined int constant. The callback method gets the
               // result of the request.
           }
       }
       */
       /*
       private void checkPermission(){
           int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

           if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
               ActivityCompat.requestPermissions(
                       getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.WRITE_EXTERNAL_STORAGE);
           } else {
               callMethod();
           }
       }
       */
/* december 17
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.INTERNET, Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.WAKE_LOCK};
    boolean bb = ! hasPermissions(this, PERMISSIONS);
    if( bb==true ){
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
    }
   */
        /*

             int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
            //TODO
        }
        @Override
public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
    switch (requestCode) {
        case REQUEST_READ_PHONE_STATE:
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                //TODO
            }
            break;

        default:
            break;
    }
}
         */
