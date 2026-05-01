# Architecture Rules

Binding architecture conventions for the Moove Android app. Sourced from the author's proandroiddev.com series and crystallised by the live codebase (`:tickets`, `:movies`, `:shared`). When writing, reviewing, or refactoring code in this repo, treat these as hard rules — if something looks "fine" by generic Android standards but violates a rule here, the rule wins.

## 1. Module graph

```
   :app
    ├── :tickets ────────┐
    ├── :movies  ────────┤
    └── :shared ─────────┤
          ├── :core      │ ... dependency arrows point right
          └── :design-system
```

- **Dependency direction:** `app → feature → shared → core / design-system`. A feature module depends on `:shared` only (never on `:app`, never on another feature).
- **`:app`** owns `Application`, `MainActivity`, top-level navigation (`AppNavigator` + `nav_graph`), and all Koin DI modules. It is also where any Hilt `@HiltAndroidApp` / `@AndroidEntryPoint` activation lives.
- **Feature modules** (`:tickets`, `:movies`, and future ones) are `com.android.library` modules that implement a single bounded context end-to-end: `data/ + domain/ + presentation/ + di/` + `res/navigation/<feature>_navigation.xml` (+ layouts if the screen is XML-based).
- **`:shared`** re-exports almost every third-party dependency via `api` (AndroidX, Compose, Orbit, Koin, Retrofit/Moshi, Coil, Paging, Firebase, accompanist). Feature modules inherit these transitively — **do not re-declare them in feature `build.gradle`**. The only feature-local dep entries are the ones truly unique to the module (e.g. Hilt in `:movies`).
- **`:core`** is project-agnostic Kotlin/Android tooling: coroutine/result extensions, `ConnectionErrorInterceptor`, `NoConnectionException`, string extensions, `ExceptionHandler` interface. No Android UI, no domain knowledge.
- **`:design-system`** contains shared styles, themes, drawables, Compose tokens (`AppTheme`, `Scaffold`, `Button`, `Spacing`, `Typography`, etc.).

### Adding a new feature module

1. `settings.gradle` → `include ':<feature>'`.
2. Copy `:tickets/build.gradle` as the template (plugins: `com.android.library`, `kotlin-android`, `kotlin-parcelize`, `androidx.navigation.safeargs.kotlin`, `com.google.devtools.ksp`; apply both `base-android-config.gradle` and `compose-android-config.gradle`). Namespace: `com.moove.<feature>`.
3. Only depend on `:shared` from the feature module itself.
4. Add `implementation project(':<feature>')` in `app/build.gradle`.
5. Create `<feature>_navigation.xml` in the feature's `res/navigation/` with `android:id="@+id/<feature>_flow"` and include it from `app_main_navigation.xml` via `<include app:graph="@navigation/<feature>_navigation" />`.
6. Wire at least one action from `homeFragment` into the new flow so the feature is reachable on a cold start (or replace the start destination if that fits the product better).

## 2. Layering inside a feature

Every feature follows **DDD-flavoured per-context layering**: `UseCase → Repository → DataSource`. The three layers live side by side inside the module; they do not get factored out into shared layers.

```
<feature>/src/main/java/com/moove/<feature>/
  data/
    <Feature>DataRepository.kt        — impl of Repository interface
    <source>/                          — net/ or local/
      api/<...>Api.kt                  — Retrofit / Room / etc.
      <Feature><Source>DataSource.kt   — wraps the raw source
      dto/<...>DTO.kt                  — DTOs + asDomain mappers colocated
  domain/
    <Feature>Repository.kt             — interface
    model/<...>.kt                     — domain data classes
    exceptions/<...>Exception.kt       — domain-specific exceptions
    use_cases/<Verb><Noun>UseCase.kt
  presentation/
    <screen>/
      <Screen>Fragment.kt
      <Screen>Route.kt                 — Compose feature
      <Screen>Screen.kt
      <Screen>State.kt                 — State + Effect
      <Screen>ViewModel.kt
      <Screen>Navigator.kt
      component/                        — reusable screen sub-components
      model/<...>Model.kt              — UI models + asPresentation + Fake
  di/
    <Feature>Module.kt                 — Koin module (or a set of Hilt modules)
```

