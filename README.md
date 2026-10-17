# Termux AI Studio

اپلیکیشن اندرویدی برای کدنویسی، چت با هوش مصنوعی، ترمینال و مدیریت فایل.

## قابلیت‌های نسخه ۱.۱

### چت هوش مصنوعی
- انتخاب مدل از داخل چت (OpenCode Free، DeepSeek Free، Gemini، ...)
- مدل‌های رایگان بدون نیاز به کلید
- پاسخ هوشمند به درخواست‌های کدنویسی، اینفوگرافیک و نصب ابزار
- آماده‌سازی برای اتصال واقعی API

### ترمینال
- اجرای برخی دستورات لینوکس (تا حد مجاز sandbox اندروید)
- دستور `ai-install` برای راهنمای نصب ابزارهای AI در Termux
- دستورات: help, date, uname, ls, pwd, whoami, id, echo و ...

### ویرایشگر کد
- ویرایشگر متن با فونت monospace
- آماده برای اتصال به sora-editor در نسخه‌های بعدی

### مدیریت فایل
- نمایش ساختار پوشه‌ها (نسخه نمایشی)

### تنظیمات
- محل وارد کردن API Key برای Gemini و DeepSeek
- راهنمای نصب OpenCode داخل Termux

## ساخت APK

۱. پروژه را در Android Studio باز کنید  
۲. `Build → Build APK(s)` را بزنید  
۳. فایل از مسیر زیر قابل دریافت است:

`app/build/outputs/apk/debug/app-debug.apk`

## استفاده همراه با Termux (قدرت کامل)

```bash
pkg update && pkg upgrade
pkg install nodejs-lts
npm install -g opencode-ai
opencode
```

سپس مدل رایگان را داخل OpenCode انتخاب کنید.

## لینک پروژه

https://github.com/aamoozaa20-coder/TermuxAIStudio

## لایسنس
MIT
