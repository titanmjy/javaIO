package com.jinyu.io.calculator.bio;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池处理客户端请求
 */
public final class ServerBetter {

	private static int DEFAULT_PORT = 12345;
	private static ServerSocket server;
	private static ExecutorService executorService = Executors.newFixedThreadPool(60);
	public static void start() throws IOException{
		start(DEFAULT_PORT);
	}

	public synchronized static void start(int port) throws IOException{
		if(server != null) return;
		try{
			server = new ServerSocket(port);
			System.out.println("服务端启动成功，端口:" + port);
			Socket socket;

			while(true){
				// 阻塞的方法，等待用户连接请求
				socket = server.accept();
				executorService.execute(new ServerHandler(socket));
			}
		}finally{
			if(server != null){
				System.out.println("服务器已经关闭");
				server.close();
				server = null;
			}
		}
	}
}