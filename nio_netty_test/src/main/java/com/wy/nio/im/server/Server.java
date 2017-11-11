package com.wy.nio.im.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.wy.nio.im.msg.MsgQueue;
import com.wy.nio.im.msg.impl.LocalQueueAdapter;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * @author wy
 * @version 创建时间：2017年11月7日 下午3:02:35
 */
public class Server {

	private ChannelFuture future;

	private EventLoopGroup bossGroup;

	private EventLoopGroup workerGroup;

	private ServerBootstrap bootstrap;

	private MsgQueue msgQueue;

	public Server() {

	}

	public void init() {
		msgQueue = new LocalQueueAdapter();
		bootstrap = new ServerBootstrap();
		bossGroup = new NioEventLoopGroup(1);
		workerGroup = new NioEventLoopGroup();
		bootstrap.group(bossGroup, workerGroup);
		bootstrap.channel(NioServerSocketChannel.class);
//		bootstrap.handler();
		
		bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				System.out.println("init channel");
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast(new ObjectEncoder());
				pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
				ServerChannelHandler handler = new ServerChannelHandler();
				handler.msgQueue = msgQueue;
				pipeline.addLast(handler);
			}

		});
	}

	public void start() {
		try {
			future = bootstrap.bind(9999).sync();
			System.out.println("server start");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Server server = new Server();
		server.init();
		server.start();
		ExecutorService service = Executors.newFixedThreadPool(1);
		ServerSendManager send = new ServerSendManager();
		send.msgQueue = server.msgQueue;
		service.execute(send);
	}

}
