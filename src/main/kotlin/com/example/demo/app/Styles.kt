package com.example.demo.app

import javafx.scene.text.FontWeight
import tornadofx.*
import javafx.scene.control.ScrollPane
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop

class Styles : Stylesheet() {
    companion object {
        val heading by cssclass()
    }

    init {
        root {
            backgroundColor = multi(c("#171D28"))
            textFill = c("#DDE2E8")
        }
        label {
            padding = box(10.px)
            backgroundColor = multi(c("#171D28"))
            textFill = c("#DDE2E8")
        }
        button {
            backgroundRadius = MultiValue(arrayOf( box(5.px),box(5.px),box(5.px),box(5.px)))
            borderRadius = MultiValue(arrayOf( box(4.px),box(4.px),box(4.px),box(4.px)))
            borderWidth = MultiValue(
                arrayOf(box(2.px), box(2.px), box(2.px), box(2.px))
            )
            borderColor = multi(box(
                    c("#01F5EA")
            ))
            backgroundColor = multi(c("#171D28"))
            textFill = c("#DDE2E8")
            startMargin = 10.px
        }
        scrollPane {
                backgroundColor = multi(c("#171D28"))
                borderWidth = MultiValue(arrayOf(box(0.px)))
        }
        scrollBar {
            backgroundColor = multi(c("#171D28"))
        }
        thumb {
            backgroundColor = multi(LinearGradient(
                0.0, 0.0, 1.0, 0.0, true, CycleMethod.NO_CYCLE,
                Stop(0.0, c("#03EBE0")),
                Stop(1.0, c("#01F5EA"))
            ))
        }
        viewport {
            backgroundColor = multi(c("#171D28"))
            padding= box(10.px)
            borderWidth = MultiValue(arrayOf(box(0.px)))
        }
    }
}