package github.christechs.pithelper.gui

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.*
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.ScissorEffect
import github.christechs.pithelper.features.events.Event
import github.christechs.pithelper.features.events.EventsHandler
import java.awt.Color

object EventsGui : WindowScreen(ElementaVersion.V2) {

    private val eventsToText = mutableMapOf<Int, Pair<UIText, UIContainer>>()

    private var scroll = ScrollComponent().constrain {
        x = 0f.pixels()
        y = 0f.pixels()
        width = window.getWidth().pixels()
        height = window.getHeight().pixels()
    }

    fun removeEvent(id: Int) {
        scroll.removeChild(eventsToText[id]?.second ?: return)
        eventsToText.remove(id)
    }

    fun setText(id: Int, text: String) {
        (eventsToText[id] ?: return).first.setText(text)
    }

    var activeEventText = UIText()

    fun refreshEvents(events: List<Event>) {

        eventsToText.clear()
        window.clearChildren()
        scroll.clearChildren()

        scroll = ScrollComponent().constrain {
            x = 0f.pixels()
            y = 0f.pixels()
            width = window.getWidth().pixels()
            height = window.getHeight().pixels()
        }

        window.addChild(scroll)

        val textHeight = UIText("TEXT HEIGHT").getHeight().pixels()

        UIBlock(Color(0, 0, 0, 255)).constrain {
            x = CenterConstraint()
            y = SiblingConstraint(padding = 2f)

            height = 10.pixels()
        } childOf scroll

        val refresh = UIBlock(Color(255, 0, 0)).constrain {
            x = CenterConstraint()
            y = SiblingConstraint(padding = 2f)

            height = textHeight * 2
            width = window.getWidth().pixels() * 0.3
        } effect ScissorEffect() childOf scroll

        refresh.onMouseClick {
            EventsHandler.refresh()
        }
        refresh.onMouseEnter {
            refresh.setColor(Color(255, 0, 0, 100))
        }
        refresh.onMouseLeave {
            refresh.setColor(Color(255, 0, 0))
        }

        refresh.addChild(UIWrappedText("Load Events", centered = true).constrain {
            x = 2.pixels()
            y = SiblingConstraint() + 5.pixels()

            width = refresh.getWidth().pixels()
        })

        val activeEventContainer = UIContainer().constrain {
            x = CenterConstraint()
            y = SiblingConstraint(padding = 2f)

            width = window.getWidth().pixels()
            height = textHeight * 1.5
        } childOf scroll

        val activeEventBlock = UIBlock(Color(255, 170, 0)).constrain {
            x = CenterConstraint()

            width = (window.getWidth() * 0.95).pixels()
            height = textHeight * 1.5
        } childOf activeEventContainer

        activeEventText = UIText("No Active Event", false).constrain {
            x = CenterConstraint()
            y = CenterConstraint() + (activeEventBlock.getHeight() * 0.07).pixels()

        } childOf activeEventContainer

        for (event in events) {
            val container = UIContainer().constrain {
                x = CenterConstraint()
                y = SiblingConstraint(padding = 2f)

                width = window.getWidth().pixels()
                height = textHeight * 1.5
            } childOf scroll

            val block = UIBlock(event.event.colour).constrain {
                x = CenterConstraint()

                width = (window.getWidth() * 0.95).pixels()
                height = textHeight * 1.5
            } childOf container

            val text = UIText("EventText", false).constrain {
                x = CenterConstraint()
                y = CenterConstraint() + (block.getHeight() * 0.07).pixels()

            } childOf container

            eventsToText[event.id] = Pair(text, container)
        }

        UIBlock(Color(0, 0, 0, 255)).constrain {
            x = CenterConstraint()
            y = SiblingConstraint(padding = 2f)

            height = 10.pixels()
        } childOf scroll
    }

}