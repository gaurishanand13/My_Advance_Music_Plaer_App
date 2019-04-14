package com.example.musicplayerapp;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    CircularImageView StopImageView;
    CircularImageView Play_PauseImageView;
    MediaPlayer mediaPlayer;
    AudioManager audioManager;
    SeekBar ProgressSeekBar;
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
        ProgressSeekBar.setProgress(0);
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Play_PauseImageView = (CircularImageView)findViewById(R.id.Play_Pause);
        StopImageView = (CircularImageView)findViewById(R.id.StopImageView);

        //Setting up of Media Player so that we can use it. Note this will play the song in the background so it won't hamper our code running.
        mediaPlayer = MediaPlayer.create(this,R.raw.ishvalalove);

        //Note just like videoView can be controlled through media controller. We don't have any music controller but we can use seekbar
        //to allow users to control music.

        //first we will use audio manager to find the current and max volume of the device.
        audioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        //Now we will access volume seek bar and set the value of max value and current value.
        //Now though we have side buttons to adjust the value of the volume seekBar. but still we are providing an extra feature.
        SeekBar VolumeSeekBar = (SeekBar)findViewById(R.id.VolumeSeekBar);
        VolumeSeekBar.setMax(maxVolume);
        VolumeSeekBar.setProgress(currentVolume);
        //Now we will perform functions if there is change in the seekBar value.

        VolumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //On full length of seekBar, it takes the value from 0 to 100. Therefore Note if we would not set up the max Value it maye happen that it may take the value volume greater
                // than than the max volume which will be an error.
                //Since now on changing the seekBar value, user is expecting to change the volume i.e make the volume equal to progress. Therefore we should make our audioManager do that.
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Now we will also show the progress of the song and allow the user to change the progress of the song through a seek bar.
        //Now we will set the max value of the this seek bar equal to the duration of the song in seconds
        ProgressSeekBar = (SeekBar) findViewById(R.id.ProgressSeekBar);
        ProgressSeekBar.setMax(mediaPlayer.getDuration()); // -> it will give the durations in seconds only by default.
        //Now we will set up the onSeekBarchange listner which will be called if the user tries to make some changes in this seekBar i.e tries to change the progress of the song.
        ProgressSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mediaPlayer.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //Now also as the song progress we should use a timer which will be called after certain regular interval of seconds and update the progress of the song on the
        //progress seekBar
        int Total_Time_For_Which_Timer_Will_Run = mediaPlayer.getDuration();
        int Time_After_Which_It_Will_repeat_its_code = 2000; // -> this time is in seconds.
        /*new CountDownTimer(Total_Time_For_Which_Timer_Will_Run,Time_After_Which_It_Will_repeat_its_code)
        {
            @Override
            public void onTick(long millisUntilFinished) {
                ProgressSeekBar.setProgress((int)millisUntilFinished);
            }

            @Override
            public void onFinish() {

            }
        };*/
        //Note -> Though the above method is a good method to use a countdowner and we should try to use that only. But here we should try to use here another timer
        //becase we are running here timer for fixed seconds like for the duration of the song which will be wrong as if the user pauses the video for the long time
        // and it increases the duration of the song. After that progress won't be shown on our ProgressSeekBar. Therefore Use AnotherSeekBar.
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ProgressSeekBar.setProgress(mediaPlayer.getCurrentPosition());
            }
        },0,5000);
    }
}
