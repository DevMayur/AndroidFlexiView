package com.mayur.flexiview;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Demo activity showcasing the interactive features of DrawableRelativeLayout
 * such as ripple effects, state changes, and animations
 */
public class InteractiveFeaturesDemoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Create a scrollable container
        ScrollView scrollView = new ScrollView(this);
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(20, 20, 20, 20);
        
        // Title
        TextView titleView = new TextView(this);
        titleView.setText("Interactive Features");
        titleView.setTextSize(24);
        titleView.setPadding(0, 0, 0, 20);
        mainLayout.addView(titleView);
        
        // Add various interactive demonstrations
        mainLayout.addView(createFeatureTitle("Ripple Effects"));
        mainLayout.addView(createRippleEffectsDemo());
        
        mainLayout.addView(createFeatureTitle("State-Based Colors"));
        mainLayout.addView(createStateBasedColorsDemo());
        
        mainLayout.addView(createFeatureTitle("Animation Effects"));
        mainLayout.addView(createAnimationDemo());
        
        mainLayout.addView(createFeatureTitle("Interactive Card"));
        mainLayout.addView(createInteractiveCardDemo());
        
        scrollView.addView(mainLayout);
        setContentView(scrollView);
    }
    
    private TextView createFeatureTitle(String title) {
        TextView textView = new TextView(this);
        textView.setText(title);
        textView.setTextSize(20);
        textView.setPadding(0, 30, 0, 10);
        return textView;
    }
    
    private View createRippleEffectsDemo() {
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        
        // Basic ripple effect
        final DrawableRelativeLayout basicRipple = new DrawableRelativeLayout(this);
        basicRipple.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 200));
        basicRipple.setBackgroundColor(Color.parseColor("#4285F4"));
        basicRipple.setCornerRadius(20);
        basicRipple.setRippleColor(Color.parseColor("#80FFFFFF"));
        basicRipple.setRippleEnabled(true);
        basicRipple.setClickable(true);
        
        TextView basicRippleText = new TextView(this);
        basicRippleText.setText("Tap for Basic Ripple Effect");
        basicRippleText.setTextColor(Color.WHITE);
        basicRippleText.setPadding(20, 20, 20, 20);
        basicRipple.addView(basicRippleText);
        
        container.addView(basicRipple);
        container.addView(createSpacer());
        
        // Colored ripple effect
        final DrawableRelativeLayout coloredRipple = new DrawableRelativeLayout(this);
        coloredRipple.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 200));
        coloredRipple.setBackgroundColor(Color.WHITE);
        coloredRipple.setCornerRadius(20);
        coloredRipple.setRippleColor(Color.parseColor("#80EA4335")); // Red ripple
        coloredRipple.setRippleEnabled(true);
        coloredRipple.setClickable(true);
        
        TextView coloredRippleText = new TextView(this);
        coloredRippleText.setText("Tap for Colored Ripple Effect");
        coloredRippleText.setPadding(20, 20, 20, 20);
        coloredRipple.addView(coloredRippleText);
        
        container.addView(coloredRipple);
        
        return container;
    }
    
    private View createStateBasedColorsDemo() {
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        
        // Background color states
        final DrawableRelativeLayout bgStates = new DrawableRelativeLayout(this);
        bgStates.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 200));
        bgStates.setStateColors(
                Color.parseColor("#4285F4"),  // Normal
                Color.parseColor("#EA4335"),  // Pressed
                Color.parseColor("#CCCCCC")   // Disabled
        );
        bgStates.setCornerRadius(20);
        bgStates.setClickable(true);
        
        TextView bgStateText = new TextView(this);
        bgStateText.setText("Tap to see background color change");
        bgStateText.setTextColor(Color.WHITE);
        bgStateText.setPadding(20, 20, 20, 20);
        bgStates.addView(bgStateText);
        
        container.addView(bgStates);
        container.addView(createSpacer());
        
        // Stroke color states
        final DrawableRelativeLayout strokeStates = new DrawableRelativeLayout(this);
        strokeStates.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 200));
        strokeStates.setBackgroundColor(Color.WHITE);
        strokeStates.setStrokeWidth(8);
        strokeStates.setStrokeStateColors(
                Color.parseColor("#4285F4"),  // Normal
                Color.parseColor("#EA4335"),  // Pressed
                Color.parseColor("#CCCCCC")   // Disabled
        );
        strokeStates.setCornerRadius(20);
        strokeStates.setClickable(true);
        
        TextView strokeStateText = new TextView(this);
        strokeStateText.setText("Tap to see stroke color change");
        strokeStateText.setPadding(20, 20, 20, 20);
        strokeStates.addView(strokeStateText);
        
        container.addView(strokeStates);
        container.addView(createSpacer());
        
        // Controls for enabling/disabling demos
        Button toggleButton = new Button(this);
        toggleButton.setText("Toggle Enabled State");
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean newState = !bgStates.isEnabled();
                bgStates.setEnabled(newState);
                strokeStates.setEnabled(newState);
            }
        });
        
        container.addView(toggleButton);
        
        return container;
    }
    
    private View createAnimationDemo() {
        final LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        
        // Animated gradient angle
        final DrawableRelativeLayout gradientAngleAnimation = new DrawableRelativeLayout(this);
        gradientAngleAnimation.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 200));
        gradientAngleAnimation.setGradientColors(new int[]{
                Color.parseColor("#4285F4"),  // Blue
                Color.parseColor("#34A853")   // Green
        });
        gradientAngleAnimation.setGradientType(0); // Linear
        gradientAngleAnimation.setGradientAngle(0);
        gradientAngleAnimation.setCornerRadius(20);
        
        TextView gradientAngleText = new TextView(this);
        gradientAngleText.setText("Animated Gradient Angle");
        gradientAngleText.setTextColor(Color.WHITE);
        gradientAngleText.setPadding(20, 20, 20, 20);
        gradientAngleAnimation.addView(gradientAngleText);
        
        container.addView(gradientAngleAnimation);
        container.addView(createSpacer());
        
        // Animated corner radius
        final DrawableRelativeLayout cornerRadiusAnimation = new DrawableRelativeLayout(this);
        cornerRadiusAnimation.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 200));
        cornerRadiusAnimation.setBackgroundColor(Color.parseColor("#EA4335"));
        cornerRadiusAnimation.setCornerRadius(20);
        
        TextView cornerRadiusText = new TextView(this);
        cornerRadiusText.setText("Animated Corner Radius");
        cornerRadiusText.setTextColor(Color.WHITE);
        cornerRadiusText.setPadding(20, 20, 20, 20);
        cornerRadiusAnimation.addView(cornerRadiusText);
        
        container.addView(cornerRadiusAnimation);
        container.addView(createSpacer());
        
        // Start/stop animation button
        final Button animateButton = new Button(this);
        animateButton.setText("Start Animations");
        
        final ValueAnimator[] animators = new ValueAnimator[2];
        
        animateButton.setOnClickListener(new View.OnClickListener() {
            boolean isAnimating = false;
            
            @Override
            public void onClick(View v) {
                if (!isAnimating) {
                    // Start animations
                    animateButton.setText("Stop Animations");
                    
                    // Gradient angle animation
                    animators[0] = ValueAnimator.ofFloat(0, 360);
                    animators[0].setDuration(3000);
                    animators[0].setRepeatCount(ValueAnimator.INFINITE);
                    animators[0].setInterpolator(new AccelerateDecelerateInterpolator());
                    animators[0].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float angle = (float) animation.getAnimatedValue();
                            gradientAngleAnimation.setGradientAngle(angle);
                        }
                    });
                    animators[0].start();
                    
                    // Corner radius animation
                    animators[1] = ValueAnimator.ofInt(0, 100);
                    animators[1].setDuration(1500);
                    animators[1].setRepeatCount(ValueAnimator.INFINITE);
                    animators[1].setRepeatMode(ValueAnimator.REVERSE);
                    animators[1].setInterpolator(new AccelerateDecelerateInterpolator());
                    animators[1].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            int radius = (int) animation.getAnimatedValue();
                            cornerRadiusAnimation.setCornerRadius(radius);
                        }
                    });
                    animators[1].start();
                } else {
                    // Stop animations
                    animateButton.setText("Start Animations");
                    for (ValueAnimator animator : animators) {
                        if (animator != null) {
                            animator.cancel();
                        }
                    }
                }
                
                isAnimating = !isAnimating;
            }
        });
        
        container.addView(animateButton);
        
        return container;
    }
    
    private View createInteractiveCardDemo() {
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        
        // Create description
        TextView descriptionText = new TextView(this);
        descriptionText.setText("This card combines multiple interactive features. Touch and hold to see the effects.");
        descriptionText.setPadding(0, 0, 0, 20);
        container.addView(descriptionText);
        
        // Create the interactive card
        final DrawableRelativeLayout interactiveCard = new DrawableRelativeLayout(this);
        interactiveCard.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 300));
        interactiveCard.setBackgroundColor(Color.parseColor("#4285F4"));
        interactiveCard.setCornerRadius(40);
        interactiveCard.setShadowColor(Color.parseColor("#80000000"));
        interactiveCard.setShadowRadius(20);
        interactiveCard.setShadowOffset(5, 5);
        interactiveCard.setGradientColors(new int[]{
                Color.parseColor("#4285F4"),
                Color.parseColor("#0F9D58")
        });
        interactiveCard.setGradientType(0); // Linear
        interactiveCard.setGradientAngle(45);
        interactiveCard.setRippleColor(Color.parseColor("#80FFFFFF"));
        interactiveCard.setRippleEnabled(true);
        interactiveCard.setClickable(true);
        
        // Add content to the card
        TextView cardText = new TextView(this);
        cardText.setText("Interactive Card");
        cardText.setTextColor(Color.WHITE);
        cardText.setTextSize(24);
        cardText.setPadding(40, 40, 40, 40);
        interactiveCard.addView(cardText);
        
        // Add touch listener for custom interactions
        interactiveCard.setOnTouchListener(new View.OnTouchListener() {
            private float initialCornerRadius;
            private float initialShadowRadius;
            private float initialX;
            private float initialY;
            
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Save initial values
                        initialCornerRadius = 40;
                        initialShadowRadius = 20;
                        initialX = event.getX();
                        initialY = event.getY();
                        return false;
                        
                    case MotionEvent.ACTION_MOVE:
                        // Calculate changes based on touch position
                        float deltaX = (event.getX() - initialX) / v.getWidth();
                        float deltaY = (event.getY() - initialY) / v.getHeight();
                        
                        // Update corner radius based on horizontal movement
                        float newCornerRadius = initialCornerRadius + (deltaX * 60);
                        interactiveCard.setCornerRadius((int) Math.max(10, newCornerRadius));
                        
                        // Update shadow radius based on vertical movement
                        float newShadowRadius = initialShadowRadius + (deltaY * 30);
                        interactiveCard.setShadowRadius(Math.max(5, newShadowRadius));
                        
                        // Update gradient angle based on touch position
                        float angle = (event.getX() / v.getWidth()) * 360f;
                        interactiveCard.setGradientAngle(angle);
                        
                        return true;
                        
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // Reset to initial values with smooth animation
                        ValueAnimator resetAnimator = ValueAnimator.ofFloat(0, 1);
                        resetAnimator.setDuration(300);
                        final float currentCornerRadius = interactiveCard.getCornerRadius();
                        final float currentShadowRadius = interactiveCard.getShadowRadius();
                        resetAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                float progress = (float) animation.getAnimatedValue();
                                float cornerRadius = currentCornerRadius + (initialCornerRadius - currentCornerRadius) * progress;
                                float shadowRadius = currentShadowRadius + (initialShadowRadius - currentShadowRadius) * progress;
                                interactiveCard.setCornerRadius((int) cornerRadius);
                                interactiveCard.setShadowRadius(shadowRadius);
                            }
                        });
                        resetAnimator.start();
                        return false;
                }
                return false;
            }
        });
        
        container.addView(interactiveCard);
        
        return container;
    }
    
    private View createSpacer() {
        View spacer = new View(this);
        spacer.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 20));
        return spacer;
    }
} 