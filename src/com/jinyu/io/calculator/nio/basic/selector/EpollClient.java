package com.jinyu.io.calculator.nio.basic.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @Description:
 * @Author jinyu.mei
 * @Date 2017/12/4 18:52
 * Version: V0.1
 */
public class EpollClient {
    public static void main(String[] args) {
        try {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress("127.0.0.1", 8000));

            ByteBuffer writeBuffer = ByteBuffer.allocate(32);
            ByteBuffer readBuffer = ByteBuffer.allocate(32);

            writeBuffer.put("hello".getBytes());
            writeBuffer.flip();

            writeBuffer.rewind();
            socketChannel.write(writeBuffer);


            socketChannel.read(readBuffer);
            readBuffer.flip();
            while (readBuffer.hasRemaining()){
                System.out.print((char)readBuffer.get());
            }
            System.out.print("\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}