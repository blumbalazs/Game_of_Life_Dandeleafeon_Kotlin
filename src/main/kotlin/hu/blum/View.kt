package hu.blum

import com.example.Game
import com.example.getResource
import hu.blum.model.FieldType
import javafx.animation.AnimationTimer
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Button
import javafx.scene.image.Image
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.paint.Color
import javafx.stage.Stage
import java.awt.Toolkit
import java.lang.reflect.Field

class View(private val primaryStage: Stage){

    companion object{
        private val WIDTH:Double = Toolkit.getDefaultToolkit().screenSize.width/1920.0*800
        private val HEIGHT:Double = WIDTH
    }
    private var lastFrameTime: Long = System.nanoTime()
    private val viewModel = ViewModel()
    private lateinit var graphicsContext: GraphicsContext
    fun show(){
        createWindow()
    }


    private val debug = false

    fun tickAndRender(currentNanoTime: Long) {
        // the time elapsed since the last frame, in nanoseconds
        // can be used for physics calculation, etc
        val elapsedNanos = currentNanoTime - lastFrameTime
        lastFrameTime = currentNanoTime

        clearBoard()
        //drawBoard()
        drawEntities()

        if(debug){
            // display crude fps counter
            val elapsedMs = elapsedNanos / 1_000_000
            if (elapsedMs != 0L) {
                graphicsContext.fill = Color.WHITE
                graphicsContext.fillText("${1000 / elapsedMs} fps", 10.0, 10.0)
            }
        }
    }


    private val fieldSize:FieldCanvasSize by lazy{
        FieldCanvasSize(
            (WIDTH / viewModel.boardWidth ) ,
            (HEIGHT / viewModel.boardHeight)
        )
    }

    private fun createWindow(){
        primaryStage.title = "Dandelifeon simulation"
        primaryStage.isResizable = false
        val root = Group()
        val mainScene = Scene(root)

        val canvas = Canvas(WIDTH,HEIGHT)

        canvas.setOnMouseClicked {
            canvasClicked(
                (it.x/(canvas.width / viewModel.boardWidth)).toInt(),
                (it.y/(canvas.height / viewModel.boardHeight)).toInt()
            )
        }

        graphicsContext = canvas.graphicsContext2D

        val top = HBox()
        val btnPause = Button("Start")
        //val btnReset = Button("reset")
        val btnStep = Button("step")

        btnStep.onAction = EventHandler { viewModel.step() }
        btnPause.onAction = EventHandler {
            if(viewModel.isClockStopped()){
                btnPause.text = "Stop"
                btnStep.isDisable = true
            }else{
                btnPause.text = "Start"
                btnStep.isDisable = false
            }
            viewModel.pauseStart()
        }


        btnPause.padding = Insets(0.0,10.0,0.0,10.0)
        btnStep.padding = Insets(0.0,10.0,0.0,10.0)
        top.padding= Insets(10.0)
        top.spacing = 10.0
        top.children.add(btnPause)
        top.children.add(btnStep)

        val mainLayout = BorderPane()
        mainLayout.top = HBox(top)
        mainLayout.center = canvas

        root.children.add(mainLayout)

        primaryStage.scene = mainScene
        primaryStage.show()
    }

    fun canvasClicked(x:Int,y:Int){
        viewModel.cycleField(x,y)

    }

    private fun clearBoard(){
        graphicsContext.clearRect(0.0,0.0,WIDTH,HEIGHT)
    }
    private fun drawBoard(){
        graphicsContext.lineWidth = 0.5

        for (i in 0 .. viewModel.boardWidth){
            graphicsContext.moveTo(WIDTH/viewModel.boardHeight*i,0.0)
            graphicsContext.lineTo(WIDTH/viewModel.boardHeight*i,HEIGHT)
            graphicsContext.stroke()

        }
        for (i in 0 .. viewModel.boardHeight){
            graphicsContext.moveTo(0.0,HEIGHT/viewModel.boardWidth*i)
            graphicsContext.lineTo(WIDTH,HEIGHT/viewModel.boardWidth*i)
            graphicsContext.stroke()

        }
    }

    private fun drawEntities(){
        val state = viewModel.gameState.statuses

        for (i in state.indices){

            when(state[i]){
                FieldType.LIVE_CELL.ordinal-> drawCell(i%viewModel.boardWidth,i/viewModel.boardHeight)
                FieldType.DANDELIFEON.ordinal-> drawDandelifeon(i%viewModel.boardWidth,i/viewModel.boardHeight)
                FieldType.WALL.ordinal-> drawWall(i%viewModel.boardWidth,i/viewModel.boardHeight)
                FieldType.FINNISH.ordinal -> drawFinal(i%viewModel.boardWidth,i/viewModel.boardHeight)
                else->{}
            }
        }
    }

    private fun drawDandelifeon(x:Int, y:Int){
        graphicsContext.drawImage(
            Image(getResource("/Dandelifeon.png")),
            WIDTH / viewModel.boardWidth * (x),
            HEIGHT / viewModel.boardHeight * (y),
            fieldSize.width,
            fieldSize.height)
    }
    private fun drawCell(x:Int, y:Int){
        graphicsContext.fill= Color.LIGHTSEAGREEN
        graphicsContext.fillRect(WIDTH / viewModel.boardWidth * (x),
            HEIGHT / viewModel.boardHeight * (y),
            fieldSize.width,
            fieldSize.height)
    }
    private fun drawWall(x:Int, y:Int){
        graphicsContext.fill= Color.BLACK
        graphicsContext.fillRect(
            WIDTH / viewModel.boardWidth * (x),
            HEIGHT / viewModel.boardHeight * (y),
            fieldSize.width,
            fieldSize.height)
    }
    private fun drawFinal(x:Int,y:Int){
        graphicsContext.fill= Color.RED
        graphicsContext.fillRect(
            WIDTH / viewModel.boardWidth * (x),
            HEIGHT / viewModel.boardHeight * (y),
            fieldSize.width,
            fieldSize.height)
    }
    data class FieldCanvasSize(val width:Double, val height:Double)

}