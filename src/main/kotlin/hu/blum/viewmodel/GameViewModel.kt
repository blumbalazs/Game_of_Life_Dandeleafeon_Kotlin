package hu.blum.viewmodel

import hu.blum.model.Coordinate
import hu.blum.model.Field
import hu.blum.model.FieldType
import hu.blum.view.View
import java.util.*

class GameViewModel {
    val boardWidth
        get() = 27
    val boardHeight
        get() = 27
    private val views:MutableList<View> = mutableListOf()
    private var timer: Timer = Timer(true)
    private val refresh: TimerTask = object: TimerTask() {
        override fun run() {
            step()
        }
    }

    private var board: Array<Array<FieldType>> = Array(boardHeight){Array(boardWidth){ FieldType.DEAD_CELL } }

    init {
        board[board.size/2][board[0].size/2] = FieldType.DANDELIFEON
        board[1][1] = FieldType.LIVE_CELL
        addBorderWallstoBoard(board)
    }

    fun addView(view:View){
        views.add(view)
    }


    private fun addBorderWallstoBoard(board:Array<Array<FieldType>>){
        for (y in board.indices){
            board[y][0] = FieldType.WALL
            board[y][board[y].size-1] = FieldType.WALL
        }
        for (x in board[0].indices){
            board[0][x] = FieldType.WALL
        }
        for (x in board[board.size-1].indices){
            board[board.size-1][x] = FieldType.WALL
        }
    }
    fun step(){

        cycle()

        invalidateViews()
    }
    private fun cycle(){
        val nextState:Array<Array<FieldType>> = Array(boardHeight){Array(boardWidth){ FieldType.DEAD_CELL } }
        nextState[board.size/2][board[0].size/2] = FieldType.DANDELIFEON

        for (y in board.indices){
            for (x in board.indices){

            }
        }
    }

    fun start(){
        timer.scheduleAtFixedRate(refresh,0,1000)
    }

    fun stop(){
        timer.cancel()
    }

    private fun invalidateViews(){
        views.forEach{
            it.invalidate()
        }
    }
    fun getBoardState():List<Field>{
        val fields:MutableList<Field> = mutableListOf()
        for (y in board.indices){
            for (x in board[y].indices){
                fields.add(Field(Coordinate(x,y),board[y][x]))
            }
        }
        return fields.toList()
    }
}