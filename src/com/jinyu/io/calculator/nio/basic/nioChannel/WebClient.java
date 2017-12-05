package com.jinyu.io.calculator.nio.basic.nioChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @Description:
 * @Author jinyu.mei
 * @Date 2017/12/4 17:03
 * Version: V0.1
 */
public class WebClient {
    public static void main(String[] args) {
        SocketChannel sc = null;
        try {
            // 创建channel
            sc = SocketChannel.open();
            // 发起连接
            sc.connect(new InetSocketAddress("127.0.0.1",8000));

            ByteBuffer writeBuffer = ByteBuffer.allocate(256);
            writeBuffer.put("hello world".getBytes());

            // writeBuffer更改为读状态
            writeBuffer.flip();
            sc.write(writeBuffer);
            sc.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
