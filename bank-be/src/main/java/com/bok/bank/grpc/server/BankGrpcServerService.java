package com.bok.bank.grpc.server;

import com.bok.bank.integration.grpc.AccountCreationRequest;
import com.bok.bank.integration.grpc.AccountCreationResponse;
import com.bok.bank.integration.grpc.AuthorizationRequest;
import com.bok.bank.integration.grpc.AuthorizationResponse;
import com.bok.bank.integration.grpc.BankGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class BankGrpcServerService extends BankGrpc.BankImplBase {
    @Override
    public void createAccount(AccountCreationRequest request, StreamObserver<AccountCreationResponse> responseObserver) {
        super.createAccount(request, responseObserver);
    }

    @Override
    public void authorize(AuthorizationRequest request, StreamObserver<AuthorizationResponse> responseObserver) {
        super.authorize(request, responseObserver);
    }
}
