package com.example.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.backend.FirebaseManager
import com.example.data.db.AppDatabase
import com.example.data.models.AiCharacter
import com.example.data.models.ContentIdea
import com.example.data.models.GenerationItem
import com.example.data.models.LoginType
import com.example.data.models.PlanType
import com.example.data.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class AppRepository(private val context: Context) {
    private val db = AppDatabase.getDatabase(context)
    private val dao = db.generationDao()
    private val characterDao = db.aiCharacterDao()
    private val productAdDao = db.productAdDao()

    companion object {
        private val KEY_CREDITS = intPreferencesKey("user_credits")
        private val KEY_PLAN_TYPE = stringPreferencesKey("user_plan")
        private val KEY_USER_NAME = stringPreferencesKey("user_name")
        private val KEY_USER_EMAIL = stringPreferencesKey("user_email")
        private val KEY_LOGIN_TYPE = stringPreferencesKey("login_type")
        private val KEY_REWARDED_ADS_TODAY = intPreferencesKey("rewarded_ads_today")
        private val KEY_LAST_RESET_TIME = longPreferencesKey("last_reset_time")
    }

    val userFlow: Flow<User> = context.dataStore.data.map { prefs ->
        val credits = prefs[KEY_CREDITS] ?: 20
        val planStr = prefs[KEY_PLAN_TYPE] ?: PlanType.FREE.name
        val name = prefs[KEY_USER_NAME] ?: "AI Creator"
        val email = prefs[KEY_USER_EMAIL] ?: "creator@aivideo.hub"
        val loginTypeStr = prefs[KEY_LOGIN_TYPE] ?: LoginType.GUEST.name
        val adsToday = prefs[KEY_REWARDED_ADS_TODAY] ?: 0
        val lastReset = prefs[KEY_LAST_RESET_TIME] ?: System.currentTimeMillis()

        val plan = try { PlanType.valueOf(planStr) } catch (e: Exception) { PlanType.FREE }
        val loginType = try { LoginType.valueOf(loginTypeStr) } catch (e: Exception) { LoginType.GUEST }

        val currentTime = System.currentTimeMillis()
        val finalAdsToday = if (currentTime - lastReset > 24 * 60 * 60 * 1000) 0 else adsToday

        User(
            id = "user_1",
            email = email,
            name = name,
            loginType = loginType,
            credits = credits,
            planType = plan,
            rewardedAdsToday = finalAdsToday,
            lastRewardedAdResetTime = lastReset,
            isAdmin = true
        )
    }

    val allGenerations: Flow<List<GenerationItem>> = dao.getAllGenerations()
    val allContentIdeas: Flow<List<ContentIdea>> = dao.getAllContentIdeas()
    val allCharacters: Flow<List<AiCharacter>> = characterDao.getAllCharacters()
    val allProductAds: Flow<List<com.example.data.models.ProductAd>> = productAdDao.getAllProductAds()

    suspend fun saveProductAd(ad: com.example.data.models.ProductAd) {
        productAdDao.insertAd(ad)
    }

    suspend fun getProductAdById(id: String): com.example.data.models.ProductAd? {
        return productAdDao.getAdById(id)
    }

    suspend fun deleteProductAd(id: String) {
        productAdDao.deleteAd(id)
    }

    suspend fun saveCharacter(character: AiCharacter) {
        characterDao.insertCharacter(character)
    }

    suspend fun getCharacterById(id: String): AiCharacter? {
        return characterDao.getCharacterById(id)
    }

    suspend fun deleteCharacter(id: String) {
        characterDao.deleteCharacter(id)
    }

    suspend fun loginUser(name: String, email: String, loginType: LoginType) {
        context.dataStore.edit { prefs ->
            prefs[KEY_USER_NAME] = name
            prefs[KEY_USER_EMAIL] = email
            prefs[KEY_LOGIN_TYPE] = loginType.name
        }
        val uid = FirebaseManager.ensureAuthenticated()
        FirebaseManager.updateUserCreditsInFirestore(uid, 20)
    }

    suspend fun logoutUser() {
        context.dataStore.edit { prefs ->
            prefs[KEY_USER_NAME] = "Guest Creator"
            prefs[KEY_USER_EMAIL] = "guest@aivideo.hub"
            prefs[KEY_LOGIN_TYPE] = LoginType.GUEST.name
        }
    }

    suspend fun deductCredits(amount: Int): Boolean {
        var success = false
        var remainingCredits = 0
        context.dataStore.edit { prefs ->
            val currentCredits = prefs[KEY_CREDITS] ?: 20
            val planStr = prefs[KEY_PLAN_TYPE] ?: PlanType.FREE.name
            val plan = try { PlanType.valueOf(planStr) } catch (e: Exception) { PlanType.FREE }

            if (plan != PlanType.FREE) {
                success = true
                remainingCredits = currentCredits
            } else if (currentCredits >= amount) {
                remainingCredits = currentCredits - amount
                prefs[KEY_CREDITS] = remainingCredits
                success = true
            } else {
                success = false
            }
        }

        if (success) {
            val uid = FirebaseManager.currentUser?.uid ?: FirebaseManager.ensureAuthenticated()
            FirebaseManager.updateUserCreditsInFirestore(uid, remainingCredits)
        }
        return success
    }

    suspend fun addCredits(amount: Int) {
        var newTotal = 0
        context.dataStore.edit { prefs ->
            val currentCredits = prefs[KEY_CREDITS] ?: 20
            newTotal = currentCredits + amount
            prefs[KEY_CREDITS] = newTotal
        }
        val uid = FirebaseManager.currentUser?.uid ?: FirebaseManager.ensureAuthenticated()
        FirebaseManager.updateUserCreditsInFirestore(uid, newTotal)
    }

    suspend fun completeRewardedAd(): Boolean {
        var rewardEarned = false
        var updatedCredits = 0
        context.dataStore.edit { prefs ->
            val adsToday = prefs[KEY_REWARDED_ADS_TODAY] ?: 0
            val lastReset = prefs[KEY_LAST_RESET_TIME] ?: System.currentTimeMillis()
            val currentTime = System.currentTimeMillis()

            val effectiveAdsToday = if (currentTime - lastReset > 24 * 60 * 60 * 1000) {
                prefs[KEY_LAST_RESET_TIME] = currentTime
                0
            } else {
                adsToday
            }

            if (effectiveAdsToday < 5) {
                prefs[KEY_REWARDED_ADS_TODAY] = effectiveAdsToday + 1
                val currentCredits = prefs[KEY_CREDITS] ?: 20
                updatedCredits = currentCredits + 2
                prefs[KEY_CREDITS] = updatedCredits
                rewardEarned = true
            }
        }

        if (rewardEarned) {
            val uid = FirebaseManager.currentUser?.uid ?: FirebaseManager.ensureAuthenticated()
            FirebaseManager.updateUserCreditsInFirestore(uid, updatedCredits)
        }
        return rewardEarned
    }

    suspend fun updatePlan(planType: PlanType) {
        var newCredits = 20
        context.dataStore.edit { prefs ->
            prefs[KEY_PLAN_TYPE] = planType.name
            if (planType != PlanType.FREE) {
                newCredits = if (planType == PlanType.YEARLY_PREMIUM) 1000 else 150
                prefs[KEY_CREDITS] = newCredits
            }
        }
        val uid = FirebaseManager.currentUser?.uid ?: FirebaseManager.ensureAuthenticated()
        FirebaseManager.updateUserPlanInFirestore(uid, planType, newCredits)
    }

    suspend fun saveGeneration(item: GenerationItem): Long {
        val id = dao.insertGeneration(item)
        val uid = FirebaseManager.currentUser?.uid ?: FirebaseManager.ensureAuthenticated()
        
        // Save generated asset content/metadata to Firebase Storage
        val assetBytes = (item.prompt + "\n\n" + item.resultText).toByteArray(Charsets.UTF_8)
        val storageUrl = FirebaseManager.uploadAssetToFirebaseStorage(
            uid = uid,
            assetName = "asset_${item.type.name.lowercase()}_$id.txt",
            bytes = assetBytes,
            folder = item.type.name.lowercase()
        )
        val finalItem = item.copy(id = id, resultUrl = if (item.resultUrl.isNotBlank()) item.resultUrl else storageUrl)
        FirebaseManager.saveGenerationToFirestore(uid, finalItem)
        return id
    }

    suspend fun deleteGeneration(id: Long) {
        dao.deleteGeneration(id)
    }

    suspend fun clearGenerations() {
        dao.clearHistory()
    }

    suspend fun saveContentIdea(idea: ContentIdea): Long {
        return dao.insertContentIdea(idea)
    }

    suspend fun updateIdeaCompletion(id: Long, completed: Boolean) {
        dao.updateIdeaCompletion(id, completed)
    }

    suspend fun deleteContentIdea(id: Long) {
        dao.deleteContentIdea(id)
    }
}
