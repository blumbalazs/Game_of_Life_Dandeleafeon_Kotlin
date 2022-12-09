package hu.blum.model

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class GameState(val width:Int, val height:Int) {

    var statuses = IntArray(height*width){ FieldType.DEAD_CELL.ordinal }

    fun changeStatus(x:Int,y:Int,type:FieldType){
        statuses[y*height+x] = type.ordinal
    }

    fun evolve(){
        val temp = IntArray(height*width){ FieldType.DEAD_CELL.ordinal }
        runBlocking {
            for (y in 0 until height) {
                for (x in 0 until width) {

                    temp[y * height + x] = calculateEvolve(x, y).await()

                }
            }
        }

        statuses = temp
    }

    fun calculateEvolve(x:Int,y:Int) = GlobalScope.async{
        val currentStatus = getStatus(x,y)

        if (currentStatus != FieldType.LIVE_CELL.ordinal && currentStatus != FieldType.DEAD_CELL.ordinal){
            return@async currentStatus

        }

        var aliveNeighbours = 0;
        for (i in -1 .. 1){
            for (j in -1 .. 1){
                if ((y+i in 0 until height) && (x+j in 0 until width)){
                    aliveNeighbours += when(statuses[(y+i)*height+x+j]){
                        FieldType.LIVE_CELL.ordinal -> 1
                        else -> 0
                    }

                }

            }
        }
        aliveNeighbours -= when(currentStatus){
            FieldType.LIVE_CELL.ordinal -> 1
            else -> 0
        }


        if ((currentStatus == FieldType.LIVE_CELL.ordinal) && (aliveNeighbours < 2))
            return@async FieldType.DEAD_CELL.ordinal

        else if ((currentStatus == FieldType.LIVE_CELL.ordinal) && (aliveNeighbours > 3))
            return@async FieldType.DEAD_CELL.ordinal

        else if ((currentStatus == FieldType.DEAD_CELL.ordinal) && (aliveNeighbours == 3))
            return@async FieldType.LIVE_CELL.ordinal;

        else
            return@async currentStatus
    }

    fun getStatus(x:Int,y:Int) = statuses[y*height+x]


}