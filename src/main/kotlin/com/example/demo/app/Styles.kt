package com.example.demo.app

import javafx.scene.text.FontWeight
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val heading by cssclass()
    }

    init {
        label and heading {
            padding = box(10.px)
            fontSize = 20.px
            fontWeight = FontWeight.BOLD
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
    }
}