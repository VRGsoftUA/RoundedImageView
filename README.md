#### [HIRE US](http://vrgsoft.net/)
# RoundedImageView
[![](https://jitpack.io/v/VRGsoftUA/RoundedImageView.svg)](https://jitpack.io/#VRGsoftUA/RoundedImageView)

Library contains the rounded image view</br></br>
<img src="https://github.com/VRGsoftUA/RoundedImageView/blob/master/demo.png" width="270" height="480" />

# Usage
*For a working implementation, Have a look at the Sample Project - app*
1. Include the library as local library project.
```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
dependencies {
    implementation 'com.github.VRGsoftUA:RoundedImageView:1.0.0'
}
```
2. Customize your `RoundedImageView`

| Property | Type | Description |
| ------- | --- | --- |
| rivTopLeft | dimension | top left corner value |
| rivBottomLeft | dimension | bottom left corner value |
| rivTopRight | dimension | top right corner value |
| rivBottomRight | dimension | bottom right corner value |
| rivCorners | dimension | default corner value  |

#### Contributing
* Contributions are always welcome
* If you want a feature and can code, feel free to fork and add the change yourself and make a pull request
