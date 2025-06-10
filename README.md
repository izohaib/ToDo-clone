# ✨ Todo App Clone – Task Manager with Reminders, Notes, and Media

A beautifully crafted **To-Do List App** built using **Jetpack Compose**, inspired by Microsoft To Do. It offers a smooth, intuitive task management experience with notifications, repeat options, notes, media attachments, and a clean Material You design.

---

## 🧩 Core Features

- 🔐 Login & Signup (Google & Email via Firebase Auth)
- 📝 Smart Bottom Sheet for creating tasks
- 📅 Due Date, ⏰ Reminder, and 🔁 Repeat Pickers
- 🔔 Notification reminders using AlarmManager
- 🗃️ Task update screen with Notes, Media (image/video), and Steps
- 🎨 Material 3 Light & Dark Theme with dynamic colors
- 💾 Offline-first experience using Room database

---

## 📸 Screenshots

| Login Screen | Signup Screen | Add Task | Due Date Picker |
|--------------|----------------|----------|-----------------|
| ![Login](https://i.postimg.cc/pX4VpC5f/photo-2025-06-10-12-50-25.jpg) | ![Signup](https://i.postimg.cc/28z9nNhm/photo-8-2025-06-10-12-52-32.jpg) | ![Add Task](https://i.postimg.cc/V6QD6xmx/photo-6-2025-06-10-12-52-32.jpg) | ![Due Date](https://i.postimg.cc/zDt5L7Hd/date.jpg) |

| Reminder Picker | Update Task Screen |
|------------------|---------------------|
| ![Reminder](https://i.postimg.cc/7YZ39H6H/photo-3-2025-06-10-12-52-32.jpg) | ![Update Screen](https://i.postimg.cc/W45FhrG0/update.jpg) |

> 📌 **Tip:** If images don’t appear in a row, try viewing on desktop. GitHub mobile may stack them vertically.

---

## 🚀 Features Breakdown

### 🔐 Authentication
- Firebase Auth integration
- Google Sign-In using recommended Identity Services flow
- Email/Password Login and Signup
- Auto-login detection and redirection to Home screen

### ➕ Task Creation & Editing
- Add task with title, description, optional image/video attachment
- Bottom Sheet UI with expandable advanced options
- Modify tasks anytime: notes, media, repeat, due date, reminders
- Add subtasks or steps for detailed workflows

### 🔔 Reminders & Notifications
- Uses **AlarmManager** + **BroadcastReceiver** for exact time alerts
- Works reliably even when the app is killed
- Automatically cancels/reschedules if tasks are updated or deleted

### 💾 Offline Support with Room
- Fully offline-capable with Room Database
- TypeConverters for complex data types (repeat mode, timestamps, URIs)
- Task data loaded via `StateFlow` and updated with ViewModel
- Architecture follows MVVM best practices

### 🎨 Material Design 3
- Built 100% with **Jetpack Compose**
- Modern UI with adaptive layouts, animations, and transitions
- Light & Dark themes using **Material You** guidelines
- Consistent color scheme and spacing across the app

---

## 🧰 Tech Stack

| Category        | Tools & Libraries                                |
|----------------|---------------------------------------------------|
| **Language**    | Kotlin                                            |
| **UI**          | Jetpack Compose, Material3                        |
| **Auth**        | Firebase Authentication                          |
| **Database**    | Room, TypeConverters                             |
| **Architecture**| MVVM, StateFlow, Repository pattern              |
| **Navigation**  | Jetpack Navigation Compose                       |
| **Reminders**   | AlarmManager, BroadcastReceiver, Notifications   |
| **File Picker** | ActivityResultContracts, ContentResolver         |
| **Storage**     | MediaStore, App Internal Storage                 |
| **Design**      | Material You, Light & Dark Themes                |

---
