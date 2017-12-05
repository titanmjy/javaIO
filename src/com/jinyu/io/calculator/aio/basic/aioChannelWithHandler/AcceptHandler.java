package com.jinyu.io.calculator.aio.basic.aioChannelWithHandler;

import com.jinyu.io.calculator.aio.server.AsyncServerHandler;
import com.jinyu.io.calculator.aio.server.ReadHandler;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * @Description:
 * @Author jinyu.mei
 * @Date 2017/12/5 11:11
 * Version: V0.1
 */
public class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, Void> {


    @Override
    public void completed(final AsynchronousSocketChannel client, Void attachment) {

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        client.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result_num, ByteBuffer attachment) {
                attachment.flip();
                CharBuffer charBuffer = CharBuffer.allocate(1024);
                CharsetDecoder decoder = Charset.defaultCharset().newDecoder();
                decoder.decode(attachment,charBuffer,false);
                charBuffer.flip();
                String data = new String(charBuffer.array(),0, charBuffer.limit());
                System.out.println("read data:" + data);
                try{
                    client.close();
                }catch (Exception e){}
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                System.out.println("read error");
            }
        });
    }

    @Override
    public void failed(Throwable exc, Void attachment) {
        System.out.println("accept error");
    }
}
