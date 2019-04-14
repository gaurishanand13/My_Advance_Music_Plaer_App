package com.example.musicplayerapp;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.mikhaellopez.circularimageview.CircularImageView;
public class MainActivity extends AppCompatActivity {
    CircularImageView StopImageView;
    CircularImageView Play_PauseImageView;
    MediaPlayer mediaPlayer;
    boolean play=false;
    public void playpause(View view)
    {
        if(!play)
        {
            play=true;
            //this line will resume
            mediaPlayer.start();
            Play_PauseImageView.setImageResource(R.drawable.pause);
        }
        else
        {
            play=false;
            mediaPlayer.pause();
            Play_PauseImageView.setImageResource(R.drawable.play);
        }
    }
    public void stop(View view)
    {
        play=false;
        mediaPlayer.seekTo(0);
        mediaPlayer.pause();
        //we can't use mediaPlaer.stop() here as again we have to set up the mediaPlayer to run it again.
        //stop function will stop the song completely though.
        Play_PauseImageView.setImageResource(R.drawable.play);
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Play_PauseImageView = (CircularImageView)findViewById(R.id.Play_Pause);
        StopImageView = (CircularImageView)findViewById(R.id.StopImageView);
        //Setting up of Media Player so that we can use it. Note this will play the song in the background so it won't hamper our code running.
        mediaPlayer = MediaPlayer.create(this,R.raw.ishvalalove);
    }
}
