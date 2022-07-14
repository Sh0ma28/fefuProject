package com.example.demo.app

import com.example.demo.view.MainView
import javafx.stage.Stage
import tornadofx.*

class MyApp: App(MainView::class, Styles::class) {
    override fun start(stage: Stage) {
        super.start(stage)
        stage.width = 800.0
        stage.height = 800.0
    }
}
