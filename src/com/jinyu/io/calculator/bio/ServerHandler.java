package com.jinyu.io.calculator.bio;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.jinyu.io.utils.Calculator;


public class ServerHandler implements Runnable{
	private Socket socket;

	public ServerHandler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		BufferedReader in = null;
		PrintWriter out = null;
		try{
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(),true);
			String expression;
			String result;
			while(true){
				if((expression = in.readLine())==null) break;
				System.out.println("服务器收到信息：" + expression);
				try{
					result = Calculator.Instance.cal(expression).toString();
				}catch(Exception e){
					result = "计算错误：" + e.getMessage();
				}
				out.println(result);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(out != null){
				out.close();
				out = null;
			}
			if(socket != null){
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}