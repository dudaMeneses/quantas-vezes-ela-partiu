package com.duda.quantasvezeselapartiu.helper;

import com.duda.quantasvezeselapartiu.model.response.SpotifyMusic;

public class SpotifyMusicHelper {

    private SpotifyMusicHelper(){}

    public static SpotifyMusic create() {
        return new SpotifyMusic("Ela Partiu", 255000L);
    }
}
