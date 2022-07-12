package com.example.demo.view

import com.example.demo.app.Styles
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.paint.Paint
import javafx.stage.FileChooser
import tornadofx.*
import java.awt.Button
import java.io.File
import java.util.Collections.max
import java.util.Collections.min

class MainView : View("Hello TornadoFX") {

    val input = SimpleStringProperty()
    val controller: MyController by inject()

    override val root = hbox {
        vbox{
            children.bind(controller.colors) {
                hbox {
                    label(it.number.value.toString())
                    colorpicker(mode = ColorPickerMode.MenuButton) {
                        value = it.color
                        setOnAction { _ ->
                            it.color = value
                        }

                    }
                }
            }

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
    var color: javafx.scene.paint.Color = javafx.scene.paint.Color.valueOf(color)
    init {
        this.number.value = number.toInt()
    }
}

class MyController: Controller() {
    var colors: ObservableList<Color> = FXCollections.observableArrayList()
    var rows: List<List<String>> = listOf()

    init {
    }

    fun IsPowerOf(n: Int, p: Int): Boolean {
        var log = (Math.log(p.toDouble()) / Math.log(n.toDouble())).toInt()
        return Math.pow(n.toDouble(), log.toDouble()) == p.toDouble()
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
        val file = files[0]
        rows = csvReader().readAll(file)

        var nums: Set<Int> = sortedSetOf()
        for (i in 0 until rows.size) {
            for (j in 0 until rows[i].size) {
                nums = nums.plusElement(Integer.parseInt(rows[i][j]))
            }
        }
        var nums_list = nums.toMutableList()
        // sort nums_list
        nums_list.sort()
        var temp_colors = mutableListOf<Color>()
        for (x in nums_list.indices) {
            temp_colors.add(Color(nums_list[x], "#000010"))
            // TODO: Set default colors here

        }
        colors.addAll(temp_colors)
        println(min(nums.toList()).toString() + max(nums.toList()).toString())
    }


}
