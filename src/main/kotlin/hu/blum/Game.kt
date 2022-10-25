package hu.blum

import hu.blum.view.GameView
import hu.blum.viewmodel.GameViewModel
import javafx.application.Application
import javafx.stage.Stage

class Game: Application() {

    private lateinit var view: GameView
    private lateinit var viewModel:GameViewModel

    override fun start(primaryStage: Stage) {

        viewModel = GameViewModel()
        view = GameView(primaryStage, viewModel)

        view.createWindow()
    }


}