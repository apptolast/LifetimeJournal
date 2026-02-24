# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

LifetimeJournal is a cross-platform personal journaling application built with Kotlin Multiplatform (KMP) and Compose Multiplatform. It enables users to capture, organize, and reflect on their life experiences across Android and iOS devices with cloud synchronization and AI-powered storybook generation.

**Tech Stack**: Kotlin Multiplatform, Compose Multiplatform, MVVM, Koin, Firebase (Auth/Firestore), Room, Ktor, Coil

## Important Guidelines for Claude Code

### Testing and Building

**DO NOT automatically run tests or build commands** after completing tasks unless explicitly requested by the user. The user will request test execution and builds when they consider it appropriate.

Only run `./gradlew test` or `./gradlew build` when:
- The user explicitly asks for it
- The task instructions specifically include testing/building steps

### Code Formatting with ktlint

This project uses **ktlint** for code style enforcement. After completing any task that involves code changes:

1. **ALWAYS run** `./gradlew ktlintFormat` to auto-format the code according to project standards
2. This ensures all code complies with the established style guidelines

**Example workflow:**
```bash
# After making code changes
./gradlew ktlintFormat

# Only if explicitly requested by user
./gradlew test
./gradlew build
```

## Build Commands

### Setup Requirements

Before building, create `local.properties` in the project root with:
```
KOTZILLA_API_KEY=<your-kotzilla-api-key>
WEB_ID_CLIENT=<your-google-web-client-id>
```

Additionally, add `google-services.json` to the `composeApp/` directory from your Firebase project.

### Common Commands

```bash
# Build the project
./gradlew build

# Run all tests
./gradlew test

# Run Android app
./gradlew :composeApp:installDebug

# Run ktlint checks
./gradlew ktlintCheck

# Auto-format code with ktlint
./gradlew ktlintFormat

# Clean build
./gradlew clean
```

## Architecture

### Project Structure (Kotlin Multiplatform)

```
LifetimeJournal/
├── composeApp/              # Compose Multiplatform UI layer
│   └── src/
│       ├── commonMain/      # Shared UI (screens, viewmodels, navigation, theme)
│       ├── androidMain/     # Android-specific (MainActivity, Application, WebView)
│       └── iosMain/         # iOS-specific (MainViewController, WebView)
├── shared/                  # Shared business logic module
│   └── src/
│       ├── commonMain/      # Data models, repositories, database, DI
│       ├── androidMain/     # Android platform implementations
│       ├── iosMain/         # iOS platform implementations
│       └── jvmMain/         # JVM platform implementations
└── iosApp/                  # iOS app wrapper (Xcode project)
```

### MVVM Pattern Structure

```
composeApp/src/commonMain/kotlin/.../features/{feature}/
├── data/{Feature}State.kt              # UI state data class
└── presentation/
    ├── {Feature}Screen.kt              # Composable UI
    ├── {Feature}ViewModel.kt           # ViewModel with StateFlow
    └── components/                     # Screen-specific components
```

**Key Conventions**:
- State management uses `MutableStateFlow` and `StateFlow`
- `.collectAsStateWithLifecycle()` in Compose for lifecycle-aware collection
- `.update {}` builder for thread-safe state modifications

### Features

| Feature | Description |
|---------|-------------|
| **login** | Google Sign-In authentication |
| **journals** | Journal list, creation (bottom sheet modal) |
| **entries** | Journal entries management (add/edit/delete) |
| **storybooks** | AI-generated storybooks (create, list, detail) |
| **settings** | User settings and logout |
| **privacypolicy** | Privacy policy WebView (platform-specific) |

### Data Layer

**Domain Models** (in `shared/`):
- `User` - Firebase user with auth state
- `Journal` - Title, description, cover, embedded entries list
- `JournalEntry` - Title, description, date (LocalDate), journalId
- `StoryBook` - AI-generated story with metadata, dates, style, generatedText

**Room Database** (version 2, 3 entities):
- `JournalEntity`, `JournalEntryEntity`, `StoryBookEntity`
- Entity mappers: `.toDomain()`, `.toEntity()` extensions in `EntityMappers.kt`
- Type converters for `LocalDate` via `kotlinx-datetime`

### Repository Pattern

Repositories in `shared/src/commonMain/.../data/repositories/`:
- **AuthRepository**: Firebase Auth wrapper (Google Sign-In, sign-out, token management)
- **FirestoreRepository**: Firestore CRUD with subcollection structure
- **JournalRepository**: Dual-write to Firebase + Room for offline-first sync
- **StoryBookRepository**: Local Room DB + AI service integration
- **AIService**: Interface for AI generation (currently `PlaceholderAIService`)

### Dependency Injection (Koin)

Modules in `shared/src/commonMain/.../di/SharedModule.kt`:
- **sharedModule**: Firebase instances, repositories, AI service
- **databaseModule**: Room database, DAOs
- **platformModule** (expect/actual): Platform-specific `RoomDatabase.Builder`, `GoogleSignInHelper`

UI DI in `composeApp/src/commonMain/.../di/AppModule.kt`:
- **viewModelsModule**: All ViewModels registered

Koin uses **KSP annotations** (`@KoinViewModel`, etc.) with `ksp.useKSP2=true`.

### Navigation

- **Type**: Compose Multiplatform Navigation with `NavHost`
- **Location**: `composeApp/src/commonMain/.../core/navigation/`
- **Routes**: Type-safe serializable destinations in `Routes.kt`
- **Bottom Nav**: Journals, StoryBooks, Settings (visible only on main tabs)

