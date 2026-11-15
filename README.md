# Kotlin: Beginner to Advanced

Each lesson is a runnable file in `src/lessonNN/`. Open it, read the comments top to
bottom, then click the green ▶ arrow next to `fun main()`. After each lesson, try
the **EXERCISES** at the bottom of the file.

| # | Level | Lesson | Key ideas |
|---|-------|--------|-----------|
| 01 | Beginner | Basics | `val`/`var`, types, inference, strings, conversion |
| 02 | Beginner | Control flow | `if`/`when` as expressions, ranges, loops, labels |
| 03 | Beginner | Functions | default/named args, `vararg`, single-expression, `tailrec` |
| 04 | Beginner | Null safety | `?`, `?.`, `?:`, `!!`, `let`, `lateinit`, `lazy` |
| 05 | Intermediate | Collections | List/Set/Map, `map`/`filter`/`fold`/`groupBy`, sequences |
| 06 | Intermediate | Classes & objects | constructors, properties, visibility, `companion`, `object` |
| 07 | Intermediate | Inheritance | `open`, `abstract`, interfaces, polymorphism, composition |
| 08 | Intermediate | Special classes | `data`, `enum`, `sealed`, `value`, exceptions |
| 09 | Intermediate+ | Lambdas & scope functions | higher-order, closures, `inline`, `let/apply/also/run/with` |
| 10 | Advanced | Generics | constraints, `in`/`out` variance, `reified` |
| 11 | Advanced | Advanced features | extensions, operators, delegation, DSLs, `typealias` |
| 12 | Advanced | Coroutines & Flow | `suspend`, `launch`/`async`, dispatchers, cancellation, Flow |

## Notes
- Each lesson uses its own package, so every file can have its own `main()`.
- Lesson 12 is saved as `.kt.txt` because it needs the `kotlinx-coroutines` library.
  See the instructions at the top of that file to enable it.
- Lesson 8 uses `data object` (Kotlin 1.9+). Lesson 5/3 use `entries`/`min()`
  (Kotlin 1.9+/1.7+). Use a recent Kotlin plugin.

## Key Kotlin Keywords & Concepts (Cheat Sheet)

### Variables: mutable vs immutable
| Keyword | Meaning | Example |
|---------|---------|---------|
| `val` | **Immutable** (read-only) reference. Assigned once, can't be reassigned. Prefer this by default. | `val name = "Sam"` |
| `var` | **Mutable** variable. Can be reassigned later. | `var age = 20; age = 21` |
| `const val` | Compile-time constant (top-level or in `object`). Only primitives/`String`. | `const val PI = 3.14` |
| `lateinit var` | Promise to initialize a non-null `var` later (before first use). | `lateinit var db: Database` |
| `by lazy` | Value computed on first access, then cached. Works with `val`. | `val data by lazy { load() }` |

> **Note:** `val` means the *reference* can't change, not that the object is deeply immutable.
> A `val list = mutableListOf(1)` can still have items added.

### Immutable vs mutable collections
| Immutable (read-only) | Mutable (can add/remove) |
|-----------------------|--------------------------|
| `listOf(1, 2)` → `List` | `mutableListOf(1, 2)` → `MutableList` |
| `setOf(1, 2)` → `Set` | `mutableSetOf(1, 2)` → `MutableSet` |
| `mapOf("a" to 1)` → `Map` | `mutableMapOf("a" to 1)` → `MutableMap` |

### Null safety
| Syntax | Meaning |
|--------|---------|
| `String?` | Nullable type (can hold `null`). `String` can never be null. |
| `?.` | Safe call: runs only if not null, otherwise gives `null`. |
| `?:` | Elvis operator: default value if left side is null. `name ?: "Unknown"` |
| `!!` | "Trust me, not null." Throws `NullPointerException` if wrong. Avoid it. |
| `as?` | Safe cast: returns `null` instead of throwing. |
| `let` | Run a block only when value is non-null: `name?.let { println(it) }` |

### Functions
| Keyword | Meaning |
|---------|---------|
| `fun` | Declares a function. |
| `return` | Returns a value from a function. |
| `Unit` | "No useful value" return type (like `void`). |
| `vararg` | Accept any number of arguments of the same type. |
| `inline` | Copies the function body to the call site (faster for lambdas). |
| `tailrec` | Optimizes tail-recursive functions into loops. |
| `suspend` | Function that can pause and resume (used in coroutines). |
| `infix` | Allows `a myFun b` call style. |
| `operator` | Lets you overload operators like `+`, `[]`, `==`. |

### Classes & objects
| Keyword | Meaning |
|---------|---------|
| `class` | Declares a class. Closed (final) by default. |
| `open` | Allows a class/method to be inherited/overridden. |
| `abstract` | Class/member with no full implementation; must be subclassed. |
| `override` | Replace a parent member's implementation. |
| `interface` | Contract that classes implement; can have default methods. |
| `data class` | Auto-generates `equals`, `hashCode`, `toString`, `copy`, `componentN`. |
| `enum class` | Fixed set of constants. |
| `sealed class/interface` | Restricted hierarchy; `when` can be exhaustive without `else`. |
| `object` | Singleton (one instance only). |
| `companion object` | Class-level members, like `static` in Java. |
| `value class` | Lightweight wrapper around one value (no extra allocation). |
| `typealias` | Another name for an existing type. |
| `init` | Initializer block that runs when an object is created. |
| `this` / `super` | Current object / parent class. |
| `by` | Delegation (`by lazy`, class delegation, property delegates). |

