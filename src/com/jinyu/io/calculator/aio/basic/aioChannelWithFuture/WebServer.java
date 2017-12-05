package com.jinyu.io.calculator.aio.basic.aioChannelWithFuture;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @Description:
 * @Author jinyu.mei
 * @Date 2017/12/5 9:07
 * Version: V0.1
 */
public class WebServer {

    public static void main(String[] args) {
        try {
            AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open();
            server.bind(new InetSocketAddress("127.0.0.1", 8889));
            /**Future对象可在以后用于检索AsynchronousSocketChannel  */
            Future<AsynchronousSocketChannel> acceptFurure = server.accept();   //立即返回，不阻塞
            System.out.println("Return immediately without block");

            AsynchronousSocketChannel channel = acceptFurure.get();     //阻塞
            System.out.println("Accept completed");

            ByteBuffer readBuffer = ByteBuffer.allocate(100);
            try {
                // read a message from the client, timeout after 10 seconds
                channel.read(readBuffer).get(10, TimeUnit.SECONDS); //阻塞10秒
                readBuffer.flip();
//                while (readBuffer.hasRemaining()){
//                    System.out.print((char)readBuffer.get());
//                }
                System.out.println("Message received from client: " + new String(readBuffer.array()));
            } catch (TimeoutException e) {
                System.out.println("Client didn't respond in time");
            }

            System.out.println("close the server");
            server.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
