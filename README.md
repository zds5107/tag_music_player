# Tag-Based Music Player (Android)

This project is a modern Android music player built as a portfolio piece to demonstrate proficiency in Kotlin, Jetpack Compose, and scalable mobile architecture. The application focuses on rethinking how users organize and interact with their local music library by introducing a flexible, tag-based system instead of relying solely on traditional album or folder structures.

The app scans songs directly from a user’s device, persists metadata using a Room database, and enables dynamic playlist generation through tag-based filtering. In addition to core playback functionality, the project highlights clean architecture principles, reactive UI design, and a well-structured MVVM implementation.

---

## Features

### Local Music Playback
- Powered by ExoPlayer
- Supports queue-based playback, skip, previous, and play/pause controls

### Tag-Based Organization
- Create and manage custom tags
- Group tags into logical categories
- Assign multiple tags to individual songs

### Dynamic Playlist Generation
- Filter songs using selected tags
- Generate playlists in real time based on user-defined criteria

### Auto-Tagging System
- Automatically assigns tags based on the folder structure of the user’s music library
- Reduces manual effort while maintaining meaningful organization

### Favorites System
- Built-in “Loved” tag for quick access to favorite songs
- Toggle directly from the player screen

### Album Art Integration
- Extracts embedded album artwork from local files
- Displays fallback placeholder when unavailable

### Modern UI
- Built entirely with Jetpack Compose
- Reactive state handling via StateFlow

---

## Architecture (MVVM)

This project follows the **Model-View-ViewModel (MVVM)** architectural pattern to ensure separation of concerns and scalability.

### Model
- Room database entities represent songs, tags, and relationships
- Repository layer abstracts data access and business logic

### ViewModel
- Manages UI state using StateFlow
- Acts as the bridge between UI and data layers
- Handles playback coordination, filtering logic, and user actions

### View (UI)
- Built with Jetpack Compose
- Observes state from ViewModels and reacts automatically to updates

**Benefits:**
- Clear separation between UI and business logic  
- Easier testing and maintainability  
- Scalable feature development  

---

## How It Works

When the user scans their device, the app retrieves audio metadata such as title, artist, album, and file location. This data is stored locally in a Room database, along with many-to-many relationships between songs, tags, and tag groups.

The auto-tagging system analyzes the folder structure of the user’s music library to infer meaningful tags (e.g., genres or categories based on directory names). This provides a fast and practical way to organize large collections without requiring manual input.

Playback is handled through a dedicated controller built on ExoPlayer, which maintains the current queue and playback state. The UI observes this state through ViewModels, ensuring real-time updates across screens.

Tag selection drives the core experience: when users choose tags, the app dynamically queries the database and generates a filtered playlist that reflects their preferences.

---

## Tech Stack

- Kotlin  
- Jetpack Compose  
- Android Architecture Components  
  - ViewModel  
  - StateFlow  
- Room Database  
- ExoPlayer  
- Coil (image loading)  

---

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/tag-music-player.git

2. Open the project in Android Studio

3. Sync Gradle dependencies

4. Run the app on:
   - A physical Android device (recommended for accessing local media files)
   - Or an emulator with media files configured

---

## Usage

- Grant media permissions on launch  
- Scan device for songs  
- Run auto-tagging or manually assign tags  
- Select tags to generate a playlist  
- Control playback from the player screen  

---

## Key Takeaways

- Implemented a scalable **MVVM architecture** with clear separation of concerns  
- Designed a **relational data model** using Room with many-to-many mappings  
- Built a **reactive UI** using StateFlow and Jetpack Compose  
- Integrated **media playback systems** using ExoPlayer  
- Developed a **custom tagging system** to enhance user interaction beyond standard music apps  

---

## Future Improvements

- UI/UX polish and animations  
- Smarter auto-tagging (metadata + heuristics)  
- Search and sorting functionality  
- Persistent playback state across app restarts  
- Playlist saving and export features  

   
