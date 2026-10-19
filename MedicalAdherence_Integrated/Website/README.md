# Medical Adherence Website — Firebase sync

This PHP-rendered website uses the same Firebase project, Firebase Authentication, and Cloud Firestore paths as the Android app. It does not use SQLite or the retired PHP bearer-token API for authentication or app data.

## Shared Firestore schema

The Android app and website use:

- `users/{uid}`: `uid`, `fullName`, `email`, `phone`, `medicalCondition`, `createdAt`
- `users/{uid}/medications/{medicationDocId}`: `medicationId`, `medicationName`, `dosage`, `medicationTime`, `status`, `frequency`, `instructions`, `createdAt`
- `users/{uid}/adherence/{logId}`: `medicationId`, `status`, `date` (`yyyy-MM-dd` in local time), `loggedAt`
- `users/{uid}/notifications/{notificationId}`: `title`, `message`, `notificationDate` (milliseconds since epoch), `read`

Firebase Auth supplies the same UID to both clients. The website reads and writes only the signed-in user's `users/{uid}` document tree. Existing Firebase accounts and records remain where they are; no data migration or copying is needed.

## Firebase Console setup

1. Use Firebase project `medical-adherence-android-app` (the project configured in Android).
2. In **Authentication → Sign-in method**, enable **Email/Password** and **Google**.
3. In **Authentication → Settings → Authorized domains**, add the deployed website hostname (and `localhost` for local preview).
4. In **Firestore Database → Rules**, deploy the included `firestore.rules` from the project root. It allows each authenticated user to access only their own document tree. Never deploy open rules such as `allow read, write: if true`.
5. Ensure the Android package's SHA-1/SHA-256 fingerprints and OAuth client are configured for Google sign-in in Firebase/Google Cloud.

The `firebaseConfig` in `assets/js/firebase-sync.js` is the public Firebase Web app configuration you supplied. It belongs in browser code and is not an Admin SDK credential. **Never put a service-account JSON/private key in this website.**

## Run and deploy

Requirements: PHP 7.4+ and a web server. SQLite, Composer, PHP Google OAuth secrets, and a custom API server are no longer required. For local preview run `php -S 0.0.0.0:8000` from this directory and open `http://localhost:8000/`.

Deploy the included rules with Firebase CLI from the project directory after signing in and selecting the existing project:

```bash
firebase use --add
firebase deploy --only firestore:rules
```

Review the selected project before deploying. Add the website domain to Firebase Auth's authorized domains and enable the sign-in providers in the Firebase Console.

## Synced behavior

Website sign-up creates a Firebase Auth account and a `users/{uid}` profile. Email/password and Google sign-in use Firebase Auth. Profile edits, medication create/edit/delete, daily adherence logs, and notifications use the same nested Firestore collections and field shapes as Android. Password recovery uses Firebase Auth. The website reminder checker reads the signed-in user's medications and writes reminder notifications into their `notifications` collection.

The previous SQLite API, PHP Google OAuth callback, and SQLite reminder endpoints are retired. Firestore Security Rules are the access-control boundary for private health data; deploy and test them before production use. Use HTTPS and test with two separate accounts to confirm data isolation.
