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

    // Constants for carousel-style layout with movie poster effect
    private static final float LEFT_CARD_VISIBLE_PERCENT = 0.20f;
    private static final float RIGHT_CARD_VISIBLE_PERCENT = 0.20f;
    private static final float SIDE_CARD_SCALE = 0.8f;
    private static final float SIDE_CARD_ROTATION_Y = 45f; // Perspective effect
    private static final float SIDE_CARD_ROTATION_Z = 5f; // Slight tilt
    private static final float SIDE_CARD_ALPHA = 0.6f; // Darkening of side cards

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
        
        // Ensure all needed views are properly detached and reattached
        detachAllViewsFromParent();
        
        // For initial view setup, ensure we have 3 items visible if possible
        int prevPosition = mCurrentPosition > 0 ? 
                          mCurrentPosition - 1 : 
                          totalChildren - 1;
                         
        int nextPosition = (mCurrentPosition + 1) % totalChildren;
        
        // Get the views for the three positions to show
        View prevView = mActiveViews.get(prevPosition);
        View nextView = mActiveViews.get(nextPosition);
        View currentView = mActiveViews.get(mCurrentPosition);
        
        // Attach in the correct z-order (bottom to top)
        // Make all needed views visible
        prevView.setVisibility(View.VISIBLE);
        nextView.setVisibility(View.VISIBLE);
        currentView.setVisibility(View.VISIBLE);
        
        // Attach in z-order (bottom to top)
        attachViewToParent(prevView, 0, prevView.getLayoutParams());
        attachViewToParent(nextView, 1, nextView.getLayoutParams());
        attachViewToParent(currentView, 2, currentView.getLayoutParams());
        
        // Hide other views if there are more than 3
        for (int i = 0; i < totalChildren; i++) {
            if (i != mCurrentPosition && i != prevPosition && i != nextPosition) {
                mActiveViews.get(i).setVisibility(View.GONE);
            }
        }
        
        // Apply transformations in post to ensure it happens after layout
        post(new Runnable() {
            @Override
            public void run() {
                applyTransformations();
            }
        });
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

        // Make sure to apply transformations after initial layout
        post(new Runnable() {
            @Override
            public void run() {
                applyTransformations();
            }
        });
    }

    /**
     * Apply scale, translation and rotation transformations to create the stack effect
     */
    private void applyTransformations() {
        // Note: For XML-declared children, we'll be operating directly on the visible children
        int childCount = getChildCount();
        if (childCount == 0) return;
        
        int totalItems = mUsingAdapter ? mAdapter.getCount() : mActiveViews.size();
        int viewWidth = getWidth();
        
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            if (view.getVisibility() == GONE) continue;
            
            // Calculate the position relative to current
            int position = (i == 0) ? mCurrentPosition : 
                          ((mCurrentPosition + i) % totalItems);
            int relativePos = position - mCurrentPosition;
            
            if (i == 0) {
                // Current/center view
                // Apply swipe animation if in progress
                float xTranslation = mSwipeProgress * viewWidth;
                view.setTranslationX(xTranslation);
                
                // Make the view disappear when swiped far enough
                float alpha = 1f;
                if (Math.abs(mSwipeProgress) > 0.5f) {
                    alpha = 1f - ((Math.abs(mSwipeProgress) - 0.5f) * 2f);
                }
                view.setAlpha(alpha);
                
                // Main card has no rotation when centered
                float scale = 1.0f;
                view.setScaleX(scale);
                view.setScaleY(scale);
                view.setTranslationY(0);
                view.setRotation(mSwipeProgress * 5f); // Small rotation for swipe
                view.setRotationY(mSwipeProgress * 15f); // Add perspective effect during swipe
                view.setCameraDistance(viewWidth * 5); // Enhance perspective effect
                view.setElevation(childCount + 10f);
            } else if (relativePos == -1 || (relativePos == totalItems - 1 && i == 1)) {
                // Left card (previous)
                float visibleWidth = viewWidth * LEFT_CARD_VISIBLE_PERCENT;
                float xTranslation = -viewWidth + visibleWidth + (mSwipeProgress > 0 ? mSwipeProgress * viewWidth : 0);
                
                view.setTranslationX(xTranslation);
                view.setScaleX(SIDE_CARD_SCALE);
                view.setScaleY(SIDE_CARD_SCALE);
                view.setTranslationY(0);
                
                // Apply perspective effect and tilt
                view.setRotationY(SIDE_CARD_ROTATION_Y);
                view.setRotation(-SIDE_CARD_ROTATION_Z);
                view.setCameraDistance(viewWidth * 5);
                
                view.setAlpha(SIDE_CARD_ALPHA);
                view.setElevation(childCount - i);
            } else if (relativePos == 1 || (relativePos == -(totalItems - 1) && i == 1)) {
                // Right card (next)
                float visibleWidth = viewWidth * RIGHT_CARD_VISIBLE_PERCENT;
                float xTranslation = viewWidth - visibleWidth + (mSwipeProgress < 0 ? mSwipeProgress * viewWidth : 0);
                
                view.setTranslationX(xTranslation);
                view.setScaleX(SIDE_CARD_SCALE);
                view.setScaleY(SIDE_CARD_SCALE);
                view.setTranslationY(0);
                
                // Apply perspective effect and tilt
                view.setRotationY(-SIDE_CARD_ROTATION_Y);
                view.setRotation(SIDE_CARD_ROTATION_Z);
                view.setCameraDistance(viewWidth * 5);
                
                view.setAlpha(SIDE_CARD_ALPHA);
                view.setElevation(childCount - i);
            } else {
                // Hide other cards
                view.setAlpha(0f);
                view.setTranslationX(relativePos > 0 ? viewWidth : -viewWidth);
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
        
        int totalCount = mAdapter.getCount();
        if (totalCount == 0) return;
        
        // We need at minimum 3 views: current, left, right (if available)
        // For initial setup:
        // - Current item is in center (mCurrentPosition)
        // - Left item is the last item or previous item (if mCurrentPosition > 0)
        // - Right item is the next item (mCurrentPosition+1 or 0 if at end)
        
        int prevPosition = mCurrentPosition > 0 ? 
                          mCurrentPosition - 1 : 
                          totalCount - 1;
                          
        int nextPosition = (mCurrentPosition + 1) % totalCount;
        
        // Add the three cards in the proper z-order
        addCardViewAtPosition(prevPosition); // Previous (bottom)
        addCardViewAtPosition(nextPosition); // Next (middle)
        addCardViewAtPosition(mCurrentPosition); // Current (top)
        
        // Apply transformations after adding all views
        post(new Runnable() {
            @Override
            public void run() {
                applyTransformations();
            }
        });
    }
    
    /**
     * Helper method to add a card at the given position
     */
    private void addCardViewAtPosition(int position) {
        if (mAdapter == null || position < 0 || position >= mAdapter.getCount()) return;
        
        View view;
        if (!mRecycledViews.isEmpty()) {
            // Reuse a recycled view
            view = mRecycledViews.remove(0);
            mAdapter.getView(position, view, this);
        } else {
            // Create a new view
            view = mAdapter.getView(position, null, this);
        }
        
        // Add the view to the stack (at position 0 to ensure proper z-order)
        addView(view, 0);
        mActiveViews.add(view);
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