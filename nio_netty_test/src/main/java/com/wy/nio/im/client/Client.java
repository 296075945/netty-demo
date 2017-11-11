package com.wy.nio.im.client;

import java.util.concurrent.Executors;

import com.wy.nio.im.common.Manager;
import com.wy.nio.im.common.User;
import com.wy.nio.im.msg.LoginMsg;
import com.wy.nio.im.msg.Msg;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * @author wy
 * @version 创建时间：2017年11月7日 下午4:45:45
 */
public class Client {
	Bootstrap bootstrap;
	ChannelFuture future;
	User user;

	public Client() {
		bootstrap = new Bootstrap();
		EventLoopGroup group = new NioEventLoopGroup(1);
		bootstrap.group(group);
		bootstrap.channel(NioSocketChannel.class);
		bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
		bootstrap.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast(new ObjectEncoder());
				pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
				ClientChannelHandler handler = new ClientChannelHandler();
				pipeline.addLast(handler);
			}

		});
		bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
	}

	public Client(User user) {
		this();
		this.user = user;
	}

	public void start() {
		connect();
	}

	public void connect() {
		try {
			future = bootstrap.connect("localhost", 9999).sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {

		// User user = new User("111111");
		// Msg msg = new LoginMsg(user);
		// client.future.channel().writeAndFlush(msg);
	}
}
