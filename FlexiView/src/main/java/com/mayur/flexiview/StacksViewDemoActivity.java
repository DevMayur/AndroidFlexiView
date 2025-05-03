package com.mayur.flexiview;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Demo activity showing how to use the StacksView with DrawableRelativeLayout
 */
public class StacksViewDemoActivity extends AppCompatActivity {

    private StacksView stacksView;
    private Button buttonPrevious;
    private Button buttonNext;

    private String[] demoItems = {
            "Card 1", "Card 2", "Card 3", "Card 4",
            "Card 5", "Card 6", "Card 7", "Card 8"
    };

    private int[] demoColors = {
            Color.parseColor("#FF5722"), // Deep Orange
            Color.parseColor("#E91E63"), // Pink
            Color.parseColor("#9C27B0"), // Purple
            Color.parseColor("#673AB7"), // Deep Purple
            Color.parseColor("#3F51B5"), // Indigo
            Color.parseColor("#2196F3"), // Blue
            Color.parseColor("#03A9F4"), // Light Blue
            Color.parseColor("#00BCD4")  // Cyan
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stacks_view_demo);

        // Find views
        stacksView = findViewById(R.id.stacks_view);
        buttonPrevious = findViewById(R.id.button_previous);
        buttonNext = findViewById(R.id.button_next);

        // Set up the adapter
        stacksView.setAdapter(new DemoAdapter());

        // Set up navigation buttons
        buttonPrevious.setOnClickListener(v -> stacksView.showPrevious());
        buttonNext.setOnClickListener(v -> stacksView.showNext());
    }

    private class DemoAdapter extends StacksView.StackAdapter {
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                convertView = inflater.inflate(R.layout.item_stack_card, parent, false);
            }
            
            TextView textView = convertView.findViewById(R.id.text_card);
            textView.setText(demoItems[position]);
            convertView.setBackgroundColor(demoColors[position]);
            
            // Make top card clickable
            final int currentPos = position;
            convertView.setOnClickListener(v -> {
                if (currentPos == stacksView.getCurrentPosition()) {
                    // Handle top card click
                    // For example: Toast.makeText(StacksViewDemoActivity.this, "Clicked card " + (currentPos + 1), Toast.LENGTH_SHORT).show();
                }
            });
            
            return convertView;
        }

        @Override
        public int getCount() {
            return demoItems.length;
        }
    }
} 