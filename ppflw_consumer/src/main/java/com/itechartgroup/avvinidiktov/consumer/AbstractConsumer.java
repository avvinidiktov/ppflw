package com.itechartgroup.avvinidiktov.consumer;

public interface AbstractConsumer<R, S> {
    S consume(R obj);
}
