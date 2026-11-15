package basic.lesson11

import kotlin.properties.Delegates
import kotlin.reflect.KProperty

/*
 * LESSON 11 — ADVANCED LANGUAGE FEATURES (Advanced)
 * -------------------------------------------------
 * Topics: extension functions/properties, operator overloading, infix functions,
 *         delegation (class + property), custom delegates, destructuring,
 *         type-safe builders (DSL), typealias, ranges & iterators, contracts of
 *         equality.
 */

// ---------- 1. Extension functions ----------
// Add functions to existing types WITHOUT inheriting or modifying them.
fun String.shout() = this.uppercase() + "!"
fun Int.isEven() = this % 2 == 0
val String.wordCount: Int get() = trim().split(Regex("\\s+")).size   // extension property

// Extensions on nullable types
fun String?.orDash() = this ?: "-"

// ---------- 2. Operator overloading ----------
data class Vec(val x: Int, val y: Int) {
    operator fun plus(o: Vec) = Vec(x + o.x, y + o.y)
    operator fun minus(o: Vec) = Vec(x - o.x, y - o.y)
    operator fun times(k: Int) = Vec(x * k, y * k)
    operator fun unaryMinus() = Vec(-x, -y)
    operator fun get(i: Int) = if (i == 0) x else y
    operator fun compareTo(o: Vec) = (x * x + y * y).compareTo(o.x * o.x + o.y * o.y)
}

// ---------- 3. Infix functions ----------
infix fun Int.power(exp: Int): Int {
    var r = 1
    repeat(exp) { r *= this }
    return r
}

// ---------- 4. Class delegation (`by`) ----------
// Implement an interface by forwarding to another object.
interface Printer { fun print(msg: String) }
class ConsolePrinter : Printer { override fun print(msg: String) = println("> $msg") }

class TimestampPrinter(p: Printer) : Printer by p {
    // Everything is forwarded except what we override
    override fun print(msg: String) = println("[time] ${msg.uppercase()}")
}

class QuietPrinter(p: Printer) : Printer by p      // pure forwarding

// ---------- 5. Property delegation ----------
class Settings {
    // lazy: computed once
    val database: String by lazy { println("connecting..."); "db-connection" }

    // observable: callback on every change
    var volume: Int by Delegates.observable(5) { _, old, new ->
        println("volume $old -> $new")
    }

    // vetoable: reject some changes
    var age: Int by Delegates.vetoable(0) { _, _, new -> new >= 0 }
}

// Custom delegate: any class with getValue/setValue operators
class Trimmed {
    private var value = ""
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String = value
    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: String) {
        value = newValue.trim()
    }
}

class Form {
    var title: String by Trimmed()
}

// ---------- 6. Type-safe builder (DSL) ----------
class Html {
    private val sb = StringBuilder()
    fun body(init: Body.() -> Unit) {
        sb.append("<html>")
        sb.append(Body().apply(init).render())
        sb.append("</html>")
    }
    override fun toString() = sb.toString()
}

class Body {
    private val parts = mutableListOf<String>()
    fun h1(text: String) { parts.add("<h1>$text</h1>") }
    fun p(text: String) { parts.add("<p>$text</p>") }
    fun render() = "<body>${parts.joinToString("")}</body>"
}

fun html(init: Html.() -> Unit): Html = Html().apply(init)

// ---------- 7. typealias ----------
typealias UserMap = Map<String, List<String>>
typealias Handler = (String) -> Unit

// ---------- 8. Custom iterable (works in for-loops) ----------
class Countdown(private val from: Int) : Iterable<Int> {
    override fun iterator() = object : Iterator<Int> {
        private var current = from
        override fun hasNext() = current > 0
        override fun next() = current--
    }
}

fun main() {
    println("hello".shout())
    println(4.isEven())
    println("one two  three".wordCount)
    val nothing: String? = null
    println(nothing.orDash())

    val a = Vec(1, 2)
    val b = Vec(3, 4)
    println(a + b)
    println(b - a)
    println(a * 3)
    println(-a)
    println(a[0])
    println(b > a)                 // uses compareTo

    println(2 power 10)            // infix: no dot, no parentheses

    TimestampPrinter(ConsolePrinter()).print("hello")
    QuietPrinter(ConsolePrinter()).print("forwarded")

    val s = Settings()
    s.volume = 8
    s.age = -5                     // vetoed
    println(s.age)                 // 0
    println(s.database)
    println(s.database)            // no second "connecting..."

    val f = Form()
    f.title = "   Kotlin   "
    println("[${f.title}]")

    println(
        html {
            body {
                h1("Title")
                p("Paragraph")
            }
        }
    )

    val users: UserMap = mapOf("a" to listOf("x"))
    val handler: Handler = { println("handled $it") }
    handler("event")
    println(users)

    for (n in Countdown(3)) print("$n ")
    println()

    // Destructuring works with any class that has componentN() functions (data classes do).
    val (x, y) = Vec(7, 8)
    println("$x,$y")

    // ---------- EXERCISES ----------
    // 1. Write List<Int>.median() as an extension function.
    // 2. Overload `+=` for a ShoppingCart (hint: plusAssign).
    // 3. Build a tiny DSL for a pizza order: pizza { size("L"); topping("cheese") }.
}
