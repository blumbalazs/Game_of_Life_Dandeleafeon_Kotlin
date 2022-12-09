package hu.blum.view


import hu.blum.model.FieldType
import hu.blum.util.getResource
import hu.blum.viewmodel.ViewModel
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
import javafx.scene.text.Text
import javafx.stage.Stage
import java.awt.Toolkit

class View(private val primaryStage: Stage): GameStateListener {

    companion object{
        private val WIDTH:Double = Toolkit.getDefaultToolkit().screenSize.width/1920.0*800
        private val HEIGHT:Double = WIDTH
    }
    private var lastFrameTime: Long = System.nanoTime()
    private val viewModel = ViewModel().also { it.listener = this }
    private lateinit var graphicsContext: GraphicsContext
    fun show(){
        createWindow()
    }


    private val debug = false

    fun tickAndRender(currentNanoTime: Long) {

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


    private val fieldSize: FieldCanvasSize by lazy{
        FieldCanvasSize(
            (WIDTH / viewModel.boardWidth ) ,
            (HEIGHT / viewModel.boardHeight)
        )
    }

    private lateinit var btnPause: Button
    private lateinit var resultText:Text

    private fun createWindow(){
        primaryStage.title = "Dandelifeon simulation"
        primaryStage.isResizable = false
        val root = Group()
        val mainScene = Scene(root)

        val canvas = Canvas(WIDTH, HEIGHT)

        canvas.setOnMouseClicked {

            canvasClicked(
                (it.x/(canvas.width / viewModel.boardWidth)).toInt(),
                (it.y/(canvas.height / viewModel.boardHeight)).toInt()
            )
        }

        graphicsContext = canvas.graphicsContext2D

        val top = HBox()
        btnPause = Button("Start")
        //val btnReset = Button("reset")
        val btnStep = Button("step")
        resultText = Text("")
        val lastResult = Text("Last result:")
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
        top.children.add(lastResult)
        top.children.add(resultText)

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
        graphicsContext.clearRect(0.0,0.0, WIDTH, HEIGHT)
    }
    private fun drawBoard(){
        graphicsContext.lineWidth = 0.5

        for (i in 0 .. viewModel.boardWidth){
            graphicsContext.moveTo(WIDTH /viewModel.boardHeight*i,0.0)
            graphicsContext.lineTo(WIDTH /viewModel.boardHeight*i, HEIGHT)
            graphicsContext.stroke()

        }
        for (i in 0 .. viewModel.boardHeight){
            graphicsContext.moveTo(0.0, HEIGHT /viewModel.boardWidth*i)
            graphicsContext.lineTo(WIDTH, HEIGHT /viewModel.boardWidth*i)
            graphicsContext.stroke()

        }
    }

    private fun drawEntities(){
        val state = viewModel.gameState.state


        for (y in state.indices){
            for (x in state[y].indices){
                when(state[y][x].status){
                    FieldType.LIVE_CELL-> drawCell(x,y,state[y][x].age)
                    FieldType.DANDELIFEON-> drawDandelifeon(x,y)
                    FieldType.WALL-> drawWall(x,y)
                    FieldType.FINNISH -> drawFinal(x,y)
                    else->{}
                }
            }
        }

    }

    private fun drawDandelifeon(x:Int, y:Int){
        val coordinate = calculateCanvasCoordinateFromOriginalCoordinate(x,y)
        graphicsContext.drawImage(
            Image(getResource("/Dandelifeon.png")),
            coordinate.x,
            coordinate.y,
            fieldSize.width,
            fieldSize.height)
    }

    private fun drawCell(x:Int, y:Int,age:Int){
        graphicsContext.fill= Color.LIGHTSEAGREEN
        val coordinate = calculateCanvasCoordinateFromOriginalCoordinate(x,y)
        graphicsContext.fillRect(
            coordinate.x,
            coordinate.y,
            fieldSize.width,
            fieldSize.height)

        graphicsContext.fill = Color.WHITE
        graphicsContext.fillText(age.toString(), WIDTH / viewModel.boardWidth * (x) + 1 , HEIGHT / viewModel.boardHeight * (y) + viewModel.boardHeight/2)
    }
    private fun drawWall(x:Int, y:Int){
        graphicsContext.fill= Color.BLACK
        val coordinate = calculateCanvasCoordinateFromOriginalCoordinate(x,y)
        graphicsContext.fillRect(
            coordinate.x,
            coordinate.y,
            fieldSize.width,
            fieldSize.height)
    }
    private fun drawFinal(x:Int,y:Int){
        graphicsContext.fill= Color.RED
        val coordinate = calculateCanvasCoordinateFromOriginalCoordinate(x,y)
        graphicsContext.fillRect(
            coordinate.x,
            coordinate.y,
            fieldSize.width,
            fieldSize.height)
    }
    class FieldCanvasSize(val width:Double, val height:Double)
    class Coordinate(val x: Double,val y: Double)
    fun calculateCanvasCoordinateFromOriginalCoordinate(x: Int,y: Int): Coordinate {return Coordinate(
        WIDTH / viewModel.boardWidth * x,
        HEIGHT / viewModel.boardHeight * y)
    }
    override fun onGameEnded() {
        resultText.text = " ${viewModel.resultPercentage} % (${viewModel.lastBest}/${8*100*60})"
        Platform.runLater {
            btnPause.text = "Start"
        }
        viewModel.restart()
    }



}