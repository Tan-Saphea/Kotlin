package advanced.lesson13

/*
 * LESSON 13 — SEQUENCES, ERROR HANDLING & REIFIED GENERICS (Advanced)
 * -------------------------------------------------------------------
 * Topics: lazy sequences, generateSequence, sequence { yield }, Result & runCatching,
 *         require/check/error, custom exceptions, use (auto-close), tailrec,
 *         inline + reified, Regex, a small mini-project.
 *
 * Run this file: click the green arrow next to `fun main()`.
 */

// ---------- 1. Sequences: lazy processing ----------
// Lists are EAGER: every step builds a whole new list.
// Sequences are LAZY: each element flows through the whole chain one at a time,
// and nothing runs until a terminal operation (toList, first, sum...) is called.
fun sequencesDemo() {
    println("--- Sequences ---")

    val result = (1..1_000_000).asSequence()
        .filter { it % 2 == 0 }
        .map { it * it }
        .take(3)            // stops after 3 results -> very cheap
        .toList()
    println(result)         // [4, 16, 36]

    // See the laziness yourself:
    listOf(1, 2, 3, 4).asSequence()
        .filter { println("  filter $it"); it % 2 == 0 }
        .map { println("  map $it"); it * 10 }
        .first()            // only processes until the first match
        .also { println("first = $it") }

    // Infinite sequences
    val powersOfTwo = generateSequence(1) { it * 2 }
    println(powersOfTwo.take(8).toList())     // [1, 2, 4, ..., 128]

    // Build a sequence with `sequence { yield(...) }`
    val fib = sequence {
        var a = 0L
        var b = 1L
        while (true) {
            yield(a)
            val next = a + b
            a = b
            b = next
        }
    }
    println(fib.take(10).toList())            // [0, 1, 1, 2, 3, 5, 8, 13, 21, 34]
}

// ---------- 2. Error handling: exceptions as you know them ----------
class InsufficientFundsException(val needed: Double) :
    Exception("Not enough money, need $needed more")

class Account(var balance: Double) {
    fun withdraw(amount: Double) {
        require(amount > 0) { "Amount must be positive" }          // bad ARGUMENT -> IllegalArgumentException
        check(balance >= 0) { "Account is in a broken state" }     // bad STATE    -> IllegalStateException
        if (amount > balance) throw InsufficientFundsException(amount - balance)
        balance -= amount
    }
}

fun exceptionsDemo() {
    println("--- Exceptions ---")
    val account = Account(100.0)

    try {
        account.withdraw(150.0)
    } catch (e: InsufficientFundsException) {
        println("Caught: ${e.message}")
    } catch (e: IllegalArgumentException) {
        println("Bad argument: ${e.message}")
    } finally {
        println("finally always runs")
    }

    // try is an EXPRESSION in Kotlin
    val number = try { "abc".toInt() } catch (e: NumberFormatException) { -1 }
    println("number = $number")

    // error(...) throws IllegalStateException; TODO() throws NotImplementedError
    // val x = error("boom")
}

// ---------- 3. Result & runCatching: errors as values ----------
fun parseAge(text: String): Result<Int> = runCatching {
    val age = text.trim().toInt()
    require(age in 0..150) { "Age out of range: $age" }
    age
}

fun resultDemo() {
    println("--- Result ---")
    for (input in listOf("25", "abc", "200")) {
        val r = parseAge(input)
        println(
            "'$input' -> " + r.fold(
                onSuccess = { "OK: $it" },
                onFailure = { "FAILED: ${it.message}" }
            )
        )
    }

    println(parseAge("30").getOrNull())              // 30
    println(parseAge("zzz").getOrDefault(0))         // 0
    println(parseAge("40").map { it + 1 }.getOrThrow())  // 41
    parseAge("zzz")
        .onSuccess { println("never printed") }
        .onFailure { println("onFailure: ${it::class.simpleName}") }
    println(parseAge("zzz").recover { -1 }.getOrNull()) // -1
}

// ---------- 4. `use`: automatic resource closing ----------
class Resource(val name: String) : AutoCloseable {
    init { println("open $name") }
    fun work() = println("working with $name")
    override fun close() = println("close $name")
}

fun useDemo() {
    println("--- use ---")
    Resource("db").use { it.work() }      // close() called even if work() throws
}

// ---------- 5. tailrec: recursion without stack overflow ----------
tailrec fun sumTo(n: Long, acc: Long = 0): Long =
    if (n == 0L) acc else sumTo(n - 1, acc + n)   // the recursive call must be the LAST thing

fun tailrecDemo() {
    println("--- tailrec ---")
    println(sumTo(1_000_000))   // 500000500000, no StackOverflowError
}

// ---------- 6. inline + reified: keep generic types at runtime ----------
// Normally `T` is erased at runtime, so `value is T` does not compile.
// An inline function with `reified T` makes it possible.
inline fun <reified T> List<Any>.onlyOfType(): List<T> = filter { it is T }.map { it as T }

inline fun <reified T> describe(): String = "Type is ${T::class.simpleName}"

fun reifiedDemo() {
    println("--- reified ---")
    val mixed: List<Any> = listOf(1, "two", 3.0, "four", 5)
    println(mixed.onlyOfType<String>())   // [two, four]
    println(mixed.onlyOfType<Int>())      // [1, 5]
    println(describe<Double>())           // Type is Double
}

// ---------- 7. Regex ----------
fun regexDemo() {
    println("--- Regex ---")
    val email = Regex("""[\w.]+@[\w.]+\.\w+""")
    println(email.matches("tan@example.com"))     // true
    println(email.matches("not-an-email"))        // false

    val text = "Call 012-345 or 098-765 today"
    val phones = Regex("""(\d{3})-(\d{3})""")
    phones.findAll(text).forEach { m ->
        val (first, second) = m.destructured
        println("found ${m.value} -> $first / $second")
    }
    println(text.replace(phones, "XXX-XXX"))
}

// ---------- 8. Mini project: word-frequency analyser ----------
// Combines sequences, Result, regex and collections.
fun wordFrequency(text: String, top: Int = 3): Result<List<Pair<String, Int>>> = runCatching {
    require(text.isNotBlank()) { "Text is empty" }
    Regex("""[a-zA-Z']+""").findAll(text.lowercase())
        .map { it.value }
        .groupingBy { it }
        .eachCount()
        .entries
        .sortedByDescending { it.value }
        .take(top)
        .map { it.key to it.value }
}

fun miniProject() {
    println("--- Mini project ---")
    val text = "Kotlin is fun. Kotlin is concise, and Kotlin is safe!"
    wordFrequency(text)
        .onSuccess { list -> list.forEach { (w, c) -> println("$w: $c") } }
        .onFailure { println("Error: ${it.message}") }

    wordFrequency("   ").onFailure { println("Error: ${it.message}") }
}

fun main() {
    sequencesDemo()
    exceptionsDemo()
    resultDemo()
    useDemo()
    tailrecDemo()
    reifiedDemo()
    regexDemo()
    miniProject()
}

/*
 * ---------- EXERCISES ----------
 * 1. Write a sequence that yields all prime numbers; print the first 15.
 * 2. Write `safeDivide(a: Int, b: Int): Result<Int>` that fails on b == 0.
 * 3. Write `inline fun <reified T> Any.isA(): Boolean` and test it.
 * 4. Make `factorial` tailrec using an accumulator (use Long or BigInteger).
 * 5. Extend wordFrequency to ignore common words like "is" and "and".
 */
