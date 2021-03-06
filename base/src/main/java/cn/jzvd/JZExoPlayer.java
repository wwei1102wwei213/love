package cn.jzvd;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.Surface;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoListener;
import com.yyspbfq.filmplay.BaseApplication;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.utils.BLog;


/**
 * Created by MinhDV on 5/3/18.
 */

public class JZExoPlayer extends JZMediaInterface implements Player.EventListener, VideoListener {
    private SimpleExoPlayer simpleExoPlayer;
    private Handler mainHandler;
    private Runnable callback;
    private String TAG = "JZExoPlayer";
    private MediaSource videoSource;
    private long previousSeek = 0;
    private BandwidthMeter bandwidthMeter;

    @Override
    public void start() {
        simpleExoPlayer.setPlayWhenReady(true);
    }

    @Override
    public void prepare() {
        mainHandler = new Handler(Looper.getMainLooper());
        Context context = JzvdMgr.getCurrentJzvd().getContext();

        bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        LoadControl loadControl = new DefaultLoadControl(new DefaultAllocator(true, C.DEFAULT_BUFFER_SEGMENT_SIZE),
                360000, 600000, 1000, 5000,
                C.LENGTH_UNSET,
                false);
        RenderersFactory renderersFactory = new DefaultRenderersFactory(context);
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(JzvdMgr.getCurrentJzvd().getContext(), renderersFactory, trackSelector, loadControl);
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, context.getResources().getString(R.string.app_name)));
        String currUrl = jzDataSource.getCurrentUrl().toString();
        if (currUrl.contains(".m3u8")) {
            videoSource = new HlsMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(currUrl), mainHandler, null);
        } else {
            videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(currUrl));
        }
        simpleExoPlayer.addVideoListener(this);
        simpleExoPlayer.addListener(this);
        simpleExoPlayer.prepare(videoSource);
        simpleExoPlayer.setPlayWhenReady(true);
        callback = new onBufferingUpdate();
        mainHandler.removeCallbacks(mSpeedRunnable);
        mainHandler.post(mSpeedRunnable);
    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        JZMediaManager.instance().currentVideoWidth = width;
        JZMediaManager.instance().currentVideoHeight = height;
        JZMediaManager.instance().mainThreadHandler.post(() -> {
            if (JzvdMgr.getCurrentJzvd() != null) {
                JzvdMgr.getCurrentJzvd().onVideoSizeChanged();
            }
        });
    }

    @Override
    public void onRenderedFirstFrame() {

    }

    private class onBufferingUpdate implements Runnable {
        @Override
        public void run() {
            final int percent = simpleExoPlayer.getBufferedPercentage();
            JZMediaManager.instance().mainThreadHandler.post(() -> {
                if (JzvdMgr.getCurrentJzvd() != null) {
                    JzvdMgr.getCurrentJzvd().setBufferProgress(percent);
                }
            });
            if (percent < 100) {
                mainHandler.postDelayed(callback, 300);
            } else {
                mainHandler.removeCallbacks(callback);
            }
        }
    }

    @Override
    public void pause() {
        if (simpleExoPlayer!=null)
        simpleExoPlayer.setPlayWhenReady(false);
    }

    @Override
    public boolean isPlaying() {
        return simpleExoPlayer.getPlayWhenReady();
    }

    @Override
    public void seekTo(long time) {
        try {
            if (simpleExoPlayer==null) return;
            if (time != previousSeek) {
                simpleExoPlayer.seekTo(time);
                previousSeek = time;
                JzvdMgr.getCurrentJzvd().seekToInAdvance = time;
            }
        } catch (Exception e){

        }

    }

    @Override
    public void release() {
        try {
            if (simpleExoPlayer != null) {
                simpleExoPlayer.release();
            }
            if (mainHandler != null) {
                mainHandler.removeCallbacks(callback);
                oldUidBytes = 0;
                mainHandler.removeCallbacks(mSpeedRunnable);
            }
        } catch (Exception e){

        }
    }

    @Override
    public long getCurrentPosition() {
        if (simpleExoPlayer != null)
            return simpleExoPlayer.getCurrentPosition();
        else return 0;
    }

    @Override
    public long getDuration() {
        if (simpleExoPlayer != null)
            return simpleExoPlayer.getDuration();
        else return 0;
    }

    @Override
    public void setSurface(Surface surface) {
        try {
            simpleExoPlayer.setVideoSurface(surface);
        } catch (Exception e){

        }
    }

    @Override
    public void setVolume(float leftVolume, float rightVolume) {
        try {
            if (simpleExoPlayer!=null) {
                simpleExoPlayer.setVolume(leftVolume);
                simpleExoPlayer.setVolume(rightVolume);
            }
        } catch (Exception e){

        }
    }

    @Override
    public void setSpeed(float speed) {
        try {
            PlaybackParameters playbackParameters = new PlaybackParameters(speed, 1.0F);
            simpleExoPlayer.setPlaybackParameters(playbackParameters);
        } catch (Exception e){

        }
    }

    @Override
    public void onTimelineChanged(final Timeline timeline, Object manifest, final int reason) {}

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {}

    @Override
    public void onPlayerStateChanged(final boolean playWhenReady, final int playbackState) {
        JZMediaManager.instance().mainThreadHandler.post(() -> {
            if (JzvdMgr.getCurrentJzvd() != null) {
                switch (playbackState) {
                    case Player.STATE_IDLE: {

                    }
                    break;
                    case Player.STATE_BUFFERING: {
                        mainHandler.post(callback);
                        mainHandler.removeCallbacks(mSpeedRunnable);
                        mainHandler.post(mSpeedRunnable);
                        JzvdMgr.getCurrentJzvd().onPreparing();
                    }
                    break;
                    case Player.STATE_READY: {
                        if (playWhenReady) {
                            JzvdMgr.getCurrentJzvd().onPrepared();
                            oldUidBytes = 0;
                            mainHandler.removeCallbacks(mSpeedRunnable);
                        } else {
                        }
                    }
                    break;
                    case Player.STATE_ENDED: {
                        JzvdMgr.getCurrentJzvd().onAutoCompletion();
                    }
                    break;
                }
            }
        });
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        JZMediaManager.instance().mainThreadHandler.post(() -> {
            if (JzvdMgr.getCurrentJzvd() != null) {
                JzvdMgr.getCurrentJzvd().onError(1000, 1000);
            }
        });
    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {
        oldUidBytes = 0;
        mainHandler.removeCallbacks(mSpeedRunnable);
        JZMediaManager.instance().mainThreadHandler.post(() -> {
            if (JzvdMgr.getCurrentJzvd() != null) {
                JzvdMgr.getCurrentJzvd().onSeekComplete();
            }
        });
    }

    private long oldSpeed = 0;
    private long oldUidBytes = 0;
    private Runnable mSpeedRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                long all = getUidRxBytes();
                if (JzvdMgr.getCurrentJzvd() != null) {
                    String temp = "0";
                    if (oldUidBytes==0) {
                        if (oldSpeed!=0) {
                            temp = oldSpeed+"";
                        }
                    } else {
                        oldSpeed = all - oldUidBytes;
                        temp = oldSpeed+"";
                    }
                    oldUidBytes = all;
                    JzvdMgr.getCurrentJzvd().showSpeedView(temp);
                }
                mainHandler.postDelayed(mSpeedRunnable, 1000);
            } catch (Exception e){
                BLog.e(e);
            }
        }
    };

    public long getUidRxBytes() { //获取总的接受字节数，包含Mobile和WiFi等
        PackageManager pm = BaseApplication.getInstance().getPackageManager();
        long result = 0;
        try {
            ApplicationInfo ai = pm.getApplicationInfo("com.yyspbfq.filmplay", 0);
            result = TrafficStats.getUidRxBytes(ai.uid) == TrafficStats.UNSUPPORTED ? 0 : (TrafficStats.getTotalRxBytes() / 1024);;
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        return result;
    }


}
