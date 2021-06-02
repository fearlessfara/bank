package com.bok.bank.integration.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.15.0)",
    comments = "Source: bank.proto")
public final class BankGrpc {

  private BankGrpc() {}

  public static final String SERVICE_NAME = "Bank";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.bok.bank.integration.grpc.AccountCreationRequest,
      com.bok.bank.integration.grpc.AccountCreationResponse> getCreateAccountMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateAccount",
      requestType = com.bok.bank.integration.grpc.AccountCreationRequest.class,
      responseType = com.bok.bank.integration.grpc.AccountCreationResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bok.bank.integration.grpc.AccountCreationRequest,
      com.bok.bank.integration.grpc.AccountCreationResponse> getCreateAccountMethod() {
    io.grpc.MethodDescriptor<com.bok.bank.integration.grpc.AccountCreationRequest, com.bok.bank.integration.grpc.AccountCreationResponse> getCreateAccountMethod;
    if ((getCreateAccountMethod = BankGrpc.getCreateAccountMethod) == null) {
      synchronized (BankGrpc.class) {
        if ((getCreateAccountMethod = BankGrpc.getCreateAccountMethod) == null) {
          BankGrpc.getCreateAccountMethod = getCreateAccountMethod = 
              io.grpc.MethodDescriptor.<com.bok.bank.integration.grpc.AccountCreationRequest, com.bok.bank.integration.grpc.AccountCreationResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "Bank", "CreateAccount"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bok.bank.integration.grpc.AccountCreationRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bok.bank.integration.grpc.AccountCreationResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new BankMethodDescriptorSupplier("CreateAccount"))
                  .build();
          }
        }
     }
     return getCreateAccountMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.bok.bank.integration.grpc.AuthorizationRequest,
      com.bok.bank.integration.grpc.AuthorizationResponse> getAuthorizeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Authorize",
      requestType = com.bok.bank.integration.grpc.AuthorizationRequest.class,
      responseType = com.bok.bank.integration.grpc.AuthorizationResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bok.bank.integration.grpc.AuthorizationRequest,
      com.bok.bank.integration.grpc.AuthorizationResponse> getAuthorizeMethod() {
    io.grpc.MethodDescriptor<com.bok.bank.integration.grpc.AuthorizationRequest, com.bok.bank.integration.grpc.AuthorizationResponse> getAuthorizeMethod;
    if ((getAuthorizeMethod = BankGrpc.getAuthorizeMethod) == null) {
      synchronized (BankGrpc.class) {
        if ((getAuthorizeMethod = BankGrpc.getAuthorizeMethod) == null) {
          BankGrpc.getAuthorizeMethod = getAuthorizeMethod = 
              io.grpc.MethodDescriptor.<com.bok.bank.integration.grpc.AuthorizationRequest, com.bok.bank.integration.grpc.AuthorizationResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "Bank", "Authorize"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bok.bank.integration.grpc.AuthorizationRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bok.bank.integration.grpc.AuthorizationResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new BankMethodDescriptorSupplier("Authorize"))
                  .build();
          }
        }
     }
     return getAuthorizeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.bok.bank.integration.grpc.AccountInfoRequest,
      com.bok.bank.integration.grpc.AccountInfoResponse> getGetAccountInfoMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetAccountInfo",
      requestType = com.bok.bank.integration.grpc.AccountInfoRequest.class,
      responseType = com.bok.bank.integration.grpc.AccountInfoResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bok.bank.integration.grpc.AccountInfoRequest,
      com.bok.bank.integration.grpc.AccountInfoResponse> getGetAccountInfoMethod() {
    io.grpc.MethodDescriptor<com.bok.bank.integration.grpc.AccountInfoRequest, com.bok.bank.integration.grpc.AccountInfoResponse> getGetAccountInfoMethod;
    if ((getGetAccountInfoMethod = BankGrpc.getGetAccountInfoMethod) == null) {
      synchronized (BankGrpc.class) {
        if ((getGetAccountInfoMethod = BankGrpc.getGetAccountInfoMethod) == null) {
          BankGrpc.getGetAccountInfoMethod = getGetAccountInfoMethod = 
              io.grpc.MethodDescriptor.<com.bok.bank.integration.grpc.AccountInfoRequest, com.bok.bank.integration.grpc.AccountInfoResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "Bank", "GetAccountInfo"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bok.bank.integration.grpc.AccountInfoRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bok.bank.integration.grpc.AccountInfoResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new BankMethodDescriptorSupplier("GetAccountInfo"))
                  .build();
          }
        }
     }
     return getGetAccountInfoMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static BankStub newStub(io.grpc.Channel channel) {
    return new BankStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static BankBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new BankBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static BankFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new BankFutureStub(channel);
  }

  /**
   */
  public static abstract class BankImplBase implements io.grpc.BindableService {

    /**
     */
    public void createAccount(com.bok.bank.integration.grpc.AccountCreationRequest request,
        io.grpc.stub.StreamObserver<com.bok.bank.integration.grpc.AccountCreationResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getCreateAccountMethod(), responseObserver);
    }

