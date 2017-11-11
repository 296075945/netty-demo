package com.wy.nio.im.client;
/**
 * @author wy
 * @version 创建时间：2017年11月8日 上午9:15:08
 */


import com.wy.nio.im.common.User;
import com.wy.nio.im.msg.InfoMsg;
import com.wy.nio.im.msg.Msg;
import com.wy.nio.im.msg.MsgQueue;
import com.wy.nio.im.msg.impl.LocalQueueAdapter;


public class ClientSendManager  {
	MsgQueue queue;
	Client client;
	Run run;

	public ClientSendManager(Client client) {
		this.queue = new LocalQueueAdapter();
		run  = new Run();
		this.client = client;
	}

	public void send(Msg msg) {
		queue.offer(msg);
	}
	public void send(User accept, String msg) {
		queue.offer(new InfoMsg(client.user, accept, msg));
	}

	public void start() {
		new Thread(run).start();

	}

	public void stop() {
		// TODO Auto-generated method stub

	}

	class Run implements Runnable {

		public void run() {
			while(true){
				Msg msg;
				if((msg =queue.poll())!=null){
					if(client.future!=null&&client.future.channel().isOpen()){
						client.future.channel().writeAndFlush(msg);
					}
				}
			}
		}

	}

}
