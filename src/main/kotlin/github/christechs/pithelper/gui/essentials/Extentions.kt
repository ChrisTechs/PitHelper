/*
                Copyright (c) EssentialsGG and contributors

                   GNU LESSER GENERAL PUBLIC LICENSE
                       Version 3, 29 June 2007

 Copyright (C) 2007 Free Software Foundation, Inc. <https://fsf.org/>
 Everyone is permitted to copy and distribute verbatim copies
 of this license document, but changing it is not allowed.


  This version of the GNU Lesser General Public License incorporates
the terms and conditions of version 3 of the GNU General Public
License, supplemented by the additional permissions listed below.

  0. Additional Definitions.

  As used herein, "this License" refers to version 3 of the GNU Lesser
General Public License, and the "GNU GPL" refers to version 3 of the GNU
General Public License.

  "The Library" refers to a covered work governed by this License,
other than an Application or a Combined Work as defined below.

  An "Application" is any work that makes use of an interface provided
by the Library, but which is not otherwise based on the Library.
Defining a subclass of a class defined by the Library is deemed a mode
of using an interface provided by the Library.

  A "Combined Work" is a work produced by combining or linking an
Application with the Library.  The particular version of the Library
with which the Combined Work was made is also called the "Linked
Version".

  The "Minimal Corresponding Source" for a Combined Work means the
Corresponding Source for the Combined Work, excluding any source code
for portions of the Combined Work that, considered in isolation, are
based on the Application, and not on the Linked Version.

  The "Corresponding Application Code" for a Combined Work means the
object code and/or source code for the Application, including any data
and utility programs needed for reproducing the Combined Work from the
Application, but excluding the System Libraries of the Combined Work.

  1. Exception to Section 3 of the GNU GPL.

  You may convey a covered work under sections 3 and 4 of this License
without being bound by section 3 of the GNU GPL.

  2. Conveying Modified Versions.

  If you modify a copy of the Library, and, in your modifications, a
facility refers to a function or data to be supplied by an Application
that uses the facility (other than as an argument passed when the
facility is invoked), then you may convey a copy of the modified
version:

   a) under this License, provided that you make a good faith effort to
   ensure that, in the event an Application does not supply the
   function or data, the facility still operates, and performs
   whatever part of its purpose remains meaningful, or

   b) under the GNU GPL, with none of the additional permissions of
   this License applicable to that copy.

  3. Object Code Incorporating Material from Library Header Files.

  The object code form of an Application may incorporate material from
a header file that is part of the Library.  You may convey such object
code under terms of your choice, provided that, if the incorporated
material is not limited to numerical parameters, data structure
layouts and accessors, or small macros, inline functions and templates
(ten or fewer lines in length), you do both of the following:

   a) Give prominent notice with each copy of the object code that the
   Library is used in it and that the Library and its use are
   covered by this License.

   b) Accompany the object code with a copy of the GNU GPL and this license
   document.

  4. Combined Works.

  You may convey a Combined Work under terms of your choice that,
taken together, effectively do not restrict modification of the
portions of the Library contained in the Combined Work and reverse
engineering for debugging such modifications, if you also do each of
the following:

   a) Give prominent notice with each copy of the Combined Work that
   the Library is used in it and that the Library and its use are
   covered by this License.

   b) Accompany the Combined Work with a copy of the GNU GPL and this license
   document.

   c) For a Combined Work that displays copyright notices during
   execution, include the copyright notice for the Library among
   these notices, as well as a reference directing the user to the
   copies of the GNU GPL and this license document.

   d) Do one of the following:

       0) Convey the Minimal Corresponding Source under the terms of this
       License, and the Corresponding Application Code in a form
       suitable for, and under terms that permit, the user to
       recombine or relink the Application with a modified version of
       the Linked Version to produce a modified Combined Work, in the
       manner specified by section 6 of the GNU GPL for conveying
       Corresponding Source.

       1) Use a suitable shared library mechanism for linking with the
       Library.  A suitable mechanism is one that (a) uses at run time
       a copy of the Library already present on the user's computer
       system, and (b) will operate properly with a modified version
       of the Library that is interface-compatible with the Linked
       Version.

   e) Provide Installation Information, but only if you would otherwise
   be required to provide such information under section 6 of the
   GNU GPL, and only to the extent that such information is
   necessary to install and execute a modified version of the
   Combined Work produced by recombining or relinking the
   Application with a modified version of the Linked Version. (If
   you use option 4d0, the Installation Information must accompany
   the Minimal Corresponding Source and Corresponding Application
   Code. If you use option 4d1, you must provide the Installation
   Information in the manner specified by section 6 of the GNU GPL
   for conveying Corresponding Source.)

  5. Combined Libraries.

  You may place library facilities that are a work based on the
Library side by side in a single library together with other library
facilities that are not Applications and are not covered by this
License, and convey such a combined library under terms of your
choice, if you do both of the following:

   a) Accompany the combined library with a copy of the same work based
   on the Library, uncombined with any other library facilities,
   conveyed under the terms of this License.

   b) Give prominent notice with the combined library that part of it
   is a work based on the Library, and explaining where to find the
   accompanying uncombined form of the same work.

  6. Revised Versions of the GNU Lesser General Public License.

  The Free Software Foundation may publish revised and/or new versions
of the GNU Lesser General Public License from time to time. Such new
versions will be similar in spirit to the present version, but may
differ in detail to address new problems or concerns.

  Each version is given a distinguishing version number. If the
Library as you received it specifies that a certain numbered version
of the GNU Lesser General Public License "or any later version"
applies to it, you have the option of following the terms and
conditions either of that published version or of any later version
published by the Free Software Foundation. If the Library as you
received it does not specify a version number of the GNU Lesser
General Public License, you may choose any version of the GNU Lesser
General Public License ever published by the Free Software Foundation.

  If the Library as you received it specifies that a proxy can decide
whether future versions of the GNU Lesser General Public License shall
apply, that proxy's public statement of acceptance of any version is
permanent authorization for you to choose that version for the
Library.
*/
package github.christechs.pithelper.gui.essentials

