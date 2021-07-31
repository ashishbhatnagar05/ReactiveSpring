package com.akka.example.counter;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class CounterActor extends UntypedActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private int count = 0;

    @Override
    public void onReceive(Object message) throws Throwable {

        if ("Stop".equals(message)) {
            getContext().stop(getSelf());
        }
        if ("Count".equals(message)) {
            getSender().tell(count, getSelf());
        }
        count++;
        log.info("Received: {}", message);
    }

    public int getCount() {
        return count;
    }
}
