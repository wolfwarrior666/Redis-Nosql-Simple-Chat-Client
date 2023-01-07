package com.example.demo2


import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import io.lettuce.core.pubsub.RedisPubSubListener
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import java.util.Random


class HelloController {


    private var clientid = Random().nextInt(10)


    @FXML
    private lateinit var welcomeText: Label

    @FXML
    private lateinit var messagefield: TextArea

    @FXML
    private fun onHelloButtonClick() {
        //welcomeText.text = messagefield.text
        val config =  RedisURI.Builder.redis("172.16.202.132",6379).withDatabase(0).build()//RedisURI.create("redis://172.16.202.132:6379").database(1));
        val redisClient = RedisClient.create(config)
        val connection: StatefulRedisPubSubConnection<String, String> = redisClient.connectPubSub()
        val async = connection.async()
        val tmp = "Client "+clientid+":"+messagefield.text.toString()
        println("send $tmp")
        async.publish("chat", tmp)
    }

    @FXML
    private fun startListener() {
        welcomeText.text = "Listener Started on Client $clientid"
        val thread= Thread {
            class Listener : RedisPubSubListener<String?, String?> {
                override fun message(s: String?, s2: String?) {
                    Platform.setImplicitExit(false)
                    //Update GUI
                    Platform.runLater {
                        welcomeText.text = welcomeText.text.toString() + "\n"+ s2
                        messagefield.clear()
                    }
                }
                override fun message(s: String?, k1: String?, s2: String?) {}
                override fun subscribed(s: String?, l: Long) {}
                override fun psubscribed(s: String?, l: Long) {}
                override fun unsubscribed(s: String?, l: Long) {}
                override fun punsubscribed(s: String?, l: Long) {}
            }
            // Erstelle Verbindung zu Redis
            val redisClient = RedisClient.create("redis://172.16.202.132:6379")
            val connection = redisClient.connectPubSub()
            //Erstelle Listener und übergbe interface isntance
            connection.addListener(Listener())
            val async = connection.async()
            //Define channel
            async.subscribe("chat")
        }
        thread.start()
    }
}