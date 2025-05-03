package com.mayur.flexiview;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Demo activity showing how to use the StacksView with DrawableRelativeLayout
 */
public class StacksViewDemoActivity extends Activity {

    private StacksView stacksView;
    private Button buttonPrevious;
    private Button buttonNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stacks_view_example);

        // Find views
        stacksView = findViewById(R.id.stacks_view);
        buttonPrevious = findViewById(R.id.button_previous);
        buttonNext = findViewById(R.id.button_next);

        // Set up the stack adapter
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

        // Create and set adapter
        StacksView.StackAdapter adapter = StacksViewExample.createStackAdapter(this, titles, colors);
        stacksView.setAdapter(adapter);

        // Set up navigation buttons
        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stacksView.showPrevious();
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stacksView.showNext();
            }
        });

        // Optional: Add a listener for position changes
        stacksView.setOnStackChangedListener(new StacksView.OnStackChangedListener() {
            @Override
            public void onStackChanged(int position) {
                // Example of updating UI based on position
                updateNavigationButtons(position, adapter.getCount());
            }
        });

        // Initial update of navigation buttons
        updateNavigationButtons(0, adapter.getCount());
    }

    /**
     * Update the state of navigation buttons based on current position
     */
    private void updateNavigationButtons(int position, int count) {
        // In this example, we're implementing circular navigation,
        // so buttons are always enabled. For linear navigation,
        // you could disable buttons at the ends.
        
        // Example of custom button styling based on position:
        buttonPrevious.setText("Previous (" + ((position - 1 + count) % count + 1) + ")");
        buttonNext.setText("Next (" + ((position + 1) % count + 1) + ")");
    }
} 