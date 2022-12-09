package hu.blum.model

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class GameState(private val width:Int, private val height:Int) {

    var state = Array(height){Array(width){ FieldState()} }

    fun changeStatus(x:Int,y:Int,type:FieldType){
        state[y][x] = FieldState(type)
    }

    private var ended = false

    fun evolve():Boolean{
        val temp = IntArray(height*width){ FieldType.DEAD_CELL.ordinal }
        val tempAge = IntArray(height*width){ FieldType.DEAD_CELL.ordinal }
        val newState = Array(height){Array(width){ FieldState()} }
        runBlocking {
            for (y in 0 until height) {
                for (x in 0 until width) {

                    val result = calculateEvolve(x, y).await()

                    temp[y * height + x] = result.status.ordinal
                    tempAge[y * height + x] = result.age
                    newState[y][x] = result

                }
            }
        }

        state = newState

        return ended
    }

    fun calculateEvolve(x:Int,y:Int):Deferred<FieldState> = GlobalScope.async{
        val currentCell = state[y][x]

        if (currentCell.status != FieldType.LIVE_CELL && currentCell.status != FieldType.DEAD_CELL && currentCell.status != FieldType.FINNISH){
            return@async currentCell
        }

        var aliveNeighbours = 0
        var maxAge = 0
        for (i in -1 .. 1){
            for (j in -1 .. 1){
                if ((y+i in 0 until height) && (x+j in 0 until width)){
                    aliveNeighbours += when(state[y+i][x+j].status){
                        FieldType.LIVE_CELL -> 1
                        else -> 0
                    }
                    maxAge = state[y+i][x+j].age.coerceAtLeast((maxAge))
                }

            }
        }
        aliveNeighbours -= when(currentCell.status){
            FieldType.LIVE_CELL -> 1
            else -> 0
        }


        if ((currentCell.status == FieldType.LIVE_CELL) && (aliveNeighbours < 2))
            return@async FieldState(FieldType.DEAD_CELL)

        else if ((currentCell.status == FieldType.LIVE_CELL) && (aliveNeighbours > 3))
            return@async FieldState(FieldType.DEAD_CELL)

        else if ((currentCell.status == FieldType.DEAD_CELL || currentCell.status == FieldType.FINNISH) && (aliveNeighbours == 3)) {
            if(currentCell.status == FieldType.FINNISH){
                ended = true
                return@async FieldState(FieldType.FINNISH, 100.coerceAtMost(maxAge+1));
            }
            else
                return@async FieldState(FieldType.LIVE_CELL, 100.coerceAtMost(maxAge+1));
        }

        else {

            return@async FieldState(currentCell.status,if(currentCell.status==FieldType.LIVE_CELL) 100.coerceAtMost(currentCell.age+1) else 0)
        }
    }

    fun generatedPoints():Int{
        var points = 0;

        for (y in state.indices){
            for( x in state[y].indices){
                if(state[y][x].status == FieldType.FINNISH)
                    points+= state[y][x].age*60
            }
        }

        return points
    }


}