package com.wy.diskio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class Test1 {

	public static void main(String[] args) throws Exception {
		OutputStream os = new FileOutputStream(new File("C:\\Users\\weiyang\\test.io"));

		byte[] buff = new byte[4096*1024*128];
		long s = System.currentTimeMillis();
		os.write(buff);
		long e = System.currentTimeMillis();
		System.out.println(e - s);
		os.close();
	}
}
