package com.itechartgroup.avvinidiktov.PeopleFlow.kafka.producer;

import java.util.concurrent.ExecutionException;

public interface AbstractProducer<R, S> {
    S sendMessage(R obj) throws ExecutionException, InterruptedException;
}
