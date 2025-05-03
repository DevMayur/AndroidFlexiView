package com.mayur.flexiview;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.SweepGradient;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

public class CustomDrawable extends Drawable {

    private final Paint paint;
    private final RectF rectF;
    private final Path path;
    private int backgroundColor;
    private int strokeColor;
    private int strokeWidth;
    private int cornerRadiusTopLeft;
    private int cornerRadiusTopRight;
    private int cornerRadiusBottomRight;
    private int cornerRadiusBottomLeft;
    private int[] gradientColors;
    private float[] gradientPositions;
    private float gradientAngle;
    private final Context context;
    private int blurColor;
    private float blurRadius;
    private boolean drawBackgroundColor = false;
    private boolean drawStrokeColor = false;
    private boolean drawGradient = false;
    private boolean drawBlur = false;
    private int gradientType = 0; // 0 for linear, 1 for radial, 2 for sweep
    private boolean reverseGradient = false;

    // Shadow properties
    private int shadowColor;
    private float shadowRadius;
    private float shadowDx;
    private float shadowDy;
    private boolean drawShadow = false;

    // Border style properties
    private int borderStyle = 0; // 0: solid, 1: dashed, 2: dotted
    private float dashWidth;
    private float dashGap;

    // Gradient center properties
    private float gradientCenterX = 0.5f;
    private float gradientCenterY = 0.5f;
    private float gradientRadius = 0f;

    // Ripple properties
    private int rippleColor;
    private boolean rippleEnabled = false;

    // State-based colors
    private int backgroundColorPressed;
    private int backgroundColorDisabled;
    private int strokeColorPressed;
    private int strokeColorDisabled;
    private boolean isPressed = false;
    private boolean isEnabled = true;

    // Animation
    private int animationDuration = 300;

    // Blur edge
    private boolean blurEdge = false;
    private float blurEdgeRadius = 0f;

    // Gradient spread
    private int gradientSpread = 0; // 0: pad, 1: repeat, 2: mirror

    // Corner style
    private int cornerStyle = 0; // 0: rounded, 1: cut

    // Border alignment
    private int borderAlignment = 1; // 0: inside, 1: center, 2: outside

    // Gradient interpolation
    private int gradientInterpolation = 0; // 0: linear, 1: accelerate, 2: decelerate

    // Blur quality
    private int blurQuality = 1; // 0: low, 1: medium, 2: high

    // State-based corner radius
    private int cornerRadiusPressed;
    private int cornerRadiusDisabled;

