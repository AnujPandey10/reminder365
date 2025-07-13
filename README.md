# RemindMe365

A modern reminder application built with Kotlin Multiplatform and Jetpack Compose, designed to help users never forget important birthdays, anniversaries, and other recurring events.

## Features

### ✅ Implemented Features

**Core Functionality:**
- **Event Management**: Add, view, and delete birthdays, anniversaries, and custom events
- **Modern UI**: Clean, minimalist design with Material Design 3 components
- **Multiplatform**: Shared codebase for Android and iOS
- **Local Storage**: SQLDelight database for persistent data storage
- **MVVM Architecture**: Clean separation of concerns with ViewModels
- **Dependency Injection**: Koin for dependency management

**UI Components:**
- **Home Screen**: Overview of upcoming events with smart date formatting
- **Events Screen**: Filterable list of events by type (Birthday, Anniversary, Custom)
- **Reminders Screen**: List of one-time reminders
- **Settings Screen**: Placeholder for app configuration
- **Add Event Form**: Comprehensive form with date picker and notification settings

**Data Models:**
- **Event**: Complete event model with name, date, type, relation, notes, email, and notification preferences
- **Reminder**: One-time reminder model with title, description, date/time, and notification settings
- **UserSettings**: User preferences for notifications and premium features

### 🚧 In Progress / Planned Features

**Core Features:**
- [ ] Cloud synchronization for data backup
- [ ] Push notifications for events and reminders
- [ ] Email notification system
- [ ] Import/Export functionality (Calendar, Contacts, CSV/JSON)
- [ ] Photo upload for event contacts

**Premium Features:**
- [ ] Auto birthday wish sender
- [ ] AI-generated custom birthday cards
- [ ] Multiple email addresses for reminders
- [ ] Advanced notification scheduling

**Technical Improvements:**
- [ ] Complete CRUD operations for events and reminders
- [ ] Date picker improvements
- [ ] Form validation
- [ ] Error handling improvements
- [ ] Unit tests and UI tests

## Technology Stack

- **Kotlin Multiplatform**: Shared business logic across platforms
- **Jetpack Compose Multiplatform**: Modern declarative UI framework
- **SQLDelight**: Type-safe database library
- **Koin**: Dependency injection framework
- **Ktor**: HTTP client for future cloud sync
- **Coil**: Image loading library
- **kotlinx-datetime**: Date and time handling

## Project Structure

```
composeApp/src/commonMain/kotlin/com/remindme365/app/
├── data/
│   ├── Models.kt              # Data models (Event, Reminder, UserSettings)
│   ├── Database.kt            # Database operations
│   ├── LocalDataSource.kt     # Local data source
│   ├── RemoteDataSource.kt    # Remote data source (placeholder)
│   └── Repository.kt          # Repository pattern implementation
├── ui/
│   ├── TabScreens.kt          # Main screen implementations
│   ├── HomeViewModel.kt       # Home screen ViewModel
│   ├── EventsViewModel.kt     # Events screen ViewModel
│   ├── RemindersViewModel.kt  # Reminders screen ViewModel
│   ├── AddEventScreen.kt      # Add event form
│   └── DatePickerDialog.kt    # Custom date picker
├── di/
│   └── AppModule.kt           # Dependency injection setup
└── App.kt                     # Main app component
```

## Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- Kotlin 1.8.0 or later
- Android SDK 24+

### Installation

1. Clone the repository:
```bash
git clone https://github.com/yourusername/RemindMe365.git
cd RemindMe365
```

2. Open the project in Android Studio

3. Sync Gradle files and build the project

4. Run on Android device or emulator

## Development Status

**Current Version**: 0.1.0 (Alpha)

The application is in early development with core UI and data layer implemented. The basic functionality for viewing and managing events is working, but many features are still in development.

### Next Steps

1. **Complete CRUD Operations**: Implement full create, read, update, delete functionality
2. **Notification System**: Add local notifications for events and reminders
3. **Cloud Sync**: Implement backend integration for data synchronization
4. **Testing**: Add comprehensive unit and UI tests
5. **Polish**: Improve UI/UX and add animations

## Contributing

This project is currently in active development. Contributions are welcome! Please feel free to submit issues and pull requests.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Built with [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)
- UI powered by [Jetpack Compose](https://developer.android.com/jetpack/compose)
- Database by [SQLDelight](https://cashapp.github.io/sqldelight/)
- Dependency injection with [Koin](https://insert-koin.io/)

# reminder365
