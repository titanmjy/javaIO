package com.jinyu.io.calculator.aio.basic.aioChannelWithHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * @Description:
 * @Author jinyu.mei
 * @Date 2017/12/5 11:03
 * Version: V0.1
 */
public class WebClient {
    public static void main(String[] args) {

        try {
            AsynchronousSocketChannel client = AsynchronousSocketChannel.open();
            client.connect(new InetSocketAddress("127.0.0.1",8888));

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.put("hello world".getBytes());
            buffer.flip();

            client.write(buffer);
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
