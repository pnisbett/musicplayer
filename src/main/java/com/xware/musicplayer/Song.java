package com.xware.musicplayer;

/**
 * Created by paul on 3/4/17.
 */

public class Song {
    private long id;
    private String title;
    private String artist;

    public String getLyric() {
        return Lyric;
    }

    public void setLyric(String lyric) {
        Lyric = lyric;
    }

    private String Lyric;
    public Song(long songID, String songTitle, String songArtist) {
        id=songID;
        title=songTitle;
        artist=songArtist;
    }
    public long getID(){return id;}
    public String getTitle(){return title;}
    public String getArtist(){return artist;}
}
