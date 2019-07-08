package se.davison.knestedproperty

import java.lang.reflect.Field
import kotlin.reflect.KProperty1

class NestedKProperty<V, T, R>(
    private val rootClass: Class<V>,
    private val parentProp: KProperty1<*, *>,
    private val prop: KProperty1<T, R>
) :
    KProperty1<T, R> by prop {

    override val name: String
        get() = "${parentProp.name}.${prop.name}"

    fun value(root: V): R = rootClass.getSubFields(name, root as Any)

    private fun <T, R> Class<T>.getSubFields(names: String, rootObject: Any): R {
        val fieldNames = names.split(".")
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


inline operator fun <reified T, R, V, X> NestedKProperty<T, R, V>.div(subProperty: KProperty1<V, X>): NestedKProperty<T, V, X> =
    NestedKProperty(T::class.java, this, subProperty)

inline operator fun <reified T, R, V> KProperty1<T, R>.div(subProperty: KProperty1<R, V>): NestedKProperty<T, R, V> =
    NestedKProperty(T::class.java, this, subProperty)