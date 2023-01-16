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


class ChatClientController {
    private val host = "172.16.202.132"
    private val port = 6379

    private var clientid = Random().nextInt(10)

    @FXML
    private lateinit var welcomeText: Label

    @FXML
    private lateinit var messagefield: TextArea

    @FXML
    private fun onHelloButtonClick() {
        //Create Redis Connection in GUI Thread
        val config = RedisURI.Builder.redis(host, port).withDatabase(0).build()
        val redisClient = RedisClient.create(config)
        val connection: StatefulRedisPubSubConnection<String, String> = redisClient.connectPubSub()
        val async = connection.async()
        val tmp = "Client " + clientid + ": " + messagefield.text.toString()
        println("send $tmp")
        async.publish("chat", tmp)
        messagefield.clear() //Clear MessageInput field when Message is send
    }

    @FXML
    private fun startListener() {
        welcomeText.text = "Listener Started on Client $clientid"
        val thread = Thread {
            class Listener : RedisPubSubListener<String?, String?> {
                //Code witch should be executed if a message arrived
                override fun message(s: String?, s2: String?) {
                    Platform.setImplicitExit(false)
                    //Update GUI in non GUI Thread
                    Platform.runLater {
                        welcomeText.text = welcomeText.text.toString() + "\n $s2"
                    }
                }

                override fun message(s: String?, k1: String?, s2: String?) {}
                override fun subscribed(s: String?, l: Long) {
                    println("Subscribe successfully to channel: $s")
                }
                override fun psubscribed(s: String?, l: Long) {}
                override fun unsubscribed(s: String?, l: Long) {}
                override fun punsubscribed(s: String?, l: Long) {}
            }
            //Create Connection to Redis in separated NonGui Thread
            val redisClient = RedisClient.create("redis://172.16.202.132:6379")
            val connection = redisClient.connectPubSub()
            //Create Listener Instance and pass them to the Connection
            connection.addListener(Listener())
            val async = connection.async()
            //Define channel
            async.subscribe("chat")
        }
        thread.start()
    }
}