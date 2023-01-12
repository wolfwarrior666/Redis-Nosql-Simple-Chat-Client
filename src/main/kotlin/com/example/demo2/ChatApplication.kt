package com.example.demo2


import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class HelloApplication : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("chat-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 320.0, 600.0)
        stage.title = "SimpleRedisChatMessenger!"
        stage.scene = scene
        stage.show()
    }
}

fun main() {
    Application.launch(HelloApplication::class.java)
}