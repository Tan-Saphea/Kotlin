package basic.lesson01

/*
 * LESSON 1 — THE BASICS (Beginner)
 * ---------------------------------
 * Topics: main function, val vs var, basic types, type inference,
 *         string templates, comments, type conversion.
 *
 * Run this file: click the green arrow next to `fun main()`.
 */

// Every Kotlin program starts at main(). Code runs top to bottom.
fun main() {
    // ---------- 1. Printing ----------
    println("Hello, Kotlin!")   // println = print + new line
    print("No new line. ")      // print = no new line
    println()

    // ---------- 2. val vs var ----------
    // val = VALUE: assigned once, cannot be changed (read-only). Prefer this.
    // var = VARIABLE: can be reassigned.
    val language = "Kotlin"
    var score = 10
    score = 20                  // OK
    // language = "Java"        // ERROR: Val cannot be reassigned
    println("$language, score=$score")

    // ---------- 3. Basic types ----------
    // Everything in Kotlin is an object; there are no "primitive" keywords.
    val age: Int = 25                 // whole number (32-bit)
    val population: Long = 8_000_000_000L // 64-bit; underscores help readability
    val price: Double = 19.99         // 64-bit decimal (default for decimals)
    val ratio: Float = 1.5f           // 32-bit decimal, needs the f suffix
    val isFun: Boolean = true         // true / false
    val letter: Char = 'K'            // single character, single quotes
    val text: String = "Hello"        // text, double quotes
    println("$age $population $price $ratio $isFun $letter $text")

    // ---------- 4. Type inference ----------
    // Kotlin figures out the type from the value. No need to write it.
    val inferredInt = 42          // Int
    val inferredDouble = 3.14     // Double
    val inferredString = "hi"     // String
    println(inferredInt::class.simpleName)    // prints: Int
    println(inferredDouble::class.simpleName) // prints: Double
    println(inferredString::class.simpleName) // prints: String

    // ---------- 5. Arithmetic ----------
    val a = 17
    val b = 5
    println(a + b)   // 22
    println(a - b)   // 12
    println(a * b)   // 85
    println(a / b)   // 3     <- integer division drops the decimal!
    println(a % b)   // 2     <- remainder (modulo)
    println(a.toDouble() / b) // 3.4 <- convert first to get decimals

    // Shortcuts
    var counter = 0
    counter += 5     // counter = counter + 5
    counter++        // add 1
    counter--        // subtract 1
    println("counter = $counter")

    // ---------- 6. Type conversion ----------
    // Kotlin never converts number types automatically. You must be explicit.
    val small: Int = 100
    val big: Long = small.toLong()
    val asText: String = small.toString()
    val parsed: Int = "123".toInt()
    val maybe: Int? = "abc".toIntOrNull()   // null instead of crashing
    println("$big $asText $parsed $maybe")

    // ---------- 7. Strings ----------
    val name = "Ada"
    println("Hello, $name!")                       // $variable
    println("Length: ${name.length}")              // ${expression}
    println("Upper: ${name.uppercase()}")
    println("2 + 3 = ${2 + 3}")

    // Multi-line "raw" strings
    val poem = """
        Roses are red,
        Kotlin is fun.
    """.trimIndent()
    println(poem)

    // Useful string functions
    val s = "  Kotlin Rocks  "
    println(s.trim())                 // remove spaces at both ends
    println(s.trim().lowercase())
    println(s.contains("Rocks"))      // true
    println(s.trim().replace("Rocks", "Rules"))
    println(s.trim().substring(0, 6)) // "Kotlin" (end index excluded)
    println("Kotlin"[0])              // 'K' — index access
    println("a,b,c".split(","))       // [a, b, c]

    // ---------- 8. Constants ----------
    // `const val` is a compile-time constant (top-level or in an object).
    println("Max users: $MAX_USERS")

    // ---------- 9. Comments ----------
    // single line
    /* multi
       line */
    /** KDoc: documentation comment */

    // ---------- EXERCISES ----------
    // 1. Create variables for your name and age; print "My name is X and I am Y".
    // 2. Convert 98.6 Fahrenheit to Celsius: (F - 32) * 5 / 9.
    // 3. Why does 7 / 2 print 3? How do you get 3.5?
}

const val MAX_USERS = 100
