A Kotlin Multiplatform template project targeting Android and iOS.

It has integrated basic functionalities:
- Compose Bottom Navigation
- Same View / ViewModel for Android and iOS.
- Repository pattern with remote / local cache


And common libraries usually needed in pretty much all apps:
- Ktor for Http requests.
- Koin for dependency injection.
- SqlDelight for local database.
- Coil for image loading.
  

The modules are:
* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, 
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

