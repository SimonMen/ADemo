package com.cedarwood.ademo.utils;

import android.animation.Animator;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

/**
 * Created by wentongmen on 2016/3/9.
 */
public class AnimationUtil {


    /**
     * 缩放动画效果
     *
     * @param view      需要缩放的View
     * @param scale     缩放比例
     * @param hasRepeat 如果需要缓慢恢复，则设为true，否则为false（主要在第一次出现或最后一次消失是为false）
     */
    public static void addScaleAnimationToView(final View view, float scale, boolean hasRepeat) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1, scale, 1, scale, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(100);
        scaleAnimation.setStartTime(Animation.START_ON_FIRST_FRAME);
        if (hasRepeat) {
            scaleAnimation.setRepeatMode(Animation.REVERSE);
            scaleAnimation.setRepeatCount(1);
        }
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(scaleAnimation);
    }

    @NonNull
    public static Animator[] concatAnimators(@NonNull final Animator[] animators, @NonNull final Animator alphaAnimator) {
        Animator[] allAnimators = new Animator[animators.length + 1];
        int i = 0;

        for (Animator animator : animators) {
            allAnimators[i] = animator;
            ++i;
        }
        allAnimators[allAnimators.length - 1] = alphaAnimator;
        return allAnimators;
    }


}
