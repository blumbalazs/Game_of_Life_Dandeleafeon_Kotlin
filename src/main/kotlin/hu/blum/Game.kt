package hu.blum

import javafx.application.Application
import javafx.stage.Stage

class Game: Application() {

    override fun start(primaryStage: Stage) {

        View(primaryStage).show()

    }


}