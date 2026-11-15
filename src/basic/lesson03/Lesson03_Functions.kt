package basic.lesson03

/*
 * LESSON 3 — FUNCTIONS (Beginner)
 * -------------------------------
 * Topics: declaring functions, parameters, return types, default & named
 *         arguments, single-expression functions, vararg, Unit, local functions,
 *         tail recursion.
 */

// Basic function: name, parameters (name: Type), return type after the colon.
fun add(a: Int, b: Int): Int {
    return a + b
}

// Single-expression function: the body is one expression, so use `=`.
fun multiply(a: Int, b: Int) = a * b      // return type inferred

// Unit = "returns nothing useful" (like void). You can omit it.
fun greet(name: String) {
    println("Hello, $name!")
}

// Default arguments: callers may skip them.
fun welcome(name: String, greeting: String = "Hello", punctuation: String = "!") =
    "$greeting, $name$punctuation"

// vararg: accept any number of arguments (becomes an array inside).
fun sum(vararg numbers: Int): Int {
    var total = 0
    for (n in numbers) total += n
    return total
}

// Returning several values: use Pair / Triple (or a data class, see lesson 8).
fun minMax(list: List<Int>): Pair<Int, Int> = Pair(list.min(), list.max())

// Recursion. `tailrec` lets the compiler turn it into a loop (no stack overflow).
tailrec fun factorial(n: Long, acc: Long = 1): Long =
    if (n <= 1) acc else factorial(n - 1, acc * n)

fun main() {
    println(add(2, 3))
    println(multiply(4, 5))
    greet("Ada")

    // Default + named arguments
    println(welcome("Bob"))
    println(welcome("Bob", "Hi"))
    println(welcome("Bob", punctuation = "?"))        // skip the middle one by naming
    println(welcome(greeting = "Hey", name = "Cy"))   // any order when named

    // vararg
    println(sum())                 // 0
    println(sum(1, 2, 3, 4))       // 10
    val nums = intArrayOf(5, 6, 7)
    println(sum(*nums))            // spread operator * unpacks an array

    // Pair destructuring
    val (lo, hi) = minMax(listOf(4, 9, 1, 7))
    println("min=$lo max=$hi")

    println(factorial(10))

    // Local function: defined inside another function, only visible there.
    fun square(x: Int) = x * x
    println(square(9))

    // Functions are values too (preview of lesson 9)
    val triple: (Int) -> Int = { it * 3 }
    println(triple(4))

    // Parameters are always read-only (val) inside the function.

    // ---------- EXERCISES ----------
    // 1. Write isPrime(n: Int): Boolean.
    // 2. Write average(vararg nums: Double): Double.
    // 3. Write fibonacci(n) recursively, then rewrite it with tailrec.
}
