package com.example.demo.view

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.embed.swing.SwingFXUtils
import javafx.scene.canvas.Canvas
import javafx.scene.control.ScrollPane
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color
import javafx.stage.FileChooser
import javafx.util.Duration
import tornadofx.*
import java.awt.image.BufferedImage
import java.awt.image.RenderedImage
import java.io.File
import java.util.Collections.max
import javax.imageio.ImageIO


class MainView : View("Построитель") {

    val controller: MyController by inject()


    override val root = hbox {
        scrollpane{
            hbarPolicy = ScrollPane.ScrollBarPolicy.NEVER

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
        }
        vbox {
            canvas {
                controller.initCanvas(this)
                val ctx = graphicsContext2D
                style {
                    padding= box(5.px, 0.px, 10.px, 10.px)
                }
                setOnMouseMoved{
                    if (0<=((it.x-controller.axisOffset)/2).toInt() && ((it.x-controller.axisOffset)/2).toInt() < controller.rows.size && 0 <= ((it.y-controller.axisOffset)/2).toInt() && ((it.y-controller.axisOffset)/2).toInt() < controller.rows.size) {
                        tooltip {
                            text = "x: ${it.x}, y: ${it.y}, value: ${controller.rows[((it.x-controller.axisOffset)/2).toInt()][((it.x-controller.axisOffset)/2).toInt()]}"
                        }
                    }
                }
            }
            hbox {
                vbox {
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
                    button {
                        text = "Открыть файл с легендой"
                        action {
                            controller.readLegendCsv(
                                chooseFile(
                                    "Выберите файл",
                                    arrayOf(FileChooser.ExtensionFilter("csv", "*.csv"))
                                )
                            )
                        }
                    }
                }
                button("Сохранить картинку") {
                    hiddenWhen(!controller.ready_to_save)
                    action {
                        controller.saveImage()
                    }
                }
            }
        }

    }

}

class ColorReference(number: Number, color: Color, controller: MyController) {
    val number = SimpleIntegerProperty()
    val controller: MyController


    var color: Color = color
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
    var ready_to_save: SimpleBooleanProperty = SimpleBooleanProperty(false);
    val axisOffset = 35.0
    lateinit var canvas: Canvas
    var legendY = ""
    var legendX = ""
    var startX = ""
    var endX = ""
    var startY = ""
    var endY = ""

    fun redrawCanvas() {
        val ctx = canvas.graphicsContext2D
        ctx.fill = Color.WHITE
        ctx.fillRect(0.0, 0.0, canvas.width, canvas.height)
        for (i in 0 until rows.size) {
            for (j in 0 until rows[i].size) {
                for (k in 0 until colors.size) {
                    if (rows[i][j] == colors[k].number.value.toString()) {
                        ctx.fill = colors[k].color
                        break
                    }
                }
                ctx.fillRect(i.toDouble()*2+axisOffset, (rows[i].size-1-j.toDouble())*2+axisOffset, 2.0, 2.0)
            }
        }
        ctx.fill = Color.BLACK
        ctx.fillText(legendY, 5.0, canvas.height/2)
        ctx.fillText(legendX,  canvas.height/2, axisOffset*1.5+rows.size*2+5)
        ctx.fillText(startX, axisOffset+3, axisOffset+rows.size*2+12)
        ctx.fillText(startY, 0.0, axisOffset+rows.size*2)
        ctx.fillText(endX, rows.size*2.0, axisOffset+rows.size*2+12)
        ctx.fillText(endY, 0.0, axisOffset)
    }
    fun initCanvas(canvas: Canvas) {
        this.canvas = canvas
        canvas.heightProperty().bind(rows.sizeProperty.multiply(2).plus(2*axisOffset))
        canvas.widthProperty().bind(rows.sizeProperty.multiply(2).plus(2*axisOffset))

        colors.onChange {
            redrawCanvas()
        }
    }

    fun IsPowerOf(n: Int, p: Int): Boolean {
        var log = (Math.log(p.toDouble()) / Math.log(n.toDouble())).toInt()
        return Math.pow(n.toDouble(), log.toDouble()) == p.toDouble() && p != 0
    }

    fun openFile(files: List<File>) {
        rows.clear()
        colors.clear()
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
        var blueStart: List<Int> = listOf(0x00, 0x03, 0xc4)
        var blueEnd: List<Int> = listOf(0x03, 0xdb, 0xfc)
        var orangeStart: List<Int> = listOf(0xf0, 0xfc, 0x03)
        var orangeEnd: List<Int> = listOf(0xfc, 0x80, 0x03)
        for (x in nums_list.indices) {
            if (nums_list[x] == 1) {
                temp_colors.add(ColorReference(nums_list[x], Color.valueOf("#070038"), this))
            } else if (IsPowerOf(2, nums_list[x])) {
                var step = Math.log(nums_list[x].toDouble())/Math.log(2.0) / (Math.log(max(nums.toList()).toDouble())/Math.log(2.0)).toInt().toDouble()
                temp_colors.add(ColorReference(
                    nums_list[x], Color.rgb(
                        ((blueEnd[0]-blueStart[0]).toDouble()*step).toInt()+blueStart[0],
                        ((blueEnd[1]-blueStart[1]).toDouble()*step).toInt()+blueStart[1],
                        ((blueEnd[2]-blueStart[2]).toDouble()*step).toInt()+blueStart[2], 1.0),
                    this
                ))
            } else if (nums_list[x] % 3 == 0 && IsPowerOf(2, nums_list[x]/3)) {
                val step =
                    Math.log(nums_list[x].toDouble() / 3.0) / Math.log(2.0) / (Math.log(max(nums.toList()).toDouble() / 3.0) / Math.log(
                        2.0
                    )).toInt().toDouble()
                temp_colors.add(ColorReference(
                    nums_list[x], Color.rgb(
                        ((orangeEnd[0]-orangeStart[0]).toDouble()*step).toInt()+orangeStart[0],
                        ((orangeEnd[1]-orangeStart[1]).toDouble()*step).toInt()+orangeStart[1],
                        ((orangeEnd[2]-orangeStart[2]).toDouble()*step).toInt()+orangeStart[2], 1.0),
                    this
                ))
            } else if (nums_list[x] == max(nums.toList())) {
                temp_colors.add(ColorReference(nums_list[x], Color.valueOf("#990000"), this))
            } else {
                temp_colors.add(ColorReference(nums_list[x], Color.valueOf("#000000"), this))
            }

        }
        colors.addAll(temp_colors)
        ready_to_save.value = true
    }

    fun readLegendCsv(files: List<File>) {
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

        legendX = rows_to_handle[0][5]
        legendY = rows_to_handle[0][6]
        startX = rows_to_handle[0][1]
        endX = rows_to_handle[0][2]
        startY = rows_to_handle[0][3]
        endY = rows_to_handle[0][4]

        redrawCanvas()
    }

    fun saveImage() {
        val fileChooser = FileChooser()
        fileChooser.extensionFilters.add(FileChooser.ExtensionFilter("PNG", "*.png"))
        val file = fileChooser.showSaveDialog(null)
        if (file != null) {
            val writableImage = WritableImage(canvas.width.toInt(),
                canvas.height.toInt())
            canvas.snapshot(null, writableImage)
            val renderedImage: RenderedImage = SwingFXUtils.fromFXImage(writableImage, null)

            ImageIO.write(renderedImage, "png", file)
        }
    }


}
