package basic.lesson06

/*
 * LESSON 6 — CLASSES & OBJECTS (Intermediate)
 * -------------------------------------------
 * Topics: classes, constructors, properties, init blocks, methods, visibility,
 *         custom getters/setters, companion objects, singletons, nested classes.
 */

// ---------- 1. A simple class with a PRIMARY constructor ----------
// `val`/`var` in the constructor automatically creates properties.
class Dog(val name: String, var age: Int) {

    // Property with a default value (not from the constructor)
    var energy = 100

    // init block runs when an object is created
    init {
        println("Dog created: $name")
    }

    // Secondary constructor must call the primary one with this(...)
    constructor(name: String) : this(name, 1)

    // Method (a function inside a class)
    fun bark() {
        println("$name says Woof!")
        energy -= 10
    }

    // Computed property (custom getter, no stored value)
    val isPuppy: Boolean
        get() = age < 2

    override fun toString() = "Dog(name=$name, age=$age)"
}

// ---------- 2. Visibility modifiers ----------
// public (default) | private (this class/file) | protected (class + subclasses)
// internal (same module)
class BankAccount(private var balance: Double) {

    fun deposit(amount: Double) {
        require(amount > 0) { "Deposit must be positive" }   // throws IllegalArgumentException
        balance += amount
    }

    fun withdraw(amount: Double) {
        check(amount <= balance) { "Insufficient funds" }    // throws IllegalStateException
        balance -= amount
    }

    // Public read, private write
    var transactions = 0
        private set

    fun currentBalance() = balance
}

// ---------- 3. Custom setter with validation ----------
class Thermostat {
    var target: Int = 20
        set(value) {
            // `field` is the hidden backing variable
            field = value.coerceIn(10, 30)      // clamp between 10 and 30
        }
}

// ---------- 4. Companion object (like static members in Java) ----------
class User private constructor(val name: String) {
    companion object {
        private var count = 0
        const val MIN_NAME_LENGTH = 2

        // Factory method: controlled way to build objects
        fun create(name: String): User {
            require(name.length >= MIN_NAME_LENGTH) { "Name too short" }
            count++
            return User(name)
        }

        fun total() = count
    }
}

// ---------- 5. object = singleton (exactly one instance) ----------
object Logger {
    private val lines = mutableListOf<String>()

    fun log(msg: String) {
        lines.add(msg)
        println("[LOG] $msg")
    }

    fun count() = lines.size
}

// ---------- 6. Nested and inner classes ----------
class Outer(val label: String) {
    class Nested {                     // cannot access Outer's members
        fun hi() = "nested"
    }

    inner class Inner {                // CAN access Outer's members
        fun hi() = "inner of $label"
    }
}

fun main() {
    val rex = Dog("Rex", 3)            // no `new` keyword in Kotlin
    rex.bark()
    rex.age = 4
    println(rex)
    println(rex.isPuppy)
    println(Dog("Pup").isPuppy)        // secondary constructor

    val account = BankAccount(100.0)
    account.deposit(50.0)
    account.withdraw(30.0)
    println(account.currentBalance())
    // account.balance                 // ERROR: private

    val t = Thermostat()
    t.target = 99
    println(t.target)                  // 30

    val u = User.create("Zoe")
    println("${u.name}, total users: ${User.total()}")

    Logger.log("Started")
    Logger.log("Finished")
    println(Logger.count())

    println(Outer.Nested().hi())
    println(Outer("X").Inner().hi())

    // Objects compare by REFERENCE with == unless equals() is overridden
    // (data classes in lesson 8 solve this).
    println(Dog("A", 1) == Dog("A", 1))   // false

    // ---------- EXERCISES ----------
    // 1. Create a Rectangle class with width, height, and computed area/perimeter.
    // 2. Create a Counter singleton with increment() and value().
    // 3. Add validation so a Person's age can never be negative.
}
