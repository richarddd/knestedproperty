package se.davison.knestedproperty

import java.lang.reflect.Field
import java.util.*
import kotlin.reflect.KProperty1

class CollectionNestedKProperty<V, T, R>(rootClass: Class<V>, parentProp: KProperty1<*, *>, prop: KProperty1<T, R>) :
    NestedKProperty<V, T, R>(rootClass, parentProp, prop) {


}

open class NestedKProperty<V, T, R>(
    private val rootClass: Class<V>,
    private val parentProp: KProperty1<*, *>,
    private val prop: KProperty1<T, R>
) :
    KProperty1<T, R> by prop {

    val propertyName: String
        get() = "${(parentProp as? NestedKProperty<*, *, *>)?.propertyName ?: parentProp.name}.${prop.name}"


    fun value(root: V): R = rootClass.getSubFields(root)

    val properties by lazy {
        getPropertyStack()
    }

    private fun getPropertyStack(): Collection<KProperty1<*, *>> {
        var currentProp: KProperty1<*, *> = this
        val items = LinkedList(listOf<KProperty1<*, *>>())
        while ((currentProp as? NestedKProperty<*, *, *>)?.parentProp !== null) {
            items.push(currentProp)
            currentProp = currentProp.parentProp
        }
        items.push(currentProp)
        return items
    }

    private fun <V, T, R> Class<T>.getSubFields(rootObject: V): R {
        val fieldNames = propertyName.split(".")
        var currentField: Field? = null
        var subObject: Any? = rootObject
        fieldNames.forEachIndexed { _, name ->
            currentField = if (currentField == null) {
                this.getDeclaredField(name)
            } else {
                currentField?.type?.getDeclaredField(name)
            }
            currentField?.isAccessible = true
            subObject = currentField?.get(subObject)
        }
        @Suppress("UNCHECKED_CAST")
        return subObject as R
    }
}


inline operator fun <reified T, R, V, X> NestedKProperty<T, R?, V>.div(subProperty: KProperty1<V, X>) =
    NestedKProperty(T::class.java, this, subProperty)

inline operator fun <reified T, R, V> KProperty1<T, R?>.div(subProperty: KProperty1<R, V>) =
    NestedKProperty(T::class.java, this, subProperty)

inline operator fun <reified T, R, V, X> NestedKProperty<T, Collection<R?>?, V>.rem(subProperty: KProperty1<V, X>) =
    CollectionNestedKProperty(T::class.java, this, subProperty)

inline operator fun <reified T, R, V> KProperty1<T, Collection<R?>?>.rem(subProperty: KProperty1<R, V>) =
    CollectionNestedKProperty(T::class.java, this, subProperty)

inline operator fun <reified T, R, V, X> NestedKProperty<T, Array<R?>?, V>.times(subProperty: KProperty1<V, X>) =
    CollectionNestedKProperty(T::class.java, this, subProperty)

inline operator fun <reified T, R, V> KProperty1<T, Array<R?>?>.times(subProperty: KProperty1<R, V>) =
    CollectionNestedKProperty(T::class.java, this, subProperty)