package com.jinyu.io.calculator.nio;
public class Server {
	private static int DEFAULT_PORT = 12345;
	private static ServerHandle serverHandle;

	// 启动服务端进程
	public static void start(){
		start(DEFAULT_PORT);
	}

	public static synchronized void start(int port){
		if(serverHandle != null)
			serverHandle.stop();
		serverHandle = new ServerHandle(port);
		new Thread(serverHandle,"Server").start();
	}

	public static void main(String[] args){
		start();
	}
}