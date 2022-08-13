package com.bryanmullen.interceptors;

import io.grpc.ForwardingClientCall;
import io.grpc.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.time.Instant;

/**
 * ClientInterceptor is a gRPC interceptor that logs the hostname of the client performing the request. This is useful
 * for debugging purposes.
 */
public class ClientInterceptor implements io.grpc.ClientInterceptor {
    Logger logger = LoggerFactory.getLogger(ClientInterceptor.class); // Logger for this class.


    @Override
    public <ReqT, RespT> io.grpc.ClientCall<ReqT, RespT> interceptCall(io.grpc.MethodDescriptor<ReqT, RespT> method, io.grpc.CallOptions callOptions, io.grpc.Channel next) {
        return new ForwardingClientCall.SimpleForwardingClientCall<>(next.newCall(method, callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                // log that metadata is being added to the request.
                logger.info("Adding Metadata");

                // find the hostname of the client. Default to Unknown if it cannot be found.
                String hostname;
                try {
                    hostname = InetAddress.getLocalHost().getHostName();
                } catch (Exception e) {
                    hostname = "Unknown";
                }

                // add the hostname to the metadata.
                headers.put(Metadata.Key.of("HOSTNAME", Metadata.ASCII_STRING_MARSHALLER),
                        hostname);

                // add the time of the request to the metadata.
                headers.put(Metadata.Key.of("TIME_OF_REQUEST", Metadata.ASCII_STRING_MARSHALLER),
                        Instant.now().toString());

                // call the superclass method to forward the request.
                super.start(responseListener, headers);
            }
        };
    }
}