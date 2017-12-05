package com.jinyu.io.calculator.aio.basic.aioChannelWithFuture;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Future;

/**
 * @Description:
 * @Author jinyu.mei
 * @Date 2017/12/5 9:07
 * Version: V0.1
 */
public class WebClient {

    public static void main(String[] args){
        try {
            AsynchronousSocketChannel client = AsynchronousSocketChannel.open();
            Future<Void> connectFuture = client.connect(new InetSocketAddress("127.0.0.1",8889));

            // if the connect hasn't happened yet cancel it
//            if (!connectFuture.isDone()) {
//                connectFuture.cancel(true);
//                return;
//            }

            // send a message to the server
            ByteBuffer message = ByteBuffer.allocate(128);
            message.put("hello world".getBytes());
            System.out.println("Sending message to the server...");
            message.flip();
            client.write(message);
//            int numberBytes = client.write(message).get();
            client.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
