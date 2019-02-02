package cn.jzvd;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Surface;

import com.orhanobut.logger.Logger;
import com.yyspbfq.filmplay.utils.BLog;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by Nathen on 2017/11/8.
 * 实现系统的播放引擎
 */
public class JZMediaSystem extends JZMediaInterface implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, MediaPlayer.OnVideoSizeChangedListener {

    public MediaPlayer mediaPlayer;

    @Override
    public void start() {
        mediaPlayer.start();
    }

    @Override
    public void prepare() {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setLooping(jzDataSource.looping);
            mediaPlayer.setOnPreparedListener(JZMediaSystem.this);
            mediaPlayer.setOnCompletionListener(JZMediaSystem.this);
            mediaPlayer.setOnBufferingUpdateListener(JZMediaSystem.this);
            mediaPlayer.setScreenOnWhilePlaying(true);
            mediaPlayer.setOnSeekCompleteListener(JZMediaSystem.this);
            mediaPlayer.setOnErrorListener(JZMediaSystem.this);
            mediaPlayer.setOnInfoListener(JZMediaSystem.this);
            mediaPlayer.setOnVideoSizeChangedListener(JZMediaSystem.this);
            Class<MediaPlayer> clazz = MediaPlayer.class;
            Method method = clazz.getDeclaredMethod("setDataSource", String.class, Map.class);
//            if (dataSourceObjects.length > 2) {
            method.invoke(mediaPlayer, jzDataSource.getCurrentUrl().toString(), jzDataSource.headerMap);
//            } else {
//                method.invoke(mediaPlayer, currentDataSource.toString(), null);
//            }
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public void seekTo(long time) {
        try {
            BLog.e("seekTo:"+time);
            mediaPlayer.seekTo((int) time);
        } catch (Exception e) {
            BLog.e(e);
            e.printStackTrace();
        }
    }

    @Override
    public void release() {
        if (mediaPlayer != null)
            mediaPlayer.release();
    }

    @Override
    public long getCurrentPosition() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }

    @Override
    public long getDuration() {
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        } else {
            return 0;
        }
    }

    @Override
    public void setSurface(Surface surface) {
        mediaPlayer.setSurface(surface);
    }

    @Override
    public void setVolume(float leftVolume, float rightVolume) {
        mediaPlayer.setVolume(leftVolume, rightVolume);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void setSpeed(float speed) {
        Logger.e("MediaPlayer setSpeed");
        PlaybackParams pp = mediaPlayer.getPlaybackParams();
        pp.setSpeed(speed);
        mediaPlayer.setPlaybackParams(pp);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Logger.e("MediaPlayer onPrepared");
        mediaPlayer.start();
        if (jzDataSource.getCurrentUrl().toString().toLowerCase().contains("mp3") ||
                jzDataSource.getCurrentUrl().toString().toLowerCase().contains("wav")) {
            JZMediaManager.instance().mainThreadHandler.post(() -> {
                if (JzvdMgr.getCurrentJzvd() != null) {
                    JzvdMgr.getCurrentJzvd().onPrepared();
                }
            });
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Logger.e("MediaPlayer onCompletion");
        JZMediaManager.instance().mainThreadHandler.post(() -> {
            if (JzvdMgr.getCurrentJzvd() != null) {
                JzvdMgr.getCurrentJzvd().onAutoCompletion();
            }
        });
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, final int percent) {
//        Logger.e("MediaPlayer onBufferingUpdate:"+percent);
        JZMediaManager.instance().mainThreadHandler.post(() -> {
            if (JzvdMgr.getCurrentJzvd() != null) {
                JzvdMgr.getCurrentJzvd().setBufferProgress(percent);
            }
        });
    }

    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {
        Logger.e("MediaPlayer onSeekComplete");
        JZMediaManager.instance().mainThreadHandler.post(() -> {
            if (JzvdMgr.getCurrentJzvd() != null) {
                JzvdMgr.getCurrentJzvd().onSeekComplete();
            }
        });
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, final int what, final int extra) {
        Logger.e("MediaPlayer onError");
        JZMediaManager.instance().mainThreadHandler.post(() -> {
            if (JzvdMgr.getCurrentJzvd() != null) {
                JzvdMgr.getCurrentJzvd().onError(what, extra);
            }
        });
        return true;
    }

    @Override
    public boolean onInfo(MediaPlayer mediaPlayer, final int what, final int extra) {
        Logger.e("MediaPlayer onInfo");
        JZMediaManager.instance().mainThreadHandler.post(() -> {
            if (JzvdMgr.getCurrentJzvd() != null) {
                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    if (JzvdMgr.getCurrentJzvd().currentState == Jzvd.CURRENT_STATE_PREPARING
                            || JzvdMgr.getCurrentJzvd().currentState == Jzvd.CURRENT_STATE_PREPARING_CHANGING_URL) {
                        JzvdMgr.getCurrentJzvd().onPrepared();
                    }
                } else {
                    JzvdMgr.getCurrentJzvd().onInfo(what, extra);
                }
            }
        });
        return false;
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int width, int height) {

        JZMediaManager.instance().currentVideoWidth = width;
        JZMediaManager.instance().currentVideoHeight = height;
        JZMediaManager.instance().mainThreadHandler.post(() -> {
            if (JzvdMgr.getCurrentJzvd() != null) {
                JzvdMgr.getCurrentJzvd().onVideoSizeChanged();
            }
        });
    }
}
