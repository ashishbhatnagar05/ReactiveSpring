package com.akka.example.basics;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class SimpleActorLiveApp {

    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create();
        ActorRef actorRef = actorSystem.actorOf(Props.create(SimpleActorLive.class), "simple-actor-demo");
        actorRef.tell("hello", null);
    }
}
