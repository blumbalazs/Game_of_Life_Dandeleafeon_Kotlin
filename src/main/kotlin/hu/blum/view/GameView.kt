package hu.blum.view

import com.example.getResource
import hu.blum.main
import hu.blum.viewmodel.GameViewModel
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.image.Image
import javafx.scene.layout.Border
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.shape.Line
import javafx.stage.Stage
import java.awt.Color
import java.awt.Toolkit
import java.awt.Window


class GameView(val primaryStage: Stage,val viewModel: GameViewModel) :View{

    private val WIDTH:Number = Toolkit.getDefaultToolkit().screenSize.width/1920.0*600
    private val HEIGHT:Number = WIDTH

    private val NUMBEROFCELLSINROW:Int = 25

    private lateinit var graphicsContext: GraphicsContext
    private lateinit var mainScene: Scene

    init {
        viewModel.addView(this)

    }

    fun show(){
        primaryStage.title = "Dandelifeon simulation"
        primaryStage.isResizable = false
        val root = Group()
        mainScene = Scene(root)

        val canvas = Canvas(WIDTH.toDouble(),HEIGHT.toDouble())

        graphicsContext = canvas.graphicsContext2D
        //root.children.add(canvas)

        val top = HBox()
        val btnPause = Button("start")
        val btnReset = Button("reset")

        btnPause.padding = Insets(0.0,10.0,0.0,10.0)
        btnReset.padding = Insets(0.0,10.0,0.0,10.0)
        top.padding= Insets(10.0)
        top.spacing = 10.0
        top.children.add(btnPause)
        top.children.add(btnReset)

        val mainLayout: BorderPane = BorderPane()
        mainLayout.top = HBox(top)
        mainLayout.center = canvas

        root.children.add(mainLayout)

        primaryStage.scene = mainScene
        primaryStage.show()
        invalidate()
    }

    override fun invalidate(){
        reDraw()
    }

    private fun reDraw(){
        drawBorder()
        drawLines()
        drawPictures()
    }

    private fun drawBorder(){
        val cellsPerRow = NUMBEROFCELLSINROW + 2
        graphicsContext.fillRect(0.0,0.0,WIDTH.toDouble(),HEIGHT.toDouble()/cellsPerRow)
        graphicsContext.fillRect(WIDTH.toDouble()/cellsPerRow*(cellsPerRow-1),0.0,WIDTH.toDouble()/cellsPerRow,HEIGHT.toDouble())
        graphicsContext.fillRect(0.0,0.0,WIDTH.toDouble()/cellsPerRow,HEIGHT.toDouble())
        graphicsContext.fillRect(0.0,HEIGHT.toDouble()/cellsPerRow*(cellsPerRow-1),WIDTH.toDouble(),HEIGHT.toDouble()/cellsPerRow)
    }
    private fun drawLines(){
        graphicsContext.lineWidth = 0.5
        val cellsPerRow = NUMBEROFCELLSINROW + 2
        for (i in 0 .. cellsPerRow){
            graphicsContext.moveTo(WIDTH.toDouble()/cellsPerRow*i,0.0)
            graphicsContext.lineTo(WIDTH.toDouble()/cellsPerRow*i,HEIGHT.toDouble())
            graphicsContext.stroke()

            graphicsContext.moveTo(0.0,HEIGHT.toDouble()/cellsPerRow*i)
            graphicsContext.lineTo(WIDTH.toDouble(),HEIGHT.toDouble()/cellsPerRow*i)
            graphicsContext.stroke()
        }
    }
    private fun drawPictures(){
        graphicsContext.drawImage(
            Image(getResource("/Dandelifeon.png")),
            WIDTH.toDouble()/27*13,
            HEIGHT.toDouble()/27*13,
            WIDTH.toDouble()/27,
            HEIGHT.toDouble()/27)
    }
}