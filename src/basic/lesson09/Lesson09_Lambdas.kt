package basic.lesson09

/*
 * LESSON 9 — LAMBDAS, HIGHER-ORDER FUNCTIONS & SCOPE FUNCTIONS (Intermediate/Advanced)
 * ------------------------------------------------------------------------------------
 * Topics: function types, lambdas, `it`, trailing lambdas, closures, higher-order
 *         functions, function references, inline, let/run/with/apply/also.
 */

// ---------- 1. Higher-order function: takes or returns a function ----------
fun operate(a: Int, b: Int, op: (Int, Int) -> Int): Int = op(a, b)

// Returns a function
fun multiplier(factor: Int): (Int) -> Int = { number -> number * factor }

// Closure: the lambda "remembers" variables from where it was created
fun makeCounter(): () -> Int {
    var count = 0
    return { ++count }
}

// ---------- 2. inline ----------
// The compiler copies the function body AND the lambda into the call site,
// avoiding object allocation for the lambda. Use for small HOFs taking lambdas.
inline fun measure(label: String, block: () -> Unit) {
    val start = System.nanoTime()
    block()
    val ms = (System.nanoTime() - start) / 1_000_000
    println("$label took ${ms}ms")
}

// ---------- 3. A DSL-style function with a lambda as the last parameter ----------
fun repeatAction(times: Int, action: (Int) -> Unit) {
    for (i in 1..times) action(i)
}

data class Config(var host: String = "", var port: Int = 0)

fun main() {
    // ---------- Lambdas ----------
    val add: (Int, Int) -> Int = { a, b -> a + b }   // full form
    val square: (Int) -> Int = { it * it }           // single param => `it`
    println(add(2, 3))
    println(square(5))

    println(operate(6, 3) { a, b -> a * b })         // trailing lambda syntax
    println(operate(6, 3, Int::minus))               // function reference
    println(multiplier(3)(10))                       // 30

    val counter = makeCounter()
    println("${counter()} ${counter()} ${counter()}")   // 1 2 3

    repeatAction(3) { println("Run #$it") }

    // Function references with ::
    val words = listOf("kiwi", "fig", "banana")
    println(words.map(String::length))
    println(words.sortedBy(String::length))
    words.forEach(::println)

    // Anonymous function (rare)
    val anon = fun(x: Int): Int { return x + 1 }
    println(anon(1))

    // Return from lambda: use label
    listOf(1, 2, 3).forEach {
        if (it == 2) return@forEach          // like `continue`
        println("item $it")
    }

    measure("loop") {
        var s = 0L
        for (i in 1..1_000_000) s += i
        println(s)
    }

    // ---------- Scope functions ----------
    // Run a block on an object. They differ in (a) how you refer to the object
    // (it / this) and (b) what they return (the lambda result / the object).
    //
    //  function | object ref | returns
    //  ---------+------------+--------------
    //  let      | it         | lambda result
    //  run      | this       | lambda result
    //  with     | this       | lambda result   (called with(obj) { })
    //  apply    | this       | the object
    //  also     | it         | the object

    // let: transform, or run on non-null
    val length = "hello".let { it.length }
    println(length)

    // apply: configure an object, get it back (builder style)
    val config = Config().apply {
        host = "localhost"
        port = 8080
    }
    println(config)

    // also: side effects (logging) without breaking a chain
    val numbers = mutableListOf(1, 2, 3)
        .also { println("Before: $it") }
        .apply { add(4) }
    println(numbers)

    // run: compute something using the object's members
    val summary = config.run { "$host:$port" }
    println(summary)

    // with: operate on an object you already have
    val text = with(config) { "Server at $host" }
    println(text)

    // ---------- Receiver lambdas -> DSLs ----------
    // A lambda with receiver, `Config.() -> Unit`, lets the body use `this`.
    fun buildConfig(init: Config.() -> Unit): Config = Config().apply(init)
    println(buildConfig { host = "example.com"; port = 443 })

    // ---------- EXERCISES ----------
    // 1. Write fun <T> List<T>.myFilter(pred: (T) -> Boolean): List<T> with a loop.
    // 2. Write compose(f, g) that returns a function computing g(f(x)).
    // 3. Rewrite a chain of null checks using ?.let.
}