import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.GradientComponent
import gg.essential.elementa.components.ScrollComponent
import gg.essential.elementa.components.Window
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.CopyConstraintFloat
import gg.essential.elementa.constraints.HeightConstraint
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.Effect
import gg.essential.elementa.events.UIClickEvent
import gg.essential.elementa.state.BasicState
import gg.essential.elementa.state.State
import gg.essential.elementa.utils.withAlpha
import gg.essential.universal.UMouse
import gg.essential.universal.UResolution
import gg.essential.vigilance.gui.VigilancePalette
import java.awt.Color
import kotlin.reflect.KProperty

inline fun UIComponent.onLeftClick(crossinline method: UIComponent.(event: UIClickEvent) -> Unit) = onMouseClick {
    if (it.mouseButton == 0) {
        this.method(it)
    }
}

private fun UIComponent.isComponentInParentChain(target: UIComponent): Boolean {
    var component: UIComponent = this
    while (component.hasParent && component !is Window) {
        component = component.parent
        if (component == target)
            return true
    }

    return false
}

fun UIComponent.onAnimationFrame(block: () -> Unit) =
    enableEffect(object : Effect() {
        override fun animationFrame() {
            block()
        }
    })

fun UIComponent.hoveredState(hitTest: Boolean = true, layoutSafe: Boolean = true): State<Boolean> {
    val unsafeHovered = BasicState(false)

    val safeHovered = BasicState(false)

    fun hitTestHovered(): Boolean {
        val halfPixel = 0.5f / UResolution.scaleFactor.toFloat()
        val mouseX = UMouse.Scaled.x.toFloat() + halfPixel
        val mouseY = UMouse.Scaled.y.toFloat() + halfPixel
        return if (isPointInside(mouseX, mouseY)) {

            val window = Window.of(this)
            val hit = (window.hoveredFloatingComponent?.hitTest(mouseX, mouseY)) ?: window.hitTest(mouseX, mouseY)

            hit.isComponentInParentChain(this) || hit == this
        } else {
            false
        }
    }

    if (hitTest) {
        var registerHitTest = true

        onAnimationFrame {
            if (registerHitTest) {
                registerHitTest = false
                Window.enqueueRenderOperation {
                    registerHitTest = true
                    unsafeHovered.set(hitTestHovered())
                }
            }
        }
    }
    onMouseEnter {
        if (hitTest) {
            unsafeHovered.set(hitTestHovered())
        } else {
            unsafeHovered.set(true)
        }
    }

    onMouseLeave {
        unsafeHovered.set(false)
    }

    return if (layoutSafe) {
        unsafeHovered.onSetValue {
            Window.enqueueRenderOperation {
                safeHovered.set(it)
            }
        }
        safeHovered
    } else {
        unsafeHovered
    }
}

