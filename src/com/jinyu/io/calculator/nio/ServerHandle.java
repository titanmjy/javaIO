package com.jinyu.io.calculator.nio;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import com.jinyu.io.utils.Calculator;

public class ServerHandle implements Runnable{
	private Selector selector;
	private ServerSocketChannel serverChannel;
	private volatile boolean started;

	public ServerHandle(int port) {
		try{
			selector = Selector.open();
//			服务端端口监听组件
			serverChannel = ServerSocketChannel.open();
			serverChannel.configureBlocking(false);
			serverChannel.socket().bind(new InetSocketAddress(port),1024);
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
			started = true;
			System.out.println("服务器已启动，端口号：" + port);
		}catch(IOException e){
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void stop(){
		started = false;
	}

	@Override
	public void run() {
		while(started){
			try {
				selector.select(1000);
				Set<SelectionKey> keys = selector.selectedKeys();
				Iterator<SelectionKey> it = keys.iterator();
				SelectionKey key = null;
				while(it.hasNext()){
					key = it.next();
					it.remove();
					try{
						handleInput(key);
					}catch(Exception e){
						if(key != null){
							key.cancel();
							if(key.channel() != null){
								key.channel().close();
							}
						}
					}
				}
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
		if(selector != null)
			try{
				selector.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
	}

	private void handleInput(SelectionKey key) throws IOException{
		if(key.isValid()){
			if(key.isAcceptable()){
				ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
				SocketChannel sc = ssc.accept();
				sc.configureBlocking(false);
				sc.register(selector, SelectionKey.OP_READ);
			}
			if(key.isReadable()){
				SocketChannel sc = (SocketChannel) key.channel();
				ByteBuffer buffer = ByteBuffer.allocate(1024);
				int readBytes = sc.read(buffer);
				if(readBytes>0){
					buffer.flip();
					byte[] bytes = new byte[buffer.remaining()];
					buffer.get(bytes);
					String expression = new String(bytes,"UTF-8");
					System.out.println("服务器收到表达式信息：" + expression);
					String result = null;
					try{
						result = Calculator.Instance.cal(expression).toString();
					}catch(Exception e){
						result = "计算错误：" + e.getMessage();
					}
					doWrite(sc,result);
				}
				else if(readBytes<0){
					key.cancel();
					sc.close();
				}
			}
		}
	}

	// 写回信息
	private void doWrite(SocketChannel channel,String response) throws IOException{
		byte[] bytes = response.getBytes();
		ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
		writeBuffer.put(bytes);
		writeBuffer.flip();
		channel.write(writeBuffer);
	}
}
