package com.mayur.flexiview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Adapter;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

public class StacksView extends ViewGroup {

    // Constants for stack appearance
    private static final int MAX_VISIBLE_ITEMS = 4;
    private static final float SCALE_DECREMENT = 0.05f;
    private static final float TRANSLATION_Y_FACTOR = 20f;
    private static final float TRANSLATION_X_FACTOR = 10f;
    private static final int ROTATION_MAX = 5;
    private static final int ANIMATION_DURATION = 250;
    private static final float SWIPE_THRESHOLD = 0.3f;

    // Variables for touch handling
    private float mLastX;
    private float mInitialX;
    private float mCurrentX;
    private boolean mIsDragging = false;
    private VelocityTracker mVelocityTracker;
    private int mTouchSlop;
    private int mMinimumVelocity;
    private int mMaximumVelocity;
    private ValueAnimator mAnimator;
    private float mSwipeProgress = 0f;

    // Stack appearance customization
    private int mVisibleItems = MAX_VISIBLE_ITEMS;
    private float mScaleDecrement = SCALE_DECREMENT;
    private float mYOffset = TRANSLATION_Y_FACTOR;
    private float mXOffset = TRANSLATION_X_FACTOR;
    private int mRotationMax = ROTATION_MAX;

    // Adapter and view recycling
    private StackAdapter mAdapter;
    private final List<View> mActiveViews = new ArrayList<>();
    private final List<View> mRecycledViews = new ArrayList<>();
    private int mCurrentPosition = 0;
    private boolean mUsingAdapter = false;
    private final DataSetObserver mObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            refreshViews();
        }

        @Override
        public void onInvalidated() {
            removeAllViews();
            mActiveViews.clear();
            mRecycledViews.clear();
            mCurrentPosition = 0;
        }
    };

    // Listener for position changes
    private OnStackChangedListener mOnStackChangedListener;

    public StacksView(Context context) {
        this(context, null);
    }

    public StacksView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StacksView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // Get ViewConfiguration for touch parameters
        ViewConfiguration config = ViewConfiguration.get(context);
        mTouchSlop = config.getScaledTouchSlop();
        mMinimumVelocity = config.getScaledMinimumFlingVelocity();
        mMaximumVelocity = config.getScaledMaximumFlingVelocity();

        // Read custom attributes if provided
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StacksView);
            mVisibleItems = a.getInteger(R.styleable.StacksView_visibleItems, MAX_VISIBLE_ITEMS);
            mScaleDecrement = a.getFloat(R.styleable.StacksView_scaleDecrement, SCALE_DECREMENT);
            mYOffset = a.getDimension(R.styleable.StacksView_yOffset, TRANSLATION_Y_FACTOR);
            mXOffset = a.getDimension(R.styleable.StacksView_xOffset, TRANSLATION_X_FACTOR);
            mRotationMax = a.getInteger(R.styleable.StacksView_rotationMax, ROTATION_MAX);
            a.recycle();
        }

        // Set clip children to false to allow for visual overflow of child views
        setClipChildren(false);
        setClipToPadding(false);
    }
    
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        
        // If we have XML-declared children and no adapter set
        if (getChildCount() > 0 && !mUsingAdapter) {
            setupWithXmlChildren();
        }
    }
    
    /**
     * Setup the stack using XML-declared children
     */
    private void setupWithXmlChildren() {
        // Store all child views declared in XML
        int childCount = getChildCount();
        
        // Clear active views
        mActiveViews.clear();
        
        // Add all children to active views list (hide them for now)
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            mActiveViews.add(child);
            child.setVisibility(View.GONE); // Hide initially
        }
        
        // Now we'll show only the visible items
        refreshChildViews();
    }
    
    /**
     * Refresh and show the visible XML-declared children
     */
    private void refreshChildViews() {
        int totalChildren = mActiveViews.size();
        if (totalChildren == 0) return;
        
        // Show visible items
        int visibleCount = Math.min(mVisibleItems, totalChildren);
        for (int i = 0; i < totalChildren; i++) {
            View child = mActiveViews.get(i);
            if (i < visibleCount) {
                int position = (mCurrentPosition + i) % totalChildren;
                
                // Make the child at position visible
                View viewToShow = mActiveViews.get(position);
                viewToShow.setVisibility(View.VISIBLE);
                
                // Ensure it's at the correct position in the ViewGroup
                // The view might already be attached, so detach it
                if (indexOfChild(viewToShow) >= 0) {
                    detachViewFromParent(viewToShow);
                }
                
                // Attach it at the top of the Z-order (visibleCount - i - 1)
                attachViewToParent(viewToShow, visibleCount - i - 1, viewToShow.getLayoutParams());
            } else {
                child.setVisibility(View.GONE);
            }
        }
        
        // Apply transformations
        applyTransformations();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        // Measure all children
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int width = right - left;
        int height = bottom - top;

        // Layout all children in the same place
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                child.layout(0, 0, width, height);
            }
        }

        // Apply transformations to create the stack effect
        applyTransformations();
    }

    /**
     * Apply scale, translation and rotation transformations to create the stack effect
     */
    private void applyTransformations() {
        // Note: For XML-declared children, we'll be operating directly on the visible children
        int childCount = getChildCount();
        if (childCount == 0) return;
        
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            if (view.getVisibility() == GONE) continue;
            
            if (i == 0) {
                // Top view is affected by swipe
                view.setTranslationX(mSwipeProgress * getWidth());
                view.setRotation(mSwipeProgress * 15f);
                
                // Make the view disappear when swiped far enough
                float alpha = 1f;
                if (Math.abs(mSwipeProgress) > 0.5f) {
                    alpha = 1f - ((Math.abs(mSwipeProgress) - 0.5f) * 2f);
                }
                view.setAlpha(alpha);
                
                // Adjust other properties based on swipe
                float scale = 1f;
                view.setScaleX(scale);
                view.setScaleY(scale);
                view.setTranslationY(0);
                view.setElevation(childCount + 10f);
            } else {
                // Adjust index based on swipe progress
                float adjustedIndex = i - Math.min(1, Math.max(0, 1 - Math.abs(mSwipeProgress)));
                
                // Stacked views
                float scale = 1f - (adjustedIndex * mScaleDecrement);
                view.setScaleX(scale);
                view.setScaleY(scale);
                
                // Offset each card
                float xOffset = adjustedIndex * mXOffset;
                float yOffset = adjustedIndex * mYOffset;
                view.setTranslationX(xOffset);
                view.setTranslationY(yOffset);
                
                // Add slight rotation for visual interest
                float rotation = 0;
                if (adjustedIndex > 0) {
                    rotation = (float) (Math.random() * mRotationMax * 2 - mRotationMax) * 0.2f;
                }
                view.setRotation(rotation);
                
                // Set elevation to ensure proper rendering order
                view.setElevation(childCount - i);
                view.setAlpha(1f);
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int childCount = mUsingAdapter ? (mAdapter != null ? mAdapter.getCount() : 0) : getChildCount();
        if (childCount <= 1) {
            return false;
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = ev.getX();
                mInitialX = mLastX;
                mIsDragging = false;
                
                // Cancel any ongoing animation
                if (mAnimator != null && mAnimator.isRunning()) {
                    mAnimator.cancel();
                }
                
                // Initialize velocity tracker
                if (mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain();
                } else {
                    mVelocityTracker.clear();
                }
                mVelocityTracker.addMovement(ev);
                break;
                
            case MotionEvent.ACTION_MOVE:
                float x = ev.getX();
                float dx = x - mLastX;
                
                if (Math.abs(x - mInitialX) > mTouchSlop) {
                    mIsDragging = true;
                    getParent().requestDisallowInterceptTouchEvent(true);
                    return true;
                }
                break;
                
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mIsDragging = false;
                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                break;
        }
        
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int childCount = mUsingAdapter ? (mAdapter != null ? mAdapter.getCount() : 0) : getChildCount();
        if (childCount <= 1) {
            return false;
        }

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = event.getX();
                mInitialX = mLastX;
                break;
                
            case MotionEvent.ACTION_MOVE:
                mCurrentX = event.getX();
                float dx = mCurrentX - mLastX;
                
                if (!mIsDragging && Math.abs(mCurrentX - mInitialX) > mTouchSlop) {
                    mIsDragging = true;
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                
                if (mIsDragging) {
                    // Calculate swipe progress as a percentage of the view width
                    mSwipeProgress += dx / getWidth();
                    
                    // Constrain swipe progress
                    mSwipeProgress = Math.max(-1f, Math.min(1f, mSwipeProgress));
                    
                    // Apply transformations
                    applyTransformations();
                    invalidate();
                }
                
                mLastX = mCurrentX;
                break;
                
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mIsDragging) {
                    mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    float velocity = mVelocityTracker.getXVelocity();
                    
                    // Determine whether to complete the swipe or return to center
                    boolean swipe;
                    if (Math.abs(velocity) > mMinimumVelocity) {
                        // Swipe if velocity is high enough
                        swipe = velocity > 0 == mSwipeProgress > 0;
                    } else {
                        // Swipe if progress is beyond threshold
                        swipe = Math.abs(mSwipeProgress) > SWIPE_THRESHOLD;
                    }
                    
                    float targetProgress = swipe ? (mSwipeProgress > 0 ? 1f : -1f) : 0f;
                    animateSwipe(targetProgress);
                }
                
                mIsDragging = false;
                
                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                break;
        }
        
        return true;
    }

    /**
     * Animate the swipe to a target position
     */
    private void animateSwipe(final float targetProgress) {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
        
        mAnimator = ValueAnimator.ofFloat(mSwipeProgress, targetProgress);
        mAnimator.setDuration(ANIMATION_DURATION);
        mAnimator.setInterpolator(new DecelerateInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mSwipeProgress = (float) animation.getAnimatedValue();
                applyTransformations();
                invalidate();
            }
        });
        
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (targetProgress != 0) {
                    // Moved to next/previous position
                    if (targetProgress > 0) {
                        // Swiped right, move to previous item
                        showPrevious();
                    } else {
                        // Swiped left, move to next item
                        showNext();
                    }
                    // Reset swipe progress
                    mSwipeProgress = 0f;
                    // Apply transformations without animation
                    applyTransformations();
                }
            }
        });
        
        mAnimator.start();
    }

    /**
     * Move to the next item in the stack
     */
    public void showNext() {
        int count = mUsingAdapter ? (mAdapter != null ? mAdapter.getCount() : 0) : mActiveViews.size();
        if (count <= 1) {
            return;
        }
        
        mCurrentPosition = (mCurrentPosition + 1) % count;
        
        if (mUsingAdapter) {
            refreshViews();
        } else {
            refreshChildViews();
        }
        
        if (mOnStackChangedListener != null) {
            mOnStackChangedListener.onStackChanged(mCurrentPosition);
        }
    }

    /**
     * Move to the previous item in the stack
     */
    public void showPrevious() {
        int count = mUsingAdapter ? (mAdapter != null ? mAdapter.getCount() : 0) : mActiveViews.size();
        if (count <= 1) {
            return;
        }
        
        mCurrentPosition = (mCurrentPosition - 1 + count) % count;
        
        if (mUsingAdapter) {
            refreshViews();
        } else {
            refreshChildViews();
        }
        
        if (mOnStackChangedListener != null) {
            mOnStackChangedListener.onStackChanged(mCurrentPosition);
        }
    }

    /**
     * Set the adapter to provide views for the stack
     */
    public void setAdapter(StackAdapter adapter) {
        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mObserver);
        }
        
        mAdapter = adapter;
        mUsingAdapter = (adapter != null);
        
        // Clear any XML-declared children from the active views
        if (mUsingAdapter) {
            mActiveViews.clear();
        }
        
        if (mAdapter != null) {
            mAdapter.registerDataSetObserver(mObserver);
            mCurrentPosition = 0;
            refreshViews();
        } else {
            // If we have no adapter but have XML children, use them
            if (!mUsingAdapter && getChildCount() > 0) {
                setupWithXmlChildren();
            } else {
                removeAllViews();
                mActiveViews.clear();
                mRecycledViews.clear();
            }
        }
    }

    /**
     * Refresh the stack with views from the adapter
     */
    private void refreshViews() {
        if (mAdapter == null) return;
        
        // Clear active views but save them for recycling
        for (View view : mActiveViews) {
            removeView(view);
            mRecycledViews.add(view);
        }
        mActiveViews.clear();
        
        // Add visible views from adapter
        int count = Math.min(mVisibleItems, mAdapter.getCount());
        for (int i = 0; i < count; i++) {
            int position = (mCurrentPosition + i) % mAdapter.getCount();
            
            View view;
            if (!mRecycledViews.isEmpty()) {
                // Reuse a recycled view
                view = mRecycledViews.remove(0);
                mAdapter.getView(position, view, this);
            } else {
                // Create a new view
                view = mAdapter.getView(position, null, this);
            }
            
            // Add the view to the stack
            addView(view, 0);
            mActiveViews.add(view);
        }
        
        // Apply transformations
        applyTransformations();
    }

    /**
     * Set current position directly
     */
    public void setCurrentPosition(int position) {
        int count = mUsingAdapter ? (mAdapter != null ? mAdapter.getCount() : 0) : mActiveViews.size();
        if (position < 0 || position >= count) {
            return;
        }
        
        mCurrentPosition = position;
        
        if (mUsingAdapter) {
            refreshViews();
        } else {
            refreshChildViews();
        }
        
        if (mOnStackChangedListener != null) {
            mOnStackChangedListener.onStackChanged(mCurrentPosition);
        }
    }

    /**
     * Get the current position in the stack
     */
    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    /**
     * Set a listener for stack position changes
     */
    public void setOnStackChangedListener(OnStackChangedListener listener) {
        mOnStackChangedListener = listener;
    }

    /**
     * Interface for stack position change callback
     */
    public interface OnStackChangedListener {
        void onStackChanged(int position);
    }

    /**
     * Adapter to provide views for the stack
     */
    public static abstract class StackAdapter {
        private final ArrayList<DataSetObserver> mObservers = new ArrayList<>();

        /**
         * Get a view for the specified position
         */
        public abstract View getView(int position, View convertView, ViewGroup parent);

        /**
         * Get the number of items in the adapter
         */
        public abstract int getCount();

        /**
         * Register an observer to receive notifications
         */
        public void registerDataSetObserver(DataSetObserver observer) {
            mObservers.add(observer);
        }

        /**
         * Unregister an observer
         */
        public void unregisterDataSetObserver(DataSetObserver observer) {
            mObservers.remove(observer);
        }

        /**
         * Notify that the data set has changed
         */
        public void notifyDataSetChanged() {
            for (DataSetObserver observer : mObservers) {
                observer.onChanged();
            }
        }

        /**
         * Notify that the data is no longer valid
         */
        public void notifyDataSetInvalidated() {
            for (DataSetObserver observer : mObservers) {
                observer.onInvalidated();
            }
        }
    }
} 