package com.mayur.flexiview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Demo activity showing how to use the StacksView with DrawableRelativeLayout
 * children declared directly in XML
 */
public class StacksViewXmlDemoActivity extends Activity {

    private StacksView stacksView;
    private Button buttonPrevious;
    private Button buttonNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stacks_view_xml_example);

        // Find views
        stacksView = findViewById(R.id.stacks_view);
        buttonPrevious = findViewById(R.id.button_previous);
        buttonNext = findViewById(R.id.button_next);

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
                // Update UI based on position change
                updateNavigationButtons(position);
            }
        });

        // Initial update of navigation buttons
        updateNavigationButtons(0);
    }

    /**
     * Update the state of navigation buttons based on current position
     */
    private void updateNavigationButtons(int position) {
        // The childCount will be the total number of cards in our stack
        int childCount = stacksView.getChildCount();
        
        // Update the button text to show which card is next/previous
        buttonPrevious.setText("Previous (" + ((position - 1 + childCount) % childCount + 1) + ")");
        buttonNext.setText("Next (" + ((position + 1) % childCount + 1) + ")");
    }
} 