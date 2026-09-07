# GradePoint

#### Video Demo : https://youtu.be/exFyIREQnZU

---

GradePoint is an Android application designed to help university students track, calculate, and manage their GPAs and CGPAs. Built with Java using the MVVM architecture pattern, the app provides offline data persistence and automatic dark theme support compliant with Material Design 3 guidelines.

This project was developed as a final project for Harvard's CS50.

---

## Features

* **GPA & CGPA Calculation:** Real-time calculation of Semester GPA (SGPA) and Cumulative GPA (CGPA) based on credit hours and grade points.
* **Academic Record Management:** Organize academic performance by creating semesters and adding individual courses with respective credit hours and letter grades.
* **Offline Storage:** Local database integration using Room to ensure data persists without requiring an internet connection.
* **System-Adaptive Theme:** Full support for Light and Dark modes (`DayNight` theme) built using Material Design 3 guidelines.
* **High Contrast & Accessible UI:** Dynamic text and navigation elements adapted for visibility in dark environments.

---

## Technical Architecture

The application follows the Android recommended MVVM (Model-View-ViewModel) architecture:

* **UI Layer:** Activity and Fragment classes utilizing ViewBinding for type-safe view interaction.
* **ViewModel Layer:** Manages UI-related data in a lifecycle-conscious way and handles business logic for GPA computations.
* **Data Layer:** Room Persistence Library wrapping SQLite, utilizing Data Access Objects (DAOs) and LiveData to propagate database changes directly to the UI.

---

## Tech Stack & Dependencies

* **Language:** Java
* **UI Components:** Material Design 3 (Material Components for Android)
* **Architecture Pattern:** MVVM (Model-View-ViewModel)
* **Database:** Room Persistence Library
* **Lifecycle Components:** LiveData, ViewModel, ViewBinding
* **Build System:** Gradle (AGP)

---

## Project Structure

```text
app/src/main/
├── java/com/example/gradepoint/
│   ├── data/          # Room Database, DAOs, and Entity Models
│   ├── repository/    # Data Abstraction Layer
│   ├── ui/            # Activities, ViewModels, and UI Adapters
│   └── util/          # Grade conversion and utility logic
└── res/
    ├── layout/        # XML Layout files
    ├── values/        # Light theme attributes, strings, colors
    └── values-night/  # Dark theme attributes and palette overrides
```

---

## Installation & Setup

### Prerequisites

* Android Studio (Ladybug / 2024.2.1 or newer recommended)
* Android SDK (API Level 24 / Android 7.0 or higher)
* JDK 17

### Building from Source

1. Clone the repository:
   ```bash
   git clone https://github.com/YOUR_USERNAME/GradePoint.git
   ```
2. Open Android Studio and select **File > Open**, then choose the cloned project folder.
3. Allow Gradle to sync dependencies automatically.
4. Run the project on an emulator or a physical device (**Run > Run 'app'**).

### Installing the APK

You can download the pre-compiled, signed APK directly from the [GitHub Releases](https://github.com/YOUR_USERNAME/GradePoint/releases) section and install it on any compatible Android device.

---
