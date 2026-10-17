# Termux AI Studio

اپلیکیشن اندرویدی حرفه‌ای که ترمینال، چت هوش مصنوعی، ویرایشگر کد و مدیریت فایل را در یک برنامه جمع می‌کند.

## ویژگی‌های نسخه فعلی (v1.0.0)

- ناوبری ۴ بخشی با Bottom Navigation
- ترمینال ساده (sandbox)
- صفحه چت AI (فعلاً آفلاین)
- ویرایشگر کد ساده
- مدیریت فایل (نمونه)
- تم تاریک و روشن
- پشتیبانی از زبان فارسی (RTL)

## ساختار پروژه

```
app/
├── src/main/java/com/termuxaistudio/app/
│   ├── MainActivity.kt
│   └── ui/
│       ├── screens/
│       │   ├── TerminalScreen.kt
│       │   ├── ChatScreen.kt
│       │   ├── EditorScreen.kt
│       │   └── FilesScreen.kt
│       └── theme/
│           ├── Theme.kt
│           └── Type.kt
```

## ساخت و اجرا

### پیش‌نیاز
- Android Studio Ladybug یا جدیدتر
- JDK 17
- Android SDK 35

### ساخت APK

```bash
./gradlew assembleDebug
```

فایل APK در مسیر زیر ساخته می‌شود:
`app/build/outputs/apk/debug/app-debug.apk`

## مراحل بعدی

1. اتصال واقعی به ترمینال (ProcessBuilder + PTY)
2. اتصال به OpenCode / DeepSeek / Gemini
3. ویرایشگر پیشرفته (sora-editor)
4. مدیریت فایل واقعی با SAF
5. ساخت اینفوگرافیک و تولید تصویر

## لایسنس

MIT
