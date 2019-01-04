package com.example.erhan.equalizer;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.MainThread;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

/**
 * Created by Erhan on 2018-12-07.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<String> mDataset;
    private Context mContext;
    private List<Uri> uri;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<String> myDataset, Context myContext, List <Uri> myUri)
    {
        mDataset = myDataset;
        mContext = myContext;
        uri = myUri;
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtHeader;
        //public TextView txtFooter;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            txtHeader = v.findViewById(R.id.firstLine);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.track_info, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.txtHeader.setText(mDataset.get(position));

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    short[] bandLevels;
                    bandLevels = getBandLevels();

                    MusicCollectionActivity.mediaPlayer.reset();
                    MusicCollectionActivity.mediaPlayer.setDataSource(mContext,uri.get(position));
                    MusicCollectionActivity.mediaPlayer.prepare();
                    MusicCollectionActivity.mediaPlayer.start();
                    MusicCollectionActivity.curSong = holder.txtHeader.getText().toString();
                    MusicCollectionActivity.toolbar.setTitle(MusicCollectionActivity.curSong);


                    MainActivity.mEqualizer.setBandLevel((short) 0,bandLevels[0]);
                    MainActivity.mEqualizer.setBandLevel((short) 1,bandLevels[1]);
                    MainActivity.mEqualizer.setBandLevel((short) 2,bandLevels[2]);
                    MainActivity.mEqualizer.setBandLevel((short) 3,bandLevels[3]);
                    MainActivity.mEqualizer.setBandLevel((short) 4,bandLevels[4]);
                }

                catch (IOException e)
                {

                }
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    short[] getBandLevels()
    {
        short[] bandLevel = new short[5];
        bandLevel[0] = MainActivity.mEqualizer.getBandLevel((short) 0);
        bandLevel[1] = MainActivity.mEqualizer.getBandLevel((short) 1);
        bandLevel[2] = MainActivity.mEqualizer.getBandLevel((short) 2);
        bandLevel[3] = MainActivity.mEqualizer.getBandLevel((short) 3);
        bandLevel[4] = MainActivity.mEqualizer.getBandLevel((short) 4);
        return bandLevel;
    }
}