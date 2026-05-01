# Code Style Rules

Naming, file organisation, and Kotlin/Android idioms. Read alongside `architecture.md` — this file covers *how* code is spelled; `architecture.md` covers *where* code lives and *which* layer owns what.

## 1. Naming

### Classes

| Role | Suffix | Example |
|---|---|---|
| UseCase | `UseCase` | `GetMovieDetailsUseCase`, `BuyTicketUseCase` |
| Repository interface (domain) | `Repository` | `MoviesRepository`, `TicketsRepository` |
| Repository impl (data) | `DataRepository` | `MoviesDataRepository`, `TicketsDataRepository` |
| DataSource | `<Kind>DataSource` | `MoviesRemoteDataSource`, `TicketsLocalDataSource`. Kind ∈ {`Local`, `Remote`, `Network`, `File`, `Memory`}. NEVER leak storage type (`UserSQLiteDataSource`, `UserDataStoreDataSource` are banned). |
| DTO | `DTO` | `MovieSummaryDTO`, `RyderDTO` |
| Domain model | (plain) | `MovieSummary`, `Ryder` |
| Presentation model | `Model` | `MovieSummaryModel`, `FareModel` |
| Fake data for UI previews | `Fake` (suffix) or `fake...` (val) | `MovieSummaryModelFake.kt`, `val fakeRyderModels` |
| Object Mother (tests) | `Mother` | `MovieSummaryDTOMother`, `RyderDTOMother` |
| Exception | `Exception` | `MovieNotFoundException`, `DynamicLinkParseException` |
| ViewModel | `ViewModel` | `MovieListViewModel` |
| State | `State` | `MovieListState` |
| Effect (sealed for one-time events) | `Effect` | `MovieListEffect`, `MovieDetailsEffect` |
| Route (Compose) | `Route` | `MovieDetailsRoute` |
| Screen (Compose) | `Screen` | `MovieDetailsScreen` |
| Navigator (per screen) | `Navigator` | `MovieListNavigator` |
| Fragment | `Fragment` | `MovieListFragment` |
| Adapter (RecyclerView) | `Adapter` | `MovieListAdapter` |
| LoadState adapter (paging) | `LoadStateAdapter` | `MoviesLoadStateAdapter` |
| DI module (Koin) | `<feature>Module` (lowercase `val`) | `val ticketsModule`, `val moviesModule` |
| DI module (Hilt) | `<Purpose>Module` (object/abstract class) | `NetworkModule`, `DataModule`, `KoinBridgeModule` |
| Qualifier (Hilt) | (plain) | `@IoDispatcher`, `@TmdbClient`, `@TmdbMoshi` |

### Packages

```
com.moove.<feature>.
  data.
    <source>.                e.g. net / local / file
      api.                   Retrofit / Room DAO interfaces
      dto.                   DTOs + asDomain mappers in same file
  domain.
    model.
    exceptions.
    use_cases.               snake_case package name — match existing convention
  presentation.
    <screen>.
      component.              reusable sub-composables
      model.                  UI models + fakes
      adapter.                RecyclerView adapter + ViewHolder (XML screens only)
  di.
```

### Functions

- **UseCase entry point is always `operator fun invoke(...)`** — no other public method.
- **Mapping functions use the `asX` prefix** when the same conceptual thing exists across layers:
  - `fun DTO.asDomain(): Domain`
  - `fun List<DTO>.asDomain(): List<Domain>`
  - `fun Domain.asPresentation(): Model`
  - `fun Domain.asDTO(): DTO` (only if something flows domain → data)
- **Mapping functions use the `toX` prefix** when converting between fundamentally different types:
  - `fun MovieSummaryModel.posterUrl(size: String): String?`
  - `fun Fare.toAdsCountFormat(): String`
- **Intent handlers on ViewModels** are `onX()` / `onXClick()`: `onMovieClick`, `onRyderClick`, `onRetry`, `onBack`.
- **Test methods** use **backtick names with spaces**, structured Given–When–Then:
  ```kotlin
  @Test
  fun `given a blank release date when mapping then produces null`() { ... }
  ```

### Files

- One top-level class per file. Exceptions:
  - DTOs colocate the `asDomain` mapper(s) in the same file as the DTO declaration.
  - Presentation models colocate the `asPresentation` mapper.
  - A `State` file may hold both `State` (data class) and `Effect` (sealed class) — they belong together logically.
- File name matches the primary class.

## 2. Class / construct conventions

### Immutability

- **Domain models** — `data class` (or `data object` for singletons). Never `Parcelable`/`Serializable`.
- **Presentation models** — `data class` with `@Parcelize`. Navigate them via Safe Args `app:argType`.
- **DTOs** — `data class` with Moshi `@JsonClass(generateAdapter = true)` and `@Json(name = "...")` on every non-default-matched field. Wire names are `snake_case` for TMDB-style APIs.
- **State** — `@Parcelize data class` with defaults on every field. For an empty state, use `@Parcelize class FooState : Parcelable`.
- **Effect** — `sealed class` (or `sealed interface`) with `data object` for parameterless cases and `data class` for ones carrying data.
- **Repositories / DataSources / UseCases** — regular classes, no copy/equals needed. Prefer `internal` for anything not consumed outside the module (DataSources, `DataRepository` impls).

