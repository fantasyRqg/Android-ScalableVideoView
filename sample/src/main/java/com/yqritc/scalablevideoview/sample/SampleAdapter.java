package com.yqritc.scalablevideoview.sample;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.RawRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yqritc.scalablevideoview.ScalableType;
import com.yqritc.scalablevideoview.ScalableVideoView;

import java.io.IOException;

/**
 * Created by yqritc on 2015/06/14.
 */
public class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.ViewHolder> {

    @RawRes
    private int mVideoResId;
    private LayoutInflater mLayoutInflater;

    public SampleAdapter(Context context) {
        super();
        mVideoResId = R.raw.landscape_sample;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.layout_sample_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();

        ScalableType scalableType = ScalableType.values()[position];
        holder.mTextView.setText(context.getString(R.string.sample_scale_title, position,
                scalableType.toString()));
        holder.setScalableType(scalableType);
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        setVideo(holder.mVideoView);
        holder.mVideoView.setScalableType(holder.mScalableType);
    }


    private static final String TAG = "SampleAdapter";

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        long start = System.nanoTime();
        holder.mVideoView.postRelease();
        Log.d(TAG, "onViewDetachedFromWindow: " + (System.nanoTime() - start));
    }

    private void setVideo(final ScalableVideoView videoView) {
        try {
            videoView.setRawData(mVideoResId);
            videoView.setVolume(0, 0);
            videoView.setLooping(true);
            videoView.prepareAsync(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    videoView.start();
                }
            });
        } catch (IOException ioe) {
            //ignore
        }
    }

    @Override
    public int getItemCount() {
        return ScalableType.values().length;
    }

    public void setVideoResId(@RawRes int id) {
        mVideoResId = id;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView;
        ScalableVideoView mVideoView;
        ScalableType mScalableType;

        public ViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.video_text);
            mVideoView = (ScalableVideoView) view.findViewById(R.id.video_view);
            mVideoView.setCornerRadius(30);
        }

        public void setScalableType(ScalableType type) {
            mScalableType = type;
        }
    }
}
