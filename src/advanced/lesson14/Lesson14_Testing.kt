package advanced.lesson14

/*
 * LESSON 14 — TESTING IN KOTLIN (Advanced)
 * ----------------------------------------
 * Topics: why test, writing testable code, assertions, a tiny test runner built
 *         from scratch (no libraries needed), fakes instead of mocks, testing
 *         exceptions and Result, and how the same tests look in kotlin.test / JUnit.
 *
 * This file runs as-is: click the green arrow next to `fun main()`.
 * Real projects use a library (see section 8) — it needs Gradle/Maven.
 */

// ---------- 1. Code under test ----------
// Pure functions (same input -> same output, no hidden state) are the easiest to test.
fun isPalindrome(text: String): Boolean {
    val clean = text.lowercase().filter { it.isLetterOrDigit() }
    return clean == clean.reversed()
}

fun fizzBuzz(n: Int): String = when {
    n % 15 == 0 -> "FizzBuzz"
    n % 3 == 0 -> "Fizz"
    n % 5 == 0 -> "Buzz"
    else -> n.toString()
}

class ShoppingCart {
    private val items = mutableMapOf<String, Pair<Double, Int>>()   // name -> (price, qty)

    fun add(name: String, price: Double, qty: Int = 1) {
        require(price >= 0) { "Price cannot be negative" }
        require(qty > 0) { "Quantity must be positive" }
        val (_, oldQty) = items[name] ?: (price to 0)
        items[name] = price to (oldQty + qty)
    }

    fun total(): Double = items.values.sumOf { (price, qty) -> price * qty }
    fun count(): Int = items.values.sumOf { it.second }
}

// ---------- 2. Design for testing: depend on an interface, not a real thing ----------
interface Clock { fun hour(): Int }

class Greeter(private val clock: Clock) {
    fun greet(name: String): String = when (clock.hour()) {
        in 5..11 -> "Good morning, $name"
        in 12..17 -> "Good afternoon, $name"
        else -> "Good evening, $name"
    }
}

// A FAKE: a simple hand-written stand-in we control. No mocking library needed.
class FakeClock(private val fixedHour: Int) : Clock {
    override fun hour() = fixedHour
}

// ---------- 3. A tiny test framework, built from scratch ----------
class AssertionFailure(message: String) : Exception(message)

fun <T> assertEquals(expected: T, actual: T) {
    if (expected != actual) throw AssertionFailure("expected <$expected> but was <$actual>")
}

fun assertTrue(condition: Boolean, message: String = "expected true") {
    if (!condition) throw AssertionFailure(message)
}

// Verifies that `block` throws exception type E. `reified` lets us check E at runtime (lesson 13).
inline fun <reified E : Throwable> assertThrows(block: () -> Unit): E {
    try {
        block()
    } catch (e: Throwable) {
        if (e is E) return e
        throw AssertionFailure("expected ${E::class.simpleName} but got ${e::class.simpleName}")
    }
    throw AssertionFailure("expected ${E::class.simpleName} but nothing was thrown")
}

class TestRunner {
    private var passed = 0
    private var failed = 0

    // `test("name") { ... }` — a function taking a lambda gives a clean DSL-like look.
    fun test(name: String, body: () -> Unit) {
        try {
            body()
            passed++
            println("  PASS  $name")
        } catch (e: AssertionFailure) {
            failed++
            println("  FAIL  $name -> ${e.message}")
        } catch (e: Exception) {
            failed++
            println("  ERROR $name -> ${e::class.simpleName}: ${e.message}")
        }
    }

    fun summary() = println("\n$passed passed, $failed failed")
}

