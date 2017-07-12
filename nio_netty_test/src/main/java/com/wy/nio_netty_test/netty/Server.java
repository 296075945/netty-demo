package com.wy.nio_netty_test.netty;

import java.net.InetAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class Server {

	public void run() {
		EventLoopGroup boosGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		ServerBootstrap b = new ServerBootstrap();
		b.group(boosGroup, workerGroup);
		b.channel(NioServerSocketChannel.class);
		b.childHandler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
				pipeline.addLast("decoder", new StringDecoder());
				pipeline.addLast("encoder", new StringEncoder());
				pipeline.addLast("handler", new HelloServerHandler());
			}

		});
		try {
			ChannelFuture cf = b.bind(9999).sync();
			cf.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			boosGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	class HelloServerHandler extends SimpleChannelInboundHandler<String> {

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
			// 收到消息直接打印输出
			System.out.println(ctx.channel().remoteAddress() + " Say : " + msg);
			// 返回客户端消息 - 我已经接收到了你的消息
			ctx.writeAndFlush("Received your message !\n");
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			System.out.println("RamoteAddress : " + ctx.channel().remoteAddress() + " active !");
			ctx.writeAndFlush("Welcome to " + InetAddress.getLocalHost().getHostName() + " service!\n");
			super.channelActive(ctx);
		}
	}

	public static void main(String[] args) {
		new Server().run();
	}
}
