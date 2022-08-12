package com.bryanmullen.interceptors;

import io.grpc.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerInterceptor implements io.grpc.ServerInterceptor {
    Logger logger = LoggerFactory.getLogger(ServerInterceptor.class); //

    @Override
    public <ReqT, RespT> io.grpc.ServerCall.Listener<ReqT> interceptCall(io.grpc.ServerCall<ReqT, RespT> call, io.grpc.Metadata headers, io.grpc.ServerCallHandler<ReqT, RespT> next) {
        String sb = "Metadata:\n" +
                headers.get(Metadata.Key.of("HOSTNAME", Metadata.ASCII_STRING_MARSHALLER)) +
                "\n" +
                headers.get(Metadata.Key.of("TIME_OF_REQUEST", Metadata.ASCII_STRING_MARSHALLER)) +
                "\n";
        logger.info(sb);
        return next.startCall(call, headers);
    }
}