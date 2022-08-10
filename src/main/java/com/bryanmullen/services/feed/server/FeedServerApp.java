package com.bryanmullen.services.feed.server;

import java.io.IOException;

public class FeedServerApp {
    public static void main(String[] args) throws IOException {
        var feedServer = new FeedServer("src/main/resources/feed.properties",
                new FeedServiceImpl());
        feedServer.run();
    }
}
