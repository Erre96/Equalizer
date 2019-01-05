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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    android.support.v7.widget.Toolbar toolbar;
    final int MAX_SLIDERS = 5;
    final SeekBar[] eqSlider = new SeekBar[MAX_SLIDERS];
    // create the equalizer with default priority of 0 & attach to our media player
    final static public Equalizer mEqualizer = new Equalizer(99,MusicCollectionActivity.mediaPlayer.getAudioSessionId());
    public static short[] bandLevels = new short[5];
    static short curPreset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_effects);
        setHelpText("");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(MusicCollectionActivity.curSong);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TrackDetailsActivity.class));
            }
        });


        Resources res = getResources();
        final Spinner presetChoises = (Spinner) findViewById(R.id.spinner);

        mEqualizer.setEnabled((true));

        final List<String> categories = new ArrayList<String>();

        for(short i = 0; i < mEqualizer.getNumberOfPresets(); i++)
        {
            categories.add(mEqualizer.getPresetName(i));

        }
        categories.add("Custom");
            final ArrayAdapter <String> dataAdapter = new ArrayAdapter<String>
                    (this, android.R.layout.simple_spinner_item, categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        presetChoises.setAdapter(dataAdapter);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        presetChoises.setSelection(curPreset);
        presetChoises.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {
                short index = (short) id;

                if(index != dataAdapter.getCount()-1)
                {
                    mEqualizer.usePreset(index);
                }

                curPreset = index;
                //setHelpText(String.valueOf(curPreset));


                int bandLevel;
                for(short curBand = 0; curBand < MAX_SLIDERS; curBand++)
                {
                    final SeekBar sk = eqSlider[curBand];
                    bandLevel = mEqualizer.getBandLevel(curBand);

                    int seekBarValue = convertToSeekBarValue(bandLevel);
                    sk.setProgress(seekBarValue);
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
                    int bandLevel = (short) sk.getProgress();
                    bandLevel = convertToBandLevel(bandLevel);


                    mEqualizer.setBandLevel(curBand,(short) bandLevel);

                    int last = dataAdapter.getCount()-1;
                    curPreset = (short) last;
                    presetChoises.setSelection(curPreset);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                    // TODO Auto-generated method stub
                    sk.setBackgroundColor(Color.parseColor("#ffffe0"));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_Player)
        {
            startActivity(new Intent(MainActivity.this, MusicCollectionActivity.class));
        }

        if(id == R.id.nextButton)
        {
            MusicCollectionActivity.goToNextTrack(getApplicationContext());
        }

        if(id == R.id.play_button)
        {
            if(!MusicCollectionActivity.mediaPlayer.isPlaying())
            {
                MusicCollectionActivity.mediaPlayer.start();
                item.setIcon(R.drawable.ic_pause_action);
            }
            else
            if(MusicCollectionActivity.mediaPlayer.isPlaying())
            {
                MusicCollectionActivity.mediaPlayer.pause();
                item.setIcon(R.drawable.play_action);
            }
        }


        return super.onOptionsItemSelected(item);
    }

    void setHelpText(String text)
    {
        final TextView printHere = findViewById(R.id.printHere);
        printHere.setText(text);
    }
    void showBandStrength(int bandLevel, int seekBarValue)
    {
        final TextView printHere = findViewById(R.id.printHere);
        String a = String.valueOf("Band Level : "+bandLevel+"    "+"\n"+"SeekBar Value : "+seekBarValue);
        a = "";
        printHere.setText(a);
    }

    int convertToSeekBarValue(int bandLevel)
    {
        int ret = 50 + (bandLevel / 30);
        //showBandStrength(bandLevel,ret);
        return ret;
    }

    int convertToBandLevel(int seekBarValue)
    {
        int ret = -1500 + (seekBarValue * 30);
        //showBandStrength(ret,seekBarValue);
        return ret;
    }

    void updateSeekbars()
    {
        int bandLevel = 0;
        for(short curBand = 0; curBand < MAX_SLIDERS; curBand++)
        {
            final SeekBar sk = eqSlider[curBand];
            bandLevel = mEqualizer.getBandLevel(curBand);

            int seekBarValue = convertToSeekBarValue(bandLevel);
            sk.setProgress(seekBarValue);
        }
    }

    public static void saveBandLevels()
    {
        bandLevels[0] = mEqualizer.getBandLevel((short) 0);
        bandLevels[1] = mEqualizer.getBandLevel((short) 1);
        bandLevels[2] = mEqualizer.getBandLevel((short) 2);
        bandLevels[3] = mEqualizer.getBandLevel((short) 3);
        bandLevels[4] = mEqualizer.getBandLevel((short) 4);
    }

    public static void setBandLevels()
    {
        mEqualizer.setBandLevel((short) 0, bandLevels[0]);
        mEqualizer.setBandLevel((short) 1, bandLevels[1]);
        mEqualizer.setBandLevel((short) 2, bandLevels[2]);
        mEqualizer.setBandLevel((short) 3, bandLevels[3]);
        mEqualizer.setBandLevel((short) 4, bandLevels[4]);
    }
}
