package xyz.hanks.loadinglayout_library;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * include three status layout
 * normal layout
 * loading layout
 * selected layout
 */
public class LoadingLayout extends FrameLayout {

    private View mNormalView;
    private View mLoadingView;
    private View mSelectedView;
    private int mloadingViewHeight;
    private int mLoadingViewWidth;

    public LoadingLayout(Context context) {
        this(context, null);
    }

    public LoadingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    private void init() {
        if (getChildCount() == 2) {
            mNormalView = getChildAt(0);
            mSelectedView = getChildAt(1);
        } else if (getChildCount() == 3) {
            mNormalView = getChildAt(0);
            mLoadingView = getChildAt(1);
            mSelectedView = getChildAt(2);
        } else {
            throw new RuntimeException("must have two or three child view");
        }
        setVisibility(mNormalView, VISIBLE);
        setVisibility(mLoadingView, INVISIBLE);
        setVisibility(mSelectedView, INVISIBLE);
    }

    public void normal() {
        if (mNormalView == null) {
            return;
        }
        if (isLoadingStatus()) {
            transiteView(mLoadingView, mNormalView);
        } else {
            setVisibility(mNormalView, VISIBLE);
            setVisibility(mLoadingView, INVISIBLE);
            setVisibility(mSelectedView, INVISIBLE);
        }
    }

    public void loading() {
        if (mLoadingView == null) {
            return;
        }
        if (isNormalStatus()) {
            transiteView(mNormalView, mLoadingView);
        } else if (isSelectedStatus()) {
            transiteView(mSelectedView, mLoadingView);
        }
    }

    private void transiteView(final View willHide, final View willShow) {

        float x = willShow.getWidth() * 1f / willHide.getWidth();
        float y = willShow.getHeight() * 1f / willHide.getHeight();
        willShow.setScaleX(1f / x);
        willShow.setScaleY(1f / y);
        willShow.setAlpha(0);
        setVisibility(willShow, VISIBLE);
        willShow.animate()
                .setDuration(300)
                .alpha(1)
                .scaleX(1)
                .scaleY(1)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        willShow.setScaleX(1f);
                        willShow.setScaleY(1f);
                        willShow.setAlpha(1);
                    }
                })
                .start();

        willHide.animate()
                .setDuration(300)
                .alpha(0)
                .scaleX(x)
                .scaleY(y)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        willHide.setScaleX(1);
                        willHide.setScaleY(1);
                        willHide.setAlpha(1);
                        if (willShow != mLoadingView) {
                            setVisibility(mLoadingView, INVISIBLE);
                        }
                        if (willShow != mNormalView) {
                            setVisibility(mNormalView, INVISIBLE);
                        }
                        if (willShow != mSelectedView) {
                            setVisibility(mSelectedView, INVISIBLE);
                        }
                    }
                })
                .start();
    }

    public void select() {
        if (mSelectedView == null) {
            return;
        }
        if (isLoadingStatus()) {
            transiteView(mLoadingView, mSelectedView);
        } else {
            setVisibility(mSelectedView, VISIBLE);
            setVisibility(mNormalView, INVISIBLE);
            setVisibility(mLoadingView, INVISIBLE);
        }
    }

    public boolean isNormalStatus() {
        return mNormalView != null && mNormalView.getVisibility() == VISIBLE;
    }

    public boolean isLoadingStatus() {
        return mLoadingView != null && mLoadingView.getVisibility() == VISIBLE;
    }

    public boolean isSelectedStatus() {
        return mSelectedView != null && mSelectedView.getVisibility() == VISIBLE;
    }

    private void setVisibility(View view, int visibility) {
        if (view == null ||
                (visibility != VISIBLE && visibility != INVISIBLE && visibility != GONE)) {
            return;
        }
        view.setVisibility(visibility);
    }

}
