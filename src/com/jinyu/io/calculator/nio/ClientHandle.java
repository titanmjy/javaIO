package com.jinyu.io.calculator.nio;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;


public class ClientHandle implements Runnable{
	private String host;
	private int port;
	private Selector selector;
	private SocketChannel socketChannel;
	private volatile boolean started;

	public ClientHandle(String ip,int port) {
		this.host = ip;
		this.port = port;
		try{
			selector = Selector.open();
			socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(false);
			started = true;
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
		try{
			// 创建连接
			doConnect();
		}catch(IOException e){
			e.printStackTrace();
			System.exit(1);
		}

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
			}catch(Exception e){
				e.printStackTrace();
				System.exit(1);
			}
		}

		// 退出处理后，关闭selector
		if(selector != null)
			try{
				selector.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
	}

	// 处理服务端返回信息
	private void handleInput(SelectionKey key) throws IOException{
		if(key.isValid()){
			SocketChannel sc = (SocketChannel) key.channel();
			if(key.isConnectable()){
				if(sc.finishConnect());
				else System.exit(1);
			}
			if(key.isReadable()){
				ByteBuffer buffer = ByteBuffer.allocate(1024);
				int readBytes = sc.read(buffer);
				if(readBytes>0){
					buffer.flip();
					byte[] bytes = new byte[buffer.remaining()];
					buffer.get(bytes);
					String result = new String(bytes,"UTF-8");
					System.out.println("客户端收到消息：" + result);
				}
				else if(readBytes<0){
					key.cancel();
					sc.close();
				}
			}
		}
	}

	// 发送消息
	private void doWrite(SocketChannel channel,String request) throws IOException{
		byte[] bytes = request.getBytes();
		ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
		writeBuffer.put(bytes);
		writeBuffer.flip();
		channel.write(writeBuffer);
	}

	private void doConnect() throws IOException{
		if(socketChannel.connect(new InetSocketAddress(host,port)));
		else socketChannel.register(selector, SelectionKey.OP_CONNECT);
	}

	public void sendMsg(String msg) throws Exception{
		socketChannel.register(selector, SelectionKey.OP_READ);
		doWrite(socketChannel, msg);
	}
}