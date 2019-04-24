package com.wy.nio_netty_test.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Server {

	public static void main(String[] args) throws IOException {
		Server server = new Server();
		server.run();
		server.listen();
	}

	// 接受和发送数据缓冲区
	private ByteBuffer send = ByteBuffer.allocate(1024);
	private ByteBuffer receive = ByteBuffer.allocate(1024);

	ServerSocketChannel ssc = null;

	Selector selector = null;

	public void run() throws IOException {
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		// 服务器配置为非阻塞
		serverSocketChannel.configureBlocking(false);
		// 检索与此通道关联的服务器套接字
		ServerSocket serverSocket = serverSocketChannel.socket();
		// 套接字的地址端口绑定
		serverSocket.bind(new InetSocketAddress(7777));
		// 通过open()方法找到Selector
		selector = Selector.open();
		// 注册到selector，等待连接
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		System.out.println("Server Start----8888:");

		// 向发送缓冲区加入数据
		send.put("data come from server".getBytes());
		//send.flip();
	}

	public void listen() throws IOException {
		System.out.println("listen");

		while (true) {
			// 等待一个连接，可能会返回多个key
			int count = selector.selectNow();
			if (count == 0) {
				continue;
			}
//			System.out.println("count=" + count);
			// 返回此选择器的已选择键集。
			Set<SelectionKey> selectionKeys = selector.selectedKeys();
			Iterator<SelectionKey> iterator = selectionKeys.iterator();
			while (iterator.hasNext()) {
				SelectionKey selectionKey = iterator.next();

				// 这里记得手动的把他remove掉，不然selector中的selectedKeys集合不会自动去除
				if (selectionKey.isAcceptable()) {
					System.out.println("isAcceptable");
					ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
					SocketChannel client = server.accept();
					client.configureBlocking(false);
					client.register(selector, SelectionKey.OP_READ |SelectionKey.OP_WRITE);
				} else if (selectionKey.isReadable()) {
					System.out.println("isReadable");
					SocketChannel client = (SocketChannel) selectionKey.channel();
					receive.clear();
					
					// 读取服务器发送来的数据到缓冲区中
					try{
						int length = client.read(receive);
						System.out.println(length);
						if(length>-1){
							String s=new String(receive.array());
							System.out.println(s);
//							send.put(s.getBytes());
							//send.flip();
						}else{
							///System.out.println();
							
							client.close();
						}
					}catch (IOException e) {
						selectionKey.cancel();
						client.close();
					}
					//selectionKey.interestOps(SelectionKey.OP_WRITE);
					//client.close();
				} else if (selectionKey.isWritable()) {
					//System.out.println("isWritable");
					SocketChannel client = (SocketChannel) selectionKey.channel();
					
					// 返回为之创建此键的通道。
					client = (SocketChannel) selectionKey.channel();
					while(send.hasRemaining()){
						System.out.println(1);
						client.write(send);
					}
//					selectionKey.cancel();
					
					// 输出到通道
					
				} else if (selectionKey.isConnectable()) {
					System.out.println("isConnectable");
				}
				iterator.remove();
//				System.out.println();
			}
		}
	}

	// 处理请求
	private void handle(SelectionKey selectionKey) throws IOException {

		ServerSocketChannel server = null;
		SocketChannel client = null;
		String receiveText;
		String sendText;
		int count = 0;

		// 测试此键的通道是否已准备好接受新的套接字连接。
		if (selectionKey.isAcceptable()) {
			System.out.println("selectionKey.isAcceptable()");
			// 返回为之创建此键的通道。
			server = (ServerSocketChannel) selectionKey.channel();

			// 此方法返回的套接字通道（如果有）将处于阻塞模式。
			client = server.accept();
			// 配置为非阻塞
			client.configureBlocking(false);
			// 注册到selector，等待连接
			client.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
		} else if (selectionKey.isReadable()) {
			System.out.println("selectionKey.isReadable()");
			// 返回为之创建此键的通道。
			client = (SocketChannel) selectionKey.channel();
			// 将缓冲区清空以备下次读取
			receive.clear();
			// 读取服务器发送来的数据到缓冲区中
			client.read(receive);

			System.out.println(new String(receive.array()));

			selectionKey.interestOps(SelectionKey.OP_WRITE);
		} else if (selectionKey.isWritable()) {
			System.out.println("selectionKey.isWritable()");
			// 将缓冲区清空以备下次写入
			send.flip();
			// 返回为之创建此键的通道。
			client = (SocketChannel) selectionKey.channel();

			// 输出到通道
			client.write(send);

			// selectionKey.interestOps(SelectionKey.OP_READ);
		}
	}
}
