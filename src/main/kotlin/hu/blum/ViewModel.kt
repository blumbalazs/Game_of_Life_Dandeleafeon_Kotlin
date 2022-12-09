package hu.blum

import hu.blum.clock.SecondTimer
import hu.blum.clock.TimerState
import hu.blum.model.FieldState
import hu.blum.model.FieldType
import hu.blum.model.GameState
import kotlinx.coroutines.*

class ViewModel {

    val boardWidth
        get() = 27
    val boardHeight
        get() = 27

    lateinit var view: View
    var gameState = GameState(boardWidth,boardHeight)

    private val timer = SecondTimer(::step)

    init {
        addWalls()

        gameState.changeStatus(3,1,FieldType.LIVE_CELL)
        gameState.changeStatus(3,2,FieldType.LIVE_CELL)
        gameState.changeStatus(3,3,FieldType.LIVE_CELL)

    }

    private fun addWalls(){
        for (y in 0 until boardHeight){
            gameState.changeStatus(0,y,FieldType.WALL)
            gameState.changeStatus(boardWidth-1,y,FieldType.WALL)
        }


        for (x in 0 until boardWidth){

            gameState.changeStatus(x,0,FieldType.WALL)
            gameState.changeStatus(x,boardHeight-1,FieldType.WALL)

        }
    }

    fun pauseStart(){
        when(timer.state){
            TimerState.STOPPED -> timer.start()
            TimerState.RUNNING -> timer.stop()
            else ->{}
        }

    }

    fun cycleField(x:Int,y:Int){
        when(gameState.getStatus(x,y)){
            FieldType.DEAD_CELL.ordinal->{gameState.changeStatus(x,y,FieldType.LIVE_CELL)}
            FieldType.LIVE_CELL.ordinal->{gameState.changeStatus(x,y,FieldType.WALL)}
            FieldType.WALL.ordinal->{gameState.changeStatus(x,y,FieldType.DEAD_CELL)}
            else->{}
        }
        view.onUpdate()
    }

    fun step(){

        gameState.evolve()
        view.onUpdate()

    }
    fun getState():IntArray{
        return gameState.statuses
    }

    fun isClockStopped():Boolean{
        return timer.state == TimerState.STOPPED
    }

}