### Visibility modifiers
| Modifier | Visible to |
|----------|-----------|
| `public` (default) | Everywhere |
| `internal` | Same module |
| `protected` | Class and subclasses |
| `private` | Same class/file only |

### Control flow
| Keyword | Meaning |
|---------|---------|
| `if` / `else` | Conditional. Works as an **expression** (returns a value). |
| `when` | Powerful `switch`; also an expression. |
| `for` / `while` / `do-while` | Loops. |
| `in` / `!in` | Check membership in range or collection: `x in 1..10`. |
| `..` / `until` / `downTo` / `step` | Ranges: `1..5`, `1 until 5`, `5 downTo 1`, `1..10 step 2`. |
| `break` / `continue` | Exit or skip a loop iteration. |
| `is` / `!is` | Type check (with smart cast). |
| `try` / `catch` / `finally` / `throw` | Exception handling. |

### Collection functions (very common)
| Function | What it does |
|----------|--------------|
| `map` | Transform each element. |
| `filter` | Keep elements that match a condition. |
| `forEach` | Do something for each element. |
| `fold` / `reduce` | Combine all elements into one value. |
| `groupBy` | Group elements into a `Map` by a key. |
| `sortedBy` / `sortedWith` | Sort by a property or comparator. |
| `first` / `firstOrNull` | Get first element (safe version returns `null`). |
| `any` / `all` / `none` | Check conditions across elements. |
| `associate` / `zip` / `flatMap` | Build maps, pair lists, flatten nested lists. |
| `asSequence()` | Lazy processing, good for big data. |

### Scope functions
| Function | Object is | Returns | Typical use |
|----------|-----------|---------|-------------|
| `let` | `it` | lambda result | Null checks, transform value |
| `run` | `this` | lambda result | Compute something using the object |
| `with` | `this` | lambda result | Many calls on one object |
| `apply` | `this` | the object | Configure/initialize an object |
| `also` | `it` | the object | Side effects (logging) |

### Generics
| Keyword | Meaning |
|---------|---------|
| `<T>` | Generic type parameter. |
| `out` | Covariant: type is only *produced* (`List<out T>`). |
| `in` | Contravariant: type is only *consumed* (`Comparable<in T>`). |
| `reified` | Keeps the real type at runtime (used with `inline`). |
| `where` | Multiple constraints on a generic type. |

### Coroutines (async code)
| Keyword | Meaning |
|---------|---------|
| `suspend` | Function that can pause without blocking a thread. |
| `launch` | Start a coroutine that returns nothing (fire and forget). |
| `async` / `await` | Start a coroutine that returns a result. |
| `Dispatchers` | Which thread pool runs it (`Main`, `IO`, `Default`). |
| `Flow` | Stream of values emitted over time. |

### Strings
| Syntax | Meaning |
|--------|---------|
| `"Hi $name"` | String template with a variable. |
| `"Sum: ${a + b}"` | String template with an expression. |
| `"""..."""` | Raw multi-line string. |

---

## What to learn next
Follow this order. Each step builds on the one before it.

1. **Practice the basics.** Redo the lesson exercises without looking at the answers. Build
   small programs: a calculator, a to-do list, a number-guessing game.
2. **Gradle & project structure.** Gradle is the build tool that downloads libraries and
   builds your project. Learn `build.gradle.kts`, dependencies, and how packages and
   modules are organized.
3. **Unit testing (JUnit / Kotest).** Write tests that check your code works. This is
   essential for real projects and helps you refactor safely.
4. **Kotlin standard library in depth.** Get comfortable with collection operations,
   `Result`, `Pair`/`Triple`, `sequence {}`, and string/regex helpers.
5. **kotlinx.serialization.** Convert objects to and from JSON. You need this for almost
   any app that talks to a server or saves data.
6. **Coroutines & Flow (deeper).** Structured concurrency, `CoroutineScope`, `Job`,
   `StateFlow`/`SharedFlow`, error handling, and testing coroutines.
7. **Pick a path:**
   - **Backend:** Ktor or Spring Boot. Build REST APIs, connect to databases (Exposed,
     JPA), handle authentication.
   - **Android:** Jetpack Compose for UI, ViewModel, Room database, Retrofit/Ktor client,
     Hilt/Koin for dependency injection.
   - **Multiplatform:** Kotlin Multiplatform (KMP) and Compose Multiplatform to share code
     across Android, iOS, desktop, and web.
8. **Software design.** Learn clean architecture, MVVM, SOLID principles, and design
   patterns, so larger projects stay organized.
9. **Build a portfolio project.** Combine what you learned into one real app and put it on
   GitHub.
