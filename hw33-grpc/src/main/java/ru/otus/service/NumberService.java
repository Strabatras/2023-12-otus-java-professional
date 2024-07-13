package ru.otus.service;

import io.grpc.stub.StreamObserver;

public interface NumberService {
    void runSequence(NumberRequest request, StreamObserver<NumberResponse> responseObserver);
}
