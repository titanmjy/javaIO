package com.jinyu.io.calculator.bio;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 每个线程处理一个客户端请求
 * */
public final class ServerNormal {
	private static int DEFAULT_PORT = 12345;
	private static ServerSocket server;
	public static void start() throws IOException{
		start(DEFAULT_PORT);
	}

	public synchronized static void start(int port) throws IOException{
		if(server != null) return;
		try{

			server = new ServerSocket(port);
			System.out.println("服务器启动成功，端口：" + port);
			Socket socket;
			while(true){
				socket = server.accept();
				new Thread(new ServerHandler(socket)).start();
			}
		}finally{
			if(server != null){
				System.out.println("服务器已关闭");
				server.close();
				server = null;
			}
		}
	}
}