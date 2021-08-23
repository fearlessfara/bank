package com.bok.bank.grpc.server;

import com.bok.bank.helper.AccountHelper;
import com.bok.bank.integration.dto.AuthorizationRequestDTO;
import com.bok.bank.integration.dto.AuthorizationResponseDTO;
import com.bok.bank.integration.grpc.AccountCreationCheckRequest;
import com.bok.bank.integration.grpc.AccountCreationCheckResponse;
import com.bok.bank.integration.grpc.AuthorizationRequest;
import com.bok.bank.integration.grpc.AuthorizationResponse;
import com.bok.bank.integration.grpc.BankGrpc;
import com.bok.bank.integration.service.BankAccountController;
import com.bok.bank.integration.service.TransactionController;
import com.bok.bank.integration.util.Money;
import com.google.common.base.Preconditions;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;
import java.util.UUID;

@GrpcService
public class BankGrpcServerService extends BankGrpc.BankImplBase {

    @Autowired
    AccountHelper accountHelper;
    @Autowired
    TransactionController transactionController;


    @Override
    public void authorize(AuthorizationRequest request, StreamObserver<AuthorizationResponse> responseObserver) {
        AuthorizationResponseDTO authorization = transactionController.authorize(request.getAccountId(), new AuthorizationRequestDTO(request.getAccountId(), UUID.fromString(request.getExtTransactionId()), new Money(Currency.getInstance(request.getMoney().getCurrency().name()), BigDecimal.valueOf(request.getMoney().getAmount())) , request.getFromMarket()));
        AuthorizationResponse.Builder responseBuilder = AuthorizationResponse.newBuilder();
        responseBuilder.setAuthorized(authorization.authorized);
        if(Objects.nonNull(authorization.extTransactionId)) {
            responseBuilder.setAuthorizationId(authorization.extTransactionId.toString());
        }
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
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
