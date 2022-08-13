package com.bryanmullen.interceptors;

import io.grpc.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ServerInterceptor is a gRPC interceptor that logs the hostname of the client performing the request. This is useful
 * for debugging purposes.
 */
public class ServerInterceptor implements io.grpc.ServerInterceptor {
    // Logger for this class
    Logger logger = LoggerFactory.getLogger(ServerInterceptor.class); //

    @Override
    public <ReqT, RespT> io.grpc.ServerCall.Listener<ReqT> interceptCall(io.grpc.ServerCall<ReqT, RespT> call, io.grpc.Metadata headers, io.grpc.ServerCallHandler<ReqT, RespT> next) {
        // String Builder for the hostname and timestamp
        String sb = "Metadata:\n" +
                headers.get(Metadata.Key.of("HOSTNAME", Metadata.ASCII_STRING_MARSHALLER)) +
                "\n" +
                headers.get(Metadata.Key.of("TIME_OF_REQUEST", Metadata.ASCII_STRING_MARSHALLER)) +
                "\n";

        // Log the hostname and timestamp
        logger.info(sb);

        // Return the next call handler
        return next.startCall(call, headers);
    }
}