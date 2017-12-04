package com.jinyu.io.calculator.bio;
import java.io.IOException;
import java.util.Random;


public class Test {
	public static void main(String[] args) throws InterruptedException {
		// 启动服务端线程，创建监听
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					ServerBetter.start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();

		Thread.sleep(100);
		char operators[] = {'+','-','*','/'};
		Random random = new Random(System.currentTimeMillis());
		// 客户端进程，发起请求
		new Thread(new Runnable() {
			int i= 0;

			@Override
			public void run() {
				while(i < 3){
					String expression = random.nextInt(10)+""+operators[random.nextInt(4)]+(random.nextInt(10)+1);
					Client.send(expression);
					try {
						Thread.currentThread().sleep(random.nextInt(1000));
						System.out.println("==================");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					i ++;
				}
			}
		}).start();
	}
}