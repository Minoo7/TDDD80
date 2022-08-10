# TDDD80 Project
Vincent Garbrant - vinga129

## Android App

### Installation
Clone this repository and open android/Savolax as a project in **Android Studio**.

### Dependencies
Android Gradle Plugin Version: 7.1.2  
Gradle Version: 7.2

Android SDK: >= 30  
**App was designed with Pixel 4 screen layout**
<details>
<summary><ins>Gradle dependencies: (also specified in build.gradle file)</ins></summary>
<pre>implementation 'androidx.appcompat:appcompat:1.4.2'
implementation 'com.google.android.material:material:1.6.0'
implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.4.1'
implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1'
implementation 'androidx.navigation:navigation-fragment:2.4.2'
implementation 'androidx.navigation:navigation-ui:2.4.2'
implementation 'androidx.legacy:legacy-support-v4:1.0.0'
implementation 'androidx.databinding:databinding-runtime:7.2.0'
implementation 'androidx.annotation:annotation:1.3.0'
implementation 'com.google.android.gms:play-services-tasks:18.0.2'
testImplementation 'junit:junit:4.13.2'
androidTestImplementation 'androidx.test.ext:junit:1.1.3'
androidTestImplementation 'androidx.test.espresso:espresso-core:3.4.0'
implementation "androidx.navigation:navigation-fragment-ktx:2.5.0-rc01"
implementation "androidx.navigation:navigation-ui-ktx:2.5.0-rc01`

implementation 'com.google.android.gms:play-services-location:20.0.0'
implementation 'com.github.dexafree:materiallist:3.2.1'
implementation 'com.google.code.gson:gson:2.9.0'

implementation 'androidx.recyclerview:recyclerview:1.2.1'
implementation 'com.squareup.picasso:picasso:2.71828'

// Retrofit
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
implementation 'com.squareup.retrofit2:adapter-rxjava2:2.9.0'
implementation "com.squareup.okhttp3:okhttp:4.9.0"

implementation 'com.airbnb.android:paris:2.0.0'

implementation 'io.github.florent37:shapeofview:1.4.7'
implementation 'com.github.Redman1037:TSnackBar:V2.0.0'

implementation 'com.squareup.okhttp3:logging-interceptor:4.7.2'
implementation "io.reactivex.rxjava3:rxjava:3.1.4"
implementation 'io.reactivex.rxjava2:rxandroid:2.0.1'

// Camera
def camerax_version = "1.2.0-alpha03"
implementation "androidx.camera:camera-core:${camerax_version}"
implementation "androidx.camera:camera-camera2:${camerax_version}"
implementation "androidx.camera:camera-lifecycle:${camerax_version}"
implementation "androidx.camera:camera-video:${camerax_version}"

implementation "androidx.camera:camera-view:${camerax_version}"
implementation "androidx.camera:camera-extensions:${camerax_version}"`</pre>
</details>

## Python Server
The server for this project is hosted via heroku.com
#### Dependencies:
Python: >= 3.10

*Remaining dependencies are specified in requirements.txt*
### Remote setup
Setup account at heroku.com and upload this project.  
Use heroku postgresql add-on and setup the config var: `DATABASE_URL_TRUE`.  
Start server and connect app via url to the server.

### Local setup
Create virtual python environment and install requirements with: `pip -r requirements.txt`  
Open MyProject/server as a project in **PyCharm**.  
Run server via MyProject/server/run.py

## Coverage
A coverage report for the python server can be found under MyProject/server/coverage.  
Open index.html to view the report.