### Constructors

- Primary constructor carries all dependencies. No `lateinit` for deps.
- A `CoroutineDispatcher` for background work is **always** constructor-injected — not `Dispatchers.IO` inline. Convention: param `backgroundDispatcher`, default `Dispatchers.IO` in the data layer. For Hilt-managed classes, use `@IoDispatcher` qualifier.
- For Hilt, `@Inject constructor(...)`. For Koin, plain `class Foo(private val bar: Bar)` + module registration.
- Constructor injection for ViewModel params that come from navigation uses `SavedStateHandle` (Hilt) or `parametersOf` (Koin, via `koinViewModel { parametersOf(...) }`).

### Kotlin features

- Use `data object` for singleton sealed subclasses, not `object`.
- `sealed interface` is fine when closed polymorphism doesn't benefit from class state — match `MainActivityEffect` / `MovieListEffect` style.
- Prefer **scope functions** (`let`, `apply`, `run`) sparingly — don't chain more than two.
- Use **named arguments** for anything with more than two positional arguments at a call site. Required in all Koin factory closures for clarity.
- Use `require(cond) { "msg" }` / `requireNotNull(x) { "msg" }` for preconditions, not `if (...) throw ...`.
- Trailing commas on multi-line argument/parameter lists.

### Nullability

- Domain models expose `?` only when the concept is genuinely optional (e.g. `posterPath: String?` — a movie may have no poster uploaded). Do **not** use `?` as a proxy for "we haven't loaded this yet" — that belongs in State as a loading/partial variant.
- DTOs mirror the wire contract — if TMDB can return `null`, the DTO field is `T?`. The `asDomain` mapper is where null-handling policy is encoded (`overview.orEmpty()`, `runtimeMinutes?.takeIf { it > 0 }`, bad-date → `null`).
- Never `!!` in production code. If the invariant is real, use `requireNotNull(...)` with a diagnostic message.

## 3. Compose conventions

- **Stateless composables.** The `Screen` takes a `State` + callbacks. The `Route` owns state collection, side-effect collection, and ViewModel acquisition.
- **Do not pass the ViewModel into the Screen composable** — this is the most violated rule under time pressure; enforce it in review.
- Pass composable slots as `@Composable () -> Unit` when flexibility matters (icons, titles, actions). Pass primitives when the content is truly one of a fixed set.
- Collect state with `val state by viewModel.composableState()` (from `:shared/presentation/viewmodel/OrbitExtensions.kt`). Collect side effects with `viewModel.composableEffect { effect -> ... }`. Both are lifecycle-aware by default.
- Compose `@Preview`s must be fed a `Fake` instance, and there should be one preview per meaningful state (Success, Failure, Loading if non-trivial).
- Use `ScreenContent(status = state.status) { ... }` from `:shared/presentation/compose/component/` for the standard Loading / Success / Failure surface on Compose screens. Its error slot takes a composable; wire Retry there.
- For the Compose root in a Fragment, use `Fragment.setAppComposeContent(viewBinding.compose) { <Feature>Route(...) }` from `:shared/presentation/compose/platform/`.

## 4. XML + View conventions

When a screen uses XML (the Popular Movies list in `:movies` does, for spec reasons):

- Layout file naming: `<feature>_<screen>.xml` → `movie_list_fragment.xml`, `movie_list_item.xml`, `movie_list_load_state.xml`.
- View binding is enabled globally. The Fragment uses the `viewBinding(...)` delegate (`:shared/presentation/fragment/delegate/FragmentViewLifecycleAwareDelegateExtensions.kt`):
  ```kotlin
  private val viewBinding by viewBinding(MovieListFragmentBinding::bind)
  ```
- **Never** `findViewById`. Never `synthetic` imports (Kotlin Android Extensions is removed).
- Prefer **Material Components** (`MaterialCardView`, `MaterialButton`) over plain AppCompat widgets.
- Use **ConstraintLayout** for layouts with 3+ children. `LinearLayout` is fine for 2-child vertical stacks.
- Attribute order in XML: `android:id` → `layout_*` → `padding*`/`margin*` → other android attrs → `app:*` → `tools:*`. Leave one blank line between logically grouped attribute blocks only when that aids reading.
- RecyclerView lists:
  - Use `PagingDataAdapter<Model, VH>` when backed by Paging, plain `ListAdapter<Model, VH>` otherwise — never `RecyclerView.Adapter` directly.
  - Provide a `DiffUtil.ItemCallback` keyed on a stable `id`.
  - Use Coil's `ImageView.load(url) { placeholder(...); error(...); crossfade(true) }` for image loading (Coil is in `:shared` via `api`).

## 5. Comments & documentation

- **Default to no comments.** Well-named identifiers carry intent; comments rot.
- Write a comment only when the **why** is non-obvious:
  - A hidden constraint (`// poster URLs are relative; join with image base`)
  - A workaround for a specific bug with context (`// TMDB returns runtime=0 for unreleased titles`)
  - Behaviour that would surprise a reader
