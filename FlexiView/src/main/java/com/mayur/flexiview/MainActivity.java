package com.mayur.flexiview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Main demo activity that serves as a launcher for testing different
 * features of the FlexiView library
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Create a scrollable layout
        ScrollView scrollView = new ScrollView(this);
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(32, 32, 32, 32);
        
        // Add title
        TextView titleView = new TextView(this);
        titleView.setText("FlexiView Demo");
        titleView.setTextSize(24);
        titleView.setPadding(0, 0, 0, 32);
        mainLayout.addView(titleView);
        
        // Add description
        TextView descView = new TextView(this);
        descView.setText("Select a demo to explore different features of the FlexiView library:");
        descView.setPadding(0, 0, 0, 32);
        mainLayout.addView(descView);
        
        // Add button for DrawableRelativeLayout demo
        mainLayout.addView(createDemoButton("DrawableRelativeLayout Features", 
                "Explore various styling features like gradients, shadows, borders, etc.", 
                DrawableRelativeLayoutDemoActivity.class));
        
        // Add button for StacksView XML demo
        mainLayout.addView(createDemoButton("StacksView with XML Children", 
                "Stack of views declared directly in XML layout", 
                StacksViewXmlDemoActivity.class));
        
        // Add button for StacksView Adapter demo
        mainLayout.addView(createDemoButton("StacksView with Adapter", 
                "Stack of views created dynamically with adapter", 
                StacksViewDemoActivity.class));
        
        // Add button for Movie Cards demo
        mainLayout.addView(createDemoButton("Movie Cards Stack Demo", 
                "Movie-themed cards in a stack with images and descriptions", 
                MovieCardsStackDemoActivity.class));
        
        // Add button for Interactive Features demo
        mainLayout.addView(createDemoButton("Interactive Features", 
                "Ripple effects, state changes, and other interactive features", 
                InteractiveFeaturesDemoActivity.class));
        
        scrollView.addView(mainLayout);
        setContentView(scrollView);
    }
    
    /**
     * Create a demo button with title, description and click handler
     */
    private View createDemoButton(String title, String description, final Class<?> activityClass) {
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setPadding(16, 16, 16, 16);
        container.setBackgroundResource(android.R.drawable.btn_default);
        container.setClickable(true);
        
        // Add title
        TextView titleView = new TextView(this);
        titleView.setText(title);
        titleView.setTextSize(18);
        titleView.setPadding(0, 0, 0, 8);
        container.addView(titleView);
        
        // Add description
        TextView descView = new TextView(this);
        descView.setText(description);
        descView.setTextSize(14);
        container.addView(descView);
        
        // Add click listener
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, activityClass);
                startActivity(intent);
            }
        });
        
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 16);
        container.setLayoutParams(params);
        
        return container;
    }
} 