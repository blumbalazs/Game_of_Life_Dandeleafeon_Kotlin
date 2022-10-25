package hu.blum.viewmodel

import hu.blum.view.View
import javafx.animation.AnimationTimer
import java.util.*

class GameViewModel {
    private val views:MutableList<View> = mutableListOf()
    private var timer: Timer = Timer(true)
    private val refresh: TimerTask = object: TimerTask() {
        override fun run() {
            step()
        }
    }

    fun addView(view:View){
        views.add(view)
    }

    fun step(){


        invalidateViews()
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
}