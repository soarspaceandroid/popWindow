package com.soar.popwlib;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

/**
 * Created by gaofei on 2016/8/30.
 */
public class SoarPopWindow extends PopupWindow {
    public final static int TYPE_POSITION_UP = 0;
    public final static int TYPE_POSITION_DOWN = 1;
    public final static int TYPE_POSITION_CENTER = 2;

    private final static int ANIMATION_TIME = 300;

    private RelativeLayout parent;
    private View childView;
    private Context context ;

    private int popWid =0  , popHei = 0;


    public SoarPopWindow(Context context) {
        super(context);
    }


    /**
     * get instance
     * @param context
     * @return
     */
    public static SoarPopWindow getInstance(Context context){
        SoarPopWindow soarPopWindow = new SoarPopWindow(context);
        return soarPopWindow;
    }

    /**
     *   set pop type backcolor and postion
     * @param contentView
     * @param bgColor
     * @param position
     */
    public SoarPopWindow setPopType(View contentView , int bgColor , @PositionLim int position){
        context = contentView.getContext();
        childView = contentView;
        setWidth(popWid == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : popWid);
        setHeight(popHei == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : popHei);
        parent = new RelativeLayout(context);
        parent.setBackgroundColor(bgColor);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        switch (position){
            case TYPE_POSITION_UP:
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                break;
            case TYPE_POSITION_DOWN:
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                break;
            case TYPE_POSITION_CENTER:
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                break;
        }
        parent.addView(childView ,params);
        setContentView(parent);

        parent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = childView.getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        doAnimationUpToBottom(parent , childView, height, SoarPopWindow.this);
                    }
                }
                return true;
            }
        });


        parent.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    doAnimationUpToBottom( parent,childView, 0, SoarPopWindow.this);
                    return true;
                }
                return false;
            }
        });

        return this;
    }




    /**
     * set popwindown size
     * @param width
     * @param height
     * @return
     */
    public SoarPopWindow setSize(int width ,  int height){
        this.popWid = width;
        this.popHei = height;
        return this;
    }


    /**
     * show  pop
     */
    public void show(){
        parent.setFocusable(true);
        parent.setFocusableInTouchMode(true);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        showAtLocation(parent , Gravity.BOTTOM , 0 , 0);
        doAnimationBottomToUp( parent,childView, 0);
    }




    /**
     * bottom to up animation
     *
     * @param viewBack
     * @param view
     * @param height
     */
    public static void doAnimationBottomToUp(View viewBack, View view, int height) {
        AnimatorSet set = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            set = new AnimatorSet();

            int temHeight = 0;
            if (height == 0) {
                temHeight = (int) view.getResources().getDimension(R.dimen.dp_400);
            } else {
                temHeight = height;
            }
            ObjectAnimator animatorTrans = ObjectAnimator.ofFloat(view, "translationY", temHeight, 0.0f);
            ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(viewBack, "alpha", 0.0f, 1.0f);
            set.playTogether(animatorAlpha, animatorTrans);
            set.setInterpolator(new AccelerateDecelerateInterpolator());
            set.setDuration(ANIMATION_TIME);
            set.start();
        }

    }

    /**
     * top ti bottom animation
     *
     * @param viewBack
     * @param view
     * @param height
     * @param popWindow
     */
    public static void doAnimationUpToBottom(View viewBack, View view, int height, final PopupWindow popWindow) {
        AnimatorSet set = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            set = new AnimatorSet();

            int temHeight = 0;
            if (height == 0) {
                temHeight = (int) view.getResources().getDimension(R.dimen.dp_400);
            } else {
                temHeight = height;
            }
            ObjectAnimator animatorTrans = ObjectAnimator.ofFloat(view, "translationY", 0.0f, temHeight);
            ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(viewBack, "alpha", 1.0f, 0.0f);
            set.playTogether(animatorAlpha, animatorTrans);
            set.setInterpolator(new DecelerateInterpolator());
            set.setDuration(ANIMATION_TIME);
            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    popWindow.dismiss();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            set.start();
        }
    }



}
