package basic.lesson10

/*
 * LESSON 10 — GENERICS (Advanced)
 * -------------------------------
 * Topics: generic classes & functions, constraints, variance (in / out),
 *         star projection, reified types, type erasure.
 */

// ---------- 1. Generic class ----------
// T is a placeholder for a type chosen by the user of the class.
class Box<T>(val content: T) {
    fun show() = println("Box holds $content")
}

class Stack<T> {
    private val items = mutableListOf<T>()
    fun push(item: T) = items.add(item)
    fun pop(): T? = if (items.isEmpty()) null else items.removeAt(items.lastIndex)
    fun peek(): T? = items.lastOrNull()
    val size get() = items.size
}

// ---------- 2. Generic functions ----------
fun <T> firstOrDefault(list: List<T>, default: T): T = list.firstOrNull() ?: default

// ---------- 3. Constraints (upper bounds) ----------
// T must be Comparable<T>, so we can use compareTo / >.
fun <T : Comparable<T>> maxOf3(a: T, b: T, c: T): T {
    var m = a
    if (b > m) m = b
    if (c > m) m = c
    return m
}

// Multiple constraints with `where`
fun <T> describe(x: T): String where T : CharSequence, T : Comparable<T> =
    "text of length ${x.length}"

// ---------- 4. Variance ----------
// Is List<Dog> a List<Animal>? It depends on how the type parameter is USED.
open class Animal
class Dog : Animal()

// `out T` (covariant): class only PRODUCES T (returns it). "Producer".
interface Source<out T> {
    fun next(): T
}

// `in T` (contravariant): class only CONSUMES T (takes it in). "Consumer".
interface Sink<in T> {
    fun accept(item: T)
}

fun variance() {
    val dogSource: Source<Dog> = object : Source<Dog> { override fun next() = Dog() }
    val animalSource: Source<Animal> = dogSource       // OK thanks to `out`

    val animalSink: Sink<Animal> = object : Sink<Animal> {
        override fun accept(item: Animal) = println("Accepted an animal")
    }
    val dogSink: Sink<Dog> = animalSink                // OK thanks to `in`
    dogSink.accept(Dog())
    println(animalSource.next()::class.simpleName)

    // Memory trick:  PECS — Producer = out, Consumer = in.
    // Kotlin's List<out E> is covariant, so List<Dog> IS a List<Animal>.
    val dogs: List<Dog> = listOf(Dog())
    val animals: List<Animal> = dogs
    println(animals.size)
    // MutableList<T> is invariant (it both reads and writes).
}

// ---------- 5. Star projection ----------
// "A list of something, I don't care what" — read as Any?, can't write.
fun printSize(list: List<*>) = println("size = ${list.size}")

// ---------- 6. Type erasure & reified ----------
// At runtime generic types are erased, so `x is T` normally fails to compile.
// `inline` + `reified` keeps the real type available.
inline fun <reified T> filterByType(items: List<Any>): List<T> {
    val out = mutableListOf<T>()
    for (i in items) if (i is T) out.add(i)
    return out
}

fun main() {
    Box(42).show()
    Box("text").show()

    val stack = Stack<String>()
    stack.push("a"); stack.push("b")
    println("${stack.pop()} ${stack.peek()} size=${stack.size}")

    println(firstOrDefault(emptyList(), "none"))
    println(maxOf3(3, 9, 5))
    println(maxOf3("pear", "apple", "zebra"))
    println(describe("hello"))

    variance()
    printSize(listOf(1, 2, 3))

    val mixed = listOf(1, "two", 3, "four", 5.0)
    println(filterByType<String>(mixed))   // [two, four]
    println(filterByType<Int>(mixed))      // [1, 3]

    // ---------- EXERCISES ----------
    // 1. Write a generic class Pair2<A, B> and a function swap() returning Pair2<B, A>.
    // 2. Write fun <T : Number> sumAll(list: List<T>): Double.
    // 3. Explain why MutableList<Dog> cannot be used as MutableList<Animal>.
}