    public CustomDrawable(Context context) {
        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.rectF = new RectF();
        this.path = new Path();
        this.context = context;
        this.paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    public void draw(Canvas canvas) {
        rectF.set(getBounds());

        // Apply shadow if enabled
        if (drawShadow) {
            paint.setShadowLayer(shadowRadius, shadowDx, shadowDy, shadowColor);
        }

        float[] fillRadii = getCornerRadii();
        path.reset();
        
        if (cornerStyle == 0) { // Rounded corners
            path.addRoundRect(rectF, fillRadii, Path.Direction.CW);
        } else { // Cut corners
            createCutCornerPath(rectF, fillRadii);
        }

        // Draw gradient
        if (drawGradient) {
            Shader gradient = createGradient();
            paint.setShader(gradient);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawPath(path, paint);
            paint.setShader(null);
        }

        // Draw background color
        if (drawBackgroundColor) {
            paint.setColor(getCurrentBackgroundColor());
            paint.setStyle(Paint.Style.FILL);
            canvas.drawPath(path, paint);
        }

        // Draw blur if needed
        if (drawBlur) {
            paint.setMaskFilter(new BlurMaskFilter(blurRadius, BlurMaskFilter.Blur.INNER));
            paint.setColor(blurColor);
            canvas.drawPath(path, paint);
            paint.setMaskFilter(null);
        }

        // Draw stroke
        if (drawStrokeColor) {
            drawStroke(canvas);
        }

        // Clear shadow
        if (drawShadow) {
            paint.clearShadowLayer();
        }
    }

    private float[] getCornerRadii() {
        int currentCornerRadius = isPressed ? cornerRadiusPressed : 
                                (!isEnabled ? cornerRadiusDisabled : 0);
        
        if (currentCornerRadius > 0) {
            return new float[]{
                currentCornerRadius, currentCornerRadius,
                currentCornerRadius, currentCornerRadius,
                currentCornerRadius, currentCornerRadius,
                currentCornerRadius, currentCornerRadius
            };
        }
        
        return new float[]{
            cornerRadiusTopLeft, cornerRadiusTopLeft,
            cornerRadiusTopRight, cornerRadiusTopRight,
            cornerRadiusBottomRight, cornerRadiusBottomRight,
            cornerRadiusBottomLeft, cornerRadiusBottomLeft
        };
    }

    private void createCutCornerPath(RectF rect, float[] radii) {
        path.moveTo(rect.left + radii[0], rect.top);
        path.lineTo(rect.right - radii[2], rect.top);
        path.lineTo(rect.right, rect.top + radii[2]);
        path.lineTo(rect.right, rect.bottom - radii[4]);
        path.lineTo(rect.right - radii[4], rect.bottom);
        path.lineTo(rect.left + radii[6], rect.bottom);
        path.lineTo(rect.left, rect.bottom - radii[6]);
        path.lineTo(rect.left, rect.top + radii[0]);
        path.close();
    }

    private Shader createGradient() {
        int[] colors = gradientColors;
        float[] positions = gradientPositions;
        
        if (reverseGradient) {
            colors = new int[gradientColors.length];
            for (int i = 0; i < gradientColors.length; i++) {
                colors[i] = gradientColors[gradientColors.length - 1 - i];
            }
            
            if (positions != null) {
                positions = new float[gradientPositions.length];
                for (int i = 0; i < gradientPositions.length; i++) {
                    positions[i] = 1f - gradientPositions[gradientPositions.length - 1 - i];
                }
            }
        }

        Shader.TileMode tileMode = getTileMode();
        Interpolator interpolator = getInterpolator();

        switch (gradientType) {
            case 0: // Linear
                return new LinearGradient(
                    rectF.left, rectF.top,
                    (float) (rectF.left + Math.cos(Math.toRadians(gradientAngle)) * rectF.width()),
                    (float) (rectF.top + Math.sin(Math.toRadians(gradientAngle)) * rectF.height()),
                    colors, positions, tileMode);
            case 1: // Radial
                float centerX = rectF.left + rectF.width() * gradientCenterX;
                float centerY = rectF.top + rectF.height() * gradientCenterY;
                float radius = gradientRadius > 0 ? gradientRadius : 
                             Math.max(rectF.width(), rectF.height()) / 2f;
                return new android.graphics.RadialGradient(
                    centerX, centerY, radius,
                    colors, positions, tileMode);
            case 2: // Sweep
                return new SweepGradient(
                    rectF.centerX(), rectF.centerY(),
                    colors, positions);
            default:
                return null;
        }
    }

    private Shader.TileMode getTileMode() {
        switch (gradientSpread) {
            case 1: return Shader.TileMode.REPEAT;
            case 2: return Shader.TileMode.MIRROR;
            default: return Shader.TileMode.CLAMP;
        }
    }

    private Interpolator getInterpolator() {
        switch (gradientInterpolation) {
            case 1: return new AccelerateInterpolator();
            case 2: return new DecelerateInterpolator();
            default: return null;
        }
    }

    private void drawStroke(Canvas canvas) {
        Path strokePath = new Path();
        float halfStrokeWidth = strokeWidth / 2f;
        
        switch (borderAlignment) {
            case 0: // Inside
                rectF.inset(halfStrokeWidth, halfStrokeWidth);
                break;
            case 2: // Outside
                rectF.inset(-halfStrokeWidth, -halfStrokeWidth);
                break;
        }

        float[] strokeRadii = getCornerRadii();
        if (borderAlignment != 1) { // Not center
            for (int i = 0; i < strokeRadii.length; i++) {
                strokeRadii[i] -= halfStrokeWidth;
            }
        }

        if (cornerStyle == 0) {
            strokePath.addRoundRect(rectF, strokeRadii, Path.Direction.CW);
        } else {
            createCutCornerPath(rectF, strokeRadii);
        }

        paint.setColor(getCurrentStrokeColor());
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);

        if (borderStyle == 1) { // Dashed
            paint.setPathEffect(new android.graphics.DashPathEffect(
                new float[]{dashWidth, dashGap}, 0));
        } else if (borderStyle == 2) { // Dotted
            paint.setPathEffect(new android.graphics.DashPathEffect(
                new float[]{strokeWidth, strokeWidth}, 0));
        }

        canvas.drawPath(strokePath, paint);
        paint.setPathEffect(null);
    }

    private int getCurrentBackgroundColor() {
        if (!isEnabled) return backgroundColorDisabled;
        if (isPressed) return backgroundColorPressed;
        return backgroundColor;
    }

