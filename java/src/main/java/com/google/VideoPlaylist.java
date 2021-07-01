package com.google;

import java.util.Collections;
import java.util.*;

/** A class used to represent a Playlist */
class VideoPlaylist {
    private  String name;
    private  List<Video> videos = new ArrayList<Video>();

    VideoPlaylist(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public Boolean isInPlaylist(Video check){
        return videos.contains(check);
    }

    public void addToPlaylist(Video newVideo){
        videos.add(newVideo);
    }

    public void getVideos(String playlistName){
        System.out.println("Showing playlist: "+ playlistName);
        if (videos.size()==0){
            System.out.println("No videos here yet");
        }
        else {
            for (int i = 0; i < videos.size(); i++) {
                System.out.println(videos.get(i).getVideoDetails());
            }
        }
    }

    public void removeVideos(String playlistName, Video video) {
        if (isInPlaylist(video)){
        videos.remove(video);
        System.out.println("Removed video from " + playlistName + ": " + video.getTitle());
        }
        else {
            System.out.println("Cannot remove video from " + playlistName + ": Video is not in playlist");
        }
    }

    public void clearPlaylist(){
        videos.clear();
    }

}