fun <T : UIComponent> T.bindParent(parent: UIComponent, state: State<Boolean>, delayed: Boolean = false) =
    bindParent(state.map {
        if (it) parent else null
    }, delayed)

fun <T : UIComponent> T.bindParent(state: State<UIComponent?>, delayed: Boolean = false) = apply {
    state.onSetValueAndNow { parent ->
        val handleStateUpdate = {
            if (this.hasParent && this.parent != parent) {
                this.parent.removeChild(this)
            }
            if (parent != null && this !in parent.children) {
                parent.addChild(this)
            }
        }
        if (delayed) {
            Window.enqueueRenderOperation {
                handleStateUpdate()
            }
        } else {
            handleStateUpdate()
        }
    }
}

fun <T : UIComponent> T.bindFloating(state: State<Boolean>) = apply {
    state.onSetValueAndNow {
        if (hasWindow) {
            this.setFloating(it)
        }
    }
}

val UIComponent.hasWindow: Boolean
    get() = this is Window || hasParent && parent.hasWindow

fun <T : UIComponent> T.centered(): T = apply {
    constrain {
        x = CenterConstraint()
        y = CenterConstraint()
    }
}

fun ScrollComponent.createScrollGradient(
    top: Boolean,
    heightSize: HeightConstraint,
    color: Color = VigilancePalette.getTextShadow(),
    maxGradient: Int = 204,
): GradientComponent {

    val percentState = BasicState(0f)

    this.addScrollAdjustEvent(false) { percent, _ ->
        percentState.set(percent)
    }

    val gradient = object : GradientComponent(
        color.withAlpha(0),
        color.withAlpha(0)
    ) {
        // Override because the gradient should be treated as if it does not exist from an input point of view
        override fun isPointInside(x: Float, y: Float): Boolean {
            return false
        }
    }.bindStartColor(percentState.map {
        if (top) {
            color.withAlpha((it * 1000).toInt().coerceIn(0..maxGradient))
        } else {
            color.withAlpha(0)
        }
    }).bindEndColor(percentState.map {
        if (top) {
            color.withAlpha(0)
        } else {
            color.withAlpha(((1 - it) * 1000).toInt().coerceIn(0..maxGradient))
        }
    }).constrain {
        y = 0.pixels(alignOpposite = !top) boundTo this@createScrollGradient
        x = CopyConstraintFloat() boundTo this@createScrollGradient
        width = CopyConstraintFloat() boundTo this@createScrollGradient
        height = heightSize
    } childOf parent
    return gradient
}

infix fun ScrollComponent.scrollGradient(heightSize: HeightConstraint) = apply {
    val topGradient = createScrollGradient(true, heightSize)
    createScrollGradient(false, 100.percent boundTo topGradient)
}

fun <T> State<T>.onSetValueAndNow(listener: (T) -> Unit) = onSetValue(listener).also { listener(get()) }

operator fun State<Boolean>.not() = map { !it }
fun State<String>.empty() = map { it.isBlank() }
infix fun State<Boolean>.and(other: State<Boolean>) = zip(other).map { (a, b) -> a && b }
infix fun State<Boolean>.or(other: State<Boolean>) = zip(other).map { (a, b) -> a || b }

class ReadOnlyState<T>(private val tate: State<T>) : State<T>() {

    init {
        tate.onSetValueAndNow {
            super.set(it)
        }
    }

    override fun get(): T {
        return tate.get()
    }

    @Suppress("DeprecatedCallableAddReplaceWith")
    @Deprecated("This state is read-only", level = DeprecationLevel.ERROR)
    override fun set(value: T) {
        throw IllegalStateException("Cannot set read only value")
    }
}

operator fun <T> State<T>.getValue(obj: Any, property: KProperty<*>): T = get()
operator fun <T> State<T>.setValue(obj: Any, property: KProperty<*>, value: T) = set(value)

fun <T> T.state() = BasicState(this)