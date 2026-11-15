package basic.lesson07

/*
 * LESSON 7 — INHERITANCE, INTERFACES & POLYMORPHISM (Intermediate)
 * ----------------------------------------------------------------
 * Topics: open classes, override, abstract classes, interfaces, polymorphism,
 *         super, composition vs inheritance, the `is` check.
 */

// ---------- 1. Inheritance ----------
// Classes are FINAL by default in Kotlin. `open` allows subclassing.
open class Animal(val name: String) {
    open val sound: String = "..."          // open = may be overridden

    open fun speak() {
        println("$name says $sound")
    }

    fun sleep() = println("$name sleeps")   // not open -> cannot be overridden
}

class Cat(name: String) : Animal(name) {    // call the parent constructor
    override val sound = "Meow"
}

class Cow(name: String) : Animal(name) {
    override val sound = "Moo"

    override fun speak() {
        super.speak()                        // run the parent version too
        println("(the cow chews grass)")
    }
}

// ---------- 2. Abstract classes ----------
// Cannot be instantiated; may hold both implemented and abstract members.
abstract class Shape {
    abstract fun area(): Double              // subclasses MUST implement
    fun describe() = "${this::class.simpleName} with area ${"%.2f".format(area())}"
}

class Circle(private val r: Double) : Shape() {
    override fun area() = Math.PI * r * r
}

class Square(private val side: Double) : Shape() {
    override fun area() = side * side
}

// ---------- 3. Interfaces ----------
// A contract. Can have default implementations. A class may implement many.
interface Flyable {
    val maxAltitude: Int                     // abstract property
    fun fly() = println("Flying up to $maxAltitude m")   // default implementation
}

interface Swimmable {
    fun swim()
}

class Duck : Flyable, Swimmable {
    override val maxAltitude = 500
    override fun swim() = println("Duck paddles")
}

// Resolving a conflict when two interfaces share a method name
interface A { fun hello() = println("A") }
interface B { fun hello() = println("B") }
class C : A, B {
    override fun hello() {
        super<A>.hello()
        super<B>.hello()
    }
}

// ---------- 4. Polymorphism ----------
// One type, many behaviours. Code depends on the abstraction.
fun printAll(shapes: List<Shape>) {
    for (s in shapes) println(s.describe())
}

// ---------- 5. Composition over inheritance ----------
// Prefer "has-a" to "is-a" when behaviours vary independently.
class Engine {
    fun start() = println("Engine started")
}

class Car(private val engine: Engine) {
    fun drive() {
        engine.start()
        println("Driving")
    }
}

fun main() {
    val animals: List<Animal> = listOf(Cat("Tom"), Cow("Daisy"), Animal("Thing"))
    for (a in animals) a.speak()             // each calls ITS OWN version

    printAll(listOf(Circle(1.5), Square(2.0)))

    val duck = Duck()
    duck.fly()
    duck.swim()
    C().hello()

    // Type checks and smart cast
    val creature: Any = duck
    if (creature is Flyable) {
        creature.fly()                       // auto-cast to Flyable
    }
    println(duck is Swimmable)

    Car(Engine()).drive()

    // ---------- EXERCISES ----------
    // 1. Add a Triangle to the Shape hierarchy.
    // 2. Create interface Payment { fun pay(amount: Double) } with CardPayment and CashPayment.
    // 3. When would you choose an abstract class over an interface?
    //    (Hint: state/constructors vs. multiple inheritance.)
}