## 3. Domain layer

- **UseCase naming:** `<verb in present tense><Noun>UseCase` — `GetPopularMoviesUseCase`, `BuyTicketUseCase`, `LogOutUserUseCase`. The verb is not negotiable — "PopularMoviesUseCase" is wrong.
- **One public method via `operator fun invoke(...)`** — nothing else on the class surface.
- Default `invoke` to **`suspend`** even when the current body is synchronous — it future-proofs the call site. The only accepted non-suspend `invoke()` shape is one that returns a `Flow<...>` (including `Flow<PagingData<T>>`, see `GetPopularMoviesUseCase`).
- UseCases MAY depend on:
  - Repositories (interfaces).
  - Other UseCases — composition is fine when the composite concept has its own meaning.
- **Domain models** are Kotlin `data class`es. Immutable. They **never** implement `Parcelable` or `Serializable`.
- **Repository interfaces live in the domain layer**, implementations in data (Dependency Inversion). Interface naming: `<Type>Repository`, e.g. `TicketsRepository`, `MoviesRepository`.
- **Domain exceptions** belong in `domain/exceptions/`. Define them when the presentation layer needs to differentiate cases (`MovieNotFoundException`, `MoviesApiException`). Don't define a domain exception just to wrap a generic network error — let `NoConnectionException` from `:core` propagate.
- **The domain layer must not know about `CoroutineScope`, `Dispatchers`, `android.*`, or any framework type.** Scopes are chosen by presentation or data. A `@Qualifier` for a dispatcher (e.g. `@IoDispatcher` in `:movies/di/`) is a data-layer concern.
- **No mappers in the domain layer.** Mapping lives in the layer that owns the non-domain type.
- **No utility classes in the domain layer.** If you need business logic, it's a UseCase.

## 4. Data layer

