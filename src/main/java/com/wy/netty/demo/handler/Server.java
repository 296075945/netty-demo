package com.wy.netty.demo.handler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class Server {

	public static void main(String[] args) throws Exception {
		ServerBootstrap bootstrap = new ServerBootstrap();
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup(16);

		bootstrap.group(bossGroup, workerGroup);
		bootstrap.channel(NioServerSocketChannel.class);
		bootstrap.option(ChannelOption.SO_BACKLOG, 128);
		bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
		bootstrap.childHandler(new ServerHandler());
		bootstrap.bind(22222).sync();
		System.out.println("start server");
	}

	private static class ServerHandler extends ChannelInitializer<SocketChannel> {

		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			ChannelPipeline pipeline = ch.pipeline();
			pipeline.addLast(new StringDecoder());
			pipeline.addLast(new ServerInboundHandler());
		}
		
	}

	private static class ServerInboundHandler extends ChannelInboundHandlerAdapter {

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			super.channelRead(ctx, msg);

			System.out.println(msg);
		}

		@Override
		public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
			super.channelRegistered(ctx);
			System.out.println("channel registered");
		}

		
 		
	}
}
