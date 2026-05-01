# Testing Rules

How to write and cover code with tests in the Moove Android app. Read alongside `architecture.md` (where code lives) and `code-style.md` (how code is spelled). This file covers what to test, how to structure the test, and which libraries to reach for. The conventions are crystallised from the live test code in `:movies` and `:tickets`. When adding a test, treat these as hard rules — if a generic Android testing tutorial conflicts with what's here, this file wins.

## 1. Source sets and module ownership

- **Unit tests** live in `<module>/src/test/`. JVM-only, no emulator. Default to this for everything testable.
- **Instrumented tests** live in `<module>/src/androidTest/`. Reserve for things that genuinely need a device/Robolectric: Compose UI semantics, real Android `Intent`/`Uri` parsing, accessibility, scroll/swipe.
- Tests sit in the **same package** as the code under test, so `internal` visibility is honoured and so the test file is easy to find by mirror-path.
- Modules that currently carry unit tests: `:movies`, `:tickets`, `:shared`, `:core`, `:app`. New feature modules inherit the same layout from the `:tickets` build template (see `architecture.md` §1).

## 2. Test stack and version-catalog bundles

Single source of truth for versions: `gradle/libs.versions.toml`. Never inline a Maven coordinate in a feature `build.gradle`.

- **JUnit 4** (`junit 4.13.2`). Not JUnit 5. `@Test` from `org.junit`, assertions from `org.junit.Assert.*` and `kotlin.test`.
- **MockK 1.13.9** for mocking — `mockk`, `every`, `verify`, `coEvery`, `coVerify`.
- **kotlinx-coroutines-test** for `runTest` and `TestScope`.
- **orbit-test 7.1.1** for `ContainerHost` ViewModels — `org.orbitmvi.orbit.test.test`. The 7.x API is **not** experimental; do not add `@OptIn(OrbitExperimental)`.
- **javafaker 1.0.2** for randomised test data.

Use the existing bundles, not individual entries:

```groovy
testImplementation libs.bundles.tests.unit        // junit, javafaker, mockk, kotlin-test, kotlin-coroutines-test
testImplementation libs.bundles.tests.viewmodel   // orbit-test — add when the module has Orbit ViewModels
androidTestImplementation libs.bundles.tests.android  // androidx-test-junit, androidx-test-espresso
```

Truth, AssertJ, Kotest, and Hamcrest are not in the catalog and should not be added. Turbine is declared but currently unused — for state and side-effect Flows, use orbit-test's DSL instead of collecting Flows by hand.

## 3. Naming

- **Test class:** `<TypeUnderTest>Test`. One public class per file. Examples: `MoviesDataRepositoryTest`, `MovieListViewModelTest`, `MovieDetailsDTOTest`.
- **Test method:** backtick-quoted, lowercase Given–When–Then:
  ```kotlin
  @Test
  fun `given a dto with null overview when mapping then overview is empty`()
  ```
  The older "On X" imperative shape (`` `On init fetch fares successfully` ``) survives in `FareListViewModelTest` for legacy reasons — do not propagate it. New tests use given/when/then.
- **Object Mother file:** `<Type>Mother.kt`, lives in `src/test/` next to the type's package. Function names: `random<Type>(...)`, `random<Type>List(...)` when shape varies.
- **Test helpers** (e.g. `httpException(code:)` in `MoviesRemoteDataSourceTest`) are `private` functions on the test class unless reused — only promote to a top-level helper file if a second test needs them.

## 4. Test data — Object Mothers and Faker

Build test data through top-level `random<Type>(...)` builders with named-default parameters seeded by `Faker`. Override only the field(s) the test asserts on; leave everything else random. This keeps tests robust to model expansion and makes the test name's claim obvious.

Reference templates:
- `movies/src/test/java/com/moove/movies/data/net/dto/MovieDetailsDTOMother.kt`
- `tickets/src/test/java/com/moove/tickets/domain/model/RyderMother.kt`

Reuse the global helpers from `:shared` — they live in production code (`shared/src/main/java/com/moove/shared/ModelMother.kt`) on purpose so feature tests can import them:

- `faker` — shared `Faker` instance, prefer this over instantiating your own when generating cross-cutting primitives.
- `randomListOf(min, max) { random<Element>() }` — list of randomised size.
- `randomNullableOf { random<X>() }` — flips a coin between `null` and a value.
- `Array<E>.random()` — pick one element uniformly.

A Mother file that needs only single-purpose primitives may declare its own `private val faker = Faker()` (this is what `MovieDetailsDTOMother` does). Either is fine.

## 5. What to test at each layer

