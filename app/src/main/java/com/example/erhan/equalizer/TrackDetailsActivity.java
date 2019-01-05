package com.example.erhan.equalizer;

import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class TrackDetailsActivity extends AppCompatActivity {
ImageView imageView;
SeekBar seekBar;
TextView curTime;
TextView titleText;
TextView artistText;
TextView totalTime;

int songLengthInMilliSeconds;
int songInCurMilliSec;

ImageButton nextButton;
ImageButton previousButton;
ImageButton playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_details);
        imageView = findViewById(R.id.imageView);
        seekBar = findViewById(R.id.track_slider);
        curTime = findViewById(R.id.curTime);
        totalTime = findViewById(R.id.totalTime);
        titleText = findViewById(R.id.titleText);
        artistText = findViewById(R.id.artistText);
        nextButton = findViewById(R.id.skip_next_button);
        previousButton = findViewById(R.id.skip_previous_button);
        playButton = findViewById(R.id.play_button);


        curTime.setText("");
        totalTime.setText("");

        updatePlayPauseImage();
        setSongLengthInMilliSeconds();
        setSliderValue();
        startClock();
        getTrackInfo();
        updateData();

        playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                playOrPauseMediaPlayer();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MusicCollectionActivity.goToNextTrack(getApplicationContext());
                getTrackInfo();
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MusicCollectionActivity.goToPreviousTrack(getApplicationContext());
                getTrackInfo();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                changeTrackPositionSlider();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                // TODO Auto-generated method stub
            }
        });
    }

    void setSongLengthInMilliSeconds()
    {
        songLengthInMilliSeconds = MusicCollectionActivity.mediaPlayer.getDuration();
    }

    void setSliderValue()
    {
        int length = songLengthInMilliSeconds / 1000;
        int curPos = songInCurMilliSec / 1000;
        float f = 100f / length * curPos;
        int seekbarValue = (int) f;
        //totalTime.setText(String.valueOf(curPos + " CUR SEC"+"\n"+String.valueOf(f)));
        seekBar.setProgress(seekbarValue);
    }

    void changeTrackPositionSlider()
    {
        float multiplyWith = seekBar.getProgress()/100f;
        float length = songLengthInMilliSeconds;
        float newPosInMilli = length * multiplyWith;
        MusicCollectionActivity.mediaPlayer.seekTo((int) newPosInMilli);
    }

    private void startClock(){
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                             updateData();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
    }


    String convertSecondsToClock(int totSecs)
    {
        int minutes = totSecs / 60;
        int reduceSecs = minutes * 60;
        int secsOver = totSecs-reduceSecs;
        String minZero = "";
        String secZero = "";

        if(minutes < 10)
        {
            minZero = "0";
        }
        if(secsOver < 10)
        {
            secZero = "0";
        }
        String ret = (minZero+minutes + ":"+secZero+secsOver);
        return ret;
    }
    void playOrPauseMediaPlayer()
    {
        if(!MusicCollectionActivity.mediaPlayer.isPlaying())
        {
            MusicCollectionActivity.mediaPlayer.start();
        }
        else
        if(MusicCollectionActivity.mediaPlayer.isPlaying())
        {
            MusicCollectionActivity.mediaPlayer.pause();
        }
        updatePlayPauseImage();
    }

    void updatePlayPauseImage()
    {
        if(MusicCollectionActivity.mediaPlayer.isPlaying())
        {
            playButton.setImageResource(R.drawable.ic_pause_action);
        }
        else
        if(!MusicCollectionActivity.mediaPlayer.isPlaying())
        {
            playButton.setImageResource(R.drawable.play_action);
        }
    }

    void getTrackInfo(){
        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
        metaRetriever.setDataSource(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Music/"+MusicCollectionActivity.fileList.get(MusicCollectionActivity.songIndex));
        String artist =  metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        String title = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        if(title != null)
        {
            titleText.setText(title);
        }
        else
        {
            titleText.setText(MusicCollectionActivity.fileList.get(MusicCollectionActivity.songIndex));
        }

        if(artist != null)
        {
            artistText.setText(artist);
        }
        else
        {
            artistText.setText("Unknown Artist");
        }
    }

    void updateData()
    {
        songInCurMilliSec = MusicCollectionActivity.mediaPlayer.getCurrentPosition();
        int secs = songInCurMilliSec / 1000;
        int totSecs = songLengthInMilliSeconds / 1000;
        String a = convertSecondsToClock(secs);
        String b = convertSecondsToClock(totSecs);
        curTime.setText(a);
        totalTime.setText(b);
        setSliderValue();
    }
}
