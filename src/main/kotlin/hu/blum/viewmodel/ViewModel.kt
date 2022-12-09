package hu.blum.viewmodel

import hu.blum.clock.SecondTimer
import hu.blum.clock.TimerState
import hu.blum.model.FieldType
import hu.blum.model.GameState
import hu.blum.view.GameStateListener

class ViewModel {

    val boardWidth
        get() = 27
    val boardHeight
        get() = 27

    var gameState = GameState(boardWidth,boardHeight)

    private val timer = SecondTimer(::step)

    lateinit var listener: GameStateListener

    var lastBest = 0
    val resultPercentage
        get() = lastBest.toDouble()/(8*100*60)*100

    init {
        initialiseBoard()
    }

    fun restart(){
        gameState = GameState(boardWidth,boardHeight)
        initialiseBoard()
    }

    fun initialiseBoard(){
        addWalls()


        gameState.changeStatus(boardWidth/2,boardHeight/2,FieldType.DANDELIFEON)
        gameState.changeStatus(boardWidth/2-1,boardHeight/2-1,FieldType.FINNISH)
        gameState.changeStatus(boardWidth/2-1,boardHeight/2,FieldType.FINNISH)
        gameState.changeStatus(boardWidth/2-1,boardHeight/2+1,FieldType.FINNISH)
        gameState.changeStatus(boardWidth/2+1,boardHeight/2-1,FieldType.FINNISH)
        gameState.changeStatus(boardWidth/2+1,boardHeight/2,FieldType.FINNISH)
        gameState.changeStatus(boardWidth/2+1,boardHeight/2+1,FieldType.FINNISH)
        gameState.changeStatus(boardWidth/2,boardHeight/2+1,FieldType.FINNISH)
        gameState.changeStatus(boardWidth/2,boardHeight/2-1,FieldType.FINNISH)
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
        when(gameState.state[y][x].status){
            FieldType.DEAD_CELL->{gameState.changeStatus(x,y,FieldType.LIVE_CELL)}
            FieldType.LIVE_CELL->{gameState.changeStatus(x,y,FieldType.WALL)}
            FieldType.WALL->{gameState.changeStatus(x,y,FieldType.DEAD_CELL)}
            else->{}
        }
    }

    fun step(){

        val gameEnded = gameState.evolve()

        if(gameEnded){
            timer.stop()

            lastBest = gameState.generatedPoints()

            listener.onGameEnded()
        }

    }

    fun isClockStopped():Boolean{
        return timer.state == TimerState.STOPPED
    }




}