package com.itechartgroup.avvinidiktov.producer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface AbstractProducer<R, S> {
    S sendMessage(R obj) throws ExecutionException, InterruptedException, TimeoutException;
}
