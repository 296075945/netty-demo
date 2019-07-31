package com.wy.netty.demo.handler;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

public class Client {

	public static void main(String[] args) throws Exception {
		Bootstrap bootstrap = new Bootstrap();

		EventLoopGroup workerGroup = new NioEventLoopGroup(2);
		bootstrap.group(workerGroup);
		bootstrap.channel(NioSocketChannel.class);
		bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
		bootstrap.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast(new StringEncoder());
			}
		});
		Channel channel = bootstrap.connect("localhost", 22222).sync().channel();

		Thread.sleep(1000);
		
		channel.writeAndFlush(Unpooled.wrappedBuffer("123".getBytes()));
		
		channel.writeAndFlush("123");
		
		System.out.println("write");
		
		channel.close();
		workerGroup.shutdownGracefully();
	}
}
