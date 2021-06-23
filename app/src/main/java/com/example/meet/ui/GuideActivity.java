package com.example.meet.ui;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.example.framework.base.BaseActivity;
import com.example.framework.base.BasePageAdapter;
import com.example.framework.manager.MediaPlayerManager;
import com.example.framework.utils.AnimUtils;
import com.example.meet.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 引导页
 */
public class GuideActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager mViewPager;
    private ImageView ivMusicSwitch;
    private TextView tvGuideSkip;
    private ImageView ivGuidePoint1;
    private ImageView ivGuidePoint2;
    private ImageView ivGuidePoint3;

    /**
     * 1.ViewPager:适配器|帧动画播放
     * 2.小圆点的逻辑
     * 3.歌曲的播放
     * 4.属性动画旋转
     * 5.跳转
     *
     * @param savedInstanceState
     */
    private View view1;
    private View view2;
    private View view3;
    private List<View> mpageList=new ArrayList<>();
    private BasePageAdapter basePageAdapter;
    private ImageView iv_guide_start;
    private ImageView iv_guide_night;
    private ImageView iv_guide_smile;
    private MediaPlayerManager mediaPlayerManager;
    private ObjectAnimator mAnim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initView();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        ivMusicSwitch = (ImageView) findViewById(R.id.iv_music_switch);
        tvGuideSkip = (TextView) findViewById(R.id.tv_guide_skip);
        ivGuidePoint1 = (ImageView) findViewById(R.id.iv_guide_point_1);
        ivGuidePoint2 = (ImageView) findViewById(R.id.iv_guide_point_2);
        ivGuidePoint3 = (ImageView) findViewById(R.id.iv_guide_point_3);
        ivMusicSwitch.setOnClickListener(this);
        tvGuideSkip.setOnClickListener(this);
        view1=View.inflate(this,R.layout.layout_pager_guide_1,null);
        view2=View.inflate(this,R.layout.layout_pager_guide_2,null);
        view3=View.inflate(this,R.layout.layout_pager_guide_3,null);
        mpageList.add(view1);
        mpageList.add(view2);
        mpageList.add(view3);
        //预加载
        mViewPager.setOffscreenPageLimit(mpageList.size());
        basePageAdapter=new BasePageAdapter(mpageList);
        mViewPager.setAdapter(basePageAdapter);
        //帧动画
        iv_guide_start=view1.findViewById(R.id.iv_guide_star);
        iv_guide_night=view2.findViewById(R.id.iv_guide_night);
        iv_guide_smile=view3.findViewById(R.id.iv_guide_smile);
        //播放帧动画
        AnimationDrawable animStart = (AnimationDrawable) iv_guide_start.getBackground();
        animStart.start();

        AnimationDrawable animNight = (AnimationDrawable) iv_guide_night.getBackground();
        animNight.start();

        AnimationDrawable animSmile = (AnimationDrawable) iv_guide_smile.getBackground();
        animSmile.start();
        //小圆点逻辑
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                seletePoint(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //歌曲的逻辑
        startMusic();
    }

    /**
     * 播放音乐
     */
    private void startMusic() {
        mediaPlayerManager=new MediaPlayerManager();
        mediaPlayerManager.setLooping(true);
        AssetFileDescriptor fileDescriptor = getResources().openRawResourceFd(R.raw.piaoxiangbeifang);
        mediaPlayerManager.startPlay(fileDescriptor);
        //旋转动画
        mAnim= AnimUtils.rotation(ivMusicSwitch);
        mAnim.start();
    }

    /**
     * 动态选择小圆点
     * @param position
     */
    private void seletePoint(int position) {
        switch (position){
            case 0:
                ivGuidePoint1.setImageResource(R.drawable.img_guide_point_p);
                ivGuidePoint2.setImageResource(R.drawable.img_guide_point);
                ivGuidePoint3.setImageResource(R.drawable.img_guide_point);
                break;
            case 1:
                ivGuidePoint2.setImageResource(R.drawable.img_guide_point_p);
                ivGuidePoint1.setImageResource(R.drawable.img_guide_point);
                ivGuidePoint3.setImageResource(R.drawable.img_guide_point);
                break;
            case 2:
                ivGuidePoint3.setImageResource(R.drawable.img_guide_point_p);
                ivGuidePoint2.setImageResource(R.drawable.img_guide_point);
                ivGuidePoint1.setImageResource(R.drawable.img_guide_point);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_music_switch:
                if (mediaPlayerManager.MEDIA_STATUS==MediaPlayerManager.MEDIA_STATUS_PAUSE){
                    mAnim.start();
                    mediaPlayerManager.continuePlay();
                    ivMusicSwitch.setImageResource(R.drawable.img_guide_music);
                }else if (mediaPlayerManager.MEDIA_STATUS==MediaPlayerManager.MEDIA_STATUS_PLAY){
                    mAnim.pause();
                    mediaPlayerManager.pausePlay();
                    ivMusicSwitch.setImageResource(R.drawable.img_guide_music_off);
                }
                break;
            case R.id.tv_guide_skip:
                startActivity(new Intent(this,LoginActivity.class));
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayerManager.stopPlay();
    }
}
