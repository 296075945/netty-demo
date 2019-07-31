package com.wy.javanio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioTest {

	public static void main(String[] args) {
		RandomAccessFile rFile;
		try {
			rFile = new RandomAccessFile("C:\\Users\\Administrator\\Desktop\\123.sql", "rw");
			FileChannel channel = rFile.getChannel();
			ByteBuffer buf = ByteBuffer.allocate(48);
			int bytesRead = channel.read(buf);
			while (bytesRead != -1) {
				// System.out.println("Read " + bytesRead);
				///buf.clear();
				buf.flip();
				while (buf.hasRemaining()) {
					System.out.print((char) buf.get());
				}

				buf.clear();
				bytesRead = channel.read(buf);
			}
			rFile.close();
				
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
