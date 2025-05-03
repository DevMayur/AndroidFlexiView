package com.mayur.flexiview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Example class demonstrating how to use StacksView with DrawableRelativeLayout
 */
public class StacksViewExample {

    /**
     * Create a new adapter for the StacksView that displays DrawableRelativeLayouts
     * with custom styling for each item
     */
    public static StacksView.StackAdapter createStackAdapter(final Context context, final String[] titles, final int[] colors) {
        return new StacksView.StackAdapter() {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                DrawableRelativeLayout view;
                
                if (convertView != null && convertView instanceof DrawableRelativeLayout) {
                    view = (DrawableRelativeLayout) convertView;
                } else {
                    view = new DrawableRelativeLayout(context);
                    
                    // Setup basic layout parameters
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                    view.setLayoutParams(params);
                    
                    // Add a TextView for displaying the title
                    TextView textView = new TextView(context);
                    textView.setId(View.generateViewId());
                    textView.setTextColor(Color.WHITE);
                    textView.setTextSize(24);
                    textView.setPadding(40, 40, 40, 40);
                    
                    DrawableRelativeLayout.LayoutParams textParams = new DrawableRelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    textParams.addRule(DrawableRelativeLayout.CENTER_IN_PARENT, DrawableRelativeLayout.TRUE);
                    view.addView(textView, textParams);
                }
                
                // Apply custom styling for this position
                int color = colors[position % colors.length];
                String title = titles[position % titles.length];
                
                // Set up the DrawableRelativeLayout with custom styling
                view.setCornerRadius(40);
                view.setBackgroundColor(color);
                view.setShadowColor(Color.parseColor("#80000000"));
                view.setShadowRadius(20);
                view.setShadowOffset(5, 10);
                
                // Apply a gradient overlay
                int startColor = color;
                int endColor = darkenColor(color, 0.7f);
                view.setGradientColors(new int[]{startColor, endColor});
                view.setGradientType(0); // Linear gradient
                view.setGradientAngle(90f); // Bottom to top
                
                // Set the title text
                TextView textView = (TextView) view.getChildAt(0);
                textView.setText(title);
                
                return view;
            }

            @Override
            public int getCount() {
                return Math.max(titles.length, colors.length);
            }
            
            /**
             * Utility method to darken a color
             */
            private int darkenColor(int color, float factor) {
                int a = Color.alpha(color);
                int r = Math.round(Color.red(color) * factor);
                int g = Math.round(Color.green(color) * factor);
                int b = Math.round(Color.blue(color) * factor);
                return Color.argb(a, 
                        Math.min(r, 255), 
                        Math.min(g, 255), 
                        Math.min(b, 255));
            }
        };
    }
    
    /**
     * Example of how to set up a StacksView in code
     */
    public static StacksView setupStacksView(Context context) {
        StacksView stacksView = new StacksView(context);
        
        // Configure stack appearance
        stacksView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        
        // Create sample data
        String[] titles = new String[]{
                "Minecraft",
                "Avengers",
                "Spider-Man",
                "Dune",
                "Star Wars"
        };
        
        int[] colors = new int[]{
                Color.parseColor("#4285F4"), // Blue
                Color.parseColor("#EA4335"), // Red
                Color.parseColor("#FBBC04"), // Yellow
                Color.parseColor("#34A853"), // Green
                Color.parseColor("#9C27B0")  // Purple
        };
        
        // Create and set the adapter
        StacksView.StackAdapter adapter = createStackAdapter(context, titles, colors);
        stacksView.setAdapter(adapter);
        
        // Optional: Add a listener for position changes
        stacksView.setOnStackChangedListener(new StacksView.OnStackChangedListener() {
            @Override
            public void onStackChanged(int position) {
                // Handle position change
                // Example: Log.d("StacksView", "Current position: " + position);
            }
        });
        
        return stacksView;
    }
} 