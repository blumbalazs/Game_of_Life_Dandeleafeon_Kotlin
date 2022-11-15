package hu.blum.viewmodel

import hu.blum.model.Coordinate
import hu.blum.model.Field
import hu.blum.model.FieldType
import hu.blum.model.FieldViewModel
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

    private var board: Array<Array<Field>> = Array(boardHeight){Array(boardWidth){ Field(FieldType.DEAD_CELL,0) } }

    init {
        board[board.size/2][board[0].size/2].type = FieldType.DANDELIFEON
        board[2][1].type = FieldType.LIVE_CELL
        board[2][2].type = FieldType.LIVE_CELL
        board[2][3].type = FieldType.LIVE_CELL
        addBorderWallsToBoard()
    }

    fun addView(view:View){
        views.add(view)
    }


    private fun addBorderWallsToBoard(){
        for (y in board.indices){
            board[y][0].type = FieldType.WALL
            board[y][board[y].size-1].type = FieldType.WALL
        }
        for (x in board[0].indices){
            board[0][x].type = FieldType.WALL
        }
        for (x in board[board.size-1].indices){
            board[board.size-1][x].type = FieldType.WALL
        }
    }
    fun step(){

        cycle()

        invalidateViews()
    }
    private fun cycle(){
        val nextState:Array<Array<Field>> = Array(boardHeight){Array(boardWidth){Field(FieldType.DEAD_CELL,0) } }

        for (y in board.indices){
            for (x in board.indices){
                nextState[y][x] = EvolveCell(Coordinate(x,y))
            }
        }
        board = nextState
    }
    private fun EvolveCell(coordinate: Coordinate):Field{

        val field:Field = board[coordinate.y][coordinate.x]

        when(field.type){
            FieldType.LIVE_CELL ->{
                val liveNeighbours = numberofLiveNeighbours(coordinate)
                if(2==liveNeighbours || liveNeighbours==3)
                    field.increaseAge()
                else
                    return Field(FieldType.DEAD_CELL,0)
                return field
            }
            FieldType.DEAD_CELL ->{
                val liveNeighbours = numberofLiveNeighbours(coordinate)
                if(3 == liveNeighbours)
                    return Field(FieldType.LIVE_CELL,numberofLiveNeighbours(coordinate)+1)
                return field
            }

            else -> return field
        }
    }
    private fun numberofLiveNeighbours(coordinate: Coordinate):Int{
        var n = 0

        if(board[coordinate.y-1][coordinate.x+1].type == FieldType.LIVE_CELL)
            n++
        if(board[coordinate.y-1][coordinate.x].type == FieldType.LIVE_CELL)
            n++
        if(board[coordinate.y-1][coordinate.x-1].type == FieldType.LIVE_CELL)
            n++
        if(board[coordinate.y][coordinate.x-1].type == FieldType.LIVE_CELL)
            n++
        if(board[coordinate.y+1][coordinate.x-1].type == FieldType.LIVE_CELL)
            n++
        if(board[coordinate.y+1][coordinate.x].type == FieldType.LIVE_CELL)
            n++
        if(board[coordinate.y+1][coordinate.x+1].type == FieldType.LIVE_CELL)
            n++
        if(board[coordinate.y][coordinate.x+1].type == FieldType.LIVE_CELL)
            n++

        return n
    }
    private fun maxNeighbourAge(coordinate: Coordinate):Int{
        var n = 0
        n = Math.max(n,board[coordinate.y-1][coordinate.x+1].age)
        n = Math.max(n,board[coordinate.y-1][coordinate.x].age)
        n = Math.max(n,board[coordinate.y-1][coordinate.x-1].age)
        n = Math.max(n,board[coordinate.y][coordinate.x-1].age)
        n = Math.max(n,board[coordinate.y+1][coordinate.x-1].age)
        n = Math.max(n,board[coordinate.y+1][coordinate.x].age)
        n = Math.max(n,board[coordinate.y+1][coordinate.x+1].age)
        n = Math.max(n,board[coordinate.y][coordinate.x+1].age)
        return n
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
    fun getBoardState():List<FieldViewModel>{
        val fields:MutableList<FieldViewModel> = mutableListOf()
        for (y in board.indices){
            for (x in board[y].indices){
                fields.add(FieldViewModel(Coordinate(x,y),board[y][x].type))
            }
        }
        return fields.toList()
    }
}