    private int getCurrentStrokeColor() {
        if (!isEnabled) return strokeColorDisabled;
        if (isPressed) return strokeColorPressed;
        return strokeColor;
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public void setBackgroundColor(int color) {
        this.backgroundColor = color;
        this.drawBackgroundColor = true;
        invalidateSelf();
    }

    public void setStrokeColor(int color) {
        this.strokeColor = color;
        this.drawStrokeColor = true;
        invalidateSelf();
    }

    public void setStrokeWidth(int width) {
        this.strokeWidth = width;
        invalidateSelf();
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadiusTopLeft = cornerRadius;
        this.cornerRadiusTopRight = cornerRadius;
        this.cornerRadiusBottomRight = cornerRadius;
        this.cornerRadiusBottomLeft = cornerRadius;
        invalidateSelf();
    }

    public void setCornerRadiusTopLeft(int cornerRadiusTopLeft) {
        this.cornerRadiusTopLeft = cornerRadiusTopLeft;
        invalidateSelf();
    }

    public void setCornerRadiusTopRight(int cornerRadiusTopRight) {
        this.cornerRadiusTopRight = cornerRadiusTopRight;
        invalidateSelf();
    }

    public void setCornerRadiusBottomRight(int cornerRadiusBottomRight) {
        this.cornerRadiusBottomRight = cornerRadiusBottomRight;
        invalidateSelf();
    }

    public void setCornerRadiusBottomLeft(int cornerRadiusBottomLeft) {
        this.cornerRadiusBottomLeft = cornerRadiusBottomLeft;
        invalidateSelf();
    }

    public void setGradientColors(int[] colors) {
        this.gradientColors = colors;
        this.drawGradient = true;
        invalidateSelf();
    }

    public void setGradientColors(int[] colors, float[] positions) {
        this.gradientColors = colors;
        this.gradientPositions = positions;
        this.drawGradient = true;
        invalidateSelf();
    }

    public void setGradientAngle(float angle) {
        this.gradientAngle = angle;
        invalidateSelf();
    }

    public void setBlurColor(int color) {
        this.blurColor = color;
        this.drawBlur = true;
        invalidateSelf();
    }

    public void setBlurRadius(float radius) {
        this.blurRadius = radius;
        invalidateSelf();
    }

    public void setGradientType(int type) {
        this.gradientType = type;
        invalidateSelf();
    }

    public void setReverseGradient(boolean reverse) {
        this.reverseGradient = reverse;
        invalidateSelf();
    }

    // New setter methods for all attributes
    public void setShadowColor(int color) {
        this.shadowColor = color;
        this.drawShadow = true;
        invalidateSelf();
    }

    public void setShadowRadius(float radius) {
        this.shadowRadius = radius;
        this.drawShadow = true;
        invalidateSelf();
    }

    public void setShadowOffset(float dx, float dy) {
        this.shadowDx = dx;
        this.shadowDy = dy;
        this.drawShadow = true;
        invalidateSelf();
    }

    public void setBorderStyle(int style) {
        this.borderStyle = style;
        invalidateSelf();
    }

    public void setDashWidth(float width) {
        this.dashWidth = width;
        invalidateSelf();
    }

    public void setDashGap(float gap) {
        this.dashGap = gap;
        invalidateSelf();
    }

    public void setGradientCenter(float x, float y) {
        this.gradientCenterX = x;
        this.gradientCenterY = y;
        invalidateSelf();
    }

    public void setGradientRadius(float radius) {
        this.gradientRadius = radius;
        invalidateSelf();
    }

    public void setRippleColor(int color) {
        this.rippleColor = color;
        this.rippleEnabled = true;
        invalidateSelf();
    }

    public void setRippleEnabled(boolean enabled) {
        this.rippleEnabled = enabled;
        invalidateSelf();
    }

    public void setStateColors(int normal, int pressed, int disabled) {
        this.backgroundColor = normal;
        this.backgroundColorPressed = pressed;
        this.backgroundColorDisabled = disabled;
        invalidateSelf();
    }

    public void setStrokeStateColors(int normal, int pressed, int disabled) {
        this.strokeColor = normal;
        this.strokeColorPressed = pressed;
        this.strokeColorDisabled = disabled;
        invalidateSelf();
    }

    public void setAnimationDuration(int duration) {
        this.animationDuration = duration;
        invalidateSelf();
    }

    public void setBlurEdge(boolean enabled, float radius) {
        this.blurEdge = enabled;
        this.blurEdgeRadius = radius;
        invalidateSelf();
    }

    public void setGradientSpread(int spread) {
        this.gradientSpread = spread;
        invalidateSelf();
    }

    public void setCornerStyle(int style) {
        this.cornerStyle = style;
        invalidateSelf();
    }

    public void setBorderAlignment(int alignment) {
        this.borderAlignment = alignment;
        invalidateSelf();
    }

    public void setGradientInterpolation(int interpolation) {
        this.gradientInterpolation = interpolation;
        invalidateSelf();
    }

    public void setBlurQuality(int quality) {
        this.blurQuality = quality;
        invalidateSelf();
    }

    public void setStateCornerRadius(int normal, int pressed, int disabled) {
        this.cornerRadiusTopLeft = normal;
        this.cornerRadiusPressed = pressed;
        this.cornerRadiusDisabled = disabled;
        invalidateSelf();
    }

    public void setPressed(boolean pressed) {
        this.isPressed = pressed;
        invalidateSelf();
    }

    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
        invalidateSelf();
    }

    public int getCornerRadius() {
        return cornerRadiusTopLeft;
    }
    
    public float getShadowRadius() {
        return shadowRadius;
    }
}