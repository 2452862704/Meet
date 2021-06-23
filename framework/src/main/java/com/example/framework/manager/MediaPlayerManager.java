package com.example.framework.manager;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.example.framework.utils.LogUtils;

import java.io.IOException;

public class MediaPlayerManager {

    //播放
    public static final int MEDIA_STATUS_PLAY=0;
    //暂停
    public static final int MEDIA_STATUS_PAUSE=1;
    //停止
    public static final int MEDIA_STATUS_STOP=2;
    //当前状态
    public int MEDIA_STATUS=MEDIA_STATUS_STOP;

    private MediaPlayer mediaPlayer;

    private static final int H_PROGRESS=1000;

    private OnMusicProgressListener musicProgressListener;
    /**
     * 计算歌曲的进度
     * 1.开始播放的时候就开启循环计算时长
     * 2.将进度计算结果对外抛出
     */
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case H_PROGRESS:
                    if (musicProgressListener!=null){
                        //拿到当前时长
                        int currentPosition=getCurrentPosition();
                        int pos=(int) (((float)currentPosition)/((float)getDuration())*100);
                        musicProgressListener.OnProgress(currentPosition,pos);
                        handler.sendEmptyMessageDelayed(H_PROGRESS,1000);
                    }
                    break;
            }
            return false;
        }
    });

    public MediaPlayerManager(){
        mediaPlayer=new MediaPlayer();
    }

    /**
     * 判断是否在播放
     * @return
     */
    public boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }
    /**
     * 开始播放
     * @param path
     */
    public void startPlay(AssetFileDescriptor path){
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path.getFileDescriptor(),path.getStartOffset(),path.getLength());
            mediaPlayer.prepare();
            mediaPlayer.start();
            handler.sendEmptyMessage(H_PROGRESS);
            MEDIA_STATUS=MEDIA_STATUS_PLAY;
        } catch (IOException e) {
            LogUtils.e(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 暂停播放
     */
    public void pausePlay(){
        if (isPlaying()){
            mediaPlayer.pause();
            MEDIA_STATUS=MEDIA_STATUS_PAUSE;
            handler.removeMessages(H_PROGRESS);
        }
    }

    /**
     * 继续播放
     */
    public void continuePlay(){
        mediaPlayer.start();
        MEDIA_STATUS=MEDIA_STATUS_PLAY;
        handler.sendEmptyMessage(H_PROGRESS);
    }

    /**
     * 停止播放
     */
    public void stopPlay(){
        mediaPlayer.stop();
        MEDIA_STATUS=MEDIA_STATUS_STOP;
        handler.removeMessages(H_PROGRESS);
    }

    /**
     * 获取当前位置
     * @return
     */
    public int getCurrentPosition(){
        return mediaPlayer.getCurrentPosition();
    }

    /**
     * 获取总时长
     * @return
     */
    public int getDuration(){
        return mediaPlayer.getDuration();
    }

    /**
     * 是否循环
     * @param isLooping
     */
    public void setLooping(boolean isLooping){
        mediaPlayer.setLooping(isLooping);
    }

    /**
     * 播放结束
     * @param listener
     */
    public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener){
        mediaPlayer.setOnCompletionListener(listener);
    }

    /**
     * 播放错误
     * @param listener
     */
    public void setOnErrorListener(MediaPlayer.OnErrorListener listener){
        mediaPlayer.setOnErrorListener(listener);
    }

    /**
     * 跳转位置
     * @param ms
     */
    public void seekTo(int ms){
        mediaPlayer.seekTo(ms);
    }
    /**
     * 播放进度
     * @param
     */
    public void setOnPreparedListener(OnMusicProgressListener listener){
        musicProgressListener=listener;
    }
    public interface OnMusicProgressListener{
        void OnProgress(int progress,int pos);
    }
}
