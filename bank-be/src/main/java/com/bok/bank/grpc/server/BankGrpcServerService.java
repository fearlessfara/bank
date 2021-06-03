package com.bok.bank.grpc.server;

import com.bok.bank.helper.AccountHelper;
import com.bok.bank.integration.grpc.AccountCreationCheckRequest;
import com.bok.bank.integration.grpc.AccountCreationCheckResponse;
import com.bok.bank.integration.grpc.AuthorizationRequest;
import com.bok.bank.integration.grpc.AuthorizationResponse;
import com.bok.bank.integration.grpc.BankGrpc;
import com.google.common.base.Preconditions;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class BankGrpcServerService extends BankGrpc.BankImplBase {

    @Autowired
    AccountHelper accountHelper;


    @Override
    public void authorize(AuthorizationRequest request, StreamObserver<AuthorizationResponse> responseObserver) {
        super.authorize(request, responseObserver);
    }

    @Override
    public void accountCreationCheck(AccountCreationCheckRequest request, StreamObserver<AccountCreationCheckResponse> responseObserver) {
        Preconditions.checkArgument(StringUtils.isNotBlank(request.getFiscalCode()) || StringUtils.isNotBlank(request.getVatNumber()), "fiscalCode and VatNumber fields are blank");
        AccountCreationCheckResponse.Builder responseBuilder = AccountCreationCheckResponse.newBuilder();
        responseBuilder.setPermitted(accountHelper.canCreate(request));
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
}