// ---------- 4. Writing the tests ----------
// Pattern: ARRANGE (set up) -> ACT (call the code) -> ASSERT (check the result).
fun runAllTests() {
    val t = TestRunner()

    println("isPalindrome")
    t.test("simple palindrome") { assertTrue(isPalindrome("level")) }
    t.test("ignores case and punctuation") { assertTrue(isPalindrome("A man, a plan, a canal: Panama")) }
    t.test("not a palindrome") { assertEquals(false, isPalindrome("kotlin")) }
    t.test("empty string counts as palindrome") { assertTrue(isPalindrome("")) }

    println("fizzBuzz")
    t.test("multiple of 3") { assertEquals("Fizz", fizzBuzz(9)) }
    t.test("multiple of 5") { assertEquals("Buzz", fizzBuzz(10)) }
    t.test("multiple of 15") { assertEquals("FizzBuzz", fizzBuzz(30)) }
    t.test("plain number") { assertEquals("7", fizzBuzz(7)) }

    println("ShoppingCart")
    t.test("empty cart totals zero") { assertEquals(0.0, ShoppingCart().total()) }
    t.test("total multiplies price by quantity") {
        val cart = ShoppingCart()                       // arrange
        cart.add("pen", 2.5, qty = 4)                   // act
        cart.add("book", 10.0)
        assertEquals(20.0, cart.total())                // assert
    }
    t.test("adding same item twice merges quantity") {
        val cart = ShoppingCart().apply { add("pen", 1.0); add("pen", 1.0) }
        assertEquals(2, cart.count())
    }
    t.test("negative price is rejected") {
        val e = assertThrows<IllegalArgumentException> { ShoppingCart().add("x", -1.0) }
        assertEquals("Price cannot be negative", e.message)
    }

    println("Greeter (with FakeClock)")
    t.test("morning") { assertEquals("Good morning, Tan", Greeter(FakeClock(8)).greet("Tan")) }
    t.test("afternoon") { assertEquals("Good afternoon, Tan", Greeter(FakeClock(14)).greet("Tan")) }
    t.test("evening") { assertEquals("Good evening, Tan", Greeter(FakeClock(22)).greet("Tan")) }

    println("Result (from lesson 13)")
    t.test("runCatching captures failure") {
        val r = runCatching { "abc".toInt() }
        assertTrue(r.isFailure)
        assertTrue(r.exceptionOrNull() is NumberFormatException)
    }

    // This one fails ON PURPOSE so you can see what a failing test looks like. Fix it!
    println("Intentional failure")
    t.test("fizzBuzz(2) should be Fizz (wrong expectation)") { assertEquals("Fizz", fizzBuzz(2)) }

    t.summary()
}

fun main() {
    runAllTests()
}

/*
 * ---------- 5. Good test habits ----------
 * - One idea per test; name it by behaviour ("negative price is rejected").
 * - Test edge cases: empty, zero, negative, very large, null.
 * - Tests must be independent: never rely on another test running first.
 * - Prefer fakes/simple stand-ins over heavy mocking.
 * - A test that never fails proves nothing — make sure it can fail.
 *
 * ---------- 6. TDD (Test-Driven Development) ----------
 * RED: write a failing test -> GREEN: write the least code to pass -> REFACTOR: clean up.
 *
 * ---------- 7. Testing coroutines (after lesson 12 is enabled) ----------
 * Use `runTest { ... }` from kotlinx-coroutines-test; it skips delay() instantly.
 *
 * ---------- 8. The same tests with kotlin.test (real projects) ----------
 * Gradle: testImplementation(kotlin("test"))
 * Put this in src/test/kotlin/ShoppingCartTest.kt:
 *
 *   import kotlin.test.*
 *
 *   class ShoppingCartTest {
 *       @Test
 *       fun `total multiplies price by quantity`() {
 *           val cart = ShoppingCart()
 *           cart.add("pen", 2.5, qty = 4)
 *           assertEquals(10.0, cart.total())
 *       }
 *
 *       @Test
 *       fun `negative price is rejected`() {
 *           assertFailsWith<IllegalArgumentException> { ShoppingCart().add("x", -1.0) }
 *       }
 *   }
 *
 * Backtick names let you write readable sentences as function names.
 *
 * ---------- EXERCISES ----------
 * 1. Fix the intentionally failing test, then watch the summary change.
 * 2. Add tests for `ShoppingCart.add` with qty = 0.
 * 3. Write `fun isLeapYear(y: Int): Boolean` using TDD: tests first (1900, 2000, 2024, 2023).
 * 4. Create a `FakeClock`-style fake for a `UserRepository` interface and test a service that uses it.
 * 5. Add an `assertNotEquals` function to the mini framework.
 */
