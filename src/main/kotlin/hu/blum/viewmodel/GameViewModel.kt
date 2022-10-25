package hu.blum.viewmodel

import hu.blum.view.View
import javafx.animation.AnimationTimer
import java.util.Timer

class GameViewModel {
    private val views:MutableList<View> = mutableListOf()
    private  lateinit var timer: AnimationTimer
    fun addView(view:View){
        views.add(view)
    }

    fun step(){


        invalidateViews()
    }

    fun start(){
        timer.start()
    }

    fun stop(){
        timer.stop()
    }

    private fun createTimer(){
        timer = object: AnimationTimer() {
            override fun handle(now: Long) {
                TODO("Not yet implemented")
            }

        }
    }

    private fun invalidateViews(){
        views.forEach{
            it.invalidate()
        }
    }
}