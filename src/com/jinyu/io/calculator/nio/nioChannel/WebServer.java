package com.jinyu.io.calculator.nio.nioChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @Description:
 * @Author jinyu.mei
 * @Date 2017/12/4 16:59
 * Version: V0.1
 */
public class WebServer {
    public static void main(String[] args) {
        try {
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.socket().bind(new InetSocketAddress("127.0.0.1", 8000));
            SocketChannel sc = ssc.accept();
            System.out.println("阻塞在此处，等待客户端连接");

            ByteBuffer readBuffer = ByteBuffer.allocate(256);
            sc.read(readBuffer);

            // readBuffer更改为读状态
            readBuffer.flip();
            while (readBuffer.hasRemaining()){
                System.out.println((char)readBuffer.get());
            }

            sc.close();
            ssc.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
