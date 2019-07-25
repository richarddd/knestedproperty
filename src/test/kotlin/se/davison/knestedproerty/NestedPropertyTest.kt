package se.davison.knestedproerty


import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import se.davison.knestedproperty.div
import se.davison.knestedproperty.rem
import se.davison.knestedproperty.times
import kotlin.reflect.jvm.javaType
import kotlin.test.assertEquals

class Foo {
    val bar: Bar? = Bar()
}

class Bar {
    val value: String? = "FooBar"
}

class FooBar {
    val foo: Foo? = null
}

class FooBarList {
    val list = listOf<Foo>()
    val array = arrayOf<Bar?>()
}

class Generic {
    var genericFoo: GenericFoo<String>? = null
}

class GenericFoo<T> {
    val generic: T? = null
}


@Suppress("unused")
object NestedPropertyTest : Spek({


    describe("test nested") {
        it("get name of nested class property") {
            assertEquals("bar.value", (Foo::bar / Bar::value).propertyName)
        }
        it("get name of deeply nested class property") {
            assertEquals("foo.bar.value", (FooBar::foo / Foo::bar / Bar::value).propertyName)
        }
        it("get value of nested class property") {
            val instance = Foo()
            assertEquals("FooBar", (Foo::bar / Bar::value).value(instance))
        }
    }

    describe("test properties") {
        it("use with to get prop names") {
            assertEquals(listOf("bar", "value"), (Foo::bar / Bar::value).properties.map { it.name })
        }
        it("use with to get prop classes") {
            assertEquals(listOf(Bar::class.java, String::class.java), (Foo::bar / Bar::value).properties.map {
                it.returnType.javaType
            })
        }
    }

    describe("test collections") {
        it("get name of nested class property") {
            assertEquals("list.bar", (FooBarList::list % Foo::bar).propertyName)
            assertEquals("array.value", (FooBarList::array * Bar::value).propertyName)
        }
        it("prop names") {
            assertEquals(listOf("list", "bar"), (FooBarList::list % Foo::bar).properties.map { it.name })
        }
        it("use with to get prop classes with generic interfaces") {
            assertEquals(
                arrayListOf("java.util.List<${Foo::class.java.typeName}>", Bar::class.java.typeName),
                (FooBarList::list % Foo::bar).properties.map {
                    it.returnType.javaType.typeName
                })
        }
        it("use with to get prop classes with generic interfaces") {
            assertEquals(
                "genericFoo.generic",
                (Generic::genericFoo / GenericFoo<String>::generic).propertyName
            )

        }
    }

})
