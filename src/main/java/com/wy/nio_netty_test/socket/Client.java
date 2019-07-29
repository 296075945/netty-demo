package com.wy.nio_netty_test.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class Client extends Thread {
	// 定义检测SocketChannel的Selector对象
	private Selector selector = null;
	// 定义处理编码和解码的字符集
	private Charset charset = Charset.forName("UTF-8");
	// 客户端SocketChannel
	private SocketChannel sc = null;

	public void init() throws IOException, InterruptedException {
		selector = Selector.open();
		InetSocketAddress isa = new InetSocketAddress("127.0.0.1", 7777);
		// 调用open静态方法创建连接到指定主机的SocketChannel
		sc = SocketChannel.open(isa);
		// 设置该sc以非阻塞方式工作
		sc.configureBlocking(false);
		// 将Socketchannel对象注册到指定Selector
		// sc.register(selector, SelectionKey.OP_READ);
		// 启动读取服务器端数据的线程
		// new ClientThread().start();
		// for(int i =0;i<3;i++){
		// Thread.sleep(3000);
		// sc.write(charset.encode("123"));
		// }
		// sc.finishConnect();
		// sc.close();
		sc.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);

		while (true) {
			int num = selector.selectNow();
			if (num > 0) {
				Set<SelectionKey> selectionKeys = selector.selectedKeys();
				Iterator<SelectionKey> iterator = selectionKeys.iterator();
				while (iterator.hasNext()) {
					SelectionKey selectionKey = iterator.next();
					if (selectionKey.isReadable()) {
						System.out.println("isReadable");
						SocketChannel client = (SocketChannel) selectionKey.channel();
						ByteBuffer receive = ByteBuffer.allocate(1024);
						
						// 读取服务器发送来的数据到缓冲区中
						try{
							int length = client.read(receive);
							System.out.println(length);
							if(length>-1){
								String s=new String(receive.array());
								System.out.println(s);
							}else{
								client.close();
							}
						}catch (IOException e) {
							selectionKey.cancel();
							client.close();
						}	
					} else if (selectionKey.isWritable()) {
						// System.out.println("isWritable");
						SocketChannel client = (SocketChannel) selectionKey.channel();
						// 返回为之创建此键的通道。
						client = (SocketChannel) selectionKey.channel();
						client.write(charset.encode("123"));
					}
				}
			}
		}

	}

	// 定义读取服务器数据的线程
	private class ClientThread extends Thread {
		public void run() {
			try {
				while (selector.select() > 0) {
					// 遍历每个有可用IO操作Channel对应的SelectionKey
					for (SelectionKey sk : selector.selectedKeys()) {
						// 删除正在处理的SelectionKey
						selector.selectedKeys().remove(sk);
						// 如果该SelectionKey对应的Channel中有可读的数据
						if (sk.isReadable()) {
							// 使用NIO读取channel中的数据
							SocketChannel sc = (SocketChannel) sk.channel();
							ByteBuffer buff = ByteBuffer.allocate(1024);
							String content = "";
							while (sc.read(buff) > 0) {
								// sc.read(buff);
								buff.flip();
								content += charset.decode(buff);
							}
							// 打印输出读取的内容
							System.out.println("聊天信息" + content);
							// 为下一次读取做准备
							sk.interestOps(SelectionKey.OP_READ);
						}
					}
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		new Client().init();
	}
}
