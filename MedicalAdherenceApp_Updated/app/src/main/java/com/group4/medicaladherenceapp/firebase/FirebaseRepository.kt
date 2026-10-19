package com.group4.medicaladherenceapp.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.group4.medicaladherenceapp.models.Medication
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

 data class UserProfile(
    val uid: String = "",
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val medicalCondition: String = ""
)

class FirebaseRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private fun uid(): String = auth.currentUser?.uid ?: error("Please sign in again")
    private fun userDoc() = db.collection("users").document(uid())
    private fun medications() = userDoc().collection("medications")

    suspend fun register(email: String, password: String, fullName: String, phone: String, condition: String) {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val user = result.user ?: error("Account creation failed")
        userDoc().set(
            mapOf("uid" to user.uid, "fullName" to fullName, "email" to email,
                "phone" to phone, "medicalCondition" to condition, "createdAt" to System.currentTimeMillis()),
            SetOptions.merge()
        ).await()
    }

    suspend fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun loginWithGoogle(idToken: String, fullName: String, email: String) {
        val credential = com.google.firebase.auth.GoogleAuthProvider.getCredential(idToken, null)
        val user = auth.signInWithCredential(credential).await().user
            ?: error("Google sign-in failed")
        // Ensure Google accounts have the same Firestore profile document as email accounts.
        userDoc().set(
            mapOf(
                "uid" to user.uid,
                "fullName" to (user.displayName ?: fullName),
                "email" to (user.email ?: email),
                "phone" to (user.phoneNumber.orEmpty()),
                "medicalCondition" to "",
                "createdAt" to System.currentTimeMillis()
            ),
            SetOptions.merge()
        ).await()
    }

    suspend fun sendPasswordReset(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

    suspend fun getProfile(): UserProfile {
        val user = auth.currentUser ?: error("Please sign in again")
        val snapshot = userDoc().get().await()
        return UserProfile(
            uid = user.uid,
            fullName = snapshot.getString("fullName").orEmpty(),
            email = user.email.orEmpty(),
            phone = snapshot.getString("phone").orEmpty(),
            medicalCondition = snapshot.getString("medicalCondition").orEmpty()
        )
    }

    suspend fun updateProfile(fullName: String, phone: String, condition: String) {
        userDoc().set(mapOf("fullName" to fullName, "phone" to phone, "medicalCondition" to condition), SetOptions.merge()).await()
    }

    suspend fun addMedication(name: String, dosage: String, time: String): String {
        val ref = medications().document()
        val data = mapOf("medicationId" to ref.id, "medicationName" to name, "dosage" to dosage,
            "medicationTime" to time, "status" to "Pending", "frequency" to "Daily",
            "instructions" to "", "createdAt" to System.currentTimeMillis())
        db.runBatch { batch ->
            batch.set(ref, data)
            batch.set(userDoc().collection("notifications").document(), mapOf(
                "title" to "Medication added", "message" to "$name was added to your medication list",
                "notificationDate" to System.currentTimeMillis(), "read" to false
            ))
        }.await()
        return ref.id
    }

    suspend fun getMedications(): List<Medication> = medications().get().await().documents.map { doc ->
        Medication(doc.id, doc.getString("medicationName").orEmpty(), doc.getString("dosage").orEmpty(), doc.getString("medicationTime").orEmpty())
    }

    suspend fun getMedication(id: String): Medication? {
        val doc = medications().document(id).get().await()
        if (!doc.exists()) return null
        return Medication(doc.id, doc.getString("medicationName").orEmpty(), doc.getString("dosage").orEmpty(), doc.getString("medicationTime").orEmpty())
    }

    suspend fun updateMedication(id: String, name: String, dosage: String, time: String) {
        medications().document(id).update(mapOf("medicationName" to name, "dosage" to dosage, "medicationTime" to time)).await()
    }

    suspend fun deleteMedication(id: String) {
        medications().document(id).delete().await()
    }

    suspend fun logDose(medicationId: String, status: String) {
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        userDoc().collection("adherence").document().set(mapOf(
            "medicationId" to medicationId, "status" to status, "date" to date, "loggedAt" to System.currentTimeMillis()
        )).await()
    }

    suspend fun getTodayAdherence(): Pair<Int, Int> {
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        val logs = userDoc().collection("adherence").whereEqualTo("date", date).get().await().documents
        return logs.size to logs.count { it.getString("status").equals("Taken", ignoreCase = true) }
    }

    suspend fun addNotification(title: String, message: String) {
        userDoc().collection("notifications").document().set(mapOf(
            "title" to title, "message" to message, "notificationDate" to System.currentTimeMillis(), "read" to false
        )).await()
    }

    suspend fun getNotifications(): List<Map<String, Any?>> = userDoc().collection("notifications")
        .orderBy("notificationDate").get().await().documents.map { it.data.orEmpty() }

    fun signOut() = auth.signOut()
}
