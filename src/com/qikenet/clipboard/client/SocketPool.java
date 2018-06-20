package com.qikenet.clipboard.client;

import com.qikenet.clipboard.data.DataFactory;
import com.qikenet.clipboard.entity.Transfer;

import java.io.*;
import java.net.Socket;

public class SocketPool {


    private int port = 5000;
    private String host;

    private Socket socket;

    public SocketPool(String host, int port) {
        this.port = port;
        this.host = host;
        init();
    }

    private void init() {

        //初始化链接
        connnect();
    }

    private void connnect() {
        while (true) {
            try {
                socket = new Socket(host, port);
                break;
            } catch (Exception ee) {
                //重试
            }
        }
    }

    public void push(Transfer transfer) {
        System.out.println("类型：" + transfer.getType());
        System.out.println("大小：" + transfer.getData().length);
        try {

            DataFactory.encoding(socket.getOutputStream(), transfer);
        } catch (IOException e) {

            connnect();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
