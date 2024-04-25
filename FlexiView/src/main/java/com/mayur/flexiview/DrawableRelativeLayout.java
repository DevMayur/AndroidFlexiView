package com.mayur.flexiview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
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
                attrs, R.styleable.custom_view, defStyle, 0);

        try {
            if (a.hasValue(R.styleable.custom_view_custom_background_color)) {
                customDrawable.setBackgroundColor(a.getColor(R.styleable.custom_view_custom_background_color, Color.WHITE));
            }

            if (a.hasValue(R.styleable.custom_view_custom_stroke_color)) {
                customDrawable.setStrokeColor(a.getColor(R.styleable.custom_view_custom_stroke_color, Color.BLACK));
            }

            avoid_padding_with_stroke_width = a.getBoolean(R.styleable.custom_view_custom_avoid_stroke_padding, false);

            if (a.hasValue(R.styleable.custom_view_custom_stroke_width)) {
                customDrawable.setStrokeWidth(a.getDimensionPixelSize(R.styleable.custom_view_custom_stroke_width, 5));
                if (!avoid_padding_with_stroke_width) {
                    setPadding(
                            a.getDimensionPixelSize(R.styleable.custom_view_custom_stroke_width, 0),
                            a.getDimensionPixelSize(R.styleable.custom_view_custom_stroke_width, 0),
                            a.getDimensionPixelSize(R.styleable.custom_view_custom_stroke_width, 0),
                            a.getDimensionPixelSize(R.styleable.custom_view_custom_stroke_width, 0)
                    );
                }
            }

            int tl=0, tr=0, br=0, bl=0;
            if (a.hasValue(R.styleable.custom_view_custom_corner_radius_top_left)) {
                customDrawable.setCornerRadiusTopLeft(a.getDimensionPixelSize(R.styleable.custom_view_custom_corner_radius_top_left, 0));
                tl = a.getDimensionPixelSize(R.styleable.custom_view_custom_corner_radius_top_left, 0);
            }

            if (a.hasValue(R.styleable.custom_view_custom_corner_radius_top_right)) {
                customDrawable.setCornerRadiusTopRight(a.getDimensionPixelSize(R.styleable.custom_view_custom_corner_radius_top_right, 0));
                tr = a.getDimensionPixelSize(R.styleable.custom_view_custom_corner_radius_top_right, 0);
            }

            if (a.hasValue(R.styleable.custom_view_custom_corner_radius_bottom_right)) {
                customDrawable.setCornerRadiusBottomRight(a.getDimensionPixelSize(R.styleable.custom_view_custom_corner_radius_bottom_right, 0));
                br = a.getDimensionPixelSize(R.styleable.custom_view_custom_corner_radius_bottom_right, 0);
            }

            if (a.hasValue(R.styleable.custom_view_custom_corner_radius_bottom_left)) {
                customDrawable.setCornerRadiusBottomLeft(a.getDimensionPixelSize(R.styleable.custom_view_custom_corner_radius_bottom_left, 0));
                bl = a.getDimensionPixelSize(R.styleable.custom_view_custom_corner_radius_bottom_left, 0);
            }

            avoid_padding_with_corner_radius = a.getBoolean(R.styleable.custom_view_custom_avoid_radius_padding, false);

            if (a.hasValue(R.styleable.custom_view_custom_corner_radius)) {
                customDrawable.setCornerRadius(a.getDimensionPixelSize(R.styleable.custom_view_custom_corner_radius, 0));
                if (!avoid_padding_with_corner_radius) {
                    setPadding(
                            a.getDimensionPixelSize(R.styleable.custom_view_custom_corner_radius, 0),
                            a.getDimensionPixelSize(R.styleable.custom_view_custom_corner_radius, 0),
                            a.getDimensionPixelSize(R.styleable.custom_view_custom_corner_radius, 0),
                            a.getDimensionPixelSize(R.styleable.custom_view_custom_corner_radius, 0)
                    );
                }
            }

            if (tl != 0 || tr != 0 || br != 0|| bl != 0) {
                if (!avoid_padding_with_corner_radius) {
                    setPadding(tl, tr, br, bl);
                }
            }

            if (a.hasValue(R.styleable.custom_view_custom_gradient_start_color) && a.hasValue(R.styleable.custom_view_custom_gradient_end_color)) {
                int startColor = a.getColor(R.styleable.custom_view_custom_gradient_start_color, Color.RED);
                int endColor = a.getColor(R.styleable.custom_view_custom_gradient_end_color, Color.BLUE);
                customDrawable.setGradientColors(new int[]{startColor, endColor});
            }

            if (a.hasValue(R.styleable.custom_view_custom_colors) && a.hasValue(R.styleable.custom_view_custom_positions)) {
                int[] colors = getResources().getIntArray(a.getResourceId(R.styleable.custom_view_custom_colors, 0));
                String[] positionsString = getResources().getStringArray(a.getResourceId(R.styleable.custom_view_custom_positions, 0));
                float[] positions = new float[positionsString.length];
                for (int i = 0; i < positionsString.length; i++) {
                    positions[i] = Float.parseFloat(positionsString[i]);
                }
                customDrawable.setGradientColors(colors, positions);
            }

            if (a.hasValue(R.styleable.custom_view_custom_gradient_angle)) {
                customDrawable.setGradientAngle(a.getFloat(R.styleable.custom_view_custom_gradient_angle, 0));
            }

            if (a.hasValue(R.styleable.custom_view_custom_padding)) {
                setCustomPadding(a.getDimensionPixelSize(R.styleable.custom_view_custom_padding, 0));
            }

            if (a.hasValue(R.styleable.custom_view_custom_blur_radius)) {
                float radius = a.getDimension(R.styleable.custom_view_custom_blur_radius, 0f);
                customDrawable.setBlurRadius(radius);
            }

            if (a.hasValue(R.styleable.custom_view_custom_blur_color)) {
                int color = a.getColor(R.styleable.custom_view_custom_blur_color, Color.TRANSPARENT);
                customDrawable.setBlurColor(color);
            }

        } finally {
            a.recycle();
        }

        setBackground(customDrawable);
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

}