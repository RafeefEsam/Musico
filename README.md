Musico is a modern Android music player app built with **Jetpack Compose** and **Clean Architecture** principles. It provides an intuitive interface for playing local audio files with advanced media controls, background playback support, and a scalable MVVM architecture.

you can try the app demo from here : [**🎵 Musico Demo**](https://drive.google.com/drive/folders/1vrOuOazju0_MH15zrEd_-3wiKqZzm4P_?usp=sharing)

---

## 🏗️ Architecture

The app follows **Clean Architecture** with a clear separation of concerns across three main layers:

### Domain Layer (`domain/`)
- **Models**: Core data structures (`AudioFile`)
- **Repository Interfaces**: Contracts for data access (`MediaRepository`)
- **Use Cases**: Business logic encapsulation  
  - `GetAudioFilesUseCase`, `GetAudioFileByIdUseCase`  
  - `PlayAudioUseCase`, `PauseAudioUseCase`, `ResumeAudioUseCase`  
  - `PlayNextTrackUseCase`, `PlayPreviousTrackUseCase`  
  - `ScanAudioFilesUseCase`, `SeekToUseCase`

### Data Layer (`data/`)
- **Repository Implementations**: Concrete implementations of domain contracts
- **MediaRepositoryImpl**: Handles audio file scanning and media player integration

### Presentation Layer (`presentation/`)
- **MVVM Pattern**: ViewModels manage UI state and business logic
- **Jetpack Compose UI**: Modern declarative UI components
- **Screens**: `SplashScreen`, `MediaListScreen`, `PlayerScreen`
- **Navigation**: Navigation Compose for screen routing

### Infrastructure
- **Dependency Injection**: Hilt/Dagger for dependency management
- **Background Service**: `MediaPlayerService` for continuous audio playback
- **Notification Manager**: `MediaNotificationManager` for media controls in the notification panel

## 📂 Project Structure
```
app/src/main/java/com/example/musico/
├── data/
│ └── repository/
│ └── MediaRepositoryImpl.kt
├── domain/
│ ├── model/
│ │ └── AudioFile.kt
│ ├── repository/
│ │ └── MediaRepository.kt
│ └── usecase/
│ ├── GetAudioFilesUseCase.kt
│ ├── GetAudioFileByIdUseCase.kt
│ ├── PlayAudioUseCase.kt
│ ├── PauseAudioUseCase.kt
│ ├── ResumeAudioUseCase.kt
│ ├── PlayNextTrackUseCase.kt
│ ├── PlayPreviousTrackUseCase.kt
│ ├── SeekToUseCase.kt
│ └── ScanAudioFilesUseCase.kt
├── di/
│ ├── RepositoryModule.kt
│ └── ServiceModule.kt
├── presentation/
│ ├── media/
│ │ ├── MediaListScreen.kt
│ │ └── MediaListViewModel.kt
│ ├── player/
│ │ ├── PlayerScreen.kt
│ │ └── PlayerViewModel.kt
│ └── utils/
│ └── PermissionHandler.kt
├── service/
│ └── MediaPlayerService.kt
└── MusicoApp.kt
```

---

## 🛠️ Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (Material 3)
- **Architecture**: Clean Architecture + MVVM
- **Dependency Injection**: Hilt (Dagger)
- **Media Playback**: Media3 (ExoPlayer)
- **Async Programming**: Kotlin Coroutines + Flow
- **Navigation**: Navigation Compose
- **Image Loading**: Coil
- **Build System**: Gradle (Kotlin DSL)

---

## 📱 Features

- 🎵 Local audio file scanning and playback  
- ⏯️ Complete media controls (play, pause, skip, seek)  
- 🔄 Background playback with foreground service  
- 📱 Media session integration with system media controls  
- 🎨 Clean, modern Material 3 UI  
- 🖼️ Album art display with placeholder support  
- 📱 Responsive design following Material Design principles  

---

## 📋 Permissions

- `READ_EXTERNAL_STORAGE` – Access audio files on device (API < 33)  
- `READ_MEDIA_AUDIO` – Access audio files on device (API 33+)  
- `FOREGROUND_SERVICE` – Background audio playback  
- `FOREGROUND_SERVICE_MEDIA_PLAYBACK` – Media playback service  
- `POST_NOTIFICATIONS` – Show playback notifications  

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio**: Arctic Fox or later  
- **JDK**: 11 or higher  
- **Android SDK**: API level 35 (target), minimum API level 21  
- **Gradle**: 7.0 or higher  

### Setup Instructions
```bash
git clone <repository-url>
cd Musico
Open in Android Studio
Sync Gradle files
Run the app on a device or emulator
```

📝 Development Notes
```
-**State Management**: ViewModels use StateFlow for reactive UI updates
-**Media Playback Architecture**: MediaPlayerService + MediaNotificationManager
-**Performance**: Efficient image loading via Coil, optimized Media3 playback
```


