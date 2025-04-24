package com.example.itubeapp;

import java.util.ArrayList;
import java.util.List;

public class PlaylistStorage {
    private static final List<String> playlist = new ArrayList<>();

    public static void addUrl(String url) {
        playlist.add(url);
    }

    public static List<String> getPlaylist() {
        return playlist;
    }
}

