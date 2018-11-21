package com.example.erhan.equalizer;

import android.content.res.Resources;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_effects);

        int MAX_SLIDERS = 5;
        final SeekBar[] eqSlider;
        eqSlider = new SeekBar[MAX_SLIDERS];
        Resources res = getResources();

        // set the device's volume control to control the audio stream we'll be playing
        //SetvolumeControlStream(AudioManager.STREAM_MUSIC);

        // create the MediaPlayer
        MediaPlayer mMediaPlayer = MediaPlayer.create(this,R.raw.test_song);
        mMediaPlayer.start();

        // create the equalizer with default priority of 0 & attach to our media player
         final Equalizer mEqualizer = new Equalizer(0,mMediaPlayer.getAudioSessionId());

        for(int i = 0; i < MAX_SLIDERS; i++)
        {
            int nameId = res.getIdentifier("seekBar"+i,"id",getApplicationContext().getPackageName());
            eqSlider[i] = (SeekBar) findViewById (nameId);
            int v = mEqualizer.getNumberOfBands();
            Log.d("Hej",String.valueOf(v));
            // eqSlider[i].setBackgroundColor(Color.parseColor("#FF0000"));
        }

         for(short i = 0; i < MAX_SLIDERS; i++)
        {
            final SeekBar sk = eqSlider[i];
            final short curBand = i;
            sk.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                    sk.setBackgroundColor(Color.parseColor("#825840"));
                    short level = (short) sk.getProgress();
                    Log.d("LEVEL", String.valueOf(level));
                    mEqualizer.setBandLevel(curBand,level);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                    // TODO Auto-generated method stub
                    sk.setBackgroundColor(Color.parseColor("#FF0000"));
                }
            });
        }



        //Visualizer mVisualizer = new Visualizer(mMediaPlayer.getAudioSessionId());

        /*
        // Set up visualizer and equalizer bars
        SetupVisualizerFxAndUI();
        SetupEqualizerFxAndUI();

        // enable the visualizer
        mVisualizer.setEnabled(true);

        // listen for when the music stream ends playing
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()) {
            public void onCompletion(MediaPlayer mediaPlayer) {
                //disable the visualizer as it's no longer needed
                mVisualizer.setEnabled(false);
            }
        });*/
    }
}
