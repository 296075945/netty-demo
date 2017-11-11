package com.wy.nio.im.server;

import com.wy.nio.im.common.User;
import com.wy.nio.im.msg.Msg;
import com.wy.nio.im.msg.MsgQueue;
import com.wy.nio.im.msg.ServerMsg;

import io.netty.channel.Channel;

/**
 * @author wy
 * @version 创建时间：2017年11月7日 下午4:17:58
 */
public class ServerSendManager implements Runnable{
	MsgQueue msgQueue;

	public void run() {
		while(true){
			Msg msg ;
			if((msg =msgQueue.poll())!=null){
				User accept = msg.getAccept();
				Channel channel = OnlineUser.get(accept);
				if(channel!=null){
					channel.writeAndFlush(msg);
				}else{
					channel = OnlineUser.get(msg.getSender());
					channel.writeAndFlush(ServerMsg.UN_ONLINE);
				}
			}
		}
	}
}
