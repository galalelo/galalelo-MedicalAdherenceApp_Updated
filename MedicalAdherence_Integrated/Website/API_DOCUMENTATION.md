# Legacy API retired

The PHP REST API described in the earlier version of this project has been disabled. Both the website and Android app use Firebase Authentication and Cloud Firestore directly in Firebase project `medical-adherence-android-app`.

The shared Firestore paths and setup steps are documented in `README.md`. Firebase Auth supplies the user identity; Firestore Security Rules in the repository root restrict access to `users/{request.auth.uid}` and that user's nested collections. Do not re-enable the former SQLite/token API for app data.
