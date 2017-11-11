package com.wy.nio.im.client;
/**
 * @author wy
 * @version 创建时间：2017年11月7日 下午4:53:01
 */

import com.wy.nio.im.msg.Msg;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientChannelHandler extends SimpleChannelInboundHandler<Msg> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Msg msg) throws Exception {
		System.out.println(msg.getSender().getUuid()+":"+msg.getMsg());

	}

}
