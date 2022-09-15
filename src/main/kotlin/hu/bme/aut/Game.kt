package hu.bme.aut

import hu.bme.aut.model.FieldModel
import hu.bme.aut.render.Renderer
import hu.bme.aut.render.StaticMovementRenderer
import javafx.application.Application
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.shape.Circle
import javafx.stage.Stage
import java.awt.Toolkit

class Game: Application() {

    private val WIDTH:Number = Toolkit.getDefaultToolkit().screenSize.width
    private val HEIGHT:Number = Toolkit.getDefaultToolkit().screenSize.height
    private val renderer:Renderer = StaticMovementRenderer()
    private val model: FieldModel = FieldModel()


    override fun start(primaryStage: Stage) {


    }

    fun createWindow(primaryStage: Stage){
        primaryStage.title = "Game of Life"
        //TODO("Canvas create")
        val root = Group()
        primaryStage.scene = Scene(root, WIDTH.toDouble(),HEIGHT.toDouble())
        primaryStage.show()
    }
}