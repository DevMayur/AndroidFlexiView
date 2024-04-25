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

        float[] fillRadii = {
                cornerRadiusTopLeft, cornerRadiusTopLeft,
                cornerRadiusTopRight, cornerRadiusTopRight,
                cornerRadiusBottomRight, cornerRadiusBottomRight,
                cornerRadiusBottomLeft, cornerRadiusBottomLeft
        };
        path.reset();
        path.addRoundRect(rectF, fillRadii, Path.Direction.CW);

        // Draw gradient first
        if (drawGradient) {
            LinearGradient gradient = new LinearGradient(
                    rectF.left, rectF.top,
                    (float) (rectF.left + Math.cos(Math.toRadians(gradientAngle)) * rectF.width()),
                    (float) (rectF.top + Math.sin(Math.toRadians(gradientAngle)) * rectF.height()),
                    gradientColors, gradientPositions, Shader.TileMode.CLAMP);

            paint.setShader(gradient);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawPath(path, paint);
            paint.setShader(null); // Clear shader after drawing gradient
        }

        // Then draw background color
        if (drawBackgroundColor) {
            paint.setColor(backgroundColor);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawPath(path, paint);
        }

        // Draw blur if needed
        if (drawBlur) {
            paint.setMaskFilter(new BlurMaskFilter(blurRadius, BlurMaskFilter.Blur.INNER));
            paint.setColor(blurColor);
            canvas.drawPath(path, paint);
            paint.setMaskFilter(null); // Clear the mask filter after drawing
        }

        // Finally, draw stroke
        Path strokePath = new Path();
        float halfStrokeWidth = strokeWidth / 2f;
        rectF.inset(halfStrokeWidth, halfStrokeWidth);
        float[] strokeRadii = {
                cornerRadiusTopLeft - halfStrokeWidth, cornerRadiusTopLeft - halfStrokeWidth,
                cornerRadiusTopRight - halfStrokeWidth, cornerRadiusTopRight - halfStrokeWidth,
                cornerRadiusBottomRight - halfStrokeWidth, cornerRadiusBottomRight - halfStrokeWidth,
                cornerRadiusBottomLeft - halfStrokeWidth, cornerRadiusBottomLeft - halfStrokeWidth
        };
        strokePath.addRoundRect(rectF, strokeRadii, Path.Direction.CW);

        if (drawStrokeColor) {
            paint.setColor(strokeColor);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(strokeWidth);
            canvas.drawPath(strokePath, paint);
        }
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


}