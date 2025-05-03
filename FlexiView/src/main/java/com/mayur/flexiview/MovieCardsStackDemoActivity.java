package com.mayur.flexiview;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Demo activity showcasing a real-world example of the StacksView
 * with beautiful movie cards
 */
public class MovieCardsStackDemoActivity extends Activity {

    private StacksView stacksView;
    private Button buttonPrevious;
    private Button buttonNext;
    private int currentPosition = 0;

    // Sample movie data
    private static final String[] MOVIE_TITLES = {
            "Minecraft: The Movie",
            "Avengers: Endgame",
            "Spider-Man: Far From Home",
            "Dune",
            "Star Wars: The Rise of Skywalker"
    };
    
    private static final String[] MOVIE_DESCRIPTIONS = {
            "When the Ender Dragon threatens the Overworld, a young miner and her friends must save their blocky universe.",
            "The Avengers must assemble once more to undo Thanos' devastating actions and restore balance to the universe.",
            "Peter Parker grapples with the consequences of his identity being revealed as he battles new threats in Europe.",
            "The son of a noble family is entrusted with the protection of the most valuable asset in the galaxy.",
            "The surviving members of the Resistance face the First Order once more in the final chapter of the Skywalker saga."
    };
    
    private static final String[] MOVIE_GENRES = {
            "Adventure, Animation, Family",
            "Action, Adventure, Sci-Fi",
            "Action, Adventure, Comedy",
            "Adventure, Drama, Sci-Fi",
            "Action, Adventure, Fantasy"
    };
    
    private static final String[] MOVIE_RATINGS = {
            "8.2",
            "8.4",
            "7.5",
            "8.0",
            "6.5"
    };
    
    private static final int[] MOVIE_COLORS = {
            Color.parseColor("#4285F4"),  // Blue for Minecraft
            Color.parseColor("#EA4335"),  // Red for Avengers
            Color.parseColor("#FBBC04"),  // Yellow for Spider-Man
            Color.parseColor("#34A853"),  // Green for Dune
            Color.parseColor("#9C27B0")   // Purple for Star Wars
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_cards_demo);
        
        // Find views
        stacksView = findViewById(R.id.stacks_view);
        buttonPrevious = findViewById(R.id.button_previous);
        buttonNext = findViewById(R.id.button_next);
        
        // Set up the adapter
        stacksView.setAdapter(new MovieCardsAdapter());
        
