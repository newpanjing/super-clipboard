package com.qikenet.clipboard.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Bootstarp {

    private static int port=5000;

    public void start() throws IOException {

        ServerSocket server = new ServerSocket(port);
        while (true) {

            Socket socket = server.accept();

        }

    }

    public static void main(String[] args) throws IOException {
        new Bootstarp().start();

    }

}
