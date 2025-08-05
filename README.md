# Musico 🎵

Musico is a modern Android music player app built with Jetpack Compose and following Clean Architecture principles. It provides an intuitive interface for playing local audio files with advanced media controls and background playback support.
You can try the app demo from here : https://drive.google.com/drive/folders/1vrOuOazju0_MH15zrEd_-3wiKqZzm4P_?usp=sharing
## 🏗️ Architecture

The app follows **Clean Architecture** with clear separation of concerns across three main layers:

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
- **Notification Manager**: `MediaNotificationManager` for media controls in notification panel

## 🛠️ Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: Clean Architecture + MVVM
- **Dependency Injection**: Hilt (Dagger)
- **Media Playback**: Media3 (ExoPlayer)
- **Async Programming**: Kotlin Coroutines + Flow
- **Navigation**: Navigation Compose
- **Image Loading**: Coil
- **Build System**: Gradle (Kotlin DSL)

## 📱 Features

- 🎵 Local audio file scanning and playback
- ⏯️ Complete media controls (play, pause, skip, seek)
- 🔄 Background playback with foreground service
- 📱 Media session integration with system media controls
- 🎨 Clean, modern Material 3 UI
- 🖼️ Album art display with placeholder support
- 📱 Responsive design following Material Design principles

## 🚀 Getting Started

### Prerequisites

- **Android Studio**: Arctic Fox or later
- **JDK**: 11 or higher
- **Android SDK**: API level 35 (target), minimum API level 21
- **Gradle**: 7.0 or higher

### Setup Instructions

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Musico
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the cloned directory and select it

3. **Sync Project**
   - Android Studio will automatically prompt to sync Gradle files
   - Wait for the sync to complete and dependencies to download

4. **Run the App**
   - Connect an Android device or start an emulator
   - Click the "Run" button or press `Ctrl+R` (Windows/Linux) / `Cmd+R` (Mac)

## 📋 Permissions

The app requires the following permissions:

- `READ_EXTERNAL_STORAGE` - Access audio files on device (API < 33)
- `READ_MEDIA_AUDIO` - Access audio files on device (API 33+)
- `FOREGROUND_SERVICE` - Background audio playback
- `FOREGROUND_SERVICE_MEDIA_PLAYBACK` - Media playback service
- `POST_NOTIFICATIONS` - Show playback notifications

## 🔧 Configuration

### Minimum Requirements
- **Android**: 5.0 (API level 21)
- **Target SDK**: 35
- **Storage**: Audio files accessible to the app
- **RAM**: 2GB recommended for smooth performance

### Supported Audio Formats
The app supports common audio formats via ExoPlayer:
- MP3, AAC, FLAC, OGG, WAV, M4A

## 📝 Development Notes

### Dependency Injection Setup
The app uses Hilt for dependency injection with modules:
- `RepositoryModule`: Binds repository implementations
- `ServiceModule`: Provides service-related dependencies

### State Management
- ViewModels use `StateFlow` for reactive UI state management
- Repository layer uses `Flow` for asynchronous data streams
- Coroutines handle background operations

### Media Playback Architecture
- `MediaPlayerService`: Handles background playback with Media3
- `MediaNotificationManager`: Manages media notifications
- Service communicates with UI through repository layer


### Performance Optimization
- The app uses efficient image loading with Coil
- Background scanning is optimized to avoid UI blocking
- Media3 provides optimized audio playback performance

## 📄 License

This project is developed as a sample application. Please ensure you have appropriate licenses for any third-party libraries used in production deployments.

## 🤝 Contributing

When contributing to this project:
1. Follow the existing architectural patterns
2. Maintain clean code principles
3. Add appropriate tests for new features
4. Update documentation for significant changes
5. Follow Material Design guidelines for UI changes

---

**Note**: This app is designed for local audio file playback. For streaming features, additional network permissions and implementations would be required.