        // Set up navigation
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stacksView.showNext();
            }
        });
        
        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stacksView.showPrevious();
            }
        });
        
        // Set up position change listener
        stacksView.setOnStackChangedListener(new StacksView.OnStackChangedListener() {
            @Override
            public void onStackChanged(int position) {
                currentPosition = position;
                updateNavigationButtons();
            }
        });
        
        // Initial update
        updateNavigationButtons();
    }
    
    private void updateNavigationButtons() {
        int count = MOVIE_TITLES.length;
        
        // Update button text to show which movie is next/previous
        String prevTitle = MOVIE_TITLES[(currentPosition - 1 + count) % count];
        String nextTitle = MOVIE_TITLES[(currentPosition + 1) % count];
        
        buttonPrevious.setText("← " + prevTitle);
        buttonNext.setText(nextTitle + " →");
    }
    
    /**
     * Adapter for providing movie cards to the StacksView
     */
    private class MovieCardsAdapter extends StacksView.StackAdapter {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            DrawableRelativeLayout cardView;
            
            if (convertView != null && convertView instanceof DrawableRelativeLayout) {
                cardView = (DrawableRelativeLayout) convertView;
                // Clear any existing views
                cardView.removeAllViews();
            } else {
                cardView = new DrawableRelativeLayout(MovieCardsStackDemoActivity.this);
                
                // Set up basic layout parameters
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                cardView.setLayoutParams(params);
            }
            
            // Set up card styling
            int color = MOVIE_COLORS[position % MOVIE_COLORS.length];
            int darkerColor = darkenColor(color, 0.7f);
            
            cardView.setCornerRadius(40);
            cardView.setBackgroundColor(color);
            cardView.setShadowColor(Color.parseColor("#66000000"));
            cardView.setShadowRadius(30);
            cardView.setShadowOffset(5, 15);
            cardView.setGradientColors(new int[]{color, darkerColor});
            cardView.setGradientType(0); // Linear
            cardView.setGradientAngle(315f); // Diagonal gradient
            
            // Create content container
            DrawableRelativeLayout contentContainer = new DrawableRelativeLayout(MovieCardsStackDemoActivity.this);
            DrawableRelativeLayout.LayoutParams containerParams = new DrawableRelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            containerParams.setMargins(40, 80, 40, 40);
            contentContainer.setLayoutParams(containerParams);
            
            // Add title
            TextView titleView = new TextView(MovieCardsStackDemoActivity.this);
            titleView.setId(View.generateViewId());
            titleView.setText(MOVIE_TITLES[position]);
            titleView.setTextColor(Color.WHITE);
            titleView.setTextSize(28);
            titleView.setTypeface(null, Typeface.BOLD);
            titleView.setGravity(Gravity.CENTER);
            
            DrawableRelativeLayout.LayoutParams titleParams = new DrawableRelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            titleParams.addRule(DrawableRelativeLayout.ALIGN_PARENT_TOP);
            contentContainer.addView(titleView, titleParams);
            
            // Add fake movie poster
            DrawableRelativeLayout posterContainer = new DrawableRelativeLayout(MovieCardsStackDemoActivity.this);
            posterContainer.setId(View.generateViewId());
            posterContainer.setCornerRadius(20);
            posterContainer.setBackgroundColor(darkerColor);
            
            DrawableRelativeLayout.LayoutParams posterParams = new DrawableRelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 
                    600);
            posterParams.addRule(DrawableRelativeLayout.BELOW, titleView.getId());
            posterParams.setMargins(0, 20, 0, 0);
            contentContainer.addView(posterContainer, posterParams);
            
            // Add description section
            TextView descriptionView = new TextView(MovieCardsStackDemoActivity.this);
            descriptionView.setId(View.generateViewId());
            descriptionView.setText(MOVIE_DESCRIPTIONS[position]);
            descriptionView.setTextColor(Color.WHITE);
            descriptionView.setTextSize(16);
            
            DrawableRelativeLayout.LayoutParams descParams = new DrawableRelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            descParams.addRule(DrawableRelativeLayout.BELOW, posterContainer.getId());
            descParams.setMargins(0, 20, 0, 0);
            contentContainer.addView(descriptionView, descParams);
            
            // Add genre and rating
            DrawableRelativeLayout infoContainer = new DrawableRelativeLayout(MovieCardsStackDemoActivity.this);
            infoContainer.setId(View.generateViewId());
            
            DrawableRelativeLayout.LayoutParams infoParams = new DrawableRelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            infoParams.addRule(DrawableRelativeLayout.BELOW, descriptionView.getId());
            infoParams.setMargins(0, 20, 0, 0);
            contentContainer.addView(infoContainer, infoParams);
            
            // Add genres
            TextView genreView = new TextView(MovieCardsStackDemoActivity.this);
            genreView.setText(MOVIE_GENRES[position]);
            genreView.setTextColor(Color.WHITE);
            genreView.setAlpha(0.8f);
            genreView.setTextSize(14);
            
            DrawableRelativeLayout.LayoutParams genreParams = new DrawableRelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, 
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            genreParams.addRule(DrawableRelativeLayout.ALIGN_PARENT_LEFT);
            infoContainer.addView(genreView, genreParams);
            
            // Add rating
            DrawableRelativeLayout ratingContainer = new DrawableRelativeLayout(MovieCardsStackDemoActivity.this);
            ratingContainer.setBackgroundColor(Color.WHITE);
            ratingContainer.setCornerRadius(20);
            
            DrawableRelativeLayout.LayoutParams ratingContainerParams = new DrawableRelativeLayout.LayoutParams(
                    80, 
                    80);
            ratingContainerParams.addRule(DrawableRelativeLayout.ALIGN_PARENT_RIGHT);
            infoContainer.addView(ratingContainer, ratingContainerParams);
            
            TextView ratingView = new TextView(MovieCardsStackDemoActivity.this);
            ratingView.setText(MOVIE_RATINGS[position]);
            ratingView.setTextColor(color);
            ratingView.setTextSize(18);
            ratingView.setTypeface(null, Typeface.BOLD);
            ratingView.setGravity(Gravity.CENTER);
            
            DrawableRelativeLayout.LayoutParams ratingParams = new DrawableRelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 
                    ViewGroup.LayoutParams.MATCH_PARENT);
            ratingContainer.addView(ratingView, ratingParams);
            
            // Add the content container to the card
            cardView.addView(contentContainer);
            
            return cardView;
        }

        @Override
        public int getCount() {
            return MOVIE_TITLES.length;
        }
        
        // Utility to darken a color
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
    }
} 