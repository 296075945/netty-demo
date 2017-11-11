package com.wy.nio.netty.test;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpVersion;

public class HttpClient {
	public static void main(String[] args) {
		new HttpClient().run();
	}

	@SuppressWarnings("deprecation")
	private void run() {
		Bootstrap b = new Bootstrap();
		EventLoopGroup group = new NioEventLoopGroup();
		b.group(group);
		b.channel(NioSocketChannel.class);
		b.handler(new HttpClientInitializer());
		b.option(ChannelOption.SO_KEEPALIVE, true);
		try {
			ChannelFuture cf = b.connect("localhost", 8888).sync();
			
			
			
			URI uri = new URI("http://localhost:8888/hello");
			DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,
					uri.toASCIIString());

			// 构建http请求
			HttpHeaders headers = new DefaultHttpHeaders();
			headers.set(HttpHeaders.Names.HOST,"");
			request.headers().set(headers);
			// 发送http请求
			while(true){
				
				cf.channel().write(request);
				cf.channel().flush();
				cf.channel().closeFuture().sync();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch blockl
			e.printStackTrace();
		} 

	}
}

class HttpClientInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		System.out.println("Initializer");
		ChannelPipeline pipe = ch.pipeline();
		pipe.addLast(new HttpResponseDecoder());
		pipe.addLast(new HttpRequestEncoder());
//		pipe.addLast(new HttpClientInboundHandler());
		pipe.addLast(new HttpClientHandler());
		// pipe.addLast(handlers)
	}

}

class HttpClientHandler extends SimpleChannelInboundHandler<Object> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		 if (msg instanceof HttpResponse) 
	        {
	            HttpResponse response = (HttpResponse) msg;
	            System.out.println("CONTENT_TYPE:" + response.headers().get(HttpHeaders.Names.SET_COOKIE));
	        }
	        if(msg instanceof HttpContent)
	        {
	            HttpContent content = (HttpContent)msg;
	            ByteBuf buf = content.content();
	            System.out.println(buf.toString(io.netty.util.CharsetUtil.UTF_8));
	            buf.release();
	        }
	}

}
