package com.mayur.flexiview;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Demo activity showcasing the various features of DrawableRelativeLayout
 */
public class DrawableRelativeLayoutDemoActivity extends Activity {

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
        titleView.setText("DrawableRelativeLayout Features");
        titleView.setTextSize(24);
        titleView.setPadding(0, 0, 0, 20);
        mainLayout.addView(titleView);
        
        // Add various feature demonstrations
        mainLayout.addView(createFeatureTitle("Basic Styling"));
        mainLayout.addView(createBasicStylingExamples());
        
        mainLayout.addView(createFeatureTitle("Gradient Types"));
        mainLayout.addView(createGradientExamples());
        
        mainLayout.addView(createFeatureTitle("Corners & Borders"));
        mainLayout.addView(createCornersAndBordersExamples());
        
        mainLayout.addView(createFeatureTitle("Shadows & Blur Effects"));
        mainLayout.addView(createShadowsAndBlurExamples());
        
        mainLayout.addView(createFeatureTitle("Interactive Features"));
        mainLayout.addView(createInteractiveExamples());
        
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
    
    private View createBasicStylingExamples() {
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        
        // Simple colored background
        DrawableRelativeLayout simpleBackground = new DrawableRelativeLayout(this);
        simpleBackground.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 200));
        simpleBackground.setBackgroundColor(Color.parseColor("#4285F4"));
        
        TextView simpleText = new TextView(this);
        simpleText.setText("Basic Background Color");
        simpleText.setTextColor(Color.WHITE);
        simpleText.setPadding(20, 20, 20, 20);
        simpleBackground.addView(simpleText);
        
        container.addView(simpleBackground);
        container.addView(createSpacer());
        
        // Background with stroke
        DrawableRelativeLayout strokeBackground = new DrawableRelativeLayout(this);
        strokeBackground.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 200));
        strokeBackground.setBackgroundColor(Color.WHITE);
        strokeBackground.setStrokeColor(Color.parseColor("#EA4335"));
        strokeBackground.setStrokeWidth(10);
        
        TextView strokeText = new TextView(this);
        strokeText.setText("Background with Stroke");
        strokeText.setPadding(20, 20, 20, 20);
        strokeBackground.addView(strokeText);
        
        container.addView(strokeBackground);
        
        return container;
    }
    
    private View createGradientExamples() {
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        
        // Linear gradient
        DrawableRelativeLayout linearGradient = new DrawableRelativeLayout(this);
        linearGradient.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 200));
        linearGradient.setGradientColors(new int[]{
                Color.parseColor("#4285F4"),
                Color.parseColor("#34A853")
        });
        linearGradient.setGradientType(0); // Linear
        linearGradient.setGradientAngle(0f);
        
        TextView linearText = new TextView(this);
        linearText.setText("Linear Gradient (Horizontal)");
        linearText.setTextColor(Color.WHITE);
        linearText.setPadding(20, 20, 20, 20);
        linearGradient.addView(linearText);
        
        container.addView(linearGradient);
        container.addView(createSpacer());
        
        // Radial gradient
        DrawableRelativeLayout radialGradient = new DrawableRelativeLayout(this);
        radialGradient.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 200));
        radialGradient.setGradientColors(new int[]{
                Color.parseColor("#EA4335"),
                Color.parseColor("#FBBC04")
        });
        radialGradient.setGradientType(1); // Radial
        
        TextView radialText = new TextView(this);
        radialText.setText("Radial Gradient");
        radialText.setTextColor(Color.WHITE);
        radialText.setPadding(20, 20, 20, 20);
        radialGradient.addView(radialText);
        
        container.addView(radialGradient);
        container.addView(createSpacer());
        
        // Sweep gradient
        DrawableRelativeLayout sweepGradient = new DrawableRelativeLayout(this);
        sweepGradient.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 200));
        sweepGradient.setGradientColors(new int[]{
                Color.parseColor("#4285F4"),
                Color.parseColor("#EA4335"),
                Color.parseColor("#FBBC04"),
                Color.parseColor("#34A853"),
                Color.parseColor("#4285F4")
        });
        sweepGradient.setGradientType(2); // Sweep
        
        TextView sweepText = new TextView(this);
        sweepText.setText("Sweep Gradient");
        sweepText.setTextColor(Color.WHITE);
        sweepText.setPadding(20, 20, 20, 20);
        sweepGradient.addView(sweepText);
        
        container.addView(sweepGradient);
        container.addView(createSpacer());
        
        // Gradient with custom center
        DrawableRelativeLayout customCenterGradient = new DrawableRelativeLayout(this);
        customCenterGradient.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 200));
        customCenterGradient.setGradientColors(new int[]{
                Color.parseColor("#FFFFFF"),
                Color.parseColor("#9C27B0")
        });
        customCenterGradient.setGradientType(1); // Radial
        customCenterGradient.setGradientCenter(0.25f, 0.25f);
        
        TextView customCenterText = new TextView(this);
        customCenterText.setText("Radial Gradient with Custom Center");
        customCenterText.setPadding(20, 20, 20, 20);
        customCenterGradient.addView(customCenterText);
        
        container.addView(customCenterGradient);
        
        return container;
    }
    
    private View createCornersAndBordersExamples() {
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        
        // Rounded corners
        DrawableRelativeLayout roundedCorners = new DrawableRelativeLayout(this);
        roundedCorners.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 200));
        roundedCorners.setBackgroundColor(Color.parseColor("#4285F4"));
        roundedCorners.setCornerRadius(40);
        
        TextView roundedText = new TextView(this);
        roundedText.setText("Rounded Corners");
        roundedText.setTextColor(Color.WHITE);
        roundedText.setPadding(20, 20, 20, 20);
        roundedCorners.addView(roundedText);
        
        container.addView(roundedCorners);
        container.addView(createSpacer());
        
        // Mixed corners
        DrawableRelativeLayout mixedCorners = new DrawableRelativeLayout(this);
        mixedCorners.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 200));
        mixedCorners.setBackgroundColor(Color.parseColor("#EA4335"));
        mixedCorners.setCornerRadiusTopLeft(60);
        mixedCorners.setCornerRadiusBottomRight(60);
        
        TextView mixedText = new TextView(this);
        mixedText.setText("Mixed Corners");
        mixedText.setTextColor(Color.WHITE);
        mixedText.setPadding(20, 20, 20, 20);
        mixedCorners.addView(mixedText);
        
        container.addView(mixedCorners);
        container.addView(createSpacer());
        
        // Dashed border
        DrawableRelativeLayout dashedBorder = new DrawableRelativeLayout(this);
        dashedBorder.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 200));
        dashedBorder.setBackgroundColor(Color.WHITE);
        dashedBorder.setStrokeColor(Color.parseColor("#34A853"));
        dashedBorder.setStrokeWidth(5);
        dashedBorder.setBorderStyle(1); // Dashed
        dashedBorder.setDashWidth(20);
        dashedBorder.setDashGap(10);
        
        TextView dashedText = new TextView(this);
        dashedText.setText("Dashed Border");
        dashedText.setPadding(20, 20, 20, 20);
        dashedBorder.addView(dashedText);
        
        container.addView(dashedBorder);
        container.addView(createSpacer());
        
        // Cut corners
        DrawableRelativeLayout cutCorners = new DrawableRelativeLayout(this);
        cutCorners.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 200));
        cutCorners.setBackgroundColor(Color.parseColor("#FBBC04"));
        cutCorners.setCornerRadius(40);
        cutCorners.setCornerStyle(1); // Cut
        
        TextView cutText = new TextView(this);
        cutText.setText("Cut Corners");
        cutText.setTextColor(Color.WHITE);
        cutText.setPadding(20, 20, 20, 20);
        cutCorners.addView(cutText);
        
        container.addView(cutCorners);
        
        return container;
    }
    
    private View createShadowsAndBlurExamples() {
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        
        // Shadow effect
        DrawableRelativeLayout shadow = new DrawableRelativeLayout(this);
        shadow.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 200));
        shadow.setBackgroundColor(Color.WHITE);
        shadow.setCornerRadius(20);
        shadow.setShadowColor(Color.parseColor("#66000000"));
        shadow.setShadowRadius(20);
        shadow.setShadowOffset(10, 10);
        
        TextView shadowText = new TextView(this);
        shadowText.setText("Shadow Effect");
        shadowText.setPadding(20, 20, 20, 20);
        shadow.addView(shadowText);
        
        container.addView(shadow);
        container.addView(createSpacer());
        
        // Inner blur
        DrawableRelativeLayout innerBlur = new DrawableRelativeLayout(this);
        innerBlur.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 200));
        innerBlur.setBackgroundColor(Color.parseColor("#4285F4"));
        innerBlur.setCornerRadius(20);
        innerBlur.setBlurColor(Color.parseColor("#80FFFFFF"));
        innerBlur.setBlurRadius(20);
        
        TextView blurText = new TextView(this);
        blurText.setText("Inner Blur Effect");
        blurText.setTextColor(Color.WHITE);
        blurText.setPadding(20, 20, 20, 20);
        innerBlur.addView(blurText);
        
        container.addView(innerBlur);
        container.addView(createSpacer());
        
        // Edge blur
        DrawableRelativeLayout edgeBlur = new DrawableRelativeLayout(this);
        edgeBlur.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 200));
        edgeBlur.setBackgroundColor(Color.parseColor("#34A853"));
        edgeBlur.setCornerRadius(20);
        edgeBlur.setBlurEdge(true, 30);
        
        TextView edgeBlurText = new TextView(this);
        edgeBlurText.setText("Edge Blur Effect");
        edgeBlurText.setTextColor(Color.WHITE);
        edgeBlurText.setPadding(20, 20, 20, 20);
        edgeBlur.addView(edgeBlurText);
        
        container.addView(edgeBlur);
        
        return container;
    }
    
    private View createInteractiveExamples() {
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        
        // Ripple effect
        final DrawableRelativeLayout ripple = new DrawableRelativeLayout(this);
        ripple.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 200));
        ripple.setBackgroundColor(Color.parseColor("#4285F4"));
        ripple.setCornerRadius(20);
        ripple.setRippleColor(Color.parseColor("#80FFFFFF"));
        ripple.setRippleEnabled(true);
        ripple.setClickable(true);
        
        TextView rippleText = new TextView(this);
        rippleText.setText("Tap for Ripple Effect");
        rippleText.setTextColor(Color.WHITE);
        rippleText.setPadding(20, 20, 20, 20);
        ripple.addView(rippleText);
        
        container.addView(ripple);
        container.addView(createSpacer());
        
        // State colors
        final DrawableRelativeLayout stateColors = new DrawableRelativeLayout(this);
        stateColors.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 200));
        stateColors.setStateColors(
                Color.parseColor("#4285F4"),  // Normal
                Color.parseColor("#EA4335"),  // Pressed
                Color.parseColor("#CCCCCC")   // Disabled
        );
        stateColors.setCornerRadius(20);
        stateColors.setClickable(true);
        
        TextView stateText = new TextView(this);
        stateText.setText("Tap to see state color change");
        stateText.setTextColor(Color.WHITE);
        stateText.setPadding(20, 20, 20, 20);
        stateColors.addView(stateText);
        
        container.addView(stateColors);
        container.addView(createSpacer());
        
        // Toggle button to enable/disable
        Button toggleButton = new Button(this);
        toggleButton.setText("Disable/Enable State Colors Demo");
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateColors.setEnabled(!stateColors.isEnabled());
            }
        });
        
        container.addView(toggleButton);
        
        return container;
    }
    
    private View createSpacer() {
        View spacer = new View(this);
        spacer.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 20));
        return spacer;
    }
} 