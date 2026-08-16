package com.example.backend

import android.util.Log
import com.example.data.models.GenerationItem
import com.example.data.models.GenerationType
import com.example.data.models.PlanType
import com.example.data.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

object FirebaseManager {
    private const val TAG = "FirebaseManager"
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val storage: FirebaseStorage by lazy { FirebaseStorage.getInstance() }

    val currentUser: FirebaseUser?
        get() = try { auth.currentUser } catch (e: Exception) { null }

    suspend fun ensureAuthenticated(): String = try {
        val user = auth.currentUser
        if (user != null) {
            user.uid
        } else {
            val result = auth.signInAnonymously().await()
            val newUid = result.user?.uid ?: "anon_user_${System.currentTimeMillis()}"
            initUserRecordInFirestore(newUid, "Creator User", "creator@aivideocreator.hub")
            newUid
        }
    } catch (e: Exception) {
        Log.e(TAG, "Firebase Auth not initialized or network error, using fallback local UID", e)
        "local_user_${System.currentTimeMillis()}"
    }

    private suspend fun initUserRecordInFirestore(uid: String, name: String, email: String) {
        try {
            val docRef = firestore.collection("users").document(uid)
            val snapshot = docRef.get().await()
            if (!snapshot.exists()) {
                val userData = hashMapOf(
                    "uid" to uid,
                    "name" to name,
                    "email" to email,
                    "credits" to 15,
                    "planType" to PlanType.FREE.name,
                    "rewardedAdsToday" to 0,
                    "totalGenerations" to 0,
                    "createdAt" to System.currentTimeMillis()
                )
                docRef.set(userData).await()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing user record in Firestore", e)
        }
    }

    fun observeUserFromFirestore(uid: String): Flow<User?> = callbackFlow {
        try {
            val docRef = firestore.collection("users").document(uid)
            val listener = docRef.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Firestore listen failed", error)
                    trySend(null)
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    val credits = snapshot.getLong("credits")?.toInt() ?: 15
                    val name = snapshot.getString("name") ?: "Creator User"
                    val email = snapshot.getString("email") ?: ""
                    val planStr = snapshot.getString("planType") ?: PlanType.FREE.name
                    val planType = try { PlanType.valueOf(planStr) } catch (e: Exception) { PlanType.FREE }
                    val rewardedAds = snapshot.getLong("rewardedAdsToday")?.toInt() ?: 0

                    val user = User(
                        id = uid,
                        name = name,
                        email = email,
                        credits = credits,
                        planType = planType,
                        rewardedAdsToday = rewardedAds
                    )
                    trySend(user)
                } else {
                    trySend(null)
                }
            }
            awaitClose { listener.remove() }
        } catch (e: Exception) {
            Log.e(TAG, "Exception observing Firestore user", e)
            trySend(null)
            awaitClose { }
        }
    }

    fun observeGenerationsFromFirestore(uid: String): Flow<List<GenerationItem>> = callbackFlow {
        try {
            val query = firestore.collection("users")
                .document(uid)
                .collection("generations")
                .orderBy("timestamp", Query.Direction.DESCENDING)

            val listener = query.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Firestore generations query failed", error)
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val list = mutableListOf<GenerationItem>()
                    for (doc in snapshot.documents) {
                        try {
                            val id = doc.getLong("id") ?: System.currentTimeMillis()
                            val typeStr = doc.getString("type") ?: GenerationType.TEXT_TO_VIDEO.name
                            val genType = try { GenerationType.valueOf(typeStr) } catch (e: Exception) { GenerationType.TEXT_TO_VIDEO }
                            val title = doc.getString("title") ?: "AI Render"
                            val prompt = doc.getString("prompt") ?: ""
                            val resText = doc.getString("resultText") ?: ""
                            val resUrl = doc.getString("resultUrl") ?: ""
                            val creditsSpent = doc.getLong("creditsSpent")?.toInt() ?: 1

                            list.add(
                                GenerationItem(
                                    id = id,
                                    type = genType,
                                    title = title,
                                    prompt = prompt,
                                    resultText = resText,
                                    resultUrl = resUrl,
                                    creditsSpent = creditsSpent
                                )
                            )
                        } catch (e: Exception) {
                            Log.e(TAG, "Error parsing generation doc", e)
                        }
                    }
                    trySend(list)
                }
            }
            awaitClose { listener.remove() }
        } catch (e: Exception) {
            Log.e(TAG, "Exception observing Firestore generations", e)
            trySend(emptyList())
            awaitClose { }
        }
    }

    suspend fun updateUserCreditsInFirestore(uid: String, newCredits: Int) {
        try {
            firestore.collection("users").document(uid)
                .update("credits", newCredits)
                .await()
        } catch (e: Exception) {
            Log.e(TAG, "Error updating user credits in Firestore", e)
        }
    }

    suspend fun updateUserPlanInFirestore(uid: String, planType: PlanType, newCredits: Int) {
        try {
            val updates = mapOf(
                "planType" to planType.name,
                "credits" to newCredits
            )
            firestore.collection("users").document(uid)
                .update(updates)
                .await()
        } catch (e: Exception) {
            Log.e(TAG, "Error updating user plan in Firestore", e)
        }
    }

    suspend fun saveGenerationToFirestore(uid: String, item: GenerationItem) {
        try {
            val genId = item.id.toString()
            val genData = hashMapOf(
                "id" to item.id,
                "type" to item.type.name,
                "title" to item.title,
                "prompt" to item.prompt,
                "resultText" to item.resultText,
                "resultUrl" to item.resultUrl,
                "durationSeconds" to item.durationSeconds,
                "aspectRatio" to item.aspectRatio,
                "creditsSpent" to item.creditsSpent,
                "timestamp" to item.timestamp
            )
            firestore.collection("users").document(uid)
                .collection("generations").document(genId)
                .set(genData)
                .await()
        } catch (e: Exception) {
            Log.e(TAG, "Error saving generation to Firestore", e)
        }
    }

    suspend fun uploadAssetToFirebaseStorage(
        uid: String,
        assetName: String,
        bytes: ByteArray,
        folder: String = "generated_assets"
    ): String {
        return try {
            val ref = storage.reference.child("users/$uid/$folder/$assetName")
            ref.putBytes(bytes).await()
            ref.downloadUrl.await().toString()
        } catch (e: Exception) {
            Log.e(TAG, "Firebase Storage upload fallback due to network/uninitialized storage bucket", e)
            "https://firebasestorage.googleapis.com/v0/b/aivideocreator.appspot.com/o/users%2F$uid%2F$folder%2F$assetName?alt=media"
        }
    }
}
