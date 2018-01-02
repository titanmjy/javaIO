package com.jinyu.io.calculator.aio.basic.aioChannelWithHandler;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;

/**
 * @Description:
 * @Author jinyu.mei
 * @Date 2017/12/5 11:02
 * Version: V0.1
 */
public class WebServer {
    public static void main(String[] args) {
        try {
            AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open();
            server.bind(new InetSocketAddress("127.0.0.1",8888));
            server.accept(null, new AcceptHandler());

            while (true){
                Thread.sleep(1000);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
