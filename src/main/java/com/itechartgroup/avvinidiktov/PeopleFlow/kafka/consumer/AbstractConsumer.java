package com.itechartgroup.avvinidiktov.PeopleFlow.kafka.consumer;

public interface AbstractConsumer<R, S> {
    S consume(R obj);
}
