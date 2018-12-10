package com.xware.musicplayer;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

//import static example.com.musicplayer.R.id.lyric;

/**
 * Created by paul on 3/4/17.
 */
public class SongAdapter extends BaseAdapter {
    private ArrayList<Song> songList;
    private ListView songView;
    private ArrayList<Song> songs;
    private LayoutInflater songInf;

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }


    public SongAdapter(Context c, ArrayList<Song> theSongs){
        songs=theSongs;
        songInf=LayoutInflater.from(c);
    }






    @Override
    public int getCount() {
        return songs.size();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //map to song layout
        LinearLayout songLay = (LinearLayout)songInf.inflate
                (R.layout.song, parent, false);
        //get title and artist views
        TextView songView = (TextView)songLay.findViewById(R.id.song_title);
        TextView artistView = (TextView)songLay.findViewById(R.id.song_artist);
        TextView lyricView = (TextView)songLay.findViewById(R.id.lyric);
        //get song using position
        Song currSong = songs.get(position);
        //get title and artist strings
        songView.setText(currSong.getTitle());
        artistView.setText(currSong.getArtist());
        lyricView.setText(currSong.getLyric());
      //  songLay.setBackgroundColor(Color.parseColor("#ff6d00"));
        //set position as tag
        songLay.setTag(position);
        return songLay;
    }

}
