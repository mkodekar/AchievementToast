# AchievementToast

Alternative to show Toast or an alert message just inside your activity.

It follows any view group but wont display outside your Activity.

##Preview
![gif](https://github.com/mkodekar/AchievementToast/blob/master/achievementtoastlibrary/blobs/AchievementToast.gif)

##Credits

code-mc
https://github.com/code-mc/loadtoast

slightly based on the same concept.


##Dependency

Add this in your root gradle / in simple word the project level gradle file

under allprojects

```
    repositories {
        maven { url 'https://jitpack.io' }
    }
```
and in app level gradle file under dependencies

```
    compile 'com.github.mkodekar:AchievementToast:0.1'
```

Grab the above demo app from here :

[![Get it on Google Play](https://play.google.com/intl/en_us/badges/images/badge_new.png)](https://play.google.com/store/apps/details?id=com.merkmod.myapplication)

##Usage
Just use it as you use toast but unlike toast it has multiple method inferences / implementation
please refer to the sample app for more details.


## Contributing

Please fork this repository and contribute back using
[pull requests](https://github.com/mkodekar/AchievementToast/pulls).

Any contributions, large or small, major features, bug fixes, are welcomed and appreciated
but will be thoroughly reviewed .

## License

* [Apache Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

```
Copyright 2016 Mohhamad Rehan Kodekar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
