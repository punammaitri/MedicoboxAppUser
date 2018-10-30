package com.aiprous.medicobox.sliderimages;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.aiprous.medicobox.R;

import java.util.Timer;
import java.util.TimerTask;

/*
* FlipperLayout is a compound layout which consists of a View Pager and a View Pager Indicator
 */
public class FlipperLayout extends FrameLayout implements CircularFlipperHandler.CurrentPageListener {

    private static final long DELAY_MS = 500;
    private static PagerAdapter mFlippingPagerAdapter;
    int currentPage = 0;
    CircularFlipperHandler circularFlipperHandler;
    private ViewPager mFlippingPager;
    private int scrollTimeInSec = 2;
    private Handler handler = new Handler();

    public FlipperLayout(Context context) {
        super(context);
        setLayout(context);
    }

    public FlipperLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayout(context);
    }

    public FlipperLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayout(context);
    }

    private static PagerAdapter getFlippingPagerAdapter() {
        return mFlippingPagerAdapter;
    }

    public int getScrollTimeInSec() {
        return scrollTimeInSec;
    }

    public void setScrollTimeInSec(int time) {
        scrollTimeInSec = time;
        startAutoCycle();
    }

    public int getCurrentPagePosition() {
        if (getFlippingPagerAdapter() != null) {
            return mFlippingPager.getCurrentItem() % mFlippingPagerAdapter.getCount();
        } else {
            throw new NullPointerException("Adapter not set");
        }
    }

    private void setLayout(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.flipper_layout, this, true);
        mFlippingPager = (ViewPager) view.findViewById(R.id.vp_flipper_layout);
        mFlippingPagerAdapter = new FlipperAdapter(context);

        mFlippingPager.setAdapter(mFlippingPagerAdapter);

        // Handler for onPageChangeListener
        circularFlipperHandler = new CircularFlipperHandler(mFlippingPager);
        circularFlipperHandler.setCurrentPageListener(this);
        mFlippingPager.addOnPageChangeListener(circularFlipperHandler);

        //Starting auto cycle at the time of setting up of layout
        startAutoCycle();
    }

    public void addFlipperView(FlipperView flipperView) {
        ((FlipperAdapter) mFlippingPagerAdapter).addFlipperView(flipperView);
    }

    private void startAutoCycle() {
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == getFlippingPagerAdapter().getCount()) {
                    currentPage = 0;
                }
                // true set for smooth transition between pager
                mFlippingPager.setCurrentItem(currentPage++, true);
            }
        };

        Timer flippingTimer = new Timer();
        flippingTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, scrollTimeInSec * 1000);
    }

    @Override
    public void onCurrentPageChanged(int currentPosition) {
        this.currentPage = currentPosition;
    }
}