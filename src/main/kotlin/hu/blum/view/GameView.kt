package hu.blum.view

import hu.blum.viewmodel.GameViewModel
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.stage.Stage
import java.awt.Toolkit

class GameView(val primaryStage: Stage,val viewModel: GameViewModel) :View{

    private val WIDTH:Number = Toolkit.getDefaultToolkit().screenSize.width
    private val HEIGHT:Number = Toolkit.getDefaultToolkit().screenSize.height

    private lateinit var graphicsContext: GraphicsContext
    private lateinit var mainScene: Scene

    init {
        viewModel.addView(this)
    }

    fun createWindow(){
        primaryStage.title = "Game of Life"
        val root = Group()
        mainScene = Scene(root, WIDTH.toDouble(),HEIGHT.toDouble())

        val canvas = Canvas(WIDTH.toDouble(),HEIGHT.toDouble())
        graphicsContext = canvas.graphicsContext2D
        root.children.add(canvas)

        primaryStage.scene = mainScene
        primaryStage.show()
    }

    override fun invalidate(){

    }
}