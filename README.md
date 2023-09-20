# Moove Android App

## 🏛 Architecture overview

### Modules 🐘

The app consists of several gradle modules:
- `app` – the main module which setups other modules together, along with the top-level navigation and DI.

  It also contains legacy features implementation that would be extracted in the future;

- `tickets/featureX` - concrete standalone feature implementation, e.g. search, bookings, etc.;
- `core` - project-agnostic language tools & extensions shared with other modules;
- `shared` - basic components and business rules to re-use across the app and feature modules:
    - `features` - shared features which could be reused in the app directly
      as the business rules (use cases): _GetSessionUseCase_ or _SubscribeToAccountUseCase_, etc;
    - `presentation` - basic presentation-level reusable components;
- `design-system` - common resources: styles, themes, drawables, strings;

### Components ❖

Check all the [dependencies](../gradle/libs.versions.toml)

#### Core/Language
- the app is written in `Kotlin`;
- `Coroutines` is the only lib used for async operations;
- Java17+ APIs are available via [desugaring](https://developer.android.com/studio/write/java8-support-table);

#### Domain/Data
Per Context (in terms of DDD) _UseCase->Repository->DataSource_ pattern is implemented with non-base own interfaces.

#### Presentation
The app is built mainly with `AndroidX` components: Navigation, Fragment and ViewModel.
The ViewModel itself is often set up in a MVI manner with the help of [Orbit-MVI](https://github.com/orbit-mvi/orbit-mvi)

#### DI
The DI is built with [Koin](https://insert-koin.io/) and fully belongs to the _App_ module.

#### Testing
We do unit test only for new: JUnit4 is used with the help of `mockk`.