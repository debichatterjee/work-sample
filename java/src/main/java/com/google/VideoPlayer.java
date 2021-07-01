package com.google;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class VideoPlayer {

  private final VideoLibrary videoLibrary;
  private String currentlyPlaying;
  private Boolean paused = false;
  private List<VideoPlaylist> playLists = new ArrayList<VideoPlaylist>();


  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();
  }

  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  public void showAllVideos() {
    ArrayList<String> videoDetails = new ArrayList<String>();
    System.out.println("Here's a list of all available videos:");
    for (int i = 0; i < videoLibrary.getVideos().size(); i++) {
      videoDetails.add(videoLibrary.getVideos().get(i).getVideoDetails());
    }
    Collections.sort(videoDetails);
    for (int i = 0; i < videoDetails.size(); i++) {
      System.out.println(videoDetails.get(i));
    }
  }

  public void playVideo(String videoId) {
    try {
      String nextToPlay = new String(videoLibrary.getVideo(videoId).getTitle());
      if (currentlyPlaying != null) {
        System.out.println("Stopping video: " + videoLibrary.getVideo(currentlyPlaying).getTitle());
      }
      System.out.println("Playing video: " + nextToPlay);
      currentlyPlaying = videoId;
      paused = false;
    } catch (NullPointerException e) {
      System.out.print("Cannot play video: Video does not exist." + "\n");
    }
  }

  public void stopVideo() {
    if (currentlyPlaying != null) {
      System.out.println("Stopping video: " + videoLibrary.getVideo(currentlyPlaying).getTitle());
      currentlyPlaying = null;
    } else {
      System.out.print("Cannot stop video: No video is currently playing" + "\n");
    }
  }

  public void playRandomVideo() {
    Random rn = new Random();
    int answer = rn.nextInt(videoLibrary.getVideos().size() - 1);
    playVideo(videoLibrary.getVideos().get(answer).getVideoId());
    paused = false;
  }

  public void pauseVideo() {
    if (currentlyPlaying == null) {
      System.out.println("Cannot pause video: No video is currently playing");
    } else if (!paused) {
      paused = true;
      System.out.println("Pausing video: " + videoLibrary.getVideo(currentlyPlaying).getTitle());
    } else if (paused && currentlyPlaying != null) {
      System.out.println("Video already paused: " + videoLibrary.getVideo(currentlyPlaying).getTitle());
    }
  }

  public void continueVideo() {
    if (paused) {
      paused = false;
      System.out.println("Continuing video: " + videoLibrary.getVideo(currentlyPlaying).getTitle());
    } else if (currentlyPlaying == null) {
      System.out.println("Cannot continue video: No video is currently playing");
    } else if (!paused && currentlyPlaying != null) {
      System.out.println("Cannot continue video: Video is not paused");
    }
  }

  public void showPlaying() {
    if (currentlyPlaying != null && !paused) {
      System.out.println("Currently playing: " + videoLibrary.getVideo(currentlyPlaying).getVideoDetails());
    } else if (currentlyPlaying != null && paused) {
      System.out.println("Currently playing: " + videoLibrary.getVideo(currentlyPlaying).getVideoDetails() + " - PAUSED");
    } else {
      System.out.println("No video is currently playing");
    }
  }

  public void createPlaylist(String playlistName) {

        if(playlistExists(playlistName)) {
          System.out.println("Cannot create playlist: A playlist with the same name already exists");
        }
        else{
          VideoPlaylist newPlaylist = new VideoPlaylist(playlistName);
          playLists.add(newPlaylist);
          System.out.println("Successfully created new playlist: " + playlistName);
        }
  }

  public void addVideoToPlaylist(String playlistName, String videoId) {
    Boolean flag = false;
    try {
      for (int i = 0; i < playLists.size(); i++) {
        if (playLists.get(i).getName().toUpperCase().equals(playlistName.toUpperCase())) {
          flag = true;
          if (!playLists.get(i).isInPlaylist(videoLibrary.getVideo(videoId))) {
            playLists.get(i).addToPlaylist(videoLibrary.getVideo(videoId));
            System.out.println("Added video to " + playlistName + ": " + videoLibrary.getVideo(videoId).getTitle());
          } else {
            System.out.println("Cannot add video to " + playlistName + ": Video already added");
          }
        }
      }
      if (!playlistExists(playlistName)) {
        System.out.println("Cannot add video to " + playlistName + ": Playlist does not exist");
      }
    } catch (NullPointerException e) {
      System.out.println("Cannot add video to " + playlistName + ": Video does not exist");
    }
  }

  public void showAllPlaylists() {
    List temp = new ArrayList();
    if (playLists.size() == 0) {
      System.out.println("No playlists exist yet");
    } else {
      System.out.println("Showing all playlists:");
      for (int i = 0; i < playLists.size(); i++) {
        temp.add(playLists.get(i).getName());
      }
      Collections.sort(temp);
      for (int i = 0; i < temp.size(); i++) {
        System.out.println(temp.get(i));
      }
    }
  }

  public void showPlaylist(String playlistName) {
    if(playlistExists(playlistName)){
        getPlaylist(playlistName).getVideos(playlistName);
    }
    else {
      System.out.println("Cannot show playlist " + playlistName + ": Playlist does not exist");
    }
  }

  public void removeFromPlaylist(String playlistName, String videoId) {
    if (playlistExists(playlistName)) {
      if (!videoLibrary.videoExists(videoId)) {
        System.out.println("Cannot remove video from " + playlistName + ": Video does not exist");
      } else {
        getPlaylist(playlistName).removeVideos(playlistName, videoLibrary.getVideo(videoId));
      }
    }
    else{
      System.out.println("Cannot remove video from " + playlistName + ": Playlist does not exist");
    }
  }


  public void clearPlaylist(String playlistName) {
    if(playlistExists(playlistName)){
        getPlaylist(playlistName).clearPlaylist();
        System.out.println("Successfully removed all videos from " + playlistName);
    }
    else {
      System.out.println("Cannot clear playlist " + playlistName + ": Playlist does not exist");
    }
  }

  public void deletePlaylist(String playlistName) {
    if(playlistExists(playlistName)){
        playLists.remove(getPlaylist(playlistName));
        System.out.println("Deleted playlist: " + playlistName);

    }
    else {
      System.out.println("Cannot delete playlist " + playlistName + ": Playlist does not exist");
    }
  }


  public void searchVideos(String searchTerm) {
    Boolean flag = false;
    List<Video> searched = new ArrayList<Video>();
    for (int i = 0; i < videoLibrary.getVideos().size(); i++) {
      if (videoLibrary.getVideos().get(i).isInTags(searchTerm.toLowerCase())) {
        flag = true;
        searched.add(videoLibrary.getVideos().get(i));
      }
    }
    if (!flag){
      System.out.println("No search results for " + searchTerm);
    }
    else {
      searched = sort(searched);
      System.out.println("Here are the results for " + searchTerm + ":");
      for (int i = 0; i < searched.size(); i++) {
        System.out.println(i + 1 + ") " + searched.get(i).getVideoDetails());
      }


      Scanner sc = new Scanner(System.in);
      System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
      System.out.println("If your answer is not a valid number, we will assume it's a no.");

      try {
        int a = sc.nextInt();
        playVideo(searched.get(a - 1).getVideoId());
      } catch (Exception e) {
      }
    }
  }



  public void searchVideosWithTag(String videoTag) {
    System.out.println("searchVideosWithTag needs implementation");
  }

  public void flagVideo(String videoId) {
    System.out.println("flagVideo needs implementation");
  }

  public void flagVideo(String videoId, String reason) {
    System.out.println("flagVideo needs implementation");
  }

  public void allowVideo(String videoId) {
    System.out.println("allowVideo needs implementation");
  }


  public Boolean playlistExists(String playlistName){
    Boolean playlistFound = false;
    for (int i = 0; i < playLists.size(); i++) {
      if (playLists.get(i).getName().toUpperCase().equals(playlistName.toUpperCase())) {
        playlistFound = true;
      }
    }
    return playlistFound;
  }

  public VideoPlaylist getPlaylist(String playlistName){
    for (int i = 0; i < playLists.size(); i++) {
      if (playLists.get(i).getName().toUpperCase().equals(playlistName.toUpperCase())) {
        return playLists.get(i);
      }
    }
    return null;
  }


  public List<Video> sort(List<Video> listToSort){
    Video[] arrayToSort = new Video[listToSort.size()];
    listToSort.toArray(arrayToSort);
    int n = arrayToSort.length;
    for (int i = 0; i < n-1; i++)
      for (int j = 0; j < n-i-1; j++) {
        if (arrayToSort[j].getTitle().compareTo(arrayToSort[j+1].getTitle()) > 0) {
          // swap arr[j+1] and arr[j]
          Video temp = arrayToSort[j];
          arrayToSort[j] = arrayToSort[j + 1];
          arrayToSort[j + 1] = temp;
        }
      }

    return Arrays.asList(arrayToSort);
  }
}