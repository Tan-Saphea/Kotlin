package basic.lesson05

/*
 * LESSON 5 — COLLECTIONS (Intermediate)
 * -------------------------------------
 * Topics: List, Set, Map, mutable vs read-only, arrays, and the functional
 *         operations: map, filter, reduce, fold, groupBy, sortedBy, etc.
 *         Sequences for lazy processing.
 */

data class Person(val name: String, val age: Int, val city: String)

fun main() {
    // ---------- 1. List (ordered, allows duplicates) ----------
    val fixed = listOf(3, 1, 2, 3)           // read-only: cannot add/remove
    println(fixed)
    println(fixed[0])                        // index access
    println(fixed.first() + fixed.last())
    println(fixed.size)

    val growing = mutableListOf("a", "b")    // can change
    growing.add("c")
    growing.remove("a")
    growing[0] = "z"
    println(growing)

    // ---------- 2. Set (no duplicates) ----------
    val set = setOf(1, 2, 2, 3)
    println(set)                             // [1, 2, 3]
    val mset = mutableSetOf("x")
    mset.add("x")                            // ignored
    mset.add("y")
    println(mset)

    // ---------- 3. Map (key -> value) ----------
    val ages = mapOf("Ann" to 30, "Bob" to 25)   // `to` creates a Pair
    println(ages["Ann"])                     // 30
    println(ages["Zed"])                     // null (key missing)
    println(ages.getOrDefault("Zed", 0))
    for ((name, age) in ages) println("$name is $age")

    val scores = mutableMapOf<String, Int>()
    scores["a"] = 1
    scores["b"] = 2
    scores.merge("a", 10) { old, new -> old + new }
    println(scores)
    println(ages.keys)
    println(ages.values)

    // ---------- 4. Arrays (fixed size, mutable elements) ----------
    val arr = arrayOf(1, 2, 3)
    arr[0] = 99
    println(arr.joinToString())
    val ints = intArrayOf(4, 5, 6)           // primitive int[] — more efficient
    println(ints.sum())

    // ---------- 5. Functional operations ----------
    val numbers = listOf(1, 2, 3, 4, 5, 6)

    println(numbers.map { it * 2 })                 // transform each
    println(numbers.filter { it % 2 == 0 })         // keep matching
    println(numbers.reduce { acc, n -> acc + n })   // combine into one
    println(numbers.fold(100) { acc, n -> acc + n })// reduce with start value
    println(numbers.any { it > 5 })                 // at least one?
    println(numbers.all { it > 0 })                 // every one?
    println(numbers.none { it > 10 })               // zero match?
    println(numbers.count { it > 3 })
    println(numbers.find { it > 3 })                // first match or null
    println(numbers.take(2))
    println(numbers.drop(4))
    println(numbers.partition { it > 3 })           // Pair(matching, rest)
    println(numbers.chunked(4))                     // [[1,2,3,4],[5,6]]
    println(numbers.windowed(3))                    // sliding windows
    println(numbers.zip(listOf("a", "b", "c")))     // pairs, shortest wins
    println(listOf(listOf(1, 2), listOf(3)).flatten())
    println(numbers.flatMap { listOf(it, -it) }.take(4))
    println(listOf(3, 1, 2).sorted())
    println(listOf(3, 1, 2).sortedDescending())
    println(listOf(1, 1, 2).distinct())
    println(numbers.sumOf { it * it })
    println(numbers.average())
    println(numbers.maxOrNull())

    // ---------- 6. Working with objects ----------
    val people = listOf(
        Person("Ann", 30, "Paris"),
        Person("Bob", 25, "Rome"),
        Person("Cat", 35, "Paris"),
        Person("Dan", 28, "Rome"),
        Person("Eve", 22, "Oslo")
    )

    println(people.sortedBy { it.age }.map { it.name })
    println(people.maxByOrNull { it.age }?.name)
    println(people.map { it.age }.average())

    // groupBy: Map<Key, List<Item>>
    val byCity = people.groupBy { it.city }
    for ((city, group) in byCity) {
        println("$city -> ${group.map { it.name }}")
    }

    // associate: build a map
    val nameToAge = people.associate { it.name to it.age }
    println(nameToAge)

    // Chaining reads like a sentence
    val result = people
        .filter { it.age > 24 }
        .sortedByDescending { it.age }
        .joinToString(", ") { "${it.name}(${it.age})" }
    println(result)

    // ---------- 7. Sequences (lazy) ----------
    // List operations create a new list at EVERY step.
    // A Sequence processes one element at a time, only as far as needed.
    val firstBig = (1..1_000_000)
        .asSequence()
        .map { it * 2 }
        .filter { it % 3 == 0 }
        .first { it > 100 }
    println(firstBig)

    // Infinite sequence
    val evens = generateSequence(0) { it + 2 }
    println(evens.take(5).toList())

    // ---------- 8. Read-only vs immutable ----------
    // `List` is a read-only VIEW. If someone holds a MutableList reference to the
    // same object, it can still change. Copy with .toList() when you need safety.

    // ---------- EXERCISES ----------
    // 1. Count how often each word appears in a sentence (hint: split + groupingBy/eachCount).
    // 2. From `people`, get the names of those living in Paris, sorted alphabetically.
    // 3. Find the average age per city (groupBy + mapValues).
}
