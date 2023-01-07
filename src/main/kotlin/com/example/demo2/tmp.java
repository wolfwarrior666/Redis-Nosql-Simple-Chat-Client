package com.example.demo2;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.pubsub.RedisPubSubListener;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import io.lettuce.core.pubsub.api.sync.RedisPubSubCommands;
import javafx.application.Platform;

public class tmp  {
    public Runnable get(){
        return new Runnable() {
            @Override
            public void run() {

                class listener implements RedisPubSubListener<String,String> {

                    @Override
                    public void message(String s, String s2) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("works");
                            }
                        });
                    }

                    @Override
                    public void message(String s, String k1, String s2) {

                    }

                    @Override
                    public void subscribed(String s, long l) {

                    }

                    @Override
                    public void psubscribed(String s, long l) {

                    }

                    @Override
                    public void unsubscribed(String s, long l) {

                    }

                    @Override
                    public void punsubscribed(String s, long l) {

                    }
                }
                // Erstelle eine Verbindung zu Redis
                RedisClient redisClient = RedisClient.create("redis://172.16.202.132:6379");
               // StatefulRedisConnection<String, String> connection = redisClient.connect();



                StatefulRedisPubSubConnection<String, String> connection
                        = redisClient.connectPubSub();
                connection.addListener(new listener());

                RedisPubSubAsyncCommands<String, String> async = connection.async();
                async.subscribe("chat");
            }
            };

    }
}
