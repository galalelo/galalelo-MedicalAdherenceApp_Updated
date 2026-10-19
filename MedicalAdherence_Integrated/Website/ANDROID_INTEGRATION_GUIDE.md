# Android and website Firebase integration

The Android app and PHP-rendered website share Firebase project `medical-adherence-android-app`, Firebase Auth UIDs, and Firestore collections. Both use `users/{uid}` with nested `medications`, `adherence`, and `notifications` collections. No PHP API base URL or app-wide API key is used.

Before testing, enable Email/Password and Google in Firebase Authentication, add the website hostname to Authorized domains, verify the Android package signing fingerprints for Google sign-in, and deploy the root `firestore.rules`. The website uses the Web app config in `assets/js/firebase-sync.js`; Android uses `app/google-services.json` and the configured Google Web OAuth client ID for its ID-token request.

Use the same existing Firebase Auth account in both clients. Both will then read and write the same UID's Firestore records. No SQLite migration is needed; data created only in the retired SQLite version is not in Firestore and is not automatically copied.
