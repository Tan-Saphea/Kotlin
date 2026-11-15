package basic.lesson08

/*
 * LESSON 8 — DATA, ENUM, SEALED & VALUE CLASSES (Intermediate)
 * ------------------------------------------------------------
 * Topics: data class, destructuring, copy, enum class, sealed class/interface,
 *         object expressions, value class, exceptions.
 */

// ---------- 1. data class ----------
// The compiler generates equals(), hashCode(), toString(), copy(), componentN().
data class Point(val x: Int, val y: Int)

// ---------- 2. enum class ----------
enum class Direction(val dx: Int, val dy: Int) {
    NORTH(0, 1), SOUTH(0, -1), EAST(1, 0), WEST(-1, 0);

    fun opposite() = when (this) {
        NORTH -> SOUTH
        SOUTH -> NORTH
        EAST -> WEST
        WEST -> EAST
    }
}

// ---------- 3. sealed class ----------
// A closed set of subtypes known at compile time. `when` can be exhaustive
// (no `else` needed), and adding a new subtype causes compile errors where you
// forgot to handle it. Perfect for modelling states and results.
sealed class Result<out T> {
    data class Success<T>(val value: T) : Result<T>()
    data class Failure(val error: String) : Result<Nothing>()
    data object Loading : Result<Nothing>()
}

fun <T> render(r: Result<T>): String = when (r) {
    is Result.Success -> "Got ${r.value}"
    is Result.Failure -> "Error: ${r.error}"
    Result.Loading -> "Loading..."
}

// ---------- 4. Value class ----------
// Wraps a single value for type safety with (usually) no runtime overhead.
@JvmInline
value class UserId(val raw: Int)

@JvmInline
value class OrderId(val raw: Int)

fun loadUser(id: UserId) = println("Loading user ${id.raw}")

// ---------- 5. Exceptions ----------
class InsufficientFundsException(message: String) : Exception(message)

fun withdraw(balance: Int, amount: Int): Int {
    if (amount > balance) throw InsufficientFundsException("Need ${amount - balance} more")
    return balance - amount
}

fun main() {
    // data class
    val p1 = Point(1, 2)
    val p2 = Point(1, 2)
    println(p1)                       // Point(x=1, y=2)
    println(p1 == p2)                 // true (compares CONTENT)
    val p3 = p1.copy(y = 10)          // copy with a change; originals are immutable
    println(p3)
    val (x, y) = p3                   // destructuring
    println("x=$x y=$y")
    println(hashSetOf(p1, p2).size)   // 1 (equal objects)

    // enum
    val d = Direction.NORTH
    println("$d ${d.dx},${d.dy} opposite=${d.opposite()}")
    println(Direction.entries)        // all values
    println(Direction.valueOf("EAST"))
    println(d.name + " " + d.ordinal) // NORTH 0

    // sealed
    println(render(Result.Success(42)))
    println(render(Result.Failure("network down")))
    println(render(Result.Loading))

    // value class
    loadUser(UserId(7))
    // loadUser(OrderId(7))           // ERROR — can't mix up IDs

    // exceptions: try is an expression too
    val outcome = try {
        withdraw(50, 80)
    } catch (e: InsufficientFundsException) {
        println("Caught: ${e.message}")
        -1
    } finally {
        println("finally always runs")
    }
    println(outcome)

    // Kotlin has no checked exceptions. Handy helpers:
    val num = runCatching { "abc".toInt() }
    println(num.isFailure)
    println(num.getOrDefault(0))

    // Anonymous object (object expression): one-off implementation
    val greeter = object {
        val msg = "hi"
        fun shout() = msg.uppercase()
    }
    println(greeter.shout())

    // ---------- EXERCISES ----------
    // 1. Model a traffic light as an enum with a next() function.
    // 2. Model payment results with a sealed interface: Paid, Declined(reason), Pending.
    // 3. Make a data class Book(title, author, year); sort a list by year.
}