**Destinations**:
```
LoginDestination → JournalDestination ←→ EntriesDestination(journalId)
                 → StoryBooksListDestination ←→ CreateStoryBookDestination
                                              ←→ StoryBookDetailDestination(storyBookId)
                 → SettingDestination → PrivacyPolicyDestination
```

### Firebase Integration

> Use the `firebase-specialist` agent for detailed Firebase architecture and MCP operations.

- **Auth** (`AuthRepository`): Google Sign-In via expect/actual `GoogleSignInHelper`
- **Firestore** (`FirestoreRepository`): Subcollection structure `journals/{id}/entries/{id}`
- **Firebase KMP**: Uses `dev.gitlive:firebase-*` multiplatform libraries (v2.4.0)
- **MCP**: Firebase MCP server configured for direct Firestore/Auth queries

**Firebase Project**: `lifetime-journal` (ID: 762663530063)

## Firestore Database Structure

```
journals/ (collection)
├── {journalId}
│   ├── id: string
│   ├── title: string
│   ├── description: string
│   ├── cover: string (URL)
│   └── entries/ (subcollection)
│       └── {entryId}
│           ├── id: string
│           ├── journalId: string
│           ├── title: string
│           ├── description: string
│           └── date: string (YYYY-MM-DD)
```

## Jira Integration

> Use the `jira-specialist` agent for ticket creation, sprint management, and workflow tracking.

- **Instance**: `apptolast.atlassian.net`
- **Project Key**: `LJ`
- **Board**: LJ board (id: 40, type: simple)
- **MCP**: Atlassian MCP server configured for direct Jira operations

## GitHub Integration

> Use the `github-specialist` agent for PR management, code reviews, and branch operations.

- **Organization**: `apptolast`
- **Repository**: `apptolast/LifetimeJournal`
- **Default Branch**: `develop`
- **MCP**: GitHub MCP server configured for direct repo operations

## Development Workflow

> **Jira is the source of truth** for task tracking. The `LJ-XXX` codes come from Jira.

1. **Create Jira ticket** → Auto-generates `LJ-XXX` key
2. **Create branch** from `develop` → `feature/LJ-XXX-short-description`
3. **Implement** → Commit messages reference `LJ-XXX`
4. **Create PR** → Title: `LJ-XXX Description`, target: `develop`
5. **Code review** → Team reviews and approves
6. **Squash and Rebase** to `develop` → Maintains linear history
7. **Cleanup** → Delete branch, update local `develop`

**Branch naming**: `{type}/LJ-XXX-short-description` where type is `feature/`, `fix/`, `refactor/`, `test/`

**Merge strategy**: Always **squash and rebase** to keep `develop` history linear and traceable via `LJ-XXX` codes.

## Adding New Features

### Adding a Screen

1. Create `composeApp/src/commonMain/.../features/{feature}/` directory
2. Create `data/{Feature}State.kt` data class for UI state
3. Create `presentation/{Feature}ViewModel.kt` with Koin injection
4. Create `presentation/{Feature}Screen.kt` composable
5. Add serializable destination to `Routes.kt`
6. Update `Navigation.kt` to include the new composable
7. Register ViewModel in `AppModule.kt` → `viewModelsModule`

### Adding Database Entities

1. Create entity in `shared/.../database/entities/Entities.kt`
2. Create domain model in `shared/.../data/datamodel/`
3. Add mapper extensions in `EntityMappers.kt`
4. Create DAO in `shared/.../database/dao/`
5. Add entity to `AppDatabase` and increment version
6. Provide DAO in `databaseModule` (SharedModule.kt)

### Adding a Repository

1. Create interface + implementation in `shared/.../data/repositories/`
2. Register as `single` in `sharedModule` (SharedModule.kt)
3. Inject in ViewModel via Koin constructor injection

## Important Notes

### Build Configuration

- **Kotlin**: 2.2.21
- **Compose Multiplatform**: 1.9.0
- **Min SDK (Android)**: 24
- **Target SDK**: 36
- **Compile SDK**: 36
- **JVM Target**: 17
- **iOS Deployment Target**: 18.0

### Key Dependencies

- **DI**: Koin 4.1.1 (with KSP compiler 2.3.1)
- **Image Loading**: Coil 3.3.0 (multiplatform with Ktor network)
- **Networking**: Ktor 3.1.3 (OkHttp on Android, Darwin on iOS)
- **Database**: Room 2.8.4, SQLite Bundled 2.6.2
- **Serialization**: kotlinx-serialization 1.9.0
- **DateTime**: kotlinx-datetime 0.7.1
- **Calendar**: kizitonwose compose-multiplatform 2.6.2
- **Firebase KMP**: gitlive/firebase 2.4.0
- **Analytics**: Kotzilla SDK 1.4.1

### Platform-Specific Implementations (expect/actual)

- `GoogleSignInHelper` - Android: Google Sign-In SDK, iOS: SignInWithGoogle
- `DatabaseBuilder` - Android: Context-based, iOS: NSFileManager-based
- `WebViewComposable` - Android/iOS platform WebViews
- `platformModule` - Koin platform-specific DI bindings

### Room Database

- Schema directory: `shared/schemas/`
- Current version: 2 (with destructive migration fallback)
- Entities: `JournalEntity`, `JournalEntryEntity`, `StoryBookEntity`

### Multiplatform Considerations

- Use `kotlinx-datetime` for date/time (not `java.time`)
- Use `kotlinx-serialization` for JSON (not Gson/Moshi)
- Use Koin for DI (not Hilt - Hilt is Android-only)
- Use `expect/actual` pattern for platform-specific code
- iOS uses CocoaPods for Firebase dependencies
