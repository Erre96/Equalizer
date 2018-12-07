package com.example.erhan.equalizer;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_effects);

        final int MAX_SLIDERS = 5;
        final SeekBar[] eqSlider;
        eqSlider = new SeekBar[MAX_SLIDERS];
        Resources res = getResources();
        final Spinner presetChoises = (Spinner) findViewById(R.id.spinner);

        Log.d("Hej","hej");
        // set the device's volume control to control the audio stream we'll be playing
        //SetvolumeControlStream(AudioManager.STREAM_MUSIC);


        final AudioManager amanager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        amanager.generateAudioSessionId();
        // create the MediaPlayer
        MediaPlayer mMediaPlayer = MediaPlayer.create(this,R.raw.test_song);
        mMediaPlayer.start();
        // create the equalizer with default priority of 0 & attach to our media player
         final Equalizer mEqualizer = new Equalizer(99,mMediaPlayer.getAudioSessionId());
         mEqualizer.setEnabled((true));

        final List<String> categories = new ArrayList<String>();

        for(short i = 0; i < mEqualizer.getNumberOfPresets(); i++)
        {
            categories.add(mEqualizer.getPresetName(i));

        }
            ArrayAdapter <String> dataAdapter = new ArrayAdapter<String>
                    (this, android.R.layout.simple_spinner_item, categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        presetChoises.setAdapter(dataAdapter);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        presetChoises.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {
                short index = (short) id;
                mEqualizer.usePreset(index);

                for(short curBand = 0; curBand < MAX_SLIDERS; curBand++)
                {
                    final SeekBar sk = eqSlider[curBand];
                    sk.setProgress(mEqualizer.getBandLevel(curBand));
                    UpdateSlider(sk, curBand, mEqualizer);
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });


        short b = 1;
         String a = String.valueOf(mEqualizer.getPresetName(b));
        //printHere.setText(a);


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
                    UpdateSlider(sk,curBand, mEqualizer);
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

    void UpdateSlider(SeekBar sk, short curBand, Equalizer mEqualizer)
    {
        final TextView printHere = (TextView) findViewById(R.id.printHere);

        sk.setBackgroundColor(Color.parseColor("#825840"));
        short level = (short) sk.getProgress();
        level *= 15;

        mEqualizer.setBandLevel(curBand,level);
        String a = String.valueOf(level);
        printHere.setText(a);
    }
}