    /**
     */
    public void authorize(com.bok.bank.integration.grpc.AuthorizationRequest request,
        io.grpc.stub.StreamObserver<com.bok.bank.integration.grpc.AuthorizationResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getAuthorizeMethod(), responseObserver);
    }

    /**
     */
    public void getAccountInfo(com.bok.bank.integration.grpc.AccountInfoRequest request,
        io.grpc.stub.StreamObserver<com.bok.bank.integration.grpc.AccountInfoResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetAccountInfoMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getCreateAccountMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.bok.bank.integration.grpc.AccountCreationRequest,
                com.bok.bank.integration.grpc.AccountCreationResponse>(
                  this, METHODID_CREATE_ACCOUNT)))
          .addMethod(
            getAuthorizeMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.bok.bank.integration.grpc.AuthorizationRequest,
                com.bok.bank.integration.grpc.AuthorizationResponse>(
                  this, METHODID_AUTHORIZE)))
          .addMethod(
            getGetAccountInfoMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.bok.bank.integration.grpc.AccountInfoRequest,
                com.bok.bank.integration.grpc.AccountInfoResponse>(
                  this, METHODID_GET_ACCOUNT_INFO)))
          .build();
    }
  }

  /**
   */
  public static final class BankStub extends io.grpc.stub.AbstractStub<BankStub> {
    private BankStub(io.grpc.Channel channel) {
      super(channel);
    }

    private BankStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BankStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new BankStub(channel, callOptions);
    }

    /**
     */
    public void createAccount(com.bok.bank.integration.grpc.AccountCreationRequest request,
        io.grpc.stub.StreamObserver<com.bok.bank.integration.grpc.AccountCreationResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCreateAccountMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void authorize(com.bok.bank.integration.grpc.AuthorizationRequest request,
        io.grpc.stub.StreamObserver<com.bok.bank.integration.grpc.AuthorizationResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getAuthorizeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getAccountInfo(com.bok.bank.integration.grpc.AccountInfoRequest request,
        io.grpc.stub.StreamObserver<com.bok.bank.integration.grpc.AccountInfoResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetAccountInfoMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class BankBlockingStub extends io.grpc.stub.AbstractStub<BankBlockingStub> {
    private BankBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private BankBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BankBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new BankBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.bok.bank.integration.grpc.AccountCreationResponse createAccount(com.bok.bank.integration.grpc.AccountCreationRequest request) {
      return blockingUnaryCall(
          getChannel(), getCreateAccountMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.bok.bank.integration.grpc.AuthorizationResponse authorize(com.bok.bank.integration.grpc.AuthorizationRequest request) {
      return blockingUnaryCall(
          getChannel(), getAuthorizeMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.bok.bank.integration.grpc.AccountInfoResponse getAccountInfo(com.bok.bank.integration.grpc.AccountInfoRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetAccountInfoMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class BankFutureStub extends io.grpc.stub.AbstractStub<BankFutureStub> {
    private BankFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private BankFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BankFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new BankFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bok.bank.integration.grpc.AccountCreationResponse> createAccount(
        com.bok.bank.integration.grpc.AccountCreationRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getCreateAccountMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bok.bank.integration.grpc.AuthorizationResponse> authorize(
        com.bok.bank.integration.grpc.AuthorizationRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getAuthorizeMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bok.bank.integration.grpc.AccountInfoResponse> getAccountInfo(
        com.bok.bank.integration.grpc.AccountInfoRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetAccountInfoMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CREATE_ACCOUNT = 0;
  private static final int METHODID_AUTHORIZE = 1;
  private static final int METHODID_GET_ACCOUNT_INFO = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final BankImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(BankImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CREATE_ACCOUNT:
          serviceImpl.createAccount((com.bok.bank.integration.grpc.AccountCreationRequest) request,
              (io.grpc.stub.StreamObserver<com.bok.bank.integration.grpc.AccountCreationResponse>) responseObserver);
          break;
        case METHODID_AUTHORIZE:
          serviceImpl.authorize((com.bok.bank.integration.grpc.AuthorizationRequest) request,
              (io.grpc.stub.StreamObserver<com.bok.bank.integration.grpc.AuthorizationResponse>) responseObserver);
          break;
        case METHODID_GET_ACCOUNT_INFO:
          serviceImpl.getAccountInfo((com.bok.bank.integration.grpc.AccountInfoRequest) request,
              (io.grpc.stub.StreamObserver<com.bok.bank.integration.grpc.AccountInfoResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class BankBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    BankBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.bok.bank.integration.grpc.BankProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Bank");
    }
  }

  private static final class BankFileDescriptorSupplier
      extends BankBaseDescriptorSupplier {
    BankFileDescriptorSupplier() {}
  }

  private static final class BankMethodDescriptorSupplier
      extends BankBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    BankMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (BankGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new BankFileDescriptorSupplier())
              .addMethod(getCreateAccountMethod())
              .addMethod(getAuthorizeMethod())
              .addMethod(getGetAccountInfoMethod())
              .build();
        }
      }
    }
    return result;
  }
}
