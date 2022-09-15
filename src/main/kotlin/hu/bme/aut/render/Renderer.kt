package hu.bme.aut.render

import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.KeyCode

interface Renderer {
    fun render(graphicsContext: GraphicsContext)
    fun keyPressHandle(keycode:KeyCode)
}