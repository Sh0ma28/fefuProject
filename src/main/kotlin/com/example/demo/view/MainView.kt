package com.example.demo.view

import com.example.demo.app.Styles
import javafx.beans.property.SimpleStringProperty
import javafx.stage.FileChooser
import tornadofx.*
import java.awt.Button
import java.io.File

class MainView : View("Hello TornadoFX") {

    val input = SimpleStringProperty()
    val controller: MyController by inject()

    override val root = vbox {
        button("Открыть xls файл") {
           action {
               controller.openFile(chooseFile("Выберите файл", arrayOf(FileChooser.ExtensionFilter("xls", "*.xls"), FileChooser.ExtensionFilter("xlsx", "*.xlsx"))))
           }
        }
    }
}

class MyController: Controller() {
    fun writeText(value: String) {
        println("Here is $value")
    }
    fun openFile(files: List<File>) {
        if (files.isEmpty()) {
            println("File was not opened")
            return
        }
        if (files.size > 1) {
            println("More than one file was selected")
            return
        }
        println("Path is ${files[0].absoluteFile}") // TODO: обработать файл
    }
}