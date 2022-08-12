package com.bryanmullen.interceptors;

import io.grpc.ForwardingClientCall;
import io.grpc.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.time.Instant;

public class ClientInterceptor implements io.grpc.ClientInterceptor {
    Logger logger = LoggerFactory.getLogger(ClientInterceptor.class); //

    @Override
    public <ReqT, RespT> io.grpc.ClientCall<ReqT, RespT> interceptCall(io.grpc.MethodDescriptor<ReqT, RespT> method, io.grpc.CallOptions callOptions, io.grpc.Channel next) {
        return new ForwardingClientCall.SimpleForwardingClientCall<>(next.newCall(method, callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                logger.info("Adding Metadata");
                String hostname;
                try {
                    hostname = InetAddress.getLocalHost().getHostName();
                } catch (Exception e) {
                    hostname = "Unknown";
                }

                headers.put(Metadata.Key.of("HOSTNAME", Metadata.ASCII_STRING_MARSHALLER),
                        hostname);
                headers.put(Metadata.Key.of("TIME_OF_REQUEST", Metadata.ASCII_STRING_MARSHALLER),
                        Instant.now().toString());
                super.start(responseListener, headers);
            }
        };
    }
}