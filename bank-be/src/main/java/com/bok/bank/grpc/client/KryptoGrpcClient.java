package com.bok.bank.grpc.client;

import com.bok.krypto.integration.grpc.KryptoGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Component
public class KryptoGrpcClient {

    @GrpcClient("krypto:3030")
    KryptoGrpc.KryptoBlockingStub kryptoBlockingStub;

    @GrpcClient("krypto:3030")
    KryptoGrpc.KryptoFutureStub kryptoFutureStub;

    public void exampleBlockingRPC() {
        //HelloReply response = stub.sayHello(HelloRequest.newBuilder().setName(name).build());
    }
}
