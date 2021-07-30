package com.widget.library.anim.coordinator_anim.lib_anim;

import android.view.View;

import com.widget.library.anim.CommonAnim;

import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListener;

/**
 * fab Anim
 * Created by Lauzy on 2017/3/15.
 */

public class LFabScaleBehaviorAnim extends CommonAnim {

    private View mFabView;

    public LFabScaleBehaviorAnim(View fabView) {
        mFabView = fabView;
    }

    public void hideFab() {

        /*ValueAnimator anim = ValueAnimator.ofFloat(mFabView.getY(), mOriginalY + mFabView.getHeight());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mFabView.setY((Float) valueAnimator.getAnimatedValue());
            }
        });
        anim.setDuration(400);
        anim.start();*/


    }

    public void showFab() {


    }

    @Override
    public void show() {
        ViewCompat.animate(mFabView).scaleX(1f).scaleY(1f)
                .setDuration(getDuration())
                .setInterpolator(getInterpolator())
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {
                        view.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                    }

                    @Override
                    public void onAnimationCancel(View view) {

                    }
                }).start();
    }

    @Override
    public void hide() {
        ViewCompat.animate(mFabView).scaleX(0f).scaleY(0f)
                .setDuration(getDuration())
                .setInterpolator(getInterpolator())
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {

                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        view.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(View view) {

                    }
                }).start();
    }
}
