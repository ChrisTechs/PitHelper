package github.christechs.pithelper.versions

import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyCollector
import gg.essential.vigilance.data.PropertyData
import gg.essential.vigilance.data.PropertyType

class PitHelperPropertyCollector : PropertyCollector() {

    fun clear() {
        getProperties().clear()
    }

    override fun collectProperties(instance: Vigilant): List<PropertyData> {
        val fieldPropertyData = instance::class.java.declaredFields
            .filter { it.isAnnotationPresent(Property::class.java) }
            .map { field ->
                field.isAccessible = true

                PropertyData.fromField(field.getAnnotation(Property::class.java), field, instance).also { data ->
                    if (!data.attributesExt.type.isFieldValid(field)) {
                        throw IllegalStateException(
                            "[Vigilance] Error while creating GUI ${instance::class.simpleName}: " +
                                    "field ${field.name} of PropertyType ${data.attributesExt.type.name} has invalid JVM type " +
                                    field.type.simpleName
                        )
                    }
                }
            }

        val methodPropertyData = instance::class.java.declaredMethods
            .filter { it.isAnnotationPresent(Property::class.java) }
            .map { method ->
                method.isAccessible = true

                PropertyData.fromMethod(method.getAnnotation(Property::class.java), method, instance).also { data ->
                    if (data.attributesExt.type != PropertyType.BUTTON) {
                        throw IllegalStateException(
                            "[Vigilance] Error while creating GUI ${instance::class.simpleName}: " +
                                    "expected method ${method.name} to have PropertyType BUTTON, but found PropertyType " +
                                    data.attributesExt.type.name
                        )
                    }
                }
            }

        return fieldPropertyData + methodPropertyData
    }

}