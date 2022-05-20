# Image Picker
A Image Picker Library for Android (Supports Android 12) with fully customizable UI


# 🔥 Features
  * Pick gallery images
  * Customizable and Material Design UI
  * Supports Android 12


# 🔥 RoadMap
  * Capture in Camera Mode (Work in Progress)
  * Videos Picker (In Future)


# 🔧 Installation
In `settings.gradle` file, add JitPack maven like below:
```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Add the following dependency in app build.gradle:
```
dependencies {
    implementation 'com.github.nafiskabbo:ImagePicker:1.0'
}
```


# ⚡ Usage

Define an `ActivityResultLauncher` class variable in `Activity` or `Fragment`.
```java
private val launcher = registerImagePicker { images ->
    // Selected images are ready to use
    if(images.isNotEmpty()){
        val sampleImage = images[0]
        Glide.with(this@MainActivity)
             .load(sampleImage.uri)
             .into(imageView)
    }
}
```

Then, launch image picker when needed.
- With default configuration:
```java
launcher.launch()
```
- With customize configuration:
```java
val config = ImagePickerConfig(
    statusBarColor = "#000000",
    isLightStatusBar = false,
    isFolderMode = true,
    isMultipleMode = true,
    maxSize = 10,
    rootDirectory = Config.ROOT_DIR_DOWNLOAD,
    subDirectory = "Photos",
    folderGridCount = GridCount(2, 4),
    imageGridCount = GridCount(3, 5),
    // See more at configuration attributes table below
)

launcher.launch(config)
```

Configuration attributes
--------

| Name | Description | Default
| --- | --- | :---: |
| `statusBarColor` | Status bar color (require API >= 21) | `#000000`
| `isLightStatusBar` | Set status bar to light/dark mode to change it's content to dark/light (require API >= 21) | `false`
| `toolbarColor` | Toolbar color | `#212121`
| `toolbarTextColor` | Toolbar text color | `#FFFFFF`
| `toolbarIconColor` | Toolbar icon color | `#FFFFFF`
| `backgroundColor` | Background color | `#424242`
| `progressIndicatorColor` | Loading indicator color | `#009688`
| `selectedIndicatorColor` | Selected image's indicator color | `#1976D2`
| `isCameraOnly` (Work in Progress) | Open camera, then capture and return an image   | `false`
| `isMultipleMode` | Allow to select multiple images | `true`
| `isFolderMode` | Show images by folders | `false`
| `folderGridCount` | Set folder colums for portrait and landscape orientation | `GridCount(2, 4)`
| `imageGridCount` | Set image colums for portrait and landscape orientation | `GridCount(3, 5)`
| `doneTitle` | Done button title | `DONE`
| `folderTitle` | Toolbar title for folder mode (require FolderMode = `true`) | `Albums`
| `imageTitle` | Toolbar title for image mode (require FolderMode = `false`) | `Photos`
| `isShowCamera` (Work in Progress) | Show camera button | `true`
| `isShowNumberIndicator` | Show selected image's indicator as number | `false`
| `isAlwaysShowDoneButton` | Show done button even though no images've been selected yet | `false`
| `rootDirectory` | Public root directory of captured image, should be one of: `RootDirectory.DCIM`, `RootDirectory.PICTURES`, `RootDirectory.DOWNLOADS`. | `RootDirectory.DCIM`
| `subDirectory` | Root directory's sub folder of captured image | Application name
| `maxSize` | Max images can be selected | `Int.MAX_VALUE`
| `limitMessage` | Message to be displayed when total selected images exceeds max size | ...
| `selectedImages` | List of images that will be shown as selected in ImagePicker | Empty list


# 💥 Compatibility

  * Library - Android Marshmallow 5.0+ (API 21)

# ✔️Changelog

### Version: 1.0

  * Initial Build

## 📃 Libraries Used
* Glide [https://github.com/bumptech/glide] - Version 4.12.0

## ✨ Contributors
<table>
    <tr>
        <td align="center">
            <a href="https://github.com/nafiskabbo">
                <img src="https://avatars.githubusercontent.com/u/69830273?v=4" width="100px;" alt=""/><br />
                <sub><b>nafiskabbo</b></sub>
            </a>
        </td>
    </tr>
</table>


## License
Copyright (c) 2021 Nafis Islam Kabbo
