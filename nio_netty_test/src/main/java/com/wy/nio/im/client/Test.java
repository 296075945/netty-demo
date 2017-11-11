package com.wy.nio.im.client;

import com.wy.nio.im.common.User;
import com.wy.nio.im.msg.InfoMsg;
import com.wy.nio.im.msg.LoginMsg;

/**
 * @author wy
 * @version 创建时间：2017年11月8日 上午9:59:40
 */
public class Test {
	public static void main(String[] args) {
		User userA = new User("a");
		User userB = new User("b");
		Client client = new Client();
		client.start();
		ClientSendManager send = new ClientSendManager(client);
		send.start();
		send.send(new LoginMsg(userA));
		Client client2 = new Client();
		client.start();
		ClientSendManager send2 = new ClientSendManager(client2);
		send.start();
		send.send(new LoginMsg(userB));
		InfoMsg info = new InfoMsg(userA,userB,"haha");
		send.send(info);
		
		
	}
	static class Run implements Runnable{

		public void run() {
			
		}
		
	}
}	
