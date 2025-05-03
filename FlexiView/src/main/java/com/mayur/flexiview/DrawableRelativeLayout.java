package com.mayur.flexiview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class DrawableRelativeLayout extends RelativeLayout {

    CustomDrawable customDrawable;
    boolean avoid_padding_with_stroke_width = false;
    boolean avoid_padding_with_corner_radius = false;

    public DrawableRelativeLayout(Context context) {
        this(context, null);
    }

    public DrawableRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawableRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        customDrawable = new CustomDrawable(context);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.DrawableRelativeLayout, defStyle, 0);

        try {
            if (a.hasValue(R.styleable.DrawableRelativeLayout_backgroundColor)) {
                customDrawable.setBackgroundColor(a.getColor(R.styleable.DrawableRelativeLayout_backgroundColor, Color.WHITE));
            }

            if (a.hasValue(R.styleable.DrawableRelativeLayout_strokeColor)) {
                customDrawable.setStrokeColor(a.getColor(R.styleable.DrawableRelativeLayout_strokeColor, Color.BLACK));
            }

            avoid_padding_with_stroke_width = a.getBoolean(R.styleable.DrawableRelativeLayout_avoidStrokePadding, false);

            if (a.hasValue(R.styleable.DrawableRelativeLayout_strokeWidth)) {
                customDrawable.setStrokeWidth(a.getDimensionPixelSize(R.styleable.DrawableRelativeLayout_strokeWidth, 5));
                if (!avoid_padding_with_stroke_width) {
                    setPadding(
                            a.getDimensionPixelSize(R.styleable.DrawableRelativeLayout_strokeWidth, 0),
                            a.getDimensionPixelSize(R.styleable.DrawableRelativeLayout_strokeWidth, 0),
                            a.getDimensionPixelSize(R.styleable.DrawableRelativeLayout_strokeWidth, 0),
                            a.getDimensionPixelSize(R.styleable.DrawableRelativeLayout_strokeWidth, 0)
                    );
                }
            }

            handleCornerRadius(a);

            handleGradient(a);

            if (a.hasValue(R.styleable.DrawableRelativeLayout_shadowColor)) {
                customDrawable.setShadowColor(a.getColor(R.styleable.DrawableRelativeLayout_shadowColor, Color.BLACK));
            }
            if (a.hasValue(R.styleable.DrawableRelativeLayout_shadowRadius)) {
                customDrawable.setShadowRadius(a.getDimension(R.styleable.DrawableRelativeLayout_shadowRadius, 0f));
            }
            if (a.hasValue(R.styleable.DrawableRelativeLayout_shadowDx) || a.hasValue(R.styleable.DrawableRelativeLayout_shadowDy)) {
                float dx = a.getDimension(R.styleable.DrawableRelativeLayout_shadowDx, 0f);
                float dy = a.getDimension(R.styleable.DrawableRelativeLayout_shadowDy, 0f);
                customDrawable.setShadowOffset(dx, dy);
            }

            if (a.hasValue(R.styleable.DrawableRelativeLayout_borderStyle)) {
                customDrawable.setBorderStyle(a.getInteger(R.styleable.DrawableRelativeLayout_borderStyle, 0));
            }
            if (a.hasValue(R.styleable.DrawableRelativeLayout_dashWidth)) {
                customDrawable.setDashWidth(a.getDimension(R.styleable.DrawableRelativeLayout_dashWidth, 0f));
            }
            if (a.hasValue(R.styleable.DrawableRelativeLayout_dashGap)) {
                customDrawable.setDashGap(a.getDimension(R.styleable.DrawableRelativeLayout_dashGap, 0f));
            }

            if (a.hasValue(R.styleable.DrawableRelativeLayout_gradientCenterX) || a.hasValue(R.styleable.DrawableRelativeLayout_gradientCenterY)) {
                float centerX = a.getFloat(R.styleable.DrawableRelativeLayout_gradientCenterX, 0.5f);
                float centerY = a.getFloat(R.styleable.DrawableRelativeLayout_gradientCenterY, 0.5f);
                customDrawable.setGradientCenter(centerX, centerY);
            }
            if (a.hasValue(R.styleable.DrawableRelativeLayout_gradientRadius)) {
                customDrawable.setGradientRadius(a.getDimension(R.styleable.DrawableRelativeLayout_gradientRadius, 0f));
            }

            if (a.hasValue(R.styleable.DrawableRelativeLayout_rippleColor)) {
                customDrawable.setRippleColor(a.getColor(R.styleable.DrawableRelativeLayout_rippleColor, Color.WHITE));
            }
            if (a.hasValue(R.styleable.DrawableRelativeLayout_rippleEnabled)) {
                customDrawable.setRippleEnabled(a.getBoolean(R.styleable.DrawableRelativeLayout_rippleEnabled, false));
            }

            if (a.hasValue(R.styleable.DrawableRelativeLayout_backgroundColorPressed) ||
                a.hasValue(R.styleable.DrawableRelativeLayout_backgroundColorDisabled)) {
                int normal = a.getColor(R.styleable.DrawableRelativeLayout_backgroundColor, Color.WHITE);
                int pressed = a.getColor(R.styleable.DrawableRelativeLayout_backgroundColorPressed, normal);
                int disabled = a.getColor(R.styleable.DrawableRelativeLayout_backgroundColorDisabled, normal);
                customDrawable.setStateColors(normal, pressed, disabled);
            }

            if (a.hasValue(R.styleable.DrawableRelativeLayout_strokeColorPressed) ||
                a.hasValue(R.styleable.DrawableRelativeLayout_strokeColorDisabled)) {
                int normal = a.getColor(R.styleable.DrawableRelativeLayout_strokeColor, Color.BLACK);
                int pressed = a.getColor(R.styleable.DrawableRelativeLayout_strokeColorPressed, normal);
                int disabled = a.getColor(R.styleable.DrawableRelativeLayout_strokeColorDisabled, normal);
                customDrawable.setStrokeStateColors(normal, pressed, disabled);
            }

            if (a.hasValue(R.styleable.DrawableRelativeLayout_animationDuration)) {
                customDrawable.setAnimationDuration(a.getInteger(R.styleable.DrawableRelativeLayout_animationDuration, 300));
            }

            if (a.hasValue(R.styleable.DrawableRelativeLayout_blurEdge)) {
                boolean enabled = a.getBoolean(R.styleable.DrawableRelativeLayout_blurEdge, false);
                float radius = a.getDimension(R.styleable.DrawableRelativeLayout_blurEdgeRadius, 0f);
                customDrawable.setBlurEdge(enabled, radius);
            }

            if (a.hasValue(R.styleable.DrawableRelativeLayout_gradientSpread)) {
                customDrawable.setGradientSpread(a.getInteger(R.styleable.DrawableRelativeLayout_gradientSpread, 0));
            }

            if (a.hasValue(R.styleable.DrawableRelativeLayout_cornerStyle)) {
                customDrawable.setCornerStyle(a.getInteger(R.styleable.DrawableRelativeLayout_cornerStyle, 0));
            }

            if (a.hasValue(R.styleable.DrawableRelativeLayout_borderAlignment)) {
                customDrawable.setBorderAlignment(a.getInteger(R.styleable.DrawableRelativeLayout_borderAlignment, 1));
            }

            if (a.hasValue(R.styleable.DrawableRelativeLayout_gradientInterpolation)) {
                customDrawable.setGradientInterpolation(a.getInteger(R.styleable.DrawableRelativeLayout_gradientInterpolation, 0));
            }

            if (a.hasValue(R.styleable.DrawableRelativeLayout_blurQuality)) {
                customDrawable.setBlurQuality(a.getInteger(R.styleable.DrawableRelativeLayout_blurQuality, 1));
            }

            if (a.hasValue(R.styleable.DrawableRelativeLayout_cornerRadiusPressed) ||
                a.hasValue(R.styleable.DrawableRelativeLayout_cornerRadiusDisabled)) {
                int normal = a.getDimensionPixelSize(R.styleable.DrawableRelativeLayout_cornerRadius, 0);
                int pressed = a.getDimensionPixelSize(R.styleable.DrawableRelativeLayout_cornerRadiusPressed, normal);
                int disabled = a.getDimensionPixelSize(R.styleable.DrawableRelativeLayout_cornerRadiusDisabled, normal);
                customDrawable.setStateCornerRadius(normal, pressed, disabled);
            }

        } finally {
            a.recycle();
        }

        setBackground(customDrawable);
    }

    private void handleCornerRadius(TypedArray a) {
        int tl=0, tr=0, br=0, bl=0;
        if (a.hasValue(R.styleable.DrawableRelativeLayout_cornerRadiusTopLeft)) {
            customDrawable.setCornerRadiusTopLeft(a.getDimensionPixelSize(R.styleable.DrawableRelativeLayout_cornerRadiusTopLeft, 0));
            tl = a.getDimensionPixelSize(R.styleable.DrawableRelativeLayout_cornerRadiusTopLeft, 0);
        }

        if (a.hasValue(R.styleable.DrawableRelativeLayout_cornerRadiusTopRight)) {
            customDrawable.setCornerRadiusTopRight(a.getDimensionPixelSize(R.styleable.DrawableRelativeLayout_cornerRadiusTopRight, 0));
            tr = a.getDimensionPixelSize(R.styleable.DrawableRelativeLayout_cornerRadiusTopRight, 0);
        }

        if (a.hasValue(R.styleable.DrawableRelativeLayout_cornerRadiusBottomRight)) {
            customDrawable.setCornerRadiusBottomRight(a.getDimensionPixelSize(R.styleable.DrawableRelativeLayout_cornerRadiusBottomRight, 0));
            br = a.getDimensionPixelSize(R.styleable.DrawableRelativeLayout_cornerRadiusBottomRight, 0);
        }

        if (a.hasValue(R.styleable.DrawableRelativeLayout_cornerRadiusBottomLeft)) {
            customDrawable.setCornerRadiusBottomLeft(a.getDimensionPixelSize(R.styleable.DrawableRelativeLayout_cornerRadiusBottomLeft, 0));
            bl = a.getDimensionPixelSize(R.styleable.DrawableRelativeLayout_cornerRadiusBottomLeft, 0);
        }

        avoid_padding_with_corner_radius = a.getBoolean(R.styleable.DrawableRelativeLayout_avoidRadiusPadding, false);

        if (a.hasValue(R.styleable.DrawableRelativeLayout_cornerRadius)) {
            customDrawable.setCornerRadius(a.getDimensionPixelSize(R.styleable.DrawableRelativeLayout_cornerRadius, 0));
            if (!avoid_padding_with_corner_radius) {
                setPadding(
                        a.getDimensionPixelSize(R.styleable.DrawableRelativeLayout_cornerRadius, 0),
                        a.getDimensionPixelSize(R.styleable.DrawableRelativeLayout_cornerRadius, 0),
                        a.getDimensionPixelSize(R.styleable.DrawableRelativeLayout_cornerRadius, 0),
                        a.getDimensionPixelSize(R.styleable.DrawableRelativeLayout_cornerRadius, 0)
                );
            }
        }

        if (tl != 0 || tr != 0 || br != 0|| bl != 0) {
            if (!avoid_padding_with_corner_radius) {
                setPadding(tl, tr, br, bl);
            }
        }
    }

    private void handleGradient(TypedArray a) {
        if (a.hasValue(R.styleable.DrawableRelativeLayout_gradientStartColor) && a.hasValue(R.styleable.DrawableRelativeLayout_gradientEndColor)) {
            int startColor = a.getColor(R.styleable.DrawableRelativeLayout_gradientStartColor, Color.RED);
            int endColor = a.getColor(R.styleable.DrawableRelativeLayout_gradientEndColor, Color.BLUE);
            customDrawable.setGradientColors(new int[]{startColor, endColor});
        }

        if (a.hasValue(R.styleable.DrawableRelativeLayout_colors) && a.hasValue(R.styleable.DrawableRelativeLayout_positions)) {
            int[] colors = getResources().getIntArray(a.getResourceId(R.styleable.DrawableRelativeLayout_colors, 0));
            String[] positionsString = getResources().getStringArray(a.getResourceId(R.styleable.DrawableRelativeLayout_positions, 0));
            float[] positions = new float[positionsString.length];
            for (int i = 0; i < positionsString.length; i++) {
                positions[i] = Float.parseFloat(positionsString[i]);
            }
            customDrawable.setGradientColors(colors, positions);
        }

        if (a.hasValue(R.styleable.DrawableRelativeLayout_gradientAngle)) {
            customDrawable.setGradientAngle(a.getFloat(R.styleable.DrawableRelativeLayout_gradientAngle, 0));
        }

        if (a.hasValue(R.styleable.DrawableRelativeLayout_gradientType)) {
            customDrawable.setGradientType(a.getInteger(R.styleable.DrawableRelativeLayout_gradientType, 0));
        }

        if (a.hasValue(R.styleable.DrawableRelativeLayout_reverseGradient)) {
            customDrawable.setReverseGradient(a.getBoolean(R.styleable.DrawableRelativeLayout_reverseGradient, false));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isClickable()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    customDrawable.setPressed(true);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    customDrawable.setPressed(false);
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        customDrawable.setEnabled(enabled);
    }

    public void setBackgroundColor(int color) {
        customDrawable.setBackgroundColor(color);
        invalidate();
    }

    public void setStrokeColor(int color) {
        customDrawable.setStrokeColor(color);
        invalidate();
    }

    public void setStrokeWidth(int width) {
        customDrawable.setStrokeWidth(width);
        setPadding(width, width, width, width);
        invalidate();
    }

    public void setCornerRadiusTopLeft(int radius) {
        customDrawable.setCornerRadiusTopLeft(radius);
        if (!avoid_padding_with_corner_radius) {
            setPadding(radius, radius, getPaddingRight(), getPaddingBottom());
        }
        invalidate();
    }

    public void setCornerRadiusTopRight(int radius) {
        customDrawable.setCornerRadiusTopRight(radius);
        if (!avoid_padding_with_corner_radius) {
            setPadding(getPaddingLeft(), radius, radius, getPaddingBottom());
        }
        invalidate();
    }

    public void setCornerRadiusBottomRight(int radius) {
        customDrawable.setCornerRadiusBottomRight(radius);
        if (!avoid_padding_with_corner_radius) {
            setPadding(getPaddingLeft(), getPaddingTop(), radius, radius);
        }
        invalidate();
    }

    public void setCornerRadiusBottomLeft(int radius) {
        customDrawable.setCornerRadiusBottomLeft(radius);
        if (!avoid_padding_with_corner_radius) {
            setPadding(radius, getPaddingTop(), getPaddingRight(), radius);
        }
        invalidate();
    }

    public void setCornerRadius(int radius) {
        customDrawable.setCornerRadius(radius);
        invalidate();
    }

    public void     setGradientColors(int[] colors) {
        customDrawable.setGradientColors(colors);
        invalidate();
    }

    public void setGradientColors(int[] colors, float[] positions) {
        customDrawable.setGradientColors(colors, positions);
        invalidate();
    }

    public void setCustomPadding(int padding) {
        setPadding(padding,padding,padding,padding);
        invalidate();
    }

    public void setBlurColor(int color) {
        customDrawable.setBlurColor(color);
        invalidate();
    }

    public void setBlurRadius(float radius) {
        customDrawable.setBlurRadius(radius);
        invalidate();
    }

    public void avoidPaddingStroke(boolean avoidPaddingStroke) {
        this.avoid_padding_with_stroke_width = avoidPaddingStroke;
        invalidate();
    }

    public void avoidRadiusStroke(boolean avoidPaddingRadius) {
        this.avoid_padding_with_corner_radius = avoidPaddingRadius;
        invalidate();
    }

    public void setGradientType(int type) {
        customDrawable.setGradientType(type);
        invalidate();
    }

    public void setReverseGradient(boolean reverse) {
        customDrawable.setReverseGradient(reverse);
        invalidate();
    }

    public void setShadowColor(int color) {
        customDrawable.setShadowColor(color);
        invalidate();
    }

    public void setShadowRadius(float radius) {
        customDrawable.setShadowRadius(radius);
        invalidate();
    }

    public void setShadowOffset(float dx, float dy) {
        customDrawable.setShadowOffset(dx, dy);
        invalidate();
    }

    public void setBorderStyle(int style) {
        customDrawable.setBorderStyle(style);
        invalidate();
    }

    public void setDashWidth(float width) {
        customDrawable.setDashWidth(width);
        invalidate();
    }

    public void setDashGap(float gap) {
        customDrawable.setDashGap(gap);
        invalidate();
    }

    public void setGradientCenter(float x, float y) {
        customDrawable.setGradientCenter(x, y);
        invalidate();
    }

    public void setGradientRadius(float radius) {
        customDrawable.setGradientRadius(radius);
        invalidate();
    }

    public void setRippleColor(int color) {
        customDrawable.setRippleColor(color);
        invalidate();
    }

    public void setRippleEnabled(boolean enabled) {
        customDrawable.setRippleEnabled(enabled);
        invalidate();
    }

    public void setStateColors(int normal, int pressed, int disabled) {
        customDrawable.setStateColors(normal, pressed, disabled);
        invalidate();
    }

    public void setStrokeStateColors(int normal, int pressed, int disabled) {
        customDrawable.setStrokeStateColors(normal, pressed, disabled);
        invalidate();
    }

    public void setAnimationDuration(int duration) {
        customDrawable.setAnimationDuration(duration);
        invalidate();
    }

    public void setBlurEdge(boolean enabled, float radius) {
        customDrawable.setBlurEdge(enabled, radius);
        invalidate();
    }

    public void setGradientSpread(int spread) {
        customDrawable.setGradientSpread(spread);
        invalidate();
    }

    public void setCornerStyle(int style) {
        customDrawable.setCornerStyle(style);
        invalidate();
    }

    public void setBorderAlignment(int alignment) {
        customDrawable.setBorderAlignment(alignment);
        invalidate();
    }

    public void setGradientInterpolation(int interpolation) {
        customDrawable.setGradientInterpolation(interpolation);
        invalidate();
    }

    public void setBlurQuality(int quality) {
        customDrawable.setBlurQuality(quality);
        invalidate();
    }

    public void setStateCornerRadius(int normal, int pressed, int disabled) {
        customDrawable.setStateCornerRadius(normal, pressed, disabled);
        invalidate();
    }
}