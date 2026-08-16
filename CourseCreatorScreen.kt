package com.example.ui.screens.generators

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.AiCourse
import com.example.data.models.CourseLesson
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel

@Composable
fun CourseCreatorScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    var topicInput by remember { mutableStateOf("") }
    var audienceInput by remember { mutableStateOf("") }
    var selectedSkillLevel by remember { mutableStateOf("Intermediate") }
    var modulesCount by remember { mutableStateOf(3) }

    var isGenerating by remember { mutableStateOf(false) }
    var activeTab by remember { mutableStateOf(0) } // 0: Generator, 1: Saved Courses & Curriculums

    // Selected Course Detail State
    var selectedCourseForDetail by remember { mutableStateOf<AiCourse?>(null) }
    var activeSubTab by remember { mutableStateOf(0) } // 0: Lessons & Scripts, 1: Quizzes, 2: Workbooks

    // Quiz Test Modal State
    var activeQuizLesson by remember { mutableStateOf<CourseLesson?>(null) }
    var selectedQuizOptionIndex by remember { mutableStateOf<Int?>(null) }
    var quizSubmitted by remember { mutableStateOf(false) }

    val savedCourses by viewModel.savedCourses.collectAsState()

    val skillLevels = listOf("Beginner", "Intermediate", "Advanced")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("course_creator_screen")
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(DarkSurface)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("AI COURSE CREATOR", fontWeight = FontWeight.Black, fontSize = 16.sp, color = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(AccentGold)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("CURRICULUM ENGINE", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.Black)
                    }
                }
                Text("Outlines, Lesson Scripts, Quizzes, Workbooks & PDF Export", fontSize = 11.sp, color = AccentCyan)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Navigation Tabs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("1. Course Generator", "2. Saved Courses (${savedCourses.size})").forEachIndexed { idx, title ->
                val isSel = activeTab == idx
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSel) PrimaryPurple else DarkSurface)
                        .border(1.dp, if (isSel) AccentGold else Color(0xFF2E2954), RoundedCornerShape(12.dp))
                        .clickable { activeTab = idx }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        title,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSel) Color.White else Color.LightGray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (activeTab) {
            // TAB 0: COURSE GENERATOR FORM
            0 -> {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFF2E2954), RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("CREATE AI COURSE CURRICULUM", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Course Subject or Topic:", fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = topicInput,
                            onValueChange = { topicInput = it },
                            placeholder = { Text("e.g. Master AI Video Editing with Kling & Runway Gen-3", color = Color.Gray, fontSize = 11.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = AccentCyan)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Target Audience:", fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = audienceInput,
                            onValueChange = { audienceInput = it },
                            placeholder = { Text("e.g. Content Creators, YouTubers & Digital Agency Owners", color = Color.Gray, fontSize = 11.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = AccentCyan)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Skill Level:", fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    skillLevels.forEach { lvl ->
                                        val isSel = selectedSkillLevel == lvl
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                                .border(1.dp, if (isSel) AccentGold else Color.Transparent, RoundedCornerShape(8.dp))
                                                .clickable { selectedSkillLevel = lvl }
                                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                        ) {
                                            Text(lvl, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.White else Color.LightGray)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text("Modules Count:", fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF1E1A36))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    IconButton(
                                        onClick = { if (modulesCount > 1) modulesCount-- },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = Color.White)
                                    }
                                    Text("$modulesCount", fontWeight = FontWeight.Black, color = AccentCyan, fontSize = 14.sp)
                                    IconButton(
                                        onClick = { if (modulesCount < 6) modulesCount++ },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = "Increase", tint = Color.White)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Features Included Badges
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1A36)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("INCLUDED IN THIS GENERATION", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("✅ Complete Course Outline & Structure\n✅ Ready-to-Record Video Lesson Scripts\n✅ Quiz Questions & Answer Keys\n✅ Practical Student Workbooks & Exercises", fontSize = 11.sp, color = Color.LightGray)
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                isGenerating = true
                                viewModel.generateAiCourse(topicInput, audienceInput, selectedSkillLevel, modulesCount) { success, msg ->
                                    isGenerating = false
                                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                    if (success) activeTab = 1
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues(0.dp),
                            enabled = !isGenerating
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Brush.horizontalGradient(listOf(PrimaryPurple, AccentCyan))),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (isGenerating) {
                                        CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("GENERATING FULL CURRICULUM...", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    } else {
                                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("GENERATE COMPLETE AI COURSE", fontWeight = FontWeight.Black, fontSize = 12.sp, color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // TAB 1: SAVED COURSES & CURRICULUM VIEWER
            1 -> {
                if (selectedCourseForDetail == null) {
                    // List of courses
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("SAVED AI COURSES & CURRICULUMS", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = AccentGold)
                            Text("${savedCourses.size} Available", fontSize = 11.sp, color = Color.Gray)
                        }

                        if (savedCourses.isEmpty()) {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.School, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(36.dp))
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text("No Courses Generated Yet", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                                    Text("Generate your first course curriculum in Tab 1!", fontSize = 11.sp, color = Color.Gray)
                                }
                            }
                        } else {
                            savedCourses.forEach { course ->
                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(1.dp, Color(0xFF2E2954), RoundedCornerShape(16.dp))
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Default.MenuBook, contentDescription = null, tint = AccentCyan, modifier = Modifier.size(20.dp))
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(course.topic, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                                            }

                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(AccentGold)
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text(course.skillLevel.uppercase(), fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.Black)
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text("Target Audience: ${course.targetAudience}", fontSize = 11.sp, color = AccentCyan)
                                        Text("📚 ${course.modules.size} Modules • 🎥 ${course.totalLessonsCount} Lessons & Video Scripts • ⏱️ ${course.estimatedHours} Hours Content", fontSize = 10.sp, color = Color.LightGray)

                                        Spacer(modifier = Modifier.height(14.dp))

                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            Button(
                                                onClick = { selectedCourseForDetail = course },
                                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                                                shape = RoundedCornerShape(10.dp),
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Icon(Icons.Default.Visibility, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text("Open Curriculum", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                            }

                                            OutlinedButton(
                                                onClick = {
                                                    viewModel.markCoursePdfExported(course.id)
                                                    Toast.makeText(context, "Exporting '${course.topic}' curriculum as formatted PDF... 📄", Toast.LENGTH_LONG).show()
                                                },
                                                shape = RoundedCornerShape(10.dp),
                                                border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.horizontalGradient(listOf(AccentCyan, AccentGold)))
                                            ) {
                                                Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = AccentCyan, modifier = Modifier.size(16.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(if (course.isPdfExported) "PDF Exported ✓" else "Export PDF", fontSize = 10.sp, color = AccentCyan, fontWeight = FontWeight.Bold)
                                            }

                                            IconButton(
                                                onClick = { viewModel.deleteCourse(course.id) },
                                                modifier = Modifier.size(36.dp)
                                            ) {
                                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red, modifier = Modifier.size(18.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // COURSE DETAIL VIEW
                    val course = selectedCourseForDetail!!

                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TextButton(onClick = { selectedCourseForDetail = null }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = AccentCyan)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Back to Courses", color = AccentCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkSurface),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(course.topic, fontWeight = FontWeight.Black, fontSize = 15.sp, color = Color.White)
                                Text("Audience: ${course.targetAudience} • Level: ${course.skillLevel}", fontSize = 11.sp, color = AccentCyan)

                                Spacer(modifier = Modifier.height(12.dp))

                                // Sub Tabs (Lessons, Quizzes, Workbooks)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    listOf("Lessons & Scripts", "Quizzes", "Workbooks").forEachIndexed { idx, title ->
                                        val isSel = activeSubTab == idx
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                                .border(1.dp, if (isSel) AccentGold else Color.Transparent, RoundedCornerShape(8.dp))
                                                .clickable { activeSubTab = idx }
                                                .padding(vertical = 8.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(title, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.White else Color.LightGray)
                                        }
                                    }
                                }
                            }
                        }

                        when (activeSubTab) {
                            // LESSONS & VIDEO SCRIPTS
                            0 -> {
                                course.modules.forEach { module ->
                                    Card(
                                        shape = RoundedCornerShape(14.dp),
                                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .border(1.dp, Color(0xFF2E2954), RoundedCornerShape(14.dp))
                                    ) {
                                        Column(modifier = Modifier.padding(14.dp)) {
                                            Text(module.title, fontWeight = FontWeight.Bold, color = AccentGold, fontSize = 13.sp)
                                            Text(module.description, fontSize = 10.sp, color = Color.Gray)

                                            Spacer(modifier = Modifier.height(10.dp))

                                            module.lessons.forEach { lesson ->
                                                Card(
                                                    shape = RoundedCornerShape(10.dp),
                                                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1A36)),
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(vertical = 4.dp)
                                                ) {
                                                    Column(modifier = Modifier.padding(10.dp)) {
                                                        Row(
                                                            modifier = Modifier.fillMaxWidth(),
                                                            horizontalArrangement = Arrangement.SpaceBetween,
                                                            verticalAlignment = Alignment.CenterVertically
                                                        ) {
                                                            Text(lesson.title, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 12.sp)
                                                            Text("⏱️ ${lesson.durationMinutes} mins", fontSize = 10.sp, color = AccentCyan)
                                                        }

                                                        Spacer(modifier = Modifier.height(6.dp))

                                                        Text("🎬 Video Lesson Teleprompter Script:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                                                        Text(lesson.videoScript, fontSize = 11.sp, color = Color.LightGray)

                                                        Spacer(modifier = Modifier.height(6.dp))

                                                        Row(
                                                            modifier = Modifier.fillMaxWidth(),
                                                            horizontalArrangement = Arrangement.End
                                                        ) {
                                                            TextButton(
                                                                onClick = {
                                                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                                                    val clip = ClipData.newPlainText("Video Script", lesson.videoScript)
                                                                    clipboard.setPrimaryClip(clip)
                                                                    Toast.makeText(context, "Teleprompter video script copied! 📋", Toast.LENGTH_SHORT).show()
                                                                }
                                                            ) {
                                                                Icon(Icons.Default.ContentCopy, contentDescription = null, tint = AccentCyan, modifier = Modifier.size(14.dp))
                                                                Spacer(modifier = Modifier.width(4.dp))
                                                                Text("Copy Script", fontSize = 10.sp, color = AccentCyan)
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            // QUIZZES
                            1 -> {
                                course.modules.flatMap { it.lessons }.forEach { lesson ->
                                    Card(
                                        shape = RoundedCornerShape(14.dp),
                                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .border(1.dp, Color(0xFF2E2954), RoundedCornerShape(14.dp))
                                    ) {
                                        Column(modifier = Modifier.padding(14.dp)) {
                                            Text(lesson.title, fontWeight = FontWeight.Bold, color = AccentCyan, fontSize = 12.sp)
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text("❓ Quiz Question: ${lesson.quizQuestion}", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 12.sp)

                                            Spacer(modifier = Modifier.height(8.dp))

                                            lesson.quizOptions.forEachIndexed { optIdx, optText ->
                                                Text("  ${optIdx + 1}. $optText", fontSize = 11.sp, color = Color.LightGray)
                                            }

                                            Spacer(modifier = Modifier.height(8.dp))

                                            Button(
                                                onClick = {
                                                    activeQuizLesson = lesson
                                                    selectedQuizOptionIndex = null
                                                    quizSubmitted = false
                                                },
                                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                                                shape = RoundedCornerShape(8.dp)
                                            ) {
                                                Text("Take Interactive Quiz", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }

                            // WORKBOOKS & EXERCISES
                            2 -> {
                                course.modules.flatMap { it.lessons }.forEach { lesson ->
                                    Card(
                                        shape = RoundedCornerShape(14.dp),
                                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .border(1.dp, Color(0xFF2E2954), RoundedCornerShape(14.dp))
                                    ) {
                                        Column(modifier = Modifier.padding(14.dp)) {
                                            Text(lesson.title, fontWeight = FontWeight.Bold, color = AccentGold, fontSize = 12.sp)
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text("📝 Student Workbook Assignment:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                                            Text(lesson.workbookExercise, fontSize = 11.sp, color = Color.White)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Quiz Testing Modal
        if (activeQuizLesson != null) {
            val qLesson = activeQuizLesson!!
            AlertDialog(
                onDismissRequest = { activeQuizLesson = null },
                containerColor = DarkSurface,
                title = { Text("Quiz: ${qLesson.title}", fontWeight = FontWeight.Bold, color = Color.White) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(qLesson.quizQuestion, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AccentGold)

                        qLesson.quizOptions.forEachIndexed { idx, opt ->
                            val isSel = selectedQuizOptionIndex == idx
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                    .border(1.dp, if (isSel) AccentCyan else Color.Transparent, RoundedCornerShape(8.dp))
                                    .clickable { if (!quizSubmitted) selectedQuizOptionIndex = idx }
                                    .padding(10.dp)
                            ) {
                                Text("${idx + 1}. $opt", fontSize = 11.sp, color = Color.White)
                            }
                        }

                        if (quizSubmitted) {
                            val isCorrect = selectedQuizOptionIndex == qLesson.correctAnswerIndex
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isCorrect) Color(0xFF1B5E20) else Color(0xFFB71C1C))
                                    .padding(10.dp)
                            ) {
                                Text(
                                    text = if (isCorrect) "🎉 Correct Answer! Great job!" else "❌ Incorrect. Correct choice was option ${qLesson.correctAnswerIndex + 1}.",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (!quizSubmitted) {
                                if (selectedQuizOptionIndex == null) {
                                    Toast.makeText(context, "Please select an answer option.", Toast.LENGTH_SHORT).show()
                                } else {
                                    quizSubmitted = true
                                }
                            } else {
                                activeQuizLesson = null
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                    ) {
                        Text(if (!quizSubmitted) "Submit Answer" else "Done", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { activeQuizLesson = null }) {
                        Text("Close", color = Color.Gray)
                    }
                }
            )
        }
    }
}
