[![](https://jitpack.io/v/DevMayur/AndroidFlexiView.svg)](https://jitpack.io/#DevMayur/AndroidFlexiView)# FlexiView
A Custom android view that supports wide range of customisations.

# DrawableRelativeLayout

`DrawableRelativeLayout` is a custom view for Android that extends `RelativeLayout`. It provides additional features such as custom backgrounds, strokes, corner radii, gradients, and optional padding controls. This custom view can be highly customized through XML layout files or programmatically.

## Features

- **Custom Background Color**: Set any color as the background.
- **Custom Stroke**: Define the color and width of the stroke.
- **Corner Radius**: Individual control over each corner's radius or a uniform radius for all.
- **Gradient Backgrounds**: Linear gradients with customizable colors and positions.
- **Padding Control**: Optional avoidance of padding addition when applying stroke width or corner radius.

## Setup

### Adding to your project


## Installation
#### Step 1. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:
```xml
  dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}
```

#### Step 2. Add the dependency
```xml
  dependencies {
	        implementation 'com.github.DevMayur:AndroidFlexiView:Tag'
	}
```

To integrate `DrawableRelativeLayout` into your Android project, include the custom view in your XML layout.

```xml
<com.mayur.flexiview.DrawableRelativeLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:custom_background_color="#FFFFFF"
    app:custom_stroke_color="#000000"
    app:custom_stroke_width="4dp"
    app:custom_corner_radius="10dp"
    ... />
```

## Attributes

You can customize `DrawableRelativeLayout` by setting the following attributes in your XML layout or programmatically:

- `custom_background_color`: Background color of the view.
- `custom_stroke_color`: Color of the stroke.
- `custom_stroke_width`: Width of the stroke.
- `custom_corner_radius_top_left`: Radius of the top-left corner.
- `custom_corner_radius_top_right`: Radius of the top-right corner.
- `custom_corner_radius_bottom_right`: Radius of the bottom-right corner.
- `custom_corner_radius_bottom_left`: Radius of the bottom-left corner.
- `custom_avoid_stroke_padding`: If true, avoids adding padding for the stroke width.
- `custom_avoid_radius_padding`: If true, avoids adding padding for corner radii.
- `custom_gradient_start_color`: Starting color of the gradient.
- `custom_gradient_end_color`: Ending color of the gradient.
- `custom_colors`: Array of colors for complex gradients.
- `custom_positions`: Array of positions corresponding to each color in the gradient.
- `custom_gradient_angle`: Angle for the gradient orientation.
- `custom_blur_radius`: Blur radius for the background.
- `custom_blur_color`: Color for the blur effect.

## Programmatic Usage

You can also configure `DrawableRelativeLayout` programmatically using the following methods:

```java
DrawableRelativeLayout drawableRelativeLayout = findViewById(R.id.your_view_id);
drawableRelativeLayout.setBackgroundColor(Color.parseColor("#FF0000"));
drawableRelativeLayout.setStrokeColor(Color.parseColor("#00FF00"));
drawableRelativeLayout.setStrokeWidth(5);
drawableRelativeLayout.setCornerRadius(20);
```

Adjust the properties of DrawableRelativeLayout programmatically to match your specific requirements. These methods provide a way to customize the view after it has been initialized.

## FlexiView

Contributions are welcome! If you have any improvements or suggestions, please feel free to submit a pull request or open an issue. We appreciate feedback from the community to help enhance the project.

## License

Specify your licensing information here. For open-source projects, it is crucial to outline how the code can be used and modified by others. Ensure that your license choice reflects your intentions for the use of your software.


This README section provides a clear outline of how to programmatically configure the custom view, how others can contribute to its development, and reminds you to specify the license under which your project is released. Adjust the content as needed to suit the specific context of your project or any additional instructions you might want to include.
