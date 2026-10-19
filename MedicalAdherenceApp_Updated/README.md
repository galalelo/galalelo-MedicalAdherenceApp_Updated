# Medical Adherence Android App — Firebase backend

Native Android/Kotlin app using Firebase Authentication and Cloud Firestore instead of the previous PHP/MySQL API.

## Firebase features integrated

- **Firebase Authentication:** email/password and Google SSO sign-in, persistent signed-in routing, sign-out, and password reset email.
- **Cloud Firestore:** user profiles, medication create/read/update/delete, adherence event records, and notification records.
- Local Android medication alarms continue to use the existing `AlarmScheduler` and notification receiver. The test-notification action also saves a Firestore notification.
- Firestore document paths are scoped to the authenticated Firebase UID; medication IDs are Firestore document IDs.

## Connect this Android app to your Firebase project

This source archive does not contain a Firebase project configuration file because that file is unique to your Firebase project. To connect it:

1. Create or open a project in the [Firebase Console](https://console.firebase.google.com/).
2. Add an **Android app** with package/application ID `com.group4.medicaladherenceapp`.
3. Download the generated `google-services.json` and place it at `app/google-services.json` (next to `app/build.gradle`). Do not commit this file to a public repository.
4. In Firebase Console → **Authentication** → **Sign-in method**, enable **Email/Password**.
   To enable Google SSO, enable the **Google** provider in the same section, add your app's SHA-1 signing certificate fingerprint under Project settings → Your apps → Android app, and save the changes.
   Then download the refreshed `google-services.json` and replace `app/google-services.json`. Configure `app/src/main/res/values/strings.xml` → `google_web_client_id` with the **Web application** OAuth client ID (not the Android client ID) shown in Google Cloud Console → APIs & Services → Credentials. The supplied Firebase config currently has no OAuth clients, so Google sign-in will not work until these project-specific values are set.
5. Create a **Cloud Firestore** database. Start in production/locked mode, then publish the rules from `firestore.rules` in this project.
6. Open the project root in Android Studio and sync Gradle. The Google Services Gradle plugin is already configured.

Do not use permissive test rules (for example, allow all reads/writes). The app stores health-related profile information; restrict access and review applicable privacy obligations before production.

## Firestore data shape

```text
users/{firebaseUid}
  uid, fullName, email, phone, medicalCondition, createdAt
  medications/{autoId}
    medicationId, medicationName, dosage, medicationTime, status,
    frequency, instructions, createdAt
  adherence/{autoId}
    medicationId, status, date (yyyy-MM-dd), loggedAt
  notifications/{autoId}
    title, message, notificationDate, read
```

Every account can read/write only its own `users/{uid}` document and nested collections under the included `firestore.rules` rules. The app does not migrate records from the former PHP/MySQL service; existing backend records need an explicit migration if they must be retained.

## Build and test

Open the project root (the directory containing `settings.gradle`) in Android Studio, add `app/google-services.json`, sync, then run on an emulator/device. This archive has no Gradle wrapper and the execution sandbox has no Android SDK, so a complete Android build/device test cannot be performed here.

Test account registration, password reset, login persistence/logout, editing a profile, medication CRUD, test notifications, and Firestore security with two separate accounts. No server/API key or PHP backend is required for the app after Firebase setup.
