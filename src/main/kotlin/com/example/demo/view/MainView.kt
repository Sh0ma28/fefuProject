package com.example.demo.view

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import javafx.beans.property.SimpleIntegerProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import javafx.stage.FileChooser
import tornadofx.*
import java.io.File
import java.util.Collections.max
import java.util.Collections.min
import kotlin.contracts.contract

class MainView : View("Hello TornadoFX") {

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
        vbox {
            canvas {
                controller.initCanvas(this)
                val ctx = graphicsContext2D
            }
            button("Открыть csv файл") {
                action {
                    controller.openFile(
                        chooseFile(
                            "Выберите файл",
                            arrayOf(FileChooser.ExtensionFilter("csv", "*.csv"))
                        )
                    )
                }
            }
        }

    }
}

class ColorReference(number: Number, color: String, controller: MyController) {
    val number = SimpleIntegerProperty()
    val controller: MyController

    var color: javafx.scene.paint.Color = javafx.scene.paint.Color.valueOf(color)
        set(value) {
            field = value
            this.controller.redrawCanvas()
            }


    init {
        this.number.value = number.toInt()
    this.controller = controller
    }
}

class MyController: Controller() {
    var colors: ObservableList<ColorReference> = FXCollections.observableArrayList()
    var rows = FXCollections.observableArrayList<MutableList<String>>()
    lateinit var canvas: Canvas

    init {

    }

    fun redrawCanvas() {
        val ctx = canvas.graphicsContext2D
        for (i in 0 until rows.size) {
            for (j in 0 until rows[i].size) {
                for (k in 0 until colors.size) {
                    if (rows[i][j] == colors[k].number.value.toString()) {
                        ctx.fill = colors[k].color
                        break
                    }
                }
                ctx.fillRect(i.toDouble(), j.toDouble(), i.toDouble(), j.toDouble())
            }
        }
    }
    fun initCanvas(canvas: Canvas) {
        this.canvas = canvas
        canvas.heightProperty().bind(rows.sizeProperty)
        canvas.widthProperty().bind(rows.sizeProperty)
        colors.onChange {
            redrawCanvas()
        }
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
        val rows_to_handle = csvReader().readAll(file)

        var nums: Set<Int> = sortedSetOf()
        for (i in 0 until rows_to_handle.size) {
            rows.add((rows_to_handle[i]).toMutableList())
            for (j in 0 until rows_to_handle[i].size) {
                nums = nums.plusElement(Integer.parseInt(rows_to_handle[i][j]))
            }
        }
        var nums_list = nums.toMutableList()
        // sort nums_list
        nums_list.sort()
        var temp_colors = mutableListOf<ColorReference>()
        for (x in nums_list.indices) {
            temp_colors.add(ColorReference(nums_list[x], "#000010", this))
            // TODO: Set default colors here

        }
        colors.addAll(temp_colors)
        println(min(nums.toList()).toString() + max(nums.toList()).toString())
    }


}
