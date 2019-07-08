package se.davison.knestedproerty


import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import se.davison.knestedproperty.div
import kotlin.test.assertEquals

class Foo {
    val bar = Bar()
}

class Bar {
    val value = "FooBar"
}


@Suppress("unused")
object NestedPoetrySpek : Spek({

    describe("test nested") {
        it("get name of nested class property") {
            assertEquals("bar.value", (Foo::bar / Bar::value).name)
        }
        it("get value of nested class property") {
            val instance = Foo()
            assertEquals("FooBars", (Foo::bar / Bar::value).value(instance))
        }
    }

})