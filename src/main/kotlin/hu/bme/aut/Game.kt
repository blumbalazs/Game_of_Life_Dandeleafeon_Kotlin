package hu.bme.aut

import hu.bme.aut.model.FieldModel
import hu.bme.aut.render.Renderer
import hu.bme.aut.render.StaticMovementRenderer
import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.shape.Circle
import javafx.stage.Stage
import java.awt.Toolkit

class Game: Application() {

    private val WIDTH:Number = Toolkit.getDefaultToolkit().screenSize.width
    private val HEIGHT:Number = Toolkit.getDefaultToolkit().screenSize.height
    private val model: FieldModel = FieldModel()


    private lateinit var renderer:Renderer
    private lateinit var graphicsContext: GraphicsContext
    private lateinit var mainScene: Scene
    override fun start(primaryStage: Stage) {
        createWindow(primaryStage)


        renderer = StaticMovementRenderer(graphicsContext)

        object: AnimationTimer(){
            override fun handle(now: Long) {
                renderer.tickAndRender(now)

            }

        }.start()
    }

    fun createWindow(primaryStage: Stage){
        primaryStage.title = "Game of Life"
        //TODO("Canvas create")
        val root = Group()
        mainScene = Scene(root, WIDTH.toDouble(),HEIGHT.toDouble())

        val canvas = Canvas(WIDTH.toDouble(),HEIGHT.toDouble())
        graphicsContext = canvas.graphicsContext2D
        root.children.add(canvas)

        primaryStage.scene = mainScene
        primaryStage.show()
    }
}