package com.xware.musicplayer;


import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Random;

import static android.R.attr.data;
import static android.R.attr.logo;
import static com.xware.musicplayer.R.layout.song;

/**
 * Created by paul on 3/5/17.
 */


/**
 * Created by paul on 3/4/17.
 */

public class MusicService extends Service  implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    //media player
    private MediaPlayer player;
    //song list
    private ArrayList<Song> songs;
    //current position
    private int songPosn;
    private final IBinder musicBind = new MusicBinder();
    private String songTitle = "";
    private static final int NOTIFY_ID = 1;
    private boolean shuffle = false;
    private Random rand;
    private long songId;

    private String songArtist="" ;

    public String getCurSong(){
        return songTitle;
    }
    public String getCurArtist(){
        return songArtist;
    }
    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    private String lyric ="";
    public void onCreate() {
        //create the service
//create the service
        super.onCreate();
//initialize position
        songPosn = 0;
//create player
        player = new MediaPlayer();
        initMusicPlayer();
        rand = new Random();

    }

    public void setShuffle() {
        if (shuffle) shuffle = false;
        else shuffle = true;
    }
    public int getPosn(){
        return player.getCurrentPosition();
    }

    public int getDur(){
        return player.getDuration();
    }

    public boolean isPng(){
        return player.isPlaying();
    }

    public void pausePlayer(){
        player.pause();
    }

    public void seek(int posn){
        player.seekTo(posn);
    }

    public void go(){
    //    player.reset();
        player.start();
    }
    public void playNext() {
        if (shuffle) {
            int newSong = songPosn;
            while (newSong == songPosn) {
                newSong = rand.nextInt(songs.size());
            }
            songPosn = newSong;
        } else {
            songPosn++;
            if (songPosn >= songs.size()) songPosn = 0;
        }
        playSong();
    }
    public void playPrev(){
        songPosn--;
        if(songPosn <0) songPosn=songs.size()-1;
        playSong();
    }

    public void setList(ArrayList<Song> theSongs) {
        songs = theSongs;
    }

    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    public void initMusicPlayer() {
        //set player properties
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);

    }


    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;


    }

    @Override
    public boolean onUnbind(Intent intent) {
        player.stop();
        player.release();
        return false;
    }

    public void playSong() {
        //play a song
        player.reset();

        //get song
        Song playSong = songs.get(songPosn);
        songTitle = playSong.getTitle();
        songArtist =playSong.getArtist();
        songId=playSong.getID();
//get id
        long currSong = playSong.getID();
//set uri
        playSong.setLyric("dum dum");
     //   updateUI(songId,songTitle,songArtist);
        playSong.setLyric(this.getLyric());
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currSong);
        try {
            player.setDataSource(getApplicationContext(), trackUri);
        } catch (Exception e) {
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
        player.prepareAsync();

    }

    protected String getLyrics(String songArtist,String title){
        DefaultHttpClient client = new DefaultHttpClient();

  //      private static AsyncHttpClient client = new AsyncHttpClient();
   /*   String url= "http://api.musixmatch.com/ws/1.1/track.search?q_artist="+songArtist+
                "&page_size=5&page=1&q_track=" +title+"";
        url = "http://api.chartlyrics.com/apiv1.asmx/SearchLyricDirect?artist="+songArtist+"&song="+title;
        HttpGet getMethod = new HttpGet(url);

        try {
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody=client.execute(getMethod,responseHandler);
        }
        catch(Throwable t){
            Toast.makeText(this,"LyricActivity request failed miserably" +t.toString(),Toast.LENGTH_LONG);


        }
        */
        return songArtist+""+title;

    }
    public void updateUI(long songId,String songTitle,String songArtist){

           String content = "This is the song" + "\n" + songId+ "\n"+songArtist +"\n"+songTitle +"\n" ;
       //    setLyric(content);

        // The connection URL
       // String url = "https://ajax.googleapis.com/ajax/" +
         //       "services/search/web?v=1.0&q={query}";
        String url = "http://api.chartlyrics.com/apiv1.asmx/SearchLyricDirect?artist="+songArtist+"&song="+songTitle+"";
// Create a new RestTemplate instance
  //      RestTemplate restTemplate = new RestTemplate();

// Add the String message converter
    //    restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

// Make the HTTP GET request, marshaling the response to a String
      //  String result = restTemplate.getForObject(url, String.class, "Android");
      String result=  getLyrics(songArtist,songTitle);
        setLyric(result);


    }

  private  String getFromRest(String urlp){

   /*     try{
            // Set Request parameter
            data +="&" + URLEncoder.encode("data", "UTF-8") + "="+url.getText();

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    */
        BufferedReader reader =null;
    try
    {

        // Defined URL  where to send data
        URL url = new URL(urlp);

        // Send POST data request

        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write( data );
        wr.flush();

        // Get the server response

         reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line = null;

        // Read Server Response
        while((line = reader.readLine()) != null)
        {
            // Append server response in string
            sb.append(line + " ");
        }

        // Append Server Response To Content String
      //  Content s= sb.toString();
    }
    catch(Exception ex)
    {
        String e = ex.getMessage();
    }
    finally
    {
        try
        {
          if (reader !=null)
            reader.close();
        }

        catch(Exception ex) {}
    }

    /*****************************************************/
    return null;
}
    // Call after onPreExecute method
    protected Void doInBackground(String... urls) {

        /************ Make Post Call To Web Server ***********/
        BufferedReader reader=null;

        // Send data
        try
        {

            // Defined URL  where to send data
            URL url = new URL(urls[0]);

            // Send POST data request

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( data );
            wr.flush();

            // Get the server response

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                // Append server response in string
                sb.append(line + " ");
            }

            // Append Server Response To Content String
          //  Content = sb.toString();
        }
        catch(Exception ex)
        {
           // Error = ex.getMessage();
        }
        finally
        {
            try
            {

                reader.close();
            }

            catch(Exception ex) {}
        }

        /*****************************************************/
        return null;
    }
    @Override
    public void onCompletion(MediaPlayer mp) {
        if(player.getCurrentPosition() >0){
            mp.reset();
            playNext();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //start playback
        mp.start();
        Intent notIntent = new Intent(this, MainActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(pendInt)
                .setSmallIcon(R.drawable.play)
           //     .setSmallIcon(R.drawable.rand)//change to pause icon
                .setTicker(songTitle)
                .setOngoing(true)
                .setContentTitle("Playing")
                .setContentText(songTitle);
        Notification not = builder.build();

        startForeground(NOTIFY_ID, not);
    }

    public void setSong(int songIndex) {
        songPosn = songIndex;
    }

    @Override
    public void onDestroy() {
        stopForeground(true);

    }


}
