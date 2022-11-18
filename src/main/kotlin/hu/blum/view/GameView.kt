package hu.blum.view

import com.example.getResource
import hu.blum.model.Coordinate
import hu.blum.model.FieldType
import hu.blum.viewmodel.GameViewModel
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


class GameView(private val primaryStage: Stage, private val viewModel: GameViewModel) :View{

    private val WIDTH:Number = Toolkit.getDefaultToolkit().screenSize.width/1920.0*800
    private val HEIGHT:Number = WIDTH

    private val NUMBEROFCELLSINROW:Int = 25

    private lateinit var graphicsContext: GraphicsContext
    private lateinit var mainScene: Scene
    private val fieldWidthInCells:Int
    private val fieldHeightInCells:Int
    init {
        viewModel.addView(this)
        fieldWidthInCells = viewModel.boardWidth
        fieldHeightInCells = viewModel.boardHeight
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
        val btnPause = Button("pause")
        //val btnReset = Button("reset")
        val btnStep = Button("step")

        btnStep.onAction = EventHandler { viewModel.step()}
        btnPause.onAction = EventHandler { viewModel.pause() }


        btnPause.padding = Insets(0.0,10.0,0.0,10.0)
        //btnReset.padding = Insets(0.0,10.0,0.0,10.0)
        btnStep.padding = Insets(0.0,10.0,0.0,10.0)
        top.padding= Insets(10.0)
        top.spacing = 10.0
        top.children.add(btnPause)
        //top.children.add(btnReset)
        top.children.add(btnStep)

        val mainLayout: BorderPane = BorderPane()
        mainLayout.top = HBox(top)
        mainLayout.center = canvas

        root.children.add(mainLayout)

        primaryStage.scene = mainScene
        primaryStage.show()
        invalidate()
    }

    override fun invalidate(){
        graphicsContext.clearRect(0.0,0.0,WIDTH.toDouble(),HEIGHT.toDouble())
        reDraw()
    }

    private fun reDraw(){
        drawLines()
        drawEntities()
    }

    private fun drawLines(){
        graphicsContext.lineWidth = 0.5

        for (i in 0 .. fieldWidthInCells){
            graphicsContext.moveTo(WIDTH.toDouble()/fieldHeightInCells*i,0.0)
            graphicsContext.lineTo(WIDTH.toDouble()/fieldHeightInCells*i,HEIGHT.toDouble())
            graphicsContext.stroke()

        }
        for (i in 0 .. fieldHeightInCells){
            graphicsContext.moveTo(0.0,HEIGHT.toDouble()/fieldWidthInCells*i)
            graphicsContext.lineTo(WIDTH.toDouble(),HEIGHT.toDouble()/fieldWidthInCells*i)
            graphicsContext.stroke()

        }
    }
    private fun drawEntities(){
        val entities = viewModel.getBoardState()

        for(entity in entities){
            when(entity.type){
                FieldType.LIVE_CELL -> drawCell(entity.coordinate)
                FieldType.WALL -> drawWall(entity.coordinate)
                FieldType.DANDELIFEON -> drawDandelifeon(entity.coordinate)

                else -> {}
            }
        }

    }
    data class CanvasCoordinate(val x:Double,val y:Double)
    data class FieldCanvasSize(val width:Double, val height:Double)
    private fun calculateViewCanvasCoordinate(coordinate: Coordinate):CanvasCoordinate =
        CanvasCoordinate(
            WIDTH.toDouble()/fieldWidthInCells*(coordinate.x),
            HEIGHT.toDouble()/fieldHeightInCells*(coordinate.y)
        )
    private fun calculateFieldCanvasSize()=FieldCanvasSize(WIDTH.toDouble()/fieldWidthInCells,HEIGHT.toDouble()/fieldHeightInCells)
    private fun drawDandelifeon(positon: Coordinate){
        val coordinate = calculateViewCanvasCoordinate(positon)
        val fieldSize = calculateFieldCanvasSize()
        graphicsContext.drawImage(
            Image(getResource("/Dandelifeon.png")),
            coordinate.x,
            coordinate.y,
            fieldSize.width,
            fieldSize.height)
    }
    private fun drawCell(positon: Coordinate){
        graphicsContext.fill= Color.LIGHTSEAGREEN
        val coordinate = calculateViewCanvasCoordinate(positon)
        val fieldSize = calculateFieldCanvasSize()
        graphicsContext.fillRect(coordinate.x,coordinate.y,fieldSize.width,fieldSize.height)
    }
    private fun drawWall(positon: Coordinate){
        graphicsContext.fill= Color.BLACK
        val coordinate = calculateViewCanvasCoordinate(positon)
        val fieldSize = calculateFieldCanvasSize()
        graphicsContext.fillRect(coordinate.x,coordinate.y,fieldSize.width,fieldSize.height)
    }
}