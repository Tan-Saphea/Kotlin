package basic.lesson02

/*
 * LESSON 2 — CONTROL FLOW (Beginner)
 * ----------------------------------
 * Topics: if as expression, when, ranges, for, while, break/continue, labels.
 */

fun main() {
    // ---------- 1. if / else ----------
    val temp = 28
    if (temp > 30) {
        println("Hot")
    } else if (temp > 20) {
        println("Warm")
    } else {
        println("Cold")
    }

    // In Kotlin, `if` is an EXPRESSION: it returns a value.
    // That is why Kotlin has no ternary operator (cond ? a : b).
    val label = if (temp > 25) "warm" else "cool"
    println(label)

    // Comparison: == ,  != , < , > , <= , >=     Logic: && (and), || (or), ! (not)
    val isWeekend = false
    println(temp > 20 && !isWeekend)

    // ---------- 2. when (a powerful switch) ----------
    val day = 3
    val dayName = when (day) {
        1 -> "Monday"
        2 -> "Tuesday"
        3 -> "Wednesday"
        4, 5 -> "Thursday or Friday"      // multiple values
        in 6..7 -> "Weekend"              // range check
        else -> "Invalid"                 // required when used as an expression
    }
    println(dayName)

    // when without an argument = cleaner if/else-if chain
    val score = 85
    val grade = when {
        score >= 90 -> "A"
        score >= 80 -> "B"
        score >= 70 -> "C"
        else -> "F"
    }
    println("Grade: $grade")

    // when with types
    fun describe(x: Any): String = when (x) {
        is Int -> "Int with value $x"
        is String -> "String of length ${x.length}"   // x is auto-cast to String
        is List<*> -> "List of size ${x.size}"
        else -> "Unknown"
    }
    println(describe(5))
    println(describe("hey"))
    println(describe(listOf(1, 2)))

    // ---------- 3. Ranges ----------
    val r1 = 1..5            // 1,2,3,4,5  (inclusive)
    val r2 = 1 until 5       // 1,2,3,4    (end excluded)
    val r3 = 5 downTo 1      // 5,4,3,2,1
    val r4 = 1..10 step 3    // 1,4,7,10
    println(r1.toList())
    println(r2.toList())
    println(r3.toList())
    println(r4.toList())
    println(3 in r1)         // true
    println(9 !in r1)        // true

    // ---------- 4. for loops ----------
    for (i in 1..3) println("i = $i")

    val fruits = listOf("apple", "banana", "cherry")
    for (fruit in fruits) println(fruit)

    // Need the index too?
    for ((index, fruit) in fruits.withIndex()) {
        println("$index -> $fruit")
    }
    for (i in fruits.indices) println("index $i")

    // ---------- 5. while & do-while ----------
    var n = 3
    while (n > 0) {          // checks first
        println("n = $n")
        n--
    }
    do {                     // runs at least once, checks after
        println("do-while runs once")
    } while (false)

    // ---------- 6. break & continue ----------
    for (i in 1..10) {
        if (i % 2 == 0) continue     // skip even numbers
        if (i > 7) break             // stop completely
        println("odd: $i")
    }

    // Labels: break out of an OUTER loop
    outer@ for (i in 1..3) {
        for (j in 1..3) {
            if (i * j == 4) break@outer
            println("i=$i j=$j")
        }
    }

    // ---------- EXERCISES ----------
    // 1. FizzBuzz: for 1..30 print Fizz (div by 3), Buzz (div by 5), FizzBuzz (both), else the number.
    // 2. Print a multiplication table for 7.
    // 3. Use `when` to convert a month number to its name.
}
