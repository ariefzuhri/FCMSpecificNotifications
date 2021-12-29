# FCM Specific Notifications

[![GitHub release][release-shield]][release-url]
[![MIT License][license-shield]][license-url]

This Android app demonstrates how you can **send notifications from one user to another using [Firebase Cloud Messaging](https://firebase.google.com/docs/cloud-messaging) (FCM)**. To send a notification, we need a user token as a recipient, which is stored in the database using the [Firebase Realtime Database](https://firebase.google.com/docs/database).

The user authentication is handled with a one-step [Google Sign-In](https://firebase.google.com/docs/auth/android/google-signin) powered by [Firebase Authentication](https://firebase.google.com/docs/auth).

## How it works
<a><img src="https://i.imgur.com/2HCvELE.png"/></a>

### How user tokens are stored
1. Each APK will generate one token.
2. When the user is authenticated, the token will be stored in the database by user ID.
3. Tokens are subject to change under certain conditions. So, we can add a mechanism to store tokens in the database periodically, for example, every time the application is opened.

### How the notifications are sent
1. Notifications are sent with a recipient token.
2. To get the recipient token, we just need to enter the user ID and the app will fetch the token by the user ID from the database.

## Download
Check out the [release page](https://github.com/ariefzuhri/FCMSpecificNotifications/releases) and download the latest apk.

## Tech-stack
- Android native with Kotlin
- Android Architecture Components, specifically [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) and [Material Components](https://material.io/develop/android)
- [Firebase Cloud Messaging](https://github.com/firebase/firebase-android-sdk), send messages and notifications to another device
- [Firebase Realtime Database](https://github.com/firebase/firebase-android-sdk), store and sync data between users in real-time
- [Firebase Authentication](https://github.com/firebase/firebase-android-sdk), secure user authentication systems
- [Google Sign-In for Android](https://developers.google.com/identity/sign-in/android), support for user authentication using Google account
- [Retrofit](https://github.com/square/retrofit), REST client framework
- [Gson](https://github.com/google/gson), parsing the JSON format
- [Chucker](https://github.com/ChuckerTeam/chucker), HTTP inspector

## Configuration
Firstly, clone this repository and import it into Android Studio (`git clone https://github.com/ariefzuhri/FCMSpecificNotifications.git`).

### Setup Firebase Project
1. Open the [Firebase Console](https://console.firebase.google.com/) and click `Add project` (or use an existing Firebase sandbox project).
2. Follow the instructions there to create a new Firebase project.
3. Go to `Project Settings` and at the bottom, click on the Android icon to register your Android app.
4. Follow the instructions there again and make sure the package name matches your app ID in the Android Studio project. It is also required to include a `debug signing certificate SHA-1` to support Google Sign-In in Firebase Authentication.
6. Download the `google-services.json` file and move it into Android app module root directory (`./app`).

### Setup Firebase Authentication
1. Go to `Authentication` and click the `Get started` button to enable it.
2. Enable `Google` in the `Sign-in method` section.

### Setup Firebase Realtime Database
1. Go to `Realtime Database` and click the `Create database` button to enable it.
2. Select `Start in test mode` as the `Security rules`

## 🤝 Support
Any contributions, issues, and feature requests are welcome.

Give a ⭐️ if you like this project.

## License
This project is licensed under the MIT License. See the [`LICENSE`](https://github.com/ariefzuhri/FCMSpecificNotifications/blob/master/LICENSE) file for details.

## Acknowledgments
This idea inspired by [KOD Dev](https://www.youtube.com/playlist?list=PLzLFqCABnRQftQQETzoVMuteXzNiXmnj8).

[release-shield]: https://img.shields.io/github/v/release/ariefzuhri/FCMSpecificNotifications?include_prereleases&style=for-the-badge
[release-url]: https://github.com/ariefzuhri/FCMSpecificNotifications/releases
[license-shield]: https://img.shields.io/github/license/ariefzuhri/FCMSpecificNotifications?style=for-the-badge
[license-url]: https://github.com/ariefzuhri/FCMSpecificNotifications/blob/master/LICENSE
