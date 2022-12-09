package hu.blum.clock

import java.util.*


class SecondTimer(val task:()->Unit) {


    private lateinit var timer:Timer


    var state:TimerState = TimerState.STOPPED
        private set

    fun start(){

        timer = Timer(true)

        val taskToRun = object: TimerTask() {
            override fun run() {
                task()
            }

        }

        timer.scheduleAtFixedRate(taskToRun,0,1000)

        state = TimerState.RUNNING
    }
    fun stop(){
        timer.cancel()
        state = TimerState.STOPPED
    }

}