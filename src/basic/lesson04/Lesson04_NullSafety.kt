package basic.lesson04

/*
 * LESSON 4 — NULL SAFETY (Beginner)
 * ---------------------------------
 * Kotlin's signature feature: the type system separates
 * "can be null" from "can never be null", eliminating most NullPointerExceptions.
 *
 * Topics: nullable types (?), safe call (?.), Elvis (?:), not-null assertion (!!),
 *         safe cast (as?), let, smart casts, lateinit, lazy.
 */

fun main() {
    // ---------- 1. Nullable vs non-null ----------
    var name: String = "Ada"
    // name = null                // ERROR: String cannot hold null

    var nickname: String? = "Ace" // the ? means "may be null"
    nickname = null               // OK

    // ---------- 2. Safe call ?. ----------
    // Returns null instead of crashing if the receiver is null.
    println(nickname?.length)     // null
    nickname = "Ace"
    println(nickname?.length)     // 3

    // ---------- 3. Elvis operator ?: ----------
    // "If left side is null, use the right side."
    nickname = null
    val len = nickname?.length ?: 0
    println(len)                  // 0

    // Great for early exit
    fun printLength(s: String?) {
        val text = s ?: return    // leave function if null
        println(text.length)      // text is non-null here
    }
    printLength(null)
    printLength("hello")

    // ---------- 4. Not-null assertion !! ----------
    // "Trust me, it's not null." Throws NullPointerException if wrong.
    // Avoid it! Use only when you are truly certain.
    val sure: String? = "yes"
    println(sure!!.length)

    // ---------- 5. Smart casts ----------
    // After a null check, Kotlin treats the variable as non-null.
    val input: String? = "kotlin"
    if (input != null) {
        println(input.uppercase())   // no ?. needed
    }
    if (input.isNullOrEmpty()) println("empty") else println("has text")

    // ---------- 6. let: run code only if non-null ----------
    val email: String? = "a@b.com"
    email?.let {
        println("Sending to $it")    // `it` is the non-null value
    }

    // ---------- 7. Safe cast as? ----------
    val anything: Any = "text"
    val asInt: Int? = anything as? Int   // null instead of ClassCastException
    println(asInt)

    // ---------- 8. Collections with nulls ----------
    val list = listOf("a", null, "c")
    println(list.filterNotNull())        // [a, c]

    // ---------- 9. lateinit ----------
    // Promise to initialize a non-null `var` later (e.g. by a framework).
    val holder = Holder()
    println(holder.isReady())            // false
    holder.setup()
    println(holder.isReady())            // true
    println(holder.config)

    // ---------- 10. lazy ----------
    // Value computed on first use, then cached. For `val` only.
    val heavy: String by lazy {
        println("Computing...")
        "result"
    }
    println("Before use")
    println(heavy)                       // computes now
    println(heavy)                       // cached, no recompute

    // ---------- EXERCISES ----------
    // 1. Write fun safeDivide(a: Int, b: Int): Int? that returns null for b == 0.
    // 2. Use ?. and ?: to print the length of a nullable String, or -1.
    // 3. Why is `!!` considered a code smell?
}

class Holder {
    lateinit var config: String          // not nullable, set later

    fun setup() {
        config = "loaded"
    }

    // ::config.isInitialized checks whether lateinit was set
    fun isReady() = this::config.isInitialized
}
