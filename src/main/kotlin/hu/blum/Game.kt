package hu.blum

import hu.blum.view.View
import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.stage.Stage

class Game: Application() {

    override fun start(primaryStage: Stage) {
        val view = View(primaryStage)
        object : AnimationTimer() {
            override fun handle(currentNanoTime: Long) {
                view.tickAndRender(currentNanoTime)
            }
        }.start()

        view.show()
    }


}