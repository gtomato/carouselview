# CarouselView

**Current Version: 2.0.0**

A wonderful library to display 2D fancy carousels for Android.

![TimeMachineViewTransformer Demo](https://gtomato.github.io/carouselview/media/timemachine.gif) ![FlatMerryGoRoundTransformer Demo](https://gtomato.github.io/carouselview/media/merrygoround-flat.gif)

Please read [the website](https://gtomato.github.io/carouselview/) for more information.


## Installation

Install via Gradle:
```groovy
compile 'com.gt.android.library:carouselview:<version>'
```


## Usage

Layout XML:

```xml
	<com.gt.android.ui.widget.CarouselView
		android:id="@+id/carousel"
		android:layout_width="match_parent"
		android:layout_height="150dp"
		android:layout_centerInParent="true"/>
```

Config:

```java
carousel.setTransformer(new FlatMerryGoRoundTransformer());
carousel.setAdapter(new MyDataAdapter());
```


## License

    Copyright (c) 2017 Green Tomato Limited
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
