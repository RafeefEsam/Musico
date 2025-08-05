# Musico - Android Music Player

A scalable Android music player application built with Jetpack Compose, MVVM, and Clean Architecture.

## Features

- **Media List Screen**: Displays all audio files (MP3, WAV) from device storage
- **Audio Player**: Basic playback functionality with play/pause/stop controls
- **Clean Architecture**: Proper separation of concerns with data, domain, and presentation layers
- **MVVM Pattern**: ViewModels manage UI state and business logic
- **Dependency Injection**: Hilt for managing dependencies
- **Navigation**: Jetpack Navigation for screen transitions
- **Permission Handling**: Runtime permissions for accessing media files

## Architecture

The application follows Clean Architecture principles with the following layers:

### Domain Layer
- **Models**: `AudioFile` data class
- **Repository Interface**: `MediaRepository` defines the contract for data operations
- **Use Cases**: Business logic for getting audio files and scanning media

### Data Layer
- **Repository Implementation**: `MediaRepositoryImpl` handles actual media scanning
- **Media Scanner**: Uses Android's MediaStore to query audio files

### Presentation Layer
- **ViewModels**: `MediaListViewModel` and `PlayerViewModel` manage UI state
- **Compose UI**: Modern Material 3 design with LazyColumn for media list
- **Navigation**: Screen navigation with NavController

## Project Structure

```
app/src/main/java/com/example/musico/
├── data/
│   └── repository/
│       └── MediaRepositoryImpl.kt
├── domain/
│   ├── model/
│   │   └── AudioFile.kt
│   ├── repository/
│   │   └── MediaRepository.kt
│   └── usecase/
│       ├── GetAudioFilesUseCase.kt
│       ├── GetAudioFileByIdUseCase.kt
│       └── ScanAudioFilesUseCase.kt
├── di/
│   ├── RepositoryModule.kt
│   └── ServiceModule.kt
├── presentation/
│   ├── media/
│   │   ├── MediaListScreen.kt
│   │   └── MediaListViewModel.kt
│   ├── player/
│   │   ├── PlayerScreen.kt
│   │   └── PlayerViewModel.kt
│   └── utils/
│       └── PermissionHandler.kt
├── service/
│   └── MediaPlayerService.kt
└── MusicoApp.kt
```

## Key Components

### MediaListScreen
- Displays audio files in a scrollable list using LazyColumn
- Each item shows title, artist, album, and duration
- Handles loading states, errors, and empty states
- Requests runtime permissions for media access

### MediaListViewModel
- Manages UI state using StateFlow
- Coordinates between use cases and UI
- Handles error states and loading indicators

### MediaRepositoryImpl
- Implements media scanning using MediaStore
- Queries audio files from device storage
- Provides reactive data streams

### MediaPlayerService
- Background service for audio playback
- Handles MediaPlayer lifecycle
- Provides playback controls

## Permissions

The app requires the following permissions:
- `READ_EXTERNAL_STORAGE` (Android < 13)
- `READ_MEDIA_AUDIO` (Android 13+)
- `FOREGROUND_SERVICE` and `FOREGROUND_SERVICE_MEDIA_PLAYBACK` for background playback

## Dependencies

- **Jetpack Compose**: Modern UI toolkit
- **Hilt**: Dependency injection
- **Navigation Compose**: Screen navigation
- **Material 3**: Design system
- **Coroutines & Flow**: Asynchronous programming
- **MediaPlayer**: Audio playback

## Usage

1. Launch the app
2. Wait for the splash screen (5 seconds with animated GIF, then static logo)
3. Grant media permissions when prompted
4. Navigate to Media List screen
5. Browse your music library
6. Tap on any song to start playback
7. Use the player controls to manage playback

## Permission Flow

The app implements a proper permission flow:
- Shows animated GIF splash screen for 5 seconds
- Switches to static logo image
- Requests media permissions (READ_MEDIA_AUDIO or READ_EXTERNAL_STORAGE)
- Shows loading indicator while waiting for user response
- Navigates to Media List after user responds to permission request
- If permissions denied, Media List shows permission warning screen
- Permission warning allows user to retry permission request
- Media List screen loads audio files automatically (if permissions granted)

## Future Enhancements

- Album art display
- Playlist functionality
- Audio visualization
- Background playback controls
- Search and filtering
- Equalizer settings
- Shuffle and repeat modes

## Building the Project

1. Clone the repository
2. Open in Android Studio
3. Sync Gradle files
4. Build and run on device/emulator

## Requirements

- Android API 21+ (Android 5.0)
- Kotlin 1.8+
- Android Studio Arctic Fox or later 