- **Repository impl naming:** `<Type>DataRepository` (e.g. `TicketsDataRepository`, `MoviesDataRepository`) — never the same name as the interface, never `Impl` suffix.
- **DataSource naming:** `<Type><SourceKind>DataSource`. Accepted source kinds: `Local`, `Remote`, `Network`, `File`, `Memory`. Examples: `TicketsLocalDataSource`, `MoviesRemoteDataSource`. **Never leak the implementation** — no `UserSQLiteDataSource`, `UserDataStoreDataSource`, `UserRoomDataSource`. Renaming the storage mechanism later must not break callers.
- Each DataSource handles **one** source. DataSources are `internal class` when the language allows it; they are accessed only by the same-module Repository.
- **DTOs:** suffix `DTO`. Moshi-first: `@JsonClass(generateAdapter = true)` with `@Json(name = "snake_case")` for wire names. DTOs MAY implement `Parcelable`/`Serializable` if they must travel through Android IPC; by default they do not.
- **DTO → Domain mappers live in the same Kotlin file as the DTO**, named `asDomain()` (and `List<X>.asDomain()` for collections). Repositories and callers of the Repository never see DTOs.
- **Exception translation happens in the DataSource** — the Repository receives domain-shaped exceptions only. Map `retrofit2.HttpException` by status code (e.g. `404 → MovieNotFoundException`, other → `MoviesApiException`). Let `NoConnectionException` (from `:core`'s `ConnectionErrorInterceptor`) propagate untouched.
- Repositories choose the `CoroutineDispatcher`. Inject it through the constructor — both for testability and because the domain layer must not know about it. Convention: parameter name `backgroundDispatcher`, default `Dispatchers.IO`.
- Repositories are **stateless when possible**, but **registered as singletons** in DI to avoid duplicate caches. DataSources follow the same rule. Use `Mutex` inside if the repo must hold mutable state.
- Paging: a Repository exposing `Flow<PagingData<Domain>>` keeps pagination internal — the `PagingSource` lives in the data layer and is instantiated by the Repository via `Pager(...)`.

## 5. Presentation layer (Orbit MVI)

Each screen is assembled from five pieces. Do not collapse them.

1. **Router Container = Fragment.** Thin wrapper. Holds the view-binding delegate, injects the Navigator from Koin with `parametersOf(findNavController(), lifecycleScope, requireContext())`, and mounts the Compose root (or the XML adapter, see below). It also carries the `@AndroidEntryPoint` annotation when the VM is Hilt-managed.
2. **Route** (Compose features only). `@Composable` function named `<Feature>Route`. Obtains the ViewModel (`koinViewModel()` / `hiltViewModel()`), collects state via `composableState()`, collects side effects via `composableEffect { ... }`, passes everything to the Screen. **The Route, never the Fragment, wires the Navigator into side-effect handling.**
3. **Screen** (Compose features only). Pure `@Composable` that takes a `State` and callbacks — nothing more. **Never pass the ViewModel into a Screen composable.** The Screen must be renderable from a `@Preview` with a `<Model>Fake` instance.
4. **ViewModel.** `ContainerHost<State, Effect>`. Built with `container(initialState, buildSettings = { exceptionHandler = ... .asCoroutineExceptionHandler() })`. All work happens in `intent { ... }` blocks. Reductions happen in `reduce { ... }`. Side effects via `postSideEffect(...)`. **Never execute a UseCase inside `reduce { }`**.
5. **Navigator.** Plain class that takes the relevant shared navigator interface(s) + `ScreenNavigator` and exposes per-screen navigation methods. Implements `ScreenNavigator` for back navigation. Lives in the feature module; gets constructed by Koin at Fragment injection time (with `parametersOf`).

### UseCase invocation from ViewModels

- Use the `executeUseCase { useCase(args) }` extension (from `:shared/presentation/viewmodel/ViewModelExtensions.kt`). It wraps the result in `kotlin.Result<R>` and forwards failures to the container's `CoroutineExceptionHandler`.
- **`executeUseCase` handles exactly ONE UseCase call.** To combine UseCases either:
  - create a composite UseCase in the domain layer, or
  - call each `executeUseCase { ... }` separately and combine `Result`s.
- If you find yourself with 10+ UseCases on a ViewModel, that's a signal to **split the ViewModel**, not to compress its surface.

### State & Effect

- **State:** `@Parcelize`-annotated `data class` with defaults on every property. `@Parcelize class FooState : Parcelable` for empty state.
- **Use distinct loading flags** when the screen has more than one concurrent loading concept (e.g. initial fetch vs pull-to-refresh). A single `status: ScreenContentStatus` field is only acceptable when the screen really has one status axis (see `FareListState`). Prefer the multi-flag approach when in doubt.
- **Effect:** `sealed class <Feature>Effect` / `sealed interface`. One-time events only (nav, toast, snackbar, dialog, analytics). Never store the VM's decisions here — those go in State.
- Naming per screen: `[Feature]State`, `[Feature]Effect`, `[Feature]ViewModel`, `[Feature]Route`, `[Feature]Screen`, `[Feature]Navigator`, `[Feature]Fragment`.

### XML screens

Some screens (e.g. the Popular Movies list in `:movies`) use XML + RecyclerView rather than Compose. The MVI split still applies:

- Fragment holds the view-binding delegate and the Navigator (Koin).
- ViewModel remains `ContainerHost<State, Effect>`. If the list uses Paging 3, expose `Flow<PagingData<PresentationModel>>` as a ViewModel property (`cachedIn(viewModelScope)`), not as part of State — Paging's `CombinedLoadStates` owns the loading/error status.
- Fragment collects `viewModel.container.sideEffectFlow` for navigation/snackbar and `adapter.loadStateFlow` for RecyclerView loading UI, both via `viewLifecycleOwner.lifecycleScope.launch { repeatOnLifecycle(STARTED) { ... } }`.

### Presentation models (UI models)

- Naming: `<Feature>Model` (e.g. `MovieSummaryModel`).
- `@Parcelize`, immutable. MAY implement `Parcelable`.
- Domain → Presentation mapping via `fun <Domain>.asPresentation()` — mapper colocated with the Model.
- Provide a `<Model>Fake` file with fakes for Compose `@Preview`s and state templates. Internal visibility by default.

## 6. Navigation

- **Android Navigation Component with Safe Args** (plugin `androidx.navigation.safeargs.kotlin`) is the only navigation mechanism.
- Each feature has its own `res/navigation/<feature>_navigation.xml` with a single `<navigation android:id="@+id/<feature>_flow">` root. Start destination is the feature's landing screen.
- The app root graph `app/src/main/res/navigation/app_main_navigation.xml` `<include>`s every feature nav graph.
- Cross-module navigation is done through **shared navigator interfaces** (`TicketsNavigator`, `MoviesNavigator`, `ScreenNavigator`) in `:shared/navigation/`. `GlobalAppNavigator` extends all of them.
- `AppNavigator` in `:app/navigation/` is the single concrete implementation — bound to every shared navigator interface at once via Koin `binds arrayOf(...)`. It is the only class allowed to call `NavController.navigate*(...)` with generated `<Fragment>Directions`.
- Use `NavController.navigateSafely(direction)` (from `:shared`) — it merges default animations and guards against double-taps.
- Cross-module destination IDs (`R.id.movieListFragment`) live in the feature's generated R, not in `:app`'s. Reference them as `com.moove.<feature>.R.id.<id>`.

## 7. Deeplinks

Pipeline (exists as-is in code — extend, don't reinvent):

1. `MainActivity.onCreate` / `onNewIntent` → `MainActivityViewModel.handleIntent(intent)` → `GetDeeplinkUseCase(uri)`.
2. `GetDeeplinkUseCase` first delegates to `GetDynamicLinkUseCase` (Firebase Dynamic Links — may return `null` when the host doesn't match), then to `DeeplinkRepository.getDeepLink(uri)`.
3. `DeeplinkRepository` delegates to `AppDeepLinkLocalDataSource` which pattern-matches the URI and returns a concrete `AppDeepLink` sealed class instance — or `AppDeepLink.Unknown`.
4. ViewModel emits `MainActivityEffect.NavigateDeepLink(link)` → `MainActivity` observes → `MainNavigator.navigateDeepLink(link)` → `DeepLinkAppNavigator.navigateTo(link)` dispatches to the right feature Navigator.

Rules for adding a new deeplink:

- Add a subclass to `AppDeepLink` (`:app/feature/deeplink/domain/AppDeepLink.kt`). Its constructor holds the extracted params — nothing else.
- Parse in `AppDeepLinkLocalDataSource.getDeepLinkData()` using **`java.net.URI`** (not `android.net.Uri`) so the parser is JVM-testable.
- Route in `DeepLinkAppNavigator.navigateTo(link)` by dispatching to a feature navigator. If the feature navigator doesn't exist yet, define one in `:shared/navigation/`, have `GlobalAppNavigator` extend it, add the impl in `AppNavigator`, and bind in `mainModule`.
- Add the corresponding `<intent-filter>` to `AndroidManifest.xml`. `android:autoVerify="true"` is only valid for http(s) App Links — drop it for custom schemes (e.g. `movieapp://`).

## 8. Networking

- Retrofit + Moshi (`moshi-kotlin`, `moshi-adapters`, `moshi-sealed-runtime`) with Moshi codegen via KSP (`ksp libs.moshi.compiler`).
- One `OkHttpClient` per API surface (TMDB, internal API, etc.) — they can share the same underlying `Moshi`. Per-API interceptors (API-key, Bearer token) attach to the per-API client.
- Always include `ConnectionErrorInterceptor` (from `:core`) so IOExceptions are surfaced as `NoConnectionException`. Add `HttpLoggingInterceptor` at `BASIC` only in `BuildConfig.DEBUG`.
- Base URLs, API keys, and other per-environment constants come from `BuildConfig` fields declared in the consuming module's `build.gradle`. Secrets that should not hit Git go through `local.properties` (see `:movies` reading `tmdb.apiKey`).
- Retrofit instances are exposed from DI as singletons; Retrofit service interfaces (`TmdbApi`, etc.) are also singletons.

## 9. Dependency Injection

- **Koin is the project default.** All existing modules (`mainModule`, `ticketsModule`, `deepLinkModule`, `netModule`, `coroutineModule`, `exceptionsModule`) and all navigator construction stay on Koin. New features should use Koin unless there's a strong reason not to.
- **Hilt coexistence pattern** (demonstrated in `:movies`): Hilt manages the feature's data/domain/ViewModels; Koin still manages navigators (because they depend on the Fragment's `NavController` passed through `parametersOf`, which is a Koin idiom).
  - Turn on Hilt by annotating `MooveApp` with `@HiltAndroidApp` (idempotent — existing Koin continues to run).
  - Each Hilt-using Fragment gets `@AndroidEntryPoint`.
  - Each Hilt-using ViewModel gets `@HiltViewModel` with `@Inject constructor`.
  - A `KoinBridgeModule` (Hilt `@Module`, `SingletonComponent`) `@Provides` the cross-cutting Koin-managed singletons (`ExceptionHandler`, dispatcher qualifiers) by calling `KoinJavaComponent.get(...)`. This keeps `MainExceptionHandler` as the single process-wide error sink.
- Feature Koin module naming: `<feature>Module` (e.g. `ticketsModule`). Registered in `MooveApp.setupDependencyInjection().startKoin { modules(...) }`.
- **DI of navigators:** register `<Feature>Navigator` as a `factory { ... }`. The top-level `AppNavigator` is bound to every navigator interface it implements via `binds arrayOf(ScreenNavigator::class, TicketsNavigator::class, MoviesNavigator::class, GlobalAppNavigator::class)`.

## 10. Cross-cutting

- **Exception handling:** the project-wide sink is `MainExceptionHandler` (composite). New feature-level `ExceptionHandler` implementations are opt-in — add them to the list injected into `MainExceptionHandler` in `exceptionsModule`. Use them only when you need UI-level reaction to a specific exception class.
- **Coroutines only** — never RxJava, never `AsyncTask`, never `Thread`. Play Services' `Task` is bridged via `kotlinx-coroutines-play-services` (`.await()`).
- **Version catalog is the single source of truth** (`gradle/libs.versions.toml`). Never inline a Maven coordinate in a `build.gradle`. Prefer libs that are already re-exported via `api` from `:shared`.
- **Kotlin global opt-ins** (from `gradle/base-android-config.gradle`) already cover `RequiresOptIn`, `ExperimentalStdlibApi`, `ExperimentalContracts`, `ExperimentalCoroutinesApi`, `FlowPreview` — do not add per-file `@OptIn(...)` for these.
- **Build types:** `debug` / `review` / `release`. Don't add new build types without a product reason; `review` exists and falls back to the `release` config (`matchingFallbacks = ['release']`).
- **Lint:** `abortOnError = true` project-wide. Don't disable rules per-module; extend `lint.xml` at the root if something is broken globally.
- **Java 17 with desugaring** — `java.time.*`, `java.util.concurrent.Flow` etc. are available down to `minSdk 23`. Prefer `java.time.LocalDate` over `Calendar`/`Date`.
