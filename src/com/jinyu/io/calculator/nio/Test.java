package com.jinyu.io.calculator.nio;
import java.util.Scanner;

public class Test {
	public static void main(String[] args) throws Exception{
//		启动服务端进程
		Server.start();
		Thread.sleep(100);
		// 启动客户端线程
		Client.start();
		while (true){
			// 客户端发送消息
			if(!Client.sendMsg(new Scanner(System.in).nextLine())){
				break;
			}
		}

	}
}