Tests follow the architecture in `architecture.md`. Cover behaviour, not lines.

### DTO mappers (`asDomain`)

One test per non-trivial null/empty/zero/edge case the mapper handles. Pattern:

```kotlin
@Test
fun `given a dto with null overview when mapping then overview is empty`() {
    val dto = randomMovieDetailsDTO(overview = null)

    val domain = dto.asDomain()

    assertEquals("", domain.overview)
}
```

If the mapper has no policy decisions (straight field-for-field copy), one happy-path test is enough. If it does (`overview.orEmpty()`, `runtimeMinutes?.takeIf { it > 0 }`, bad-date → `null`, `genres.orEmpty()`), each branch gets its own test. Reference: `movies/src/test/java/com/moove/movies/data/net/dto/MovieDetailsDTOTest.kt`.

### DataSource

The DataSource is where exception translation happens, so tests there are largely exception tests. One test per HTTP status code the source maps:

```kotlin
@Test
fun `given 404 when getting details then MovieNotFoundException is thrown`() = runTest {
    coEvery { api.getMovieDetails(movieId = 99L) } throws httpException(code = 404)

    try {
        dataSource.getDetails(99L)
        fail("Expected MovieNotFoundException")
    } catch (e: MovieNotFoundException) {
        assertEquals(99L, e.movieId)
    }
}
```

Plus one happy-path test that confirms the DTO round-trips. Reference: `movies/src/test/java/com/moove/movies/data/net/MoviesRemoteDataSourceTest.kt` (note the local `httpException(code:)` helper).

### Repository

Repositories are thin — they delegate to a DataSource and apply a dispatcher. A single happy-path test per public method that confirms DTO→domain mapping survives the boundary is enough. Don't re-test exception translation (that's the DataSource's job). Reference: `movies/src/test/java/com/moove/movies/data/MoviesDataRepositoryTest.kt`.

### UseCase

Mock the repository, call `useCase(args)`, assert on the return value, `coVerify` the repository was called with the arguments the UseCase received. Don't re-test mapping — the Repository test owns that.

### ViewModel

See section 7.

### Skip

Fragments, Compose `Screen` composables, Navigators, DI modules, generated `*Args` classes, and the `:design-system` module's tokens. `Screen` composables get `@Preview` coverage with `Fake` instances (see `code-style.md` §3); that's the spec.

## 6. Coroutines, dispatchers, and `runTest`

- Every async test runs inside `runTest { ... }` from `kotlinx.coroutines.test`. Never `runBlocking`.
- Inject `Dispatchers.Unconfined` into the SUT through the constructor — repositories take a `backgroundDispatcher` exactly so this is possible:
  ```kotlin
  private val repository = MoviesDataRepository(
      remoteDataSource = remote,
      backgroundDispatcher = Dispatchers.Unconfined,
  )
  ```
- Do **not** call `Dispatchers.setMain` and do not introduce a `MainCoroutineRule`. The project doesn't use them, and they're unnecessary when dispatchers are constructor-injected.
- For ViewModels, orbit-test's `buildSettings = { isolateFlow = false }` lets state and side-effect emissions resolve eagerly within `runTest`.

## 7. Orbit ViewModel testing

Use `org.orbitmvi.orbit.test.test` (7.x). Standard shape:

```kotlin
private fun TestScope.createViewModel(state: FooState = defaultState) =
    FooViewModel(deps).test(
        initialState = state,
        buildSettings = { isolateFlow = false },
    )

@Test
fun `given init when fetching then loading then success`() = runTest {
    coEvery { useCase(arg) } returns data

    createViewModel()
        .runOnCreate()
        .assert(defaultState) {
            states(
                { copy(status = Loading) },
                { copy(status = Success, items = data.asPresentation()) },
            )
            postedSideEffects(/* if any */)
        }

    coVerify { useCase(arg) }
}
```

- Drive user intents with `testIntent { onClick(...) }`.
- Drive lifecycle with `runOnCreate()` for the ViewModel's `init` block.
- Stub all init-path collaborators **before** calling `createViewModel()` — Orbit's `init` runs eagerly under `isolateFlow = false`.
- Assert state transitions as a sequence of `copy { ... }` lambdas in `states { ... }` — orbit-test verifies the full ordered emission, so each entry must match exactly. Use `postedSideEffects(...)` for one-time events.
- For tests that only assert a side effect after a single intent, the shorter shape is also acceptable:
  ```kotlin
  subject.test(this) {
      expectInitialState()
      containerHost.onMovieClick(clicked)
      expectSideEffect(MovieListEffect.GoToDetails(42L))
  }
  ```
  Switch to `.assert { states(...) }` whenever state transitions matter — `expect*` is for one-shot effect checks.

Templates: `tickets/src/test/java/com/moove/tickets/presentation/fare/FareListViewModelTest.kt`, `movies/src/test/java/com/moove/movies/presentation/list/MovieListViewModelTest.kt`.

Always test the exception path — `coEvery { useCase(arg) } throws RuntimeException(...)` and assert the failure state plus the `Effect.ShowGenericError`-style side effect. ViewModels that rely on `executeUseCase { ... }` route exceptions through the container's `CoroutineExceptionHandler`; in tests, supply an `ExceptionHandler` mock (`mockk(relaxed = true)`) and verify the resulting state/effect, not the handler call.

## 8. MockK conventions

- `mockk()` is strict by default — every called member must be stubbed. Use this for the primary collaborator under verification (the API in a DataSource test, the repository in a UseCase test).
- `mockk(relaxed = true)` for collaborators whose calls aren't part of the test's claim — `ExceptionHandler`, peripheral UseCases, navigation collaborators.
- `coEvery` / `coVerify` for suspend functions; `every` / `verify` for sync. Mixing them is fine in the same test.
- Stub before constructing the SUT when the SUT calls into the mock at construction time (true for Orbit ViewModels with `init` work).
- Don't `verify` everything. Verify the call that proves the test name's claim — usually one `coVerify { ... }` per test.
- No `clearMocks` / `unmockkAll` housekeeping is needed: each test class instantiates fresh mocks per field, JUnit 4 creates a new test instance per `@Test`.

## 9. Test structure and assertions

Three-block layout inside every test, separated by blank lines:

```kotlin
@Test
fun `given X when Y then Z`() = runTest {
    // arrange
    val dto = randomMovieDetailsDTO(id = 5L)
    coEvery { remote.getDetails(5L) } returns dto

    // act
    val result = repository.getDetails(5L)

    // assert
    assertEquals(5L, result.id)
    assertEquals(dto.title, result.title)
}
```

Assertion library: `org.junit.Assert.*` (`assertEquals`, `assertNull`, `assertTrue`, `assertFalse`, `fail`) and `kotlin.test.assertEquals`. Pick one per test for consistency. For exceptions, either of these is fine:

```kotlin
try {
    dataSource.getDetails(99L)
    fail("Expected MovieNotFoundException")
} catch (e: MovieNotFoundException) {
    assertEquals(99L, e.movieId)
}
```

```kotlin
val e = assertFailsWith<MovieNotFoundException> { dataSource.getDetails(99L) }
assertEquals(99L, e.movieId)
```

Don't introduce `@get:Rule ExpectedException` — the project doesn't use it.

## 10. Coverage philosophy

No Jacoco or Kover is configured; don't add one without a product reason. Aim for **behaviour coverage**, not line percentage:

- Every DTO mapper edge case (null, empty, zero, malformed).
- Every exception translation branch in a DataSource.
- Every ViewModel state path that produces a different UI.
- Every UseCase argument-forwarding contract.

A pull request that touches a mapper or a state machine without a corresponding test is incomplete. A pull request that touches a Fragment, Navigator, or DI module without a test is fine.

## 11. Anti-patterns

- No `runBlocking` — use `runTest`.
- No `Thread.sleep`, no real timers, no `delay` outside of `runTest`'s virtual time.
- No `!!` in tests. If the data is missing, the Mother needs an override.
- No shared mutable state across tests. `companion object` is for true constants only (`defaultState`, fixed IDs) — never for cached MockK instances.
- No `@Ignore` without a tracking reference and an inline plan describing the unblock condition.
- No emojis in test names, log strings, or assertions.
- No `@RunWith(MockitoJUnitRunner::class)` — the project is on MockK.
- No re-instantiating `Faker()` per test method when `:shared`'s `faker` covers it.

## 12. Instrumented and Compose UI tests

When a screen genuinely needs UI-level coverage:

- Add `src/androidTest/` to the feature module if it doesn't exist.
- Depend on `libs.bundles.tests.android` (`androidx-test-junit`, `androidx-test-espresso`).
- For Compose, add `androidx.compose.ui:ui-test-junit4` (declare in the catalog first) and use `createComposeRule()`.
- Keep instrumented tests focused on Compose-specific concerns: semantics, accessibility, scrolling, gesture recognition. Anything testable on the JVM stays in `src/test/`.
- Fragments, Navigators, and DI wiring still don't get UI tests — the value is in the Compose semantics, not in the lifecycle plumbing.
