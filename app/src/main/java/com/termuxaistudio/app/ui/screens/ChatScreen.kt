package com.termuxaistudio.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val modelName: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

data class AiModel(
    val id: String,
    val name: String,
    val description: String,
    val isFree: Boolean,
    val needsKey: Boolean
)

val availableModels = listOf(
    AiModel("opencode-free", "OpenCode Free", "مدل رایگان داخلی OpenCode - بدون نیاز به کلید", true, false),
    AiModel("deepseek-free", "DeepSeek Flash Free", "مدل رایگان کدنویسی - مناسب ساخت برنامه", true, false),
    AiModel("gemini-flash", "Gemini Flash", "مدل رایگان گوگل - نیاز به API Key رایگان", true, true),
    AiModel("deepseek", "DeepSeek Chat", "کیفیت بالا برای کدنویسی - نیاز به API Key", false, true),
    AiModel("local-sim", "Local Simulator", "پاسخ شبیه‌سازی‌شده آفلاین", true, false)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen() {
    val messages = remember {
        mutableStateListOf(
            ChatMessage(
                text = "سلام! من دستیار کدنویسی و طراحی شما هستم.\n\nاز منوی بالا می‌توانید مدل هوش مصنوعی را انتخاب کنید.\nمدل‌های رایگان بدون کلید هم موجود هستند.\n\nدستورات مفید:\n• کد بنویس\n• اینفوگرافیک بساز\n• برنامه اندروید طراحی کن",
                isUser = false,
                modelName = "System"
            )
        )
    }
    var inputText by remember { mutableStateOf("") }
    var selectedModel by remember { mutableStateOf(availableModels[0]) }
    var showModelPicker by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header with model selector
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "AI Chat",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    // Model selector button
                    FilterChip(
                        selected = true,
                        onClick = { showModelPicker = true },
                        label = {
                            Text(
                                text = selectedModel.name,
                                maxLines = 1
                            )
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.SmartToy,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        trailingIcon = {
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    )
                }
                // Status bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (selectedModel.isFree) Icons.Default.CheckCircle else Icons.Default.Key,
                        contentDescription = null,
                        tint = if (selectedModel.isFree) Color(0xFF3FB950) else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (selectedModel.isFree) "رایگان • آماده استفاده" else "نیاز به API Key",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Messages
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(messages) { message ->
                MessageBubble(message)
            }
            if (isLoading) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("در حال فکر کردن...", fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
        }

        // Input
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 3.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* Attach later */ }) {
                    Icon(Icons.Default.AttachFile, contentDescription = "پیوست")
                }
                BasicTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(24.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    decorationBox = { inner ->
                        if (inputText.isEmpty()) {
                            Text(
                                "پیام بنویسید یا دستور بدهید...",
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                        }
                        inner()
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                FilledIconButton(
                    onClick = {
                        if (inputText.isNotBlank() && !isLoading) {
                            val userMsg = inputText.trim()
                            messages.add(ChatMessage(userMsg, true))
                            inputText = ""
                            isLoading = true
                            scope.launch {
                                delay(800) // simulate thinking
                                val reply = generateReply(userMsg, selectedModel)
                                messages.add(
                                    ChatMessage(
                                        text = reply,
                                        isUser = false,
                                        modelName = selectedModel.name
                                    )
                                )
                                isLoading = false
                                listState.animateScrollToItem(messages.size)
                            }
                        }
                    },
                    enabled = !isLoading
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "ارسال")
                }
            }
        }
    }

    // Model Picker Dialog
    if (showModelPicker) {
        AlertDialog(
            onDismissRequest = { showModelPicker = false },
            title = { Text("انتخاب هوش مصنوعی") },
            text = {
                Column {
                    availableModels.forEach { model ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedModel = model
                                    showModelPicker = false
                                    messages.add(
                                        ChatMessage(
                                            text = "مدل تغییر کرد به: ${model.name}\n${model.description}",
                                            isUser = false,
                                            modelName = "System"
                                        )
                                    )
                                }
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedModel.id == model.id,
                                onClick = {
                                    selectedModel = model
                                    showModelPicker = false
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(model.name, style = MaterialTheme.typography.bodyLarge)
                                    if (model.isFree) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Surface(
                                            color = Color(0xFF3FB950).copy(alpha = 0.2f),
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                "رایگان",
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                style = MaterialTheme.typography.labelSmall,
                                                color = Color(0xFF3FB950)
                                            )
                                        }
                                    }
                                }
                                Text(
                                    model.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showModelPicker = false }) {
                    Text("بستن")
                }
            }
        )
    }
}

@Composable
fun MessageBubble(message: ChatMessage) {
    val isUser = message.isUser
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        if (!isUser && message.modelName.isNotEmpty()) {
            Text(
                text = message.modelName,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)
            )
        }
        Surface(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            color = if (isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.widthIn(max = 320.dp)
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(12.dp),
                color = if (isUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 15.sp
            )
        }
    }
}

private fun generateReply(userMessage: String, model: AiModel): String {
    val lower = userMessage.lowercase()
    return when {
        lower.contains("سلام") || lower.contains("hello") || lower.contains("hi") ->
            "سلام! من با مدل ${model.name} پاسخ می‌دهم.\nچطور می‌توانم کمکتان کنم؟"

        lower.contains("کد") || lower.contains("برنامه") || lower.contains("اپ") || lower.contains("android") ->
            """برای ساخت برنامه اندروید می‌توانم کمکتان کنم.

مثال ساختار ساده:
```kotlin
@Composable
fun MyScreen() {
    Column {
        Text("Hello Android")
        Button(onClick = { }) {
            Text("Click me")
        }
    }
}
```

بیشتر توضیح بدهید چه برنامه‌ای می‌خواهید تا کد کامل‌تری بنویسم."""

        lower.contains("اینفو") || lower.contains("عکس") || lower.contains("تصویر") || lower.contains("پوستر") ->
            """برای ساخت اینفوگرافیک و عکس:

۱. می‌توانید از Cloudflare Workers AI (رایگان) استفاده کنید
۲. یا از ابزارهای وب مثل Napkin AI و Gamma
۳. در نسخه‌های بعدی دکمه تولید تصویر مستقیم داخل چت اضافه می‌شود

موضوع اینفوگرافیک را بگویید تا متن و ساختار آن را برایتان طراحی کنم."""

        lower.contains("نصب") || lower.contains("install") || lower.contains("ترموکس") ->
            """برای نصب ابزارهای هوش مصنوعی داخل Termux:

```bash
# نصب OpenCode (پیشنهادی)
pkg install nodejs-lts
npm install -g opencode-ai
opencode

# یا نصب Ollama (اگر رم کافی باشد)
pkg install ollama
```

بعد از نصب در Termux، می‌توانید از این اپ به عنوان رابط استفاده کنید."""

        lower.contains("کمک") || lower.contains("help") ->
            """دستورات مفید:
• ساخت کد و برنامه
• طراحی اینفوگرافیک
• راهنمای نصب ابزار در Termux
• توضیح مفاهیم برنامه‌نویسی

از منوی بالا می‌توانید مدل AI را عوض کنید."""

        else ->
            "پیام شما دریافت شد (مدل: ${model.name}).\n\nمن فعلاً در حالت آفلاین/شبیه‌سازی پاسخ می‌دهم.\nبرای اتصال واقعی به مدل‌های ابری، از بخش تنظیمات API Key وارد کنید یا OpenCode را در Termux نصب کنید.\n\nسوال یا درخواست دقیق‌تری بپرسید تا بهتر کمکتان کنم."
    }
}
