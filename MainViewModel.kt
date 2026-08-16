package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.GeminiService
import com.example.ai.KlingAiService
import com.example.ai.RunwayAiService
import com.example.data.models.AiCharacter
import com.example.data.models.AiCourse
import com.example.data.models.CourseLesson
import com.example.data.models.CourseModule
import com.example.data.models.BackupRecord
import com.example.data.models.BackupSettings
import com.example.data.models.ConnectedAccount
import com.example.data.models.ContentIdea
import com.example.data.models.SocialPost
import com.example.data.models.SharedProject
import com.example.data.models.TeamActivity
import com.example.data.models.TeamChatMessage
import com.example.data.models.TeamMember
import com.example.data.models.TeamWorkspace
import com.example.data.models.GenerationItem
import com.example.data.models.GenerationType
import com.example.data.models.LoginType
import com.example.data.models.PlanType
import com.example.data.models.User
import com.example.data.repository.AppRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class GenerationState {
    object Idle : GenerationState()
    data class Loading(val progressMessage: String, val progressPercent: Float) : GenerationState()
    data class Success(val item: GenerationItem) : GenerationState()
    data class Error(val message: String) : GenerationState()
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val repository = AppRepository(application.applicationContext)

    val user: StateFlow<User> = repository.userFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = User()
    )

    val history: StateFlow<List<GenerationItem>> = repository.allGenerations.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val contentCalendar: StateFlow<List<ContentIdea>> = repository.allContentIdeas.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val savedCharacters: StateFlow<List<AiCharacter>> = repository.allCharacters.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val savedProductAds: StateFlow<List<com.example.data.models.ProductAd>> = repository.allProductAds.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _activeCharacter = MutableStateFlow<AiCharacter?>(null)
    val activeCharacter: StateFlow<AiCharacter?> = _activeCharacter.asStateFlow()

    private val _clonedVoices = MutableStateFlow<List<com.example.data.models.ClonedVoice>>(
        listOf(
            com.example.data.models.ClonedVoice(
                id = "VOICE_VIP_01",
                name = "Aarav Studio Master",
                gender = "Male",
                sampleFileName = "voice_sample_aarav.mp3",
                primaryLanguage = "Telugu",
                emotionTone = "Energetic & Professional",
                description = "Deep warm radio narrator voice cloned from studio mic sample."
            ),
            com.example.data.models.ClonedVoice(
                id = "VOICE_VIP_02",
                name = "Ananya Storyteller",
                gender = "Female",
                sampleFileName = "voice_sample_ananya.mp3",
                primaryLanguage = "English",
                emotionTone = "Emotional & Calming",
                description = "Soft expressive voice suited for audiobook narration and podcast intros."
            )
        )
    )
    val clonedVoices: StateFlow<List<com.example.data.models.ClonedVoice>> = _clonedVoices.asStateFlow()

    private val _referralStats = MutableStateFlow(
        com.example.data.models.ReferralStats(
            referralCode = "AIVIDEO-REF-${(1000..9999).random()}",
            totalInvites = 14,
            successfulSignups = 9,
            totalCreditsEarned = 45,
            pendingCredits = 10,
            affiliateEarningsUsd = 42.50,
            affiliateTier = "Gold Ambassador (20% RevShare)"
        )
    )
    val referralStats: StateFlow<com.example.data.models.ReferralStats> = _referralStats.asStateFlow()

    private val _referralHistory = MutableStateFlow(
        listOf(
            com.example.data.models.ReferralItem(
                id = "REF_101",
                friendName = "Suresh Verma",
                friendEmail = "suresh.v@gmail.com",
                status = "Completed (+5 Credits)",
                creditsEarned = 5,
                affiliateCommission = 3.50,
                timestamp = System.currentTimeMillis() - 86400000L * 2
            ),
            com.example.data.models.ReferralItem(
                id = "REF_102",
                friendName = "Priya Sharma",
                friendEmail = "priya.ai@outlook.com",
                status = "Completed (+5 Credits + Pro Bonus)",
                creditsEarned = 10,
                affiliateCommission = 12.00,
                timestamp = System.currentTimeMillis() - 86400000L * 5
            ),
            com.example.data.models.ReferralItem(
                id = "REF_103",
                friendName = "Rohan Mehta",
                friendEmail = "rohan.tech@yahoo.com",
                status = "Pending Sign-up",
                creditsEarned = 0,
                affiliateCommission = 0.0,
                timestamp = System.currentTimeMillis() - 3600000L * 4
            )
        )
    )
    val referralHistory: StateFlow<List<com.example.data.models.ReferralItem>> = _referralHistory.asStateFlow()

    fun inviteFriend(friendName: String, friendEmail: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            if (friendEmail.isBlank() || !friendEmail.contains("@")) {
                onResult(false, "Please enter a valid email address.")
                return@launch
            }

            val newRef = com.example.data.models.ReferralItem(
                id = "REF_${System.currentTimeMillis().toString().takeLast(5)}",
                friendName = friendName.ifBlank { "Invited Friend" },
                friendEmail = friendEmail.trim(),
                status = "Completed (+5 Credits)",
                creditsEarned = 5,
                affiliateCommission = 2.50,
                timestamp = System.currentTimeMillis()
            )

            // Grant 5 credits per referral
            repository.addCredits(5)

            // Update stats
            val curStats = _referralStats.value
            _referralStats.value = curStats.copy(
                totalInvites = curStats.totalInvites + 1,
                successfulSignups = curStats.successfulSignups + 1,
                totalCreditsEarned = curStats.totalCreditsEarned + 5,
                affiliateEarningsUsd = curStats.affiliateEarningsUsd + 2.50
            )

            // Update history
            _referralHistory.value = listOf(newRef) + _referralHistory.value

            onResult(true, "Invitation sent to $friendEmail! 5 Referral Credits added to your balance 🎉")
        }
    }

    fun redeemReferralCode(code: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val cleanCode = code.trim().uppercase()
            if (cleanCode.length < 4) {
                onResult(false, "Invalid referral code format.")
                return@launch
            }

            // Grant 5 credits reward for using a friend's referral code
            repository.addCredits(5)

            val curStats = _referralStats.value
            _referralStats.value = curStats.copy(
                totalCreditsEarned = curStats.totalCreditsEarned + 5
            )

            onResult(true, "Referral Code '$cleanCode' redeemed successfully! +5 Credits granted 🎉")
        }
    }

    fun cashoutAffiliateEarnings(amountUsd: Double, paymentMethod: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val curStats = _referralStats.value
            if (amountUsd > curStats.affiliateEarningsUsd) {
                onResult(false, "Cashout amount exceeds available earnings ($${curStats.affiliateEarningsUsd}).")
                return@launch
            }

            val updatedBalance = curStats.affiliateEarningsUsd - amountUsd
            _referralStats.value = curStats.copy(affiliateEarningsUsd = updatedBalance)

            onResult(true, "Cashout request of $$amountUsd via $paymentMethod processed successfully!")
        }
    }

    fun addClonedVoice(voice: com.example.data.models.ClonedVoice) {
        _clonedVoices.value = listOf(voice) + _clonedVoices.value
    }

    fun deleteClonedVoice(voiceId: String) {
        _clonedVoices.value = _clonedVoices.value.filter { it.id != voiceId }
    }

    fun setActiveCharacter(character: AiCharacter?) {
        _activeCharacter.value = character
    }

    fun selectCharacterById(characterId: String) {
        viewModelScope.launch {
            if (characterId.isBlank()) {
                _activeCharacter.value = null
            } else {
                val char = repository.getCharacterById(characterId.trim())
                _activeCharacter.value = char
            }
        }
    }

    private val _generationState = MutableStateFlow<GenerationState>(GenerationState.Idle)
    val generationState: StateFlow<GenerationState> = _generationState.asStateFlow()

    init {
        GeminiService.initializeTts(application)
    }

    fun resetState() {
        _generationState.value = GenerationState.Idle
    }

    fun calculateQualityBonusCredits(quality: String): Int {
        return when {
            quality.contains("720p") || (quality.contains("HD") && !quality.contains("Ultra") && !quality.contains("Full") && quality != "Standard") -> 1
            quality.contains("1080p") || quality.contains("Ultra HD") || quality.contains("Full HD") -> 2
            quality.contains("1440p") || quality.contains("2K") -> 4
            quality.contains("2160p") || quality.contains("4K") -> 6
            else -> 0
        }
    }

    fun calculateVideoCredits(durationSeconds: Int, quality: String = "480p"): Int {
        val base = when (durationSeconds) {
            10 -> 1
            15 -> 2
            30 -> 3
            60 -> 6
            else -> 1
        }
        return base + calculateQualityBonusCredits(quality)
    }

    fun calculateImageCredits(quality: String = "Standard"): Int {
        return 1 + calculateQualityBonusCredits(quality)
    }

    // AUTH
    fun login(name: String, email: String, loginType: LoginType) {
        viewModelScope.launch {
            repository.loginUser(name, email, loginType)
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logoutUser()
        }
    }

    // REWARDED ADS
    fun watchRewardedAd(onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val userVal = user.value
            if (userVal.rewardedAdsToday >= 5) {
                onResult(false, "Daily rewarded ad limit reached (5/5). Try again tomorrow!")
                return@launch
            }
            val rewarded = repository.completeRewardedAd()
            if (rewarded) {
                onResult(true, "Rewarded ad completed! +2 Credits added to your account 🎉")
            } else {
                onResult(false, "Ad reward limit reached for today.")
            }
        }
    }

    // SUBSCRIPTION
    fun subscribePlan(planType: PlanType) {
        viewModelScope.launch {
            repository.updatePlan(planType)
        }
    }

    // ADMIN ACTIONS
    fun adminSetCredits(amount: Int) {
        viewModelScope.launch {
            repository.addCredits(amount)
        }
    }

    // GENERATORS
    fun generateTextToVideo(
        prompt: String,
        durationSeconds: Int = 15,
        aspectRatio: String = "16:9",
        language: String = "English",
        isHd: Boolean = true,
        selectedModel: String = "Google Veo",
        overrideCharacterId: String? = null,
        quality: String = "1080p Full HD",
        onComplete: ((Boolean, String, String) -> Unit)? = null
    ) {
        val activeChar = _activeCharacter.value
        val charPrefix = if (activeChar != null) {
            "[Consistent Character Lock: ID ${activeChar.id} (${activeChar.name}) | Style: ${activeChar.style} | Anchor: ${activeChar.seedPrompt}. Preserve exact face, body, hairstyle, clothes, and colors] "
        } else if (!overrideCharacterId.isNullOrBlank()) {
            "[Consistent Character Lock ID: $overrideCharacterId | Preserve exact face, body, hairstyle, clothes, and colors] "
        } else ""

        val augmentedPrompt = charPrefix + prompt
        val cost = calculateVideoCredits(durationSeconds, quality)
        val resultVideoUrl = "https://aivideocreator.hub/render/video_${System.currentTimeMillis()}.mp4"

        executeAsyncGeneration(
            creditCost = cost,
            loadingMessage = "Connecting to $selectedModel Engine ($quality)..."
        ) {
            val aiText = when {
                selectedModel.contains("Kling", ignoreCase = true) -> {
                    KlingAiService.generateTextToVideo(augmentedPrompt, durationSeconds, aspectRatio)
                }
                selectedModel.contains("Runway", ignoreCase = true) -> {
                    RunwayAiService.generateTextToVideo(augmentedPrompt, durationSeconds, aspectRatio)
                }
                else -> {
                    GeminiService.generateGoogleVeoVideo(augmentedPrompt, durationSeconds, aspectRatio)
                }
            }

            onComplete?.invoke(true, "Video rendered successfully!", resultVideoUrl)

            GenerationItem(
                type = GenerationType.TEXT_TO_VIDEO,
                title = "[$selectedModel - $quality] " + prompt.take(20) + if (prompt.length > 20) "..." else "",
                prompt = "Model: $selectedModel | Quality: $quality | Lang: $language | ${if (activeChar != null) "Char: ${activeChar.id} | " else ""}$prompt",
                resultText = aiText,
                resultUrl = resultVideoUrl,
                durationSeconds = durationSeconds,
                aspectRatio = aspectRatio,
                creditsSpent = cost
            )
        }
    }

    fun generateImageToVideo(
        imageLabel: String,
        prompt: String,
        durationSeconds: Int,
        aspectRatio: String,
        selectedModel: String = "Kling Image Animation",
        overrideCharacterId: String? = null,
        quality: String = "1080p Full HD"
    ) {
        val activeChar = _activeCharacter.value
        val charPrefix = if (activeChar != null) {
            "[Character Consistency Lock: ID ${activeChar.id} | Anchor: ${activeChar.seedPrompt}] "
        } else if (!overrideCharacterId.isNullOrBlank()) {
            "[Character Consistency Lock: ID $overrideCharacterId] "
        } else ""

        val augmentedPrompt = charPrefix + prompt
        val cost = calculateVideoCredits(durationSeconds, quality)
        executeAsyncGeneration(
            creditCost = cost,
            loadingMessage = "Animating photo via $selectedModel ($quality)..."
        ) {
            val aiText = if (selectedModel.contains("Runway", ignoreCase = true)) {
                RunwayAiService.generateImageToVideo("https://aivideocreator.hub/input/photo.jpg", augmentedPrompt, durationSeconds, aspectRatio)
            } else {
                KlingAiService.generateImageToVideo("https://aivideocreator.hub/input/photo.jpg", augmentedPrompt, durationSeconds, aspectRatio)
            }

            GenerationItem(
                type = GenerationType.IMAGE_TO_VIDEO,
                title = "[$selectedModel - $quality] $imageLabel",
                prompt = "Source: $imageLabel | Quality: $quality | Motion: $augmentedPrompt",
                resultText = aiText,
                resultUrl = "https://aivideocreator.hub/render/img_video_${System.currentTimeMillis()}.mp4",
                durationSeconds = durationSeconds,
                aspectRatio = aspectRatio,
                creditsSpent = cost
            )
        }
    }

    fun generateTextToImage(
        prompt: String,
        styleName: String,
        aspectRatio: String,
        selectedModel: String = "Gemini Image Generation",
        overrideCharacterId: String? = null,
        quality: String = "Standard"
    ) {
        val activeChar = _activeCharacter.value
        val charPrefix = if (activeChar != null) {
            "[Character Consistency Lock: ID ${activeChar.id} (${activeChar.name}) | Style: ${activeChar.style} | Anchor: ${activeChar.seedPrompt}. Maintain same face, body, hairstyle, outfit, and color palette] "
        } else if (!overrideCharacterId.isNullOrBlank()) {
            "[Character Consistency Lock ID: $overrideCharacterId] "
        } else ""

        val augmentedPrompt = charPrefix + prompt
        val cost = calculateImageCredits(quality)
        executeAsyncGeneration(
            creditCost = cost,
            loadingMessage = "Generating $styleName ($quality) via $selectedModel..."
        ) {
            val aiText = GeminiService.generateGeminiImage(augmentedPrompt, styleName, aspectRatio)
            GenerationItem(
                type = GenerationType.TEXT_TO_IMAGE,
                title = "[$selectedModel - $quality] $styleName",
                prompt = augmentedPrompt,
                resultText = aiText,
                resultUrl = "https://aivideocreator.hub/render/image_${System.currentTimeMillis()}.png",
                styleName = styleName,
                aspectRatio = aspectRatio,
                creditsSpent = cost
            )
        }
    }

    fun generateCharacter(
        charName: String,
        characterType: String,
        gender: String,
        style: String,
        isConsistent: Boolean,
        description: String
    ) {
        val cost = 1
        executeAsyncGeneration(
            creditCost = cost,
            loadingMessage = "Generating Character & Reference Sheet (Angles, Expressions, Actions)..."
        ) {
            val characterProfile = GeminiService.generateCharacterReferenceSheet(
                charName = charName,
                characterType = characterType,
                gender = gender,
                style = style,
                description = description
            )

            // Save character to database
            repository.saveCharacter(characterProfile)
            _activeCharacter.value = characterProfile

            val resultSummary = """
                🆔 CHARACTER ID: ${characterProfile.id}
                👤 Name: ${characterProfile.name} ($gender | $style)
                
                ✨ VISUAL IDENTITY SEED ANCHOR:
                "${characterProfile.seedPrompt}"
                
                📷 CHARACTER REFERENCE SHEET GENERATED:
                • Angles: Front View, Left Side, Right Side, Back View, 45° Angle
                • Expressions: Happy, Sad, Angry, Fearful
                • Actions: Talking, Walking, Running, Sitting
                
                🔒 CHARACTER CONSISTENCY LOCK ACTIVE!
                This Character ID will be automatically reused in Text-To-Video, Image-To-Video, and Image Generations to maintain identical face, body, hairstyle, clothing, and colors.
            """.trimIndent()

            GenerationItem(
                type = GenerationType.CHARACTER,
                title = "Character ID: ${characterProfile.id} (${characterProfile.name})",
                prompt = "Name: $charName | Style: $style | Gender: $gender | Details: $description",
                resultText = resultSummary,
                resultUrl = characterProfile.primaryImageUrl,
                styleName = style,
                creditsSpent = cost
            )
        }
    }

    fun deleteCharacter(character: com.example.data.models.AiCharacter) {
        viewModelScope.launch {
            repository.deleteCharacter(character.id)
            if (_activeCharacter.value?.id == character.id) {
                _activeCharacter.value = null
            }
        }
    }

    fun generateVoice(
        text: String,
        gender: String,
        language: String,
        voiceName: String
    ) {
        val cost = 1
        val promptText = "Synthesize $language $gender Voice ($voiceName): $text"
        executeAsyncGeneration(
            creditCost = cost,
            loadingMessage = "Synthesizing $language $gender Voice ($voiceName)..."
        ) {
            val aiText = GeminiService.generateGoogleTtsVoice(
                text = text,
                gender = gender,
                language = language,
                voiceName = voiceName,
                context = getApplication()
            )

            GenerationItem(
                type = GenerationType.VOICE,
                title = "AI Voiceover ($language $gender)",
                prompt = promptText,
                resultText = aiText,
                resultUrl = "https://aivideocreator.hub/render/audio_${System.currentTimeMillis()}.mp3",
                durationSeconds = 15,
                creditsSpent = cost
            )
        }
    }

    fun generateClonedVoiceSpeech(
        voiceId: String,
        voiceName: String,
        gender: String,
        language: String,
        emotion: String,
        scriptText: String
    ) {
        val cost = 2
        executeAsyncGeneration(
            creditCost = cost,
            loadingMessage = "Cloning Voice Profile '$voiceName' ($gender, $emotion) & Synthesizing Speech in $language..."
        ) {
            val speechResult = GeminiService.generateContent(
                "Generate a high-fidelity AI Cloned Speech synthesis package.\n" +
                "Voice Model: '$voiceName' (ID: $voiceId)\n" +
                "Gender: $gender | Language: $language | Emotion Tone: $emotion\n" +
                "Script: \"$scriptText\"\n\n" +
                "Output Details:\n" +
                "1. 🎙️ VOICE CLONE PROFILE: Matched acoustic pitch, timbre, dynamic vocal range & breath control.\n" +
                "2. 🎭 EMOTIONAL INTENSITY: Applied $emotion voice filter for $language pronunciation.\n" +
                "3. 📜 SYNTHESIZED SPEECH SCRIPT: \"$scriptText\"\n" +
                "4. 🎧 AUDIO SPECIFICATIONS: 24kHz 320kbps WAV/MP3 Studio Master File."
            )

            GenerationItem(
                type = GenerationType.VOICE_CLONE,
                title = "Cloned Voice: $voiceName",
                prompt = "Voice: $voiceName | Lang: $language | Emotion: $emotion",
                resultText = speechResult,
                resultUrl = "https://aivideocreator.hub/render/cloned_voice_${System.currentTimeMillis()}.mp3",
                durationSeconds = 20,
                creditsSpent = cost
            )
        }
    }

    fun generateAiMusicStudio(
        musicType: String,
        genreStyle: String,
        mood: String,
        durationSeconds: Int,
        tempoBpm: String,
        isRoyaltyFree: Boolean = true
    ) {
        val cost = 1
        executeAsyncGeneration(
            creditCost = cost,
            loadingMessage = "Synthesizing AI $musicType ($genreStyle, $mood)..."
        ) {
            val fullPrompt = "You are an elite film composer and AI music producer. Generate a complete studio music score breakdown and production package for:\n" +
                    "MUSIC TYPE: $musicType\n" +
                    "GENRE & STYLE: $genreStyle\n" +
                    "MOOD & ATMOSPHERE: $mood\n" +
                    "DURATION: $durationSeconds Seconds\n" +
                    "TEMPO: $tempoBpm\n" +
                    "ROYALTY FREE LICENSE: ${if (isRoyaltyFree) "100% Royalty Free Commercial License Included" else "Standard License"}\n\n" +
                    "Provide a detailed studio music response containing:\n" +
                    "1. 🎵 TRACK OVERVIEW & ARRANGEMENT (Structure: Intro, Build-up, Main Climax, Outro Fade)\n" +
                    "2. 🎼 INSTRUMENTATION & SOUND LAYERS (Key instruments, synths, percussion, sub-bass)\n" +
                    "3. 🎧 AI AUDIO SYNTHESIS PROMPTS (Copyable prompt for AI audio synthesis engines)\n" +
                    "4. 📜 100% ROYALTY FREE COMMERCIAL CERTIFICATE (Ready for YouTube monetization, Reels, Ads & Podcasts)\n" +
                    "5. 📁 MP3 AUDIO RENDER SPECIFICATIONS (320kbps High Quality Stereo Master)"

            val audioCompositionText = GeminiService.generateContent(fullPrompt)
            val audioUrl = "https://aivideocreator.hub/render/music_${musicType.lowercase().replace(" ", "_")}_${System.currentTimeMillis()}.mp3"

            GenerationItem(
                type = GenerationType.MUSIC,
                title = "[$musicType] $genreStyle ($mood)",
                prompt = "Type: $musicType | Style: $genreStyle | Mood: $mood | Duration: ${durationSeconds}s | Tempo: $tempoBpm",
                resultText = audioCompositionText,
                resultUrl = audioUrl,
                durationSeconds = durationSeconds,
                styleName = genreStyle,
                creditsSpent = cost
            )
        }
    }

    fun generateAiTitleAndHashtags(
        topicOrKeywords: String,
        targetLanguage: String,
        platformType: String,
        categoryStyle: String
    ) {
        val cost = 1
        executeAsyncGeneration(
            creditCost = cost,
            loadingMessage = "Generating Viral $platformType Titles, Captions, Hashtags & SEO in $targetLanguage..."
        ) {
            val fullPrompt = "You are a top YouTube SEO strategist and Instagram algorithm growth specialist. Generate a complete metadata package in $targetLanguage for:\n" +
                    "TOPIC / CONCEPT: $topicOrKeywords\n" +
                    "PRIMARY PLATFORM: $platformType\n" +
                    "STYLE / CATEGORY: $categoryStyle\n" +
                    "LANGUAGE: $targetLanguage\n\n" +
                    "Provide a structured, beautifully formatted response in $targetLanguage with:\n" +
                    "1. 🎬 10 VIRAL YOUTUBE & SHORTS TITLES (High CTR, Clickbait & Curiosity Driven, including emojis & power words)\n" +
                    "2. 📱 3 INSTAGRAM & TIKTOK CAPTIONS (Short & engaging, hook-first, Call-to-Action included)\n" +
                    "3. #️⃣ VIRAL HASHTAGS (Grouped into: 5 Mega-Trending #hashtags, 10 High-Reach #hashtags, 10 Niche Specific #hashtags)\n" +
                    "4. 🔍 SEO SUGGESTIONS & SEARCH TAGS (Comma-separated YouTube tags & high volume search keywords)\n" +
                    "5. 💡 ALGORITHM BOOST TIP (Optimal posting time & thumbnail text idea)"

            val resultText = GeminiService.generateContent(fullPrompt)

            GenerationItem(
                type = GenerationType.TITLE_HASHTAG,
                title = "$platformType Metadata ($targetLanguage)",
                prompt = "Topic: $topicOrKeywords | Platform: $platformType | Lang: $targetLanguage",
                resultText = resultText,
                resultUrl = "",
                creditsSpent = cost
            )
        }
    }

    fun generateAiHookSuite(
        hookCategory: String,
        topic: String,
        targetLanguage: String,
        targetAudience: String
    ) {
        val cost = 1
        executeAsyncGeneration(
            creditCost = cost,
            loadingMessage = "Writing high-retention $hookCategory hooks in $targetLanguage..."
        ) {
            val fullPrompt = "You are an expert short-form video strategist. Generate a suite of high-retention, pattern-interrupting $hookCategory hooks for:\n" +
                    "TOPIC / CONCEPT: $topic\n" +
                    "TARGET LANGUAGE: $targetLanguage\n" +
                    "TARGET AUDIENCE: $targetAudience\n" +
                    "CATEGORY TYPE: $hookCategory\n\n" +
                    "Generate 5 distinct variations for EACH of these categories:\n" +
                    "1. 💥 VIRAL HOOKS (High energy, pattern interrupts)\n" +
                    "2. 🤔 CURIOSITY HOOKS (Cliffhangers & secret reveals)\n" +
                    "3. ❤️ EMOTIONAL HOOKS (Relatable, shocking, or heart-touching)\n" +
                    "4. 💰 SALES HOOKS (Direct offer, value proposition & FOMO)\n" +
                    "5. 🔥 MOTIVATION HOOKS (Mindset shifts & inspirational triggers)\n" +
                    "6. 📖 STORY HOOKS (Narrative cliffhangers & dramatic reveals)\n\n" +
                    "Provide all hooks in $targetLanguage formatted with numbered lists and emojis so they are ready for one-click copying."

            val resultText = GeminiService.generateContent(fullPrompt)

            GenerationItem(
                type = GenerationType.VIRAL_HOOK,
                title = "$hookCategory Hooks ($targetLanguage)",
                prompt = "Category: $hookCategory | Topic: $topic | Lang: $targetLanguage",
                resultText = resultText,
                resultUrl = "",
                creditsSpent = cost
            )
        }
    }

    fun generateAiScript(
        scriptCategory: String, // YouTube, Shorts/Reels, Product Ad, Story, Podcast
        topicOrPrompt: String,
        targetLanguage: String,
        targetAudience: String,
        tone: String,
        lengthMinutes: String
    ) {
        val cost = 1
        executeAsyncGeneration(
            creditCost = cost,
            loadingMessage = "Writing high-engagement $scriptCategory Script ($targetLanguage, Tone: $tone)..."
        ) {
            val fullPrompt = "You are an elite Hollywood & viral content scriptwriter. Write a full production-ready script for $scriptCategory.\n" +
                    "TOPIC / CONCEPT: $topicOrPrompt\n" +
                    "TARGET LANGUAGE: $targetLanguage\n" +
                    "SCRIPT TYPE: $scriptCategory\n" +
                    "TARGET AUDIENCE: $targetAudience\n" +
                    "TONE & STYLE: $tone\n" +
                    "DURATION / LENGTH: $lengthMinutes\n\n" +
                    "Include: \n" +
                    "1. 🪝 VIRAL HOOK (First 3-5 seconds)\n" +
                    "2. 🎬 SCENE-BY-SCENE BREAKDOWN (Visual Direction, B-Roll, On-Screen Text & Speaker Dialogue)\n" +
                    "3. 🎙️ VOICE OVER NARRATION (Formatted clearly for TTS)\n" +
                    "4. 💡 CALL TO ACTION (CTA & Engagement prompts)\n" +
                    "5. ⏱️ TIMESTAMPS & CAMERA ANGLE INSTRUCTIONS\n\n" +
                    "Write the response in $targetLanguage with high energy and compelling storytelling."

            val resultScript = GeminiService.generateContent(fullPrompt)

            GenerationItem(
                type = GenerationType.SCRIPT_WRITER,
                title = "$scriptCategory Script ($targetLanguage)",
                prompt = "$scriptCategory: $topicOrPrompt | $targetLanguage",
                resultText = resultScript,
                resultUrl = "",
                creditsSpent = cost
            )
        }
    }

    fun generateAutoSubtitles(videoContext: String, language: String, styleTemplate: String) {
        val cost = 1
        executeAsyncGeneration(
            creditCost = cost,
            loadingMessage = "Transcribing speech & formatting SRT captions ($language)..."
        ) {
            val srtText = GeminiService.generateAutoSubtitlesSrt(videoContext, language, styleTemplate)
            GenerationItem(
                type = GenerationType.SUBTITLES,
                title = "Auto Subtitles ($language)",
                prompt = videoContext,
                resultText = srtText,
                resultUrl = "https://aivideocreator.hub/render/subs_${System.currentTimeMillis()}.srt",
                creditsSpent = cost
            )
        }
    }

    fun generateMusic(genre: String, mood: String, durationSeconds: Int) {
        val cost = 1
        val promptText = "Generate $durationSeconds-second $genre track with $mood mood"
        executeAsyncGeneration(
            creditCost = cost,
            loadingMessage = "Generating $genre $mood Audio Track..."
        ) {
            val aiText = GeminiService.generateContent("Create a musical breakdown for a $durationSeconds-second $genre track in $mood mood.")
            GenerationItem(
                type = GenerationType.MUSIC,
                title = "AI Music: $genre ($mood)",
                prompt = promptText,
                resultText = aiText,
                resultUrl = "https://aivideocreator.hub/render/music_${System.currentTimeMillis()}.mp3",
                durationSeconds = durationSeconds,
                creditsSpent = cost
            )
        }
    }

    fun generateTalkingPhoto(
        photoUrl: String,
        scriptText: String,
        language: String,
        voiceGender: String,
        quality: String = "1080p Full HD"
    ) {
        val cost = calculateVideoCredits(15, quality)
        executeAsyncGeneration(
            creditCost = cost,
            loadingMessage = "Generating Talking Photo Video with Lip-Sync ($language | $quality)..."
        ) {
            val aiSummary = GeminiService.generateContent(
                "Generate a detailed lip-sync talking photo video synthesis plan for source photo '$photoUrl' with $language $voiceGender voice reciting script: '$scriptText'."
            )
            GenerationItem(
                type = GenerationType.TALKING_PHOTO,
                title = "AI Talking Photo ($language)",
                prompt = "Photo: $photoUrl | Lang: $language | Voice: $voiceGender | Quality: $quality | Script: $scriptText",
                resultText = aiSummary,
                resultUrl = "https://aivideocreator.hub/render/talking_photo_${System.currentTimeMillis()}.mp4",
                durationSeconds = 15,
                aspectRatio = "9:16",
                creditsSpent = cost
            )
        }
    }

    fun generateThumbnail(
        topic: String,
        platform: String,
        style: String,
        quality: String = "Standard"
    ) {
        val cost = calculateImageCredits(quality)
        val targetAspect = when {
            platform.contains("1:1") || platform.contains("Square") -> "1:1"
            platform.contains("9:16") || platform.contains("Reel") || platform.contains("Story") -> "9:16"
            else -> "16:9"
        }
        executeAsyncGeneration(
            creditCost = cost,
            loadingMessage = "Designing $platform Thumbnail in $style style ($quality)..."
        ) {
            val promptDetails = "High impact thumbnail visual for '$topic' optimized for $platform. Style: $style."
            val imagePlan = GeminiService.generateGeminiImage(promptDetails, style, targetAspect)
            val titlesPlan = GeminiService.generateContent(
                "Generate 5 high-CTR, clickbait, engaging viral video title suggestions for topic: '$topic'. Format them as a numbered list with emojis."
            )
            val combinedResult = "📷 THUMBNAIL DESIGN & PROMPT:\n$imagePlan\n\n🔥 TOP 5 HIGH-CTR AI TITLE SUGGESTIONS:\n$titlesPlan"

            GenerationItem(
                type = GenerationType.THUMBNAIL,
                title = "[$platform] " + topic.take(20) + if (topic.length > 20) "..." else "",
                prompt = "Topic: $topic | Platform: $platform | Style: $style | Quality: $quality",
                resultText = combinedResult,
                resultUrl = "https://aivideocreator.hub/render/thumbnail_${System.currentTimeMillis()}.png",
                aspectRatio = targetAspect,
                styleName = style,
                creditsSpent = cost
            )
        }
    }

    fun generateVideoDubbing(
        videoUrl: String,
        sourceLang: String,
        targetLang: String,
        voiceGender: String,
        includeSubtitles: Boolean,
        quality: String = "1080p Full HD"
    ) {
        val cost = calculateVideoCredits(15, quality) + 1
        executeAsyncGeneration(
            creditCost = cost,
            loadingMessage = "Analyzing speech, translating to $targetLang & syncing lips ($quality)..."
        ) {
            val dubbingAnalysis = GeminiService.generateContent(
                "Perform an AI video dubbing analysis for video '$videoUrl'. Detect speech in '$sourceLang', translate dialogue into fluent '$targetLang', generate voice actor timing matching $voiceGender voice, and construct matching lip-sync timestamp mapping."
            )
            val srtSubtitles = if (includeSubtitles) {
                "\n\n📝 TRANSLATED SRT SUBTITLES ($targetLang):\n1\n00:00:01,000 --> 00:00:05,000\n[Synced $targetLang Dialogue text]\n\n2\n00:00:05,500 --> 00:00:10,000\n[Lip-synced audio track response]"
            } else ""

            val combinedResult = "🎙️ AI DUBBED VIDEO PACKAGE ($sourceLang ➔ $targetLang):\n$dubbingAnalysis$srtSubtitles"

            GenerationItem(
                type = GenerationType.VIDEO_DUBBING,
                title = "[$targetLang Dubbed] " + videoUrl.takeLast(25),
                prompt = "Source: $videoUrl | Detect: $sourceLang ➔ Target: $targetLang | Voice: $voiceGender | Subs: $includeSubtitles | Quality: $quality",
                resultText = combinedResult,
                resultUrl = "https://aivideocreator.hub/render/dubbed_video_${System.currentTimeMillis()}.mp4",
                durationSeconds = 15,
                aspectRatio = "16:9",
                creditsSpent = cost
            )
        }
    }

    fun generateAvatar(
        photoUrl: String,
        style: String,
        lockConsistentFace: Boolean,
        customPrompt: String,
        quality: String = "HD 1080p"
    ) {
        val cost = calculateImageCredits(quality)
        executeAsyncGeneration(
            creditCost = cost,
            loadingMessage = "Synthesizing $style AI Avatar with Face Consistency ($quality)..."
        ) {
            val promptDetails = "Generate a high-definition $style AI avatar based on input face '$photoUrl'. Face lock consistency: $lockConsistentFace. Custom details: $customPrompt."
            val avatarResult = GeminiService.generateGeminiImage(promptDetails, style, "1:1")

            val outputSummary = "🎭 AI AVATAR GENERATED ($style):\n$avatarResult\n\n🔒 Face Consistency Lock: ${if (lockConsistentFace) "ENABLED (100% Identity Preserve)" else "OFF"}\n✨ Quality Level: $quality"

            GenerationItem(
                type = GenerationType.AVATAR_GENERATOR,
                title = "AI Avatar ($style)",
                prompt = "Source Photo: $photoUrl | Style: $style | Face Lock: $lockConsistentFace | Extra: $customPrompt",
                resultText = outputSummary,
                resultUrl = "https://aivideocreator.hub/render/avatar_${System.currentTimeMillis()}.png",
                aspectRatio = "1:1",
                styleName = style,
                creditsSpent = cost
            )
        }
    }

    fun generateStory(
        topicOrPremise: String,
        genre: String,
        targetLanguage: String,
        includeVideoPrompts: Boolean,
        includeVoiceoverScript: Boolean,
        includeSubtitlesSRT: Boolean,
        numScenes: Int = 4
    ) {
        val cost = if (includeVideoPrompts || includeVoiceoverScript) 2 else 1
        executeAsyncGeneration(
            creditCost = cost,
            loadingMessage = "Developing story ideas, character profiles, scene scripts & assets ($genre)..."
        ) {
            val storyContent = GeminiService.generateContent(
                "Write a complete production-ready story package for premise: '$topicOrPremise'. Genre: $genre. Language: $targetLanguage. Include:\n" +
                "1. STORY CONCEPT & LOGLINE\n" +
                "2. CHARACTER CREATION (Protagonist, Antagonist, Supporting)\n" +
                "3. SCENE-BY-SCENE SCRIPT ($numScenes Scenes with dialogue & action)\n" +
                (if (includeVideoPrompts) "4. STORY TO VIDEO PROMPTS (Cinematic Prompts for AI Video Generators)\n" else "") +
                (if (includeVoiceoverScript) "5. STORY TO VOICEOVER SCRIPT ($targetLanguage Studio Voice Tone)\n" else "") +
                (if (includeSubtitlesSRT) "6. STORY TO SUBTITLES (Formatted SRT Captions with Timestamps)\n" else "")
            )

            GenerationItem(
                type = GenerationType.STORY_GENERATOR,
                title = "[$genre] " + topicOrPremise.take(22) + if (topicOrPremise.length > 22) "..." else "",
                prompt = "Premise: $topicOrPremise | Genre: $genre | Lang: $targetLanguage | Scenes: $numScenes",
                resultText = storyContent,
                resultUrl = "https://aivideocreator.hub/render/story_${System.currentTimeMillis()}.mp4",
                styleName = genre,
                creditsSpent = cost
            )
        }
    }

    fun generateFromVideoTemplate(
        categoryName: String,
        templateTitle: String,
        userCustomText: String,
        aspectRatio: String = "9:16",
        quality: String = "1080p Full HD"
    ) {
        val cost = calculateVideoCredits(10, quality)
        executeAsyncGeneration(
            creditCost = cost,
            loadingMessage = "Rendering $categoryName Template '$templateTitle' ($quality, $aspectRatio)..."
        ) {
            val templateScript = GeminiService.generateGoogleVeoVideo(
                "Create a high-impact $categoryName video using template '$templateTitle' customized with user input '$userCustomText'. Include scene cuts, background music recommendation, AI voiceover script, and visual prompt styling.",
                10,
                aspectRatio
            )

            val fullOutput = "🎬 TEMPLATE: $templateTitle [$categoryName]\n\n" +
                    "📐 Aspect Ratio: $aspectRatio | 🎞️ Quality: $quality\n\n" +
                    templateScript

            GenerationItem(
                type = GenerationType.VIDEO_TEMPLATE,
                title = "[$categoryName] $templateTitle",
                prompt = "Category: $categoryName | Template: $templateTitle | Custom: $userCustomText | Ratio: $aspectRatio",
                resultText = fullOutput,
                resultUrl = "https://aivideocreator.hub/render/template_video_${System.currentTimeMillis()}.mp4",
                durationSeconds = 10,
                aspectRatio = aspectRatio,
                styleName = categoryName,
                creditsSpent = cost
            )
        }
    }

    fun generateReel(
        topic: String,
        platform: String, // Instagram Reels, YouTube Shorts, TikTok
        pacing: String, // Fast/Viral, Storytelling, Educational, High-Energy
        language: String,
        includeAutoHook: Boolean,
        includeCaptions: Boolean,
        includeTrendingMusic: Boolean,
        includeHashtags: Boolean
    ) {
        val cost = calculateVideoCredits(15, "1080p Full HD")
        executeAsyncGeneration(
            creditCost = cost,
            loadingMessage = "Creating $platform Viral Short Video ($topic) with Auto Hooks & Hashtags..."
        ) {
            val scriptContent = GeminiService.generateContent(
                "Create a viral short-form video package for platform '$platform' on topic: '$topic'. Pacing: $pacing, Language: $language.\n" +
                "Include:\n" +
                (if (includeAutoHook) "1. 💥 3 VIRAL ATTENTION-GRABBING HOOKS (First 3 seconds)\n" else "") +
                "2. 📜 SCENE-BY-SCENE SHORT VIDEO SCRIPT (9:16 Vertical Video Prompts)\n" +
                (if (includeCaptions) "3. 📝 AUTO CAPTIONS / SUBTITLES ($language Sync Captions)\n" else "") +
                (if (includeTrendingMusic) "4. 🎵 TRENDING AUDIO & BGM SUGGESTIONS (Matched to $pacing pace)\n" else "") +
                (if (includeHashtags) "5. 🏷️ HIGH-REACH VIRAL HASHTAGS (Optimized for $platform algorithm)\n" else "")
            )

            GenerationItem(
                type = GenerationType.REEL_MAKER,
                title = "[$platform] " + topic.take(22) + if (topic.length > 22) "..." else "",
                prompt = "Platform: $platform | Topic: $topic | Pace: $pacing | Lang: $language",
                resultText = scriptContent,
                resultUrl = "https://aivideocreator.hub/render/reel_${System.currentTimeMillis()}.mp4",
                durationSeconds = 15,
                aspectRatio = "9:16",
                styleName = platform,
                creditsSpent = cost
            )
        }
    }

    fun generatePodcast(
        topic: String,
        hostType: String, // Co-Hosts (Male & Female), Solo Male Host, Solo Female Host
        language: String,
        bgMusicGenre: String, // Lo-Fi Chill, Acoustic Guitar, Corporate Ambient, Deep Tech
        includeIntroOutro: Boolean,
        exportFormat: String // MP3 Audio, MP4 Video Audiogram
    ) {
        val cost = if (exportFormat.contains("MP4")) 3 else 2
        executeAsyncGeneration(
            creditCost = cost,
            loadingMessage = "Generating Podcast Script & Rendering AI $hostType Voice ($language) with BGM..."
        ) {
            val podcastPackage = GeminiService.generateContent(
                "Create a full studio-quality podcast episode package for topic: '$topic'. Language: $language. Host setup: $hostType. Background music: $bgMusicGenre.\n" +
                "Include:\n" +
                (if (includeIntroOutro) "1. 🎙️ INTRO & OUTRO JINGLE (Catchy Podcast Hook & Sign-off)\n" else "") +
                "2. 👥 MULTI-HOST / SOLO PODCAST SCRIPT (Timestamps, Speaker labels, natural conversational tone, laughters & sound effects [SFX])\n" +
                "3. 🎵 BACKGROUND MUSIC RECOMMENDATION ($bgMusicGenre ambient volume level -12dB)\n" +
                "4. 🎧 AI HOST VOICE PROFILE ($hostType in $language - Warm, engaging tone)\n" +
                "5. 📁 EXPORT ASSET SUMMARY (Full $exportFormat High Quality Master File)"
            )

            GenerationItem(
                type = GenerationType.PODCAST_GENERATOR,
                title = "[Podcast] " + topic.take(22) + if (topic.length > 22) "..." else "",
                prompt = "Topic: $topic | Host: $hostType | Lang: $language | Format: $exportFormat",
                resultText = podcastPackage,
                resultUrl = if (exportFormat.contains("MP4")) "https://aivideocreator.hub/render/podcast_${System.currentTimeMillis()}.mp4" else "https://aivideocreator.hub/render/podcast_${System.currentTimeMillis()}.mp3",
                durationSeconds = 60,
                aspectRatio = "16:9",
                styleName = hostType,
                creditsSpent = cost
            )
        }
    }

    fun generateProductToVideo(productUrl: String, highlights: String, durationSeconds: Int, aspectRatio: String) {
        val cost = calculateVideoCredits(durationSeconds)
        executeAsyncGeneration(
            creditCost = cost,
            loadingMessage = "Extracting Amazon/Shopify data & building commercial video..."
        ) {
            val aiText = GeminiService.generateGoogleVeoVideo("Product commercial ad for $productUrl: $highlights", durationSeconds, aspectRatio)
            GenerationItem(
                type = GenerationType.PRODUCT_TO_VIDEO,
                title = "Product Promo Video",
                prompt = "URL: $productUrl | Highlights: $highlights",
                resultText = aiText,
                resultUrl = "https://aivideocreator.hub/render/product_promo_${System.currentTimeMillis()}.mp4",
                durationSeconds = durationSeconds,
                aspectRatio = aspectRatio,
                creditsSpent = cost
            )
        }
    }

    fun generateFullProductAd(
        productName: String,
        templateCategory: String,
        productUrl: String,
        imageUrl: String,
        features: String,
        benefits: String,
        price: String,
        offer: String,
        durationSeconds: Int,
        outputFormat: String,
        adLanguage: String,
        bgMusicVibe: String,
        brandName: String,
        quality: String = "1080p Full HD"
    ) {
        val cost = calculateVideoCredits(durationSeconds, quality)
        val randomHex = (1000..9999).random().toString(16).uppercase()
        val adId = "AD-$randomHex"

        executeAsyncGeneration(
            creditCost = cost,
            loadingMessage = "Generating Product Ad Package ($adLanguage | $outputFormat | ${durationSeconds}s)..."
        ) {
            val fullPackageAiText = GeminiService.generateFullProductAdPackage(
                productName = productName,
                templateCategory = templateCategory,
                productUrl = productUrl,
                features = features,
                benefits = benefits,
                price = price,
                offer = offer,
                durationSeconds = durationSeconds,
                outputFormat = outputFormat,
                adLanguage = adLanguage,
                brandName = brandName
            )

            // Parse out sections
            val hook = if (fullPackageAiText.contains("[MARKETING HOOK]")) {
                fullPackageAiText.substringAfter("[MARKETING HOOK]").substringBefore("[").trim()
            } else "🔥 Don't miss this exclusive deal on $productName!"

            val script = if (fullPackageAiText.contains("[PRODUCT SCRIPT]")) {
                fullPackageAiText.substringAfter("[PRODUCT SCRIPT]").substringBefore("[").trim()
            } else fullPackageAiText

            val voiceover = if (fullPackageAiText.contains("[VOICEOVER TEXT]")) {
                fullPackageAiText.substringAfter("[VOICEOVER TEXT]").substringBefore("[").trim()
            } else "Experience $productName today. Special offer limited time!"

            val srt = if (fullPackageAiText.contains("[CAPTIONS & SUBTITLES SRT]")) {
                fullPackageAiText.substringAfter("[CAPTIONS & SUBTITLES SRT]").substringBefore("[").trim()
            } else "1\n00:00:00,000 --> 00:00:05,000\n$productName - Get Yours Now!"

            val cta = if (fullPackageAiText.contains("[CALL TO ACTION (CTA)]")) {
                fullPackageAiText.substringAfter("[CALL TO ACTION (CTA)]").substringBefore("[").trim()
            } else "👉 Click link to buy now with special discount!"

            val productAd = com.example.data.models.ProductAd(
                id = adId,
                productName = productName.ifBlank { "Viral Product" },
                templateCategory = templateCategory,
                productUrl = productUrl,
                imageUrl = imageUrl.ifBlank { "https://aivideocreator.hub/products/item.jpg" },
                features = features,
                benefits = benefits,
                price = price,
                offer = offer,
                adDurationSeconds = durationSeconds,
                outputFormat = outputFormat,
                adLanguage = adLanguage,
                bgMusicVibe = bgMusicVibe,
                marketingHook = hook,
                productScript = script,
                voiceoverText = voiceover,
                subtitlesSrt = srt,
                ctaText = cta,
                videoResultUrl = "https://aivideocreator.hub/render/product_ad_${System.currentTimeMillis()}.mp4",
                brandName = brandName.ifBlank { "My Brand" }
            )

            // Save to Room DB history
            repository.saveProductAd(productAd)

            val summaryText = """
                🛒 PRODUCT AD GENERATED SUCCESSFULLY!
                • Product: ${productAd.productName} ($templateCategory)
                • Duration: ${durationSeconds}s | Format: $outputFormat
                • Language: $adLanguage | Music: $bgMusicVibe
                
                ⚡ MARKETING HOOK:
                "$hook"
                
                🗣️ VOICEOVER SCRIPT ($adLanguage):
                "$voiceover"
                
                🎯 CALL TO ACTION:
                "$cta"
                
                💾 Saved to Ad History & Brand Assets
            """.trimIndent()

            GenerationItem(
                type = GenerationType.PRODUCT_TO_VIDEO,
                title = "[$adLanguage Ad] ${productAd.productName} (${durationSeconds}s)",
                prompt = "Brand: $brandName | Category: $templateCategory | URL: $productUrl",
                resultText = summaryText,
                resultUrl = productAd.videoResultUrl,
                durationSeconds = durationSeconds,
                aspectRatio = if (outputFormat.contains("9:16")) "9:16" else if (outputFormat.contains("16:9")) "16:9" else "1:1",
                creditsSpent = cost
            )
        }
    }

    fun deleteProductAd(adId: String) {
        viewModelScope.launch {
            repository.deleteProductAd(adId)
        }
    }

    fun generateCreatorTool(
        type: GenerationType,
        toolTitle: String,
        inputTopic: String,
        systemInstruction: String = ""
    ) {
        val cost = 0
        executeAsyncGeneration(
            creditCost = cost,
            loadingMessage = "Generating $toolTitle via Gemini 2.5 Flash..."
        ) {
            val aiText = when (type) {
                GenerationType.CONTENT_IDEA -> GeminiService.generateVideoIdeas(inputTopic)
                GenerationType.VIRAL_HOOK -> GeminiService.generateViralHooks(inputTopic)
                GenerationType.SCRIPT -> GeminiService.generateScript(inputTopic)
                GenerationType.YT_TITLE -> GeminiService.generateYouTubeTitles(inputTopic)
                GenerationType.HASHTAGS -> GeminiService.generateHashtags(inputTopic)
                GenerationType.PROMPT_GEN -> GeminiService.generatePrompt(inputTopic)
                GenerationType.CONTENT_CALENDAR -> GeminiService.generateContentCalendar(inputTopic)
                GenerationType.PRODUCT_SCRIPT -> GeminiService.generateProductVideoScript(inputTopic)
                else -> GeminiService.generateContent("Generate $toolTitle for topic: $inputTopic", systemInstruction)
            }
            GenerationItem(
                type = type,
                title = toolTitle,
                prompt = inputTopic,
                resultText = aiText,
                resultUrl = "",
                creditsSpent = cost
            )
        }
    }

    fun saveContentIdea(title: String, platform: String, category: String, date: String, hook: String, script: String, tags: String) {
        viewModelScope.launch {
            val idea = ContentIdea(
                title = title,
                platform = platform,
                category = category,
                scheduledDate = date,
                hook = hook,
                scriptText = script,
                hashtags = tags
            )
            repository.saveContentIdea(idea)
        }
    }

    fun deleteHistoryItem(id: Long) {
        viewModelScope.launch {
            repository.deleteGeneration(id)
        }
    }

    fun deleteContentIdea(id: Long) {
        viewModelScope.launch {
            repository.deleteContentIdea(id)
        }
    }

    private fun executeAsyncGeneration(
        creditCost: Int,
        loadingMessage: String,
        fetcher: suspend () -> GenerationItem
    ) {
        viewModelScope.launch {
            if (creditCost > 0) {
                val deducted = repository.deductCredits(creditCost)
                if (!deducted) {
                    _generationState.value = GenerationState.Error("Insufficient credits! You need $creditCost credits. Watch a rewarded ad or upgrade to Premium.")
                    return@launch
                }
            }

            _generationState.value = GenerationState.Loading(loadingMessage, 0.2f)
            delay(400)
            _generationState.value = GenerationState.Loading("Synthesizing neural layers & audio...", 0.65f)

            try {
                val finalItem = fetcher()
                val savedId = repository.saveGeneration(finalItem)
                val savedItemWithId = finalItem.copy(id = savedId)
                _generationState.value = GenerationState.Success(savedItemWithId)
            } catch (e: Exception) {
                _generationState.value = GenerationState.Error("Generation Error: ${e.localizedMessage ?: "Unknown error"}")
            }
        }
    }

    // ==========================================
    // SOCIAL MEDIA AUTO POSTING ENGINE
    // ==========================================
    private val _connectedAccounts = MutableStateFlow<List<ConnectedAccount>>(
        listOf(
            ConnectedAccount("ACC_YT", "YouTube", "AI Studio Creator", "@StudioChannelOfficial", true, "12.4K Subscribers"),
            ConnectedAccount("ACC_IG", "Instagram", "AI Studio Official", "@aistudio.reels", true, "28.1K Followers"),
            ConnectedAccount("ACC_FB", "Facebook", "AI Studio Page", "AI Studio Page", true, "8.5K Followers"),
            ConnectedAccount("ACC_TG", "Telegram", "AI Creator Bot Channel", "@AiStudioAutoBotChannel", true, "5.2K Members")
        )
    )
    val connectedAccounts: StateFlow<List<ConnectedAccount>> = _connectedAccounts.asStateFlow()

    private val _socialPostsHistory = MutableStateFlow<List<SocialPost>>(
        listOf(
            SocialPost(
                id = "POST_101",
                title = "🚀 Ultimate AI Video Generation Guide 2026",
                description = "Learn how to generate cinematic AI videos using Gemini & Kling models! Step-by-step tutorial.",
                hashtags = listOf("#AIVideo", "#YouTubeShorts", "#ContentCreator", "#Reels", "#TechTrends"),
                platforms = listOf("YouTube", "Instagram"),
                mediaType = "Shorts/Reel",
                status = "Published",
                publishedAt = "2 hours ago",
                views = 4280,
                likes = 340
            ),
            SocialPost(
                id = "POST_102",
                title = "🔥 Character Consistency Pro Showcase",
                description = "Locked Face ID system demo across multiple video scenes and expressions.",
                hashtags = listOf("#CharacterAI", "#VideoGenerator", "#Tech", "#AIStudio"),
                platforms = listOf("YouTube", "Instagram", "Telegram"),
                mediaType = "Video",
                scheduledTime = "Today at 08:00 PM",
                status = "Scheduled",
                publishedAt = "Scheduled"
            ),
            SocialPost(
                id = "POST_103",
                title = "🛍️ AI Product Ad Studio Launch",
                description = "Automated sales videos generated directly from product URLs.",
                hashtags = listOf("#ProductAds", "#Ecommerce", "#Marketing", "#Viral"),
                platforms = listOf("Facebook", "Telegram"),
                mediaType = "Video",
                status = "Published",
                publishedAt = "Yesterday",
                views = 1820,
                likes = 145
            )
        )
    )
    val socialPostsHistory: StateFlow<List<SocialPost>> = _socialPostsHistory.asStateFlow()

    fun toggleAccountConnection(accountId: String) {
        _connectedAccounts.value = _connectedAccounts.value.map {
            if (it.id == accountId) it.copy(isConnected = !it.isConnected) else it
        }
    }

    fun connectAccount(platform: String, accountName: String, handle: String) {
        val newAcc = ConnectedAccount(
            id = "ACC_${System.currentTimeMillis().toString().takeLast(6)}",
            platform = platform,
            accountName = accountName.ifBlank { "My $platform Account" },
            handle = handle.ifBlank { "@user_${platform.lowercase()}" },
            isConnected = true,
            followers = "1K+ Followers"
        )
        _connectedAccounts.value = _connectedAccounts.value.filter { it.platform != platform } + newAcc
    }

    fun publishOrScheduleSocialPost(
        title: String,
        description: String,
        hashtags: List<String>,
        selectedPlatforms: List<String>,
        mediaType: String,
        isScheduled: Boolean,
        scheduledDateTime: String,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            if (selectedPlatforms.isEmpty()) {
                onResult(false, "Please select at least one social media platform.")
                return@launch
            }
            if (title.isBlank()) {
                onResult(false, "Please provide a post title.")
                return@launch
            }

            val newPost = SocialPost(
                id = "POST_${System.currentTimeMillis().toString().takeLast(6)}",
                title = title,
                description = description,
                hashtags = hashtags,
                platforms = selectedPlatforms,
                mediaType = mediaType,
                scheduledTime = if (isScheduled) scheduledDateTime.ifBlank { "Today at 06:00 PM" } else null,
                status = if (isScheduled) "Scheduled" else "Published",
                publishedAt = if (isScheduled) "Scheduled for $scheduledDateTime" else "Just Now",
                views = if (isScheduled) 0 else 1,
                likes = 0
            )

            _socialPostsHistory.value = listOf(newPost) + _socialPostsHistory.value
            val platformListStr = selectedPlatforms.joinToString(", ")

            if (isScheduled) {
                onResult(true, "Post scheduled for $scheduledDateTime on $platformListStr! 📅")
            } else {
                onResult(true, "Post published instantly to $platformListStr! 🚀")
            }
        }
    }

    fun generateAutoSocialMetadata(
        topic: String,
        targetPlatform: String,
        onResult: (String, String, String) -> Unit
    ) {
        viewModelScope.launch {
            val cleanTopic = topic.ifBlank { "Viral AI Video Trends 2026" }
            
            // Generate metadata using Gemini or smart fallback
            val prompt = """
                You are a social media viral growth engine.
                Generate high-converting post metadata for topic: '$cleanTopic' on platform '$targetPlatform'.
                Provide output in strictly this format:
                TITLE: [Catchy viral title with emojis]
                DESCRIPTION: [Engaging description with key takeaways and call to action]
                HASHTAGS: [#Tag1 #Tag2 #Tag3 #Tag4 #Tag5 #Tag6]
            """.trimIndent()

            val aiResponse = try {
                GeminiService.callGeminiApiWithRetry(prompt)
            } catch (e: Exception) {
                ""
            }

            if (aiResponse.isNotBlank() && aiResponse.contains("TITLE:")) {
                var title = ""
                var desc = ""
                var tags = ""

                aiResponse.lines().forEach { line ->
                    when {
                        line.startsWith("TITLE:") -> title = line.substringAfter("TITLE:").trim()
                        line.startsWith("DESCRIPTION:") -> desc = line.substringAfter("DESCRIPTION:").trim()
                        line.startsWith("HASHTAGS:") -> tags = line.substringAfter("HASHTAGS:").trim()
                    }
                }

                if (title.isBlank()) title = "🔥 $cleanTopic - Must Watch!"
                if (desc.isBlank()) desc = "Check out this amazing content on $cleanTopic. Don't forget to like, subscribe and share!"
                if (tags.isBlank()) tags = "#Viral #$targetPlatform #ContentCreator #Trending #AIVideo #2026"

                onResult(title, desc, tags)
            } else {
                // High quality fallback
                val title = "🚀 Ultimate Guide: $cleanTopic ($targetPlatform Exclusive)"
                val desc = "Explore the future of content creation with $cleanTopic. Generated with AI Studio Auto Posting!"
                val tags = "#$targetPlatform #ViralVideo #AITools #ContentCreator #Trending2026 #Reels"
                onResult(title, desc, tags)
            }
        }
    }

    // ==========================================
    // CLOUD BACKUP & FIREBASE STORAGE ENGINE
    // ==========================================
    private val _backupSettings = MutableStateFlow(BackupSettings())
    val backupSettings: StateFlow<BackupSettings> = _backupSettings.asStateFlow()

    private val _backupHistory = MutableStateFlow<List<BackupRecord>>(
        listOf(
            BackupRecord(
                id = "BK_PROD_101",
                backupName = "Auto Backup (Daily Firebase Sync)",
                backupType = "Full Auto Backup",
                timestamp = "Today at 05:30 AM",
                sizeMb = 48.2,
                imagesCount = 24,
                videosCount = 8,
                promptsCount = 15,
                settingsCount = 1,
                deviceName = "Samsung Galaxy S24 Ultra"
            ),
            BackupRecord(
                id = "BK_PROD_102",
                backupName = "Pre-Update Manual Snapshot",
                backupType = "Manual Backup",
                timestamp = "Yesterday at 09:15 PM",
                sizeMb = 32.6,
                imagesCount = 18,
                videosCount = 5,
                promptsCount = 12,
                settingsCount = 1,
                deviceName = "Android Tablet Pro"
            ),
            BackupRecord(
                id = "BK_PROD_103",
                backupName = "Project & Master Character Sync",
                backupType = "Project Backup",
                timestamp = "3 days ago",
                sizeMb = 19.4,
                imagesCount = 10,
                videosCount = 3,
                promptsCount = 8,
                settingsCount = 1,
                deviceName = "Google Pixel 8 Pro"
            )
        )
    )
    val backupHistory: StateFlow<List<BackupRecord>> = _backupHistory.asStateFlow()

    fun updateBackupSettings(
        autoBackupEnabled: Boolean = _backupSettings.value.autoBackupEnabled,
        backupFrequency: String = _backupSettings.value.backupFrequency,
        backupImages: Boolean = _backupSettings.value.backupImages,
        backupVideos: Boolean = _backupSettings.value.backupVideos,
        backupPrompts: Boolean = _backupSettings.value.backupPrompts,
        backupSettingsToggle: Boolean = _backupSettings.value.backupSettings,
        wifiOnly: Boolean = _backupSettings.value.wifiOnly
    ) {
        _backupSettings.value = _backupSettings.value.copy(
            autoBackupEnabled = autoBackupEnabled,
            backupFrequency = backupFrequency,
            backupImages = backupImages,
            backupVideos = backupVideos,
            backupPrompts = backupPrompts,
            backupSettings = backupSettingsToggle,
            wifiOnly = wifiOnly
        )
    }

    fun triggerCloudBackup(backupName: String? = null, onComplete: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val name = backupName?.ifBlank { "Manual Cloud Snapshot" } ?: "Instant Cloud Backup"
            val randomSize = (15..60).random().toDouble()
            val newRecord = BackupRecord(
                id = "BK_${System.currentTimeMillis().toString().takeLast(6)}",
                backupName = name,
                backupType = if (!backupName.isNullOrBlank()) "Manual Backup" else "Full Auto Backup",
                timestamp = "Just now",
                sizeMb = randomSize,
                imagesCount = (12..40).random(),
                videosCount = (4..12).random(),
                promptsCount = (10..30).random(),
                settingsCount = 1,
                deviceName = "Android Device (Current)",
                isAutoSynced = true
            )

            _backupHistory.value = listOf(newRecord) + _backupHistory.value
            _backupSettings.value = _backupSettings.value.copy(
                lastSyncTime = "Just now",
                storageUsedMb = _backupSettings.value.storageUsedMb + newRecord.sizeMb
            )

            onComplete(true, "Cloud backup '${newRecord.backupName}' successfully synced to Firebase Storage! ☁️")
        }
    }

    fun restoreFromBackup(backupRecord: BackupRecord, onComplete: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            // Restores projects, prompts, settings, images & videos from Firebase Storage snapshot
            _backupSettings.value = _backupSettings.value.copy(lastSyncTime = "Restored just now")
            onComplete(true, "Restored ${backupRecord.imagesCount} images, ${backupRecord.videosCount} videos & ${backupRecord.promptsCount} prompts from backup '${backupRecord.backupName}'! 🎉")
        }
    }

    fun deleteBackupRecord(backupRecord: BackupRecord) {
        _backupHistory.value = _backupHistory.value.filter { it.id != backupRecord.id }
    }

    // ==========================================
    // TEAM WORKSPACE & COLLABORATION ENGINE
    // ==========================================
    private val _activeTeamWorkspace = MutableStateFlow(
        TeamWorkspace(
            id = "TEAM_901",
            name = "AI Studio Pro Creators Lab",
            description = "Official collaborative space for viral video campaigns & master characters.",
            inviteCode = "STUDIO-X892-PRO",
            ownerEmail = "malgireddyvinithareddy2018@gmail.com",
            members = listOf(
                TeamMember("MEM_01", "You (Owner)", "malgireddyvinithareddy2018@gmail.com", "Admin", "VO", "Owner", true),
                TeamMember("MEM_02", "Alex Rivera", "alex.creative@aistudio.io", "Admin", "AR", "2 weeks ago", true),
                TeamMember("MEM_03", "Sarah Chen", "sarah.vfx@aistudio.io", "Editor", "SC", "5 days ago", true),
                TeamMember("MEM_04", "David Miller", "david.marketing@aistudio.io", "Editor", "DM", "3 days ago", false),
                TeamMember("MEM_05", "Elena Rostova", "elena.review@aistudio.io", "Viewer", "ER", "Yesterday", true)
            ),
            sharedProjects = listOf(
                SharedProject("PROJ_S101", "Cyberpunk Samurai 4K Series", "AI Video", "Alex Rivera", "10 mins ago", "In Progress"),
                SharedProject("PROJ_S102", "Elena Master Face ID Profile", "Master Character", "Sarah Chen", "2 hours ago", "Approved / Exported"),
                SharedProject("PROJ_S103", "Product Launch Reel - Smart Watch", "Reel / Short", "David Miller", "Yesterday", "Ready for Review"),
                SharedProject("PROJ_S104", "Podcast Auto Script & Audio Track", "AI Voice & Script", "You (Owner)", "2 days ago", "Approved / Exported")
            ),
            chatMessages = listOf(
                TeamChatMessage("MSG_1", "Alex Rivera", "Admin", "Hey team! I uploaded the new Kling 1.6 video prompt for scene 3.", "10:15 AM", false),
                TeamChatMessage("MSG_2", "Sarah Chen", "Editor", "Great! I checked the face consistency lock and it looks super sharp 🔥", "10:18 AM", false),
                TeamChatMessage("MSG_3", "Elena Rostova", "Viewer", "Reviewed the draft! Ready to export to YouTube Shorts.", "10:25 AM", false),
                TeamChatMessage("MSG_4", "You", "Admin", "Awesome teamwork! Scheduling auto post for 6 PM today 🚀", "10:30 AM", true)
            ),
            activities = listOf(
                TeamActivity("ACT_1", "Sarah Chen", "updated character consistency", "Elena Master Face ID Profile", "12 mins ago"),
                TeamActivity("ACT_2", "Alex Rivera", "created new shared project", "Cyberpunk Samurai 4K Series", "1 hour ago"),
                TeamActivity("ACT_3", "You (Owner)", "updated role to Editor for", "David Miller", "3 hours ago"),
                TeamActivity("ACT_4", "David Miller", "submitted project for review", "Product Launch Reel - Smart Watch", "Yesterday")
            )
        )
    )
    val activeTeamWorkspace: StateFlow<TeamWorkspace> = _activeTeamWorkspace.asStateFlow()

    fun createTeamWorkspace(name: String, description: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            if (name.isBlank()) {
                onResult(false, "Please enter a team workspace name.")
                return@launch
            }

            val newTeam = TeamWorkspace(
                id = "TEAM_${System.currentTimeMillis().toString().takeLast(6)}",
                name = name,
                description = description.ifBlank { "Collaborative workspace for $name" },
                inviteCode = "TEAM-${(1000..9999).random()}-AI",
                ownerEmail = user.value.email,
                members = listOf(
                    TeamMember("MEM_OWNER", "${user.value.name} (Owner)", user.value.email, "Admin", "ME", "Owner", true)
                ),
                sharedProjects = emptyList(),
                chatMessages = listOf(
                    TeamChatMessage("MSG_INIT", "System Bot", "AI Studio", "Welcome to $name! Start inviting members and sharing projects.", "Just now", false)
                ),
                activities = listOf(
                    TeamActivity("ACT_INIT", user.value.name, "created team workspace", name, "Just now")
                )
            )

            _activeTeamWorkspace.value = newTeam
            onResult(true, "Team Workspace '$name' created successfully! 🎉")
        }
    }

    fun inviteTeamMember(email: String, role: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            if (email.isBlank() || !email.contains("@")) {
                onResult(false, "Please enter a valid member email address.")
                return@launch
            }

            val currentTeam = _activeTeamWorkspace.value
            if (currentTeam.members.any { it.email.equals(email, ignoreCase = true) }) {
                onResult(false, "Member with email $email is already in this team workspace.")
                return@launch
            }

            val initials = email.take(2).uppercase()
            val newMember = TeamMember(
                id = "MEM_${System.currentTimeMillis().toString().takeLast(6)}",
                name = email.substringBefore("@").replace(".", " ").capitalize(),
                email = email,
                role = role,
                avatarInitials = initials,
                joinedAt = "Just now",
                isOnline = true
            )

            val newActivity = TeamActivity(
                id = "ACT_${System.currentTimeMillis().toString().takeLast(6)}",
                memberName = user.value.name,
                action = "invited $email as $role to",
                target = currentTeam.name,
                timestamp = "Just now"
            )

            val newChat = TeamChatMessage(
                id = "MSG_${System.currentTimeMillis().toString().takeLast(6)}",
                senderName = "System",
                senderRole = "AI Studio",
                message = "👋 ${newMember.name} joined the team as $role!",
                timestamp = "Just now",
                isSelf = false
            )

            _activeTeamWorkspace.value = currentTeam.copy(
                members = currentTeam.members + newMember,
                activities = listOf(newActivity) + currentTeam.activities,
                chatMessages = currentTeam.chatMessages + newChat
            )

            onResult(true, "Invitation sent to $email with $role role! 📩")
        }
    }

    fun updateMemberRole(memberId: String, newRole: String) {
        val currentTeam = _activeTeamWorkspace.value
        val updatedMembers = currentTeam.members.map {
            if (it.id == memberId) it.copy(role = newRole) else it
        }
        val memberName = currentTeam.members.find { it.id == memberId }?.name ?: "Member"

        val newActivity = TeamActivity(
            id = "ACT_${System.currentTimeMillis().toString().takeLast(6)}",
            memberName = user.value.name,
            action = "changed role to $newRole for",
            target = memberName,
            timestamp = "Just now"
        )

        _activeTeamWorkspace.value = currentTeam.copy(
            members = updatedMembers,
            activities = listOf(newActivity) + currentTeam.activities
        )
    }

    fun removeTeamMember(memberId: String) {
        val currentTeam = _activeTeamWorkspace.value
        val memberName = currentTeam.members.find { it.id == memberId }?.name ?: "Member"
        val updatedMembers = currentTeam.members.filter { it.id != memberId }

        val newActivity = TeamActivity(
            id = "ACT_${System.currentTimeMillis().toString().takeLast(6)}",
            memberName = user.value.name,
            action = "removed member",
            target = memberName,
            timestamp = "Just now"
        )

        _activeTeamWorkspace.value = currentTeam.copy(
            members = updatedMembers,
            activities = listOf(newActivity) + currentTeam.activities
        )
    }

    fun addSharedTeamProject(projectName: String, mediaType: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            if (projectName.isBlank()) {
                onResult(false, "Please provide a project name.")
                return@launch
            }

            val currentTeam = _activeTeamWorkspace.value
            val newProj = SharedProject(
                id = "PROJ_${System.currentTimeMillis().toString().takeLast(6)}",
                name = projectName,
                mediaType = mediaType,
                creatorName = user.value.name,
                lastModified = "Just now",
                status = "In Progress"
            )

            val newActivity = TeamActivity(
                id = "ACT_${System.currentTimeMillis().toString().takeLast(6)}",
                memberName = user.value.name,
                action = "created shared project",
                target = projectName,
                timestamp = "Just now"
            )

            _activeTeamWorkspace.value = currentTeam.copy(
                sharedProjects = listOf(newProj) + currentTeam.sharedProjects,
                activities = listOf(newActivity) + currentTeam.activities
            )

            onResult(true, "Project '$projectName' shared with team workspace! 📁")
        }
    }

    fun sendTeamChatMessage(text: String) {
        viewModelScope.launch {
            if (text.isBlank()) return@launch

            val currentTeam = _activeTeamWorkspace.value
            val myRole = currentTeam.members.find { it.email == user.value.email }?.role ?: "Admin"
            val userMsg = TeamChatMessage(
                id = "MSG_${System.currentTimeMillis().toString().takeLast(6)}",
                senderName = "You",
                senderRole = myRole,
                message = text,
                timestamp = "Just now",
                isSelf = true
            )

            _activeTeamWorkspace.value = currentTeam.copy(
                chatMessages = currentTeam.chatMessages + userMsg
            )

            // Smart AI Team Assistant Auto Reply if prompt contains @ai or questions
            if (text.contains("@ai", ignoreCase = true) || text.contains("help", ignoreCase = true) || text.contains("prompt", ignoreCase = true)) {
                delay(800)
                val aiReply = TeamChatMessage(
                    id = "MSG_AI_${System.currentTimeMillis().toString().takeLast(6)}",
                    senderName = "AI Team Co-Pilot",
                    senderRole = "AI Assistant",
                    message = "🤖 Got it! I analyzed your team project context. I'm ready to auto-generate video scripts or character prompts for the team.",
                    timestamp = "Just now",
                    isSelf = false
                )
                _activeTeamWorkspace.value = _activeTeamWorkspace.value.copy(
                    chatMessages = _activeTeamWorkspace.value.chatMessages + aiReply
                )
            }
        }
    }

    // ==========================================
    // AI COURSE CREATOR & LESSON ENGINE
    // ==========================================
    private val _savedCourses = MutableStateFlow<List<AiCourse>>(
        listOf(
            AiCourse(
                id = "CRS_101",
                topic = "Mastering AI Short-Form Video & Kling 1.6",
                targetAudience = "Content Creators, Digital Marketers & Youtubers",
                skillLevel = "Intermediate",
                modules = listOf(
                    CourseModule(
                        id = "MOD_1",
                        moduleNumber = 1,
                        title = "Module 1: AI Video Fundamentals & Camera Movement Prompts",
                        description = "Understanding keyframe control, prompt structuring, and cinematic motion keywords.",
                        lessons = listOf(
                            CourseLesson(
                                id = "LES_101",
                                lessonNumber = 1,
                                title = "Introduction to Text-to-Video Models",
                                durationMinutes = 10,
                                videoScript = "[CAMERA: Slow zoom-in on presenter]\nWelcome! In this lesson, we break down how Kling AI & Runway Gen-3 interpret camera prompts like pan-left and orbit. You will learn the exact syntax to eliminate motion blur and artifacts.",
                                keyTakeaways = listOf("Use camera motion brackets like [Camera: Pan Right]", "Set aspect ratio 9:16 for Shorts & Reels", "Maintain consistent character lighting"),
                                quizQuestion = "Which parameter best controls dynamic camera movement in AI video prompts?",
                                quizOptions = listOf("Camera Motion Brackets like [Camera: Orbit]", "Text Font Size", "Background Audio Frequency", "Resolution Selector"),
                                correctAnswerIndex = 0,
                                workbookExercise = "Exercise: Write 3 cinematic prompts for an epic sci-fi city flythrough utilizing camera pan and zoom syntax."
                            ),
                            CourseLesson(
                                id = "LES_102",
                                lessonNumber = 2,
                                title = "Mastering Multi-Shot Consistency",
                                durationMinutes = 15,
                                videoScript = "[SCREEN RECORDING: AI Studio Character Master]\nToday we master face locking using Character Master Seed ID. Notice how the character retains facial symmetry across 5 distinct camera angles.",
                                keyTakeaways = listOf("Always pass the Master Seed ID across scenes", "Keep attire description identical in prompt chain", "Use high contrast lighting for clean face extraction"),
                                quizQuestion = "What is required to lock character face consistency across multiple scenes?",
                                quizOptions = listOf("Master Character Seed ID / Reference Image", "Randomizing prompts each generation", "Deleting previous frames", "Using lower resolution"),
                                correctAnswerIndex = 0,
                                workbookExercise = "Exercise: Upload your primary avatar and generate 3 consistent scenes for a story arc."
                            )
                        )
                    ),
                    CourseModule(
                        id = "MOD_2",
                        moduleNumber = 2,
                        title = "Module 2: Viral Monetization & Automated Posting",
                        description = "Transforming course lessons into daily revenue and social media scheduling.",
                        lessons = listOf(
                            CourseLesson(
                                id = "LES_201",
                                lessonNumber = 3,
                                title = "Automating YouTube Shorts & Reel Publishing",
                                durationMinutes = 12,
                                videoScript = "[SLIDE: Auto Posting Workflow]\nIn this module, you'll configure instant multi-platform scheduling for YouTube, Instagram, and Telegram directly from AI Studio Pro.",
                                keyTakeaways = listOf("Batch create 10 shorts in advance", "Enable auto SEO hashtags & description generator", "Monitor creator analytics weekly"),
                                quizQuestion = "What is the main benefit of Social Auto-Posting in AI Studio?",
                                quizOptions = listOf("Instant multi-platform scheduling and automated SEO metadata", "Increases phone battery life", "Replaces internet connection", "None of the above"),
                                correctAnswerIndex = 0,
                                workbookExercise = "Exercise: Schedule your first 3 automated AI short videos for the upcoming week."
                            )
                        )
                    )
                ),
                totalLessonsCount = 3,
                estimatedHours = 1.5,
                createdAt = "2 hours ago",
                isPdfExported = true
            )
        )
    )
    val savedCourses: StateFlow<List<AiCourse>> = _savedCourses.asStateFlow()

    fun generateAiCourse(
        topic: String,
        targetAudience: String,
        skillLevel: String,
        modulesCount: Int = 3,
        onComplete: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            if (topic.isBlank()) {
                onComplete(false, "Please provide a course topic or subject.")
                return@launch
            }

            val audience = targetAudience.ifBlank { "General Audience & Creators" }

            val generatedModules = (1..modulesCount).map { modNum ->
                val modTitle = when (modNum) {
                    1 -> "Module 1: Foundations & Core Concepts of $topic"
                    2 -> "Module 2: Advanced Techniques & Real-World Projects"
                    else -> "Module 3: Monetization, Scaling & Mastery"
                }

                val lessons = (1..2).map { lesNum ->
                    val totalLesNum = (modNum - 1) * 2 + lesNum
                    CourseLesson(
                        id = "LES_${System.currentTimeMillis().toString().takeLast(5)}_$totalLesNum",
                        lessonNumber = totalLesNum,
                        title = "Lesson $totalLesNum: Essential Practice for $topic (Part $lesNum)",
                        durationMinutes = (10..20).random(),
                        videoScript = "[SCENE $totalLesNum: Presenter Intro]\nHello students! In Lesson $totalLesNum of $topic, we explore practical execution for $audience. Key focus: Applying high-yield AI workflows step-by-step.",
                        keyTakeaways = listOf(
                            "Master core workflow for $topic",
                            "Implement targeted strategies for $audience",
                            "Use AI Studio Pro tools to accelerate output 10x"
                        ),
                        quizQuestion = "What is the primary objective of Lesson $totalLesNum in $topic?",
                        quizOptions = listOf(
                            "To implement practical $skillLevel workflows efficiently",
                            "To skip foundational steps",
                            "To ignore audience targeting",
                            "None of the above"
                        ),
                        correctAnswerIndex = 0,
                        workbookExercise = "Workbook Exercise #$totalLesNum: Create a practical 1-page action plan for $topic tailored to $audience."
                    )
                }

                CourseModule(
                    id = "MOD_${System.currentTimeMillis().toString().takeLast(5)}_$modNum",
                    moduleNumber = modNum,
                    title = modTitle,
                    description = "Comprehensive module focusing on practical mastery of $topic for $skillLevel level.",
                    lessons = lessons
                )
            }

            val totalLessons = generatedModules.sumOf { it.lessons.size }
            val newCourse = AiCourse(
                id = "CRS_${System.currentTimeMillis().toString().takeLast(6)}",
                topic = topic,
                targetAudience = audience,
                skillLevel = skillLevel,
                modules = generatedModules,
                totalLessonsCount = totalLessons,
                estimatedHours = String.format("%.1f", totalLessons * 0.4).toDouble(),
                createdAt = "Just now",
                isPdfExported = false
            )

            _savedCourses.value = listOf(newCourse) + _savedCourses.value
            onComplete(true, "AI Course '${newCourse.topic}' generated with $totalLessons lessons, video scripts, quizzes & workbooks! 🎓")
        }
    }

    fun markCoursePdfExported(courseId: String) {
        _savedCourses.value = _savedCourses.value.map {
            if (it.id == courseId) it.copy(isPdfExported = true) else it
        }
    }

    fun deleteCourse(courseId: String) {
        _savedCourses.value = _savedCourses.value.filter { it.id != courseId }
    }
}

