package com.example.demo.view

import com.example.demo.app.Styles
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.stage.FileChooser
import tornadofx.*
import java.awt.Button
import java.io.File

class MainView : View("Hello TornadoFX") {

    val input = SimpleStringProperty()
    val controller: MyController by inject()

    override val root = hbox {
        tableview(controller.colors) {
            readonlyColumn("Номер", Color::number)
            column("Цвет", Color::color)
        }
        button("Открыть csv файл") {
           action {
               controller.openFile(chooseFile("Выберите файл", arrayOf(FileChooser.ExtensionFilter("csv", "*.csv"))))
           }
        }
    }
}

class Color(number: Number, color: String) {
    val number = SimpleIntegerProperty()
    val color = SimpleStringProperty()
    init {
        this.number.value = number.toInt()
        this.color.value = color
    }
}

class MyController: Controller() {
    var colors = mutableListOf<Color>().asObservable()

    init {
        colors.add(Color(1, "#000010"))
    }

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