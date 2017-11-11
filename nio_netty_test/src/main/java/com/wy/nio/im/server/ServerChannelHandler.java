package com.wy.nio.im.server;

import com.wy.nio.im.msg.Msg;
import com.wy.nio.im.msg.MsgQueue;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author wy
 * @version 创建时间：2017年11月7日 下午3:20:51
 */
public class ServerChannelHandler extends SimpleChannelInboundHandler<Msg> {

	MsgQueue msgQueue;
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Msg msg) throws Exception {
		System.out.println("msg"+msg);
		switch (msg.getType()) {
		case login:
			login(msg,ctx.channel());
			break;
		case logout:
			logout(msg);
			break;
		case info:
			info(msg);
			break;
		default:
		}

	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		
		super.exceptionCaught(ctx, cause);
	}

	void login(Msg msg,Channel channel) {
		OnlineUser.add(msg.getSender(), channel);
	}
	void logout(Msg msg) {
		OnlineUser.remove(msg.getSender());
	}
	void info(Msg msg){
		msgQueue.offer(msg);
	}

}