- **Do not** explain *what* the code does — the code does that.
- **Do not** reference the task, fix, or caller in a comment ("added for MOO-123", "used by ListFragment") — that belongs in the commit or PR description.
- KDoc (`/** ... */`) is welcome on public APIs in `:shared` and `:core` where the contract is not obvious from the signature.
- Comments use normal prose — no ASCII art, no banners, no `// region/endregion` sprawl (some older code has these; don't add more).

## 6. Resources & strings

- Every user-facing string goes in `res/values/strings.xml` inside the owning module — never hardcoded in Kotlin or XML layouts.
- String naming: `<feature>_<purpose>` (`movies_list_title`, `movie_details_error_title`, `movies_retry`). Format strings use positional `%1$s`, `%1$d`, `%1$.1f`.
- Icons / drawables live in the consuming module when they're feature-specific (`:movies/res/drawable/ic_poster_placeholder.xml`). Shared icons go in `:design-system`.
- Colors / typography / spacing tokens always come from `:design-system` (`AppTheme.colors.*`, `AppTheme.typography.*`, `Spacing.*`, `Dimensions.*`). **Never** hardcode hex / sp / dp values in Compose screens that are part of a shared design vocabulary — one-offs (e.g. `Modifier.size(18.dp)` for a star icon) are fine.

## 7. Error handling

- **Boundaries.** Validate at system boundaries (URI parsing, network responses, user input). Trust your own code in between.
- **Data layer catches and translates.** Retrofit/Room exceptions become domain exceptions at the DataSource level. Repositories re-throw domain exceptions, never network exceptions.
- **ViewModel:** use `executeUseCase { ... }` — it returns `Result<R>`, forwards exceptions to the container's `CoroutineExceptionHandler`, and lets you `.onSuccess`/`.onFailure` for state transitions and side effects.
- **Fallbacks for impossible scenarios are a smell.** If the code that would trigger a `?: error(...)` is in the same module and guaranteed, don't write the `?:`. Only validate where the compiler can't.
- **`Result<T>` is for ViewModel-side handling of UseCase outcomes**, not for public APIs. Repositories and UseCases throw; ViewModels convert to Result via `executeUseCase`.

## 8. Kotlin opt-ins already global

`gradle/base-android-config.gradle` enables these project-wide — do not `@OptIn(...)` for them at file level:

- `kotlin.RequiresOptIn`
- `kotlin.ExperimentalStdlibApi`
- `kotlin.contracts.ExperimentalContracts`
- `kotlinx.coroutines.ExperimentalCoroutinesApi`
- `kotlinx.coroutines.FlowPreview`

For orbit-test's `OrbitExperimental`, the **new `org.orbitmvi.orbit.test.*` API (7.x) is not experimental** — just import `org.orbitmvi.orbit.test.test` and call it. The old `org.orbitmvi.orbit.test` (deprecated) did require the opt-in.

## 9. Build / dependencies

- Every dependency entry goes in `gradle/libs.versions.toml`. Never inline Maven coordinates in a `build.gradle`. Use bundles (`libs.bundles.androidx.compose`, `libs.bundles.tests.unit`, etc.) when declaring groups.
- Feature modules should **inherit** third-party deps from `:shared` (which re-exports via `api`). A feature `build.gradle` should typically only list:
  - `implementation project(':shared')`
  - `ksp libs.moshi.compiler` (if the module declares `@JsonClass` DTOs)
  - Hilt deps (if the module opts into Hilt)
  - Test dep bundles
- Namespace: `com.moove.<feature>`. BuildConfig fields are declared in the module that owns them (e.g. `:movies` owns `TMDB_API_KEY`, `TMDB_BASE_URL`, `TMDB_IMAGE_BASE_URL`; `:app` owns `FIREBASE_DYNAMIC_LINK_HOST`).
- Secrets come from `local.properties` (which is gitignored) and are read in `build.gradle`:
  ```groovy
  def tmdbProperties = new Properties()
  def tmdbPropertiesFile = rootProject.file('local.properties')
  if (tmdbPropertiesFile.exists()) tmdbProperties.load(new FileInputStream(tmdbPropertiesFile))
  def tmdbApiKey = tmdbProperties.getProperty('tmdb.apiKey', '')
  ```
  Then surface as `buildConfigField "String", "TMDB_API_KEY", "\"$tmdbApiKey\""`.

## 10. Small but non-negotiable

- **No emojis in source code**, commit messages, or documentation unless the user asks. This includes log strings.
- **No `TODO`/`FIXME` without a tracking reference** or an inline plan. A lone `// TODO` is indistinguishable from dead code and will be removed on sight.
- **No wildcard imports** — IDE is configured to avoid them; don't enable them locally.
- **Trailing newline** at the end of every source file.
- **4-space indents** in Kotlin, matching existing files. Continuation indents also 4 (not 8).
- **Line length:** prefer ≤120 chars. Wrap long argument lists with one argument per line, trailing comma.
