package com.chi.question;

import java.io.FileInputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Test {

	public static void main(String[] args) throws Exception {
		final URL resource = Test.class.getClassLoader().getResource("init.sql");
		final String file = resource.getFile();
		FileInputStream f1 = new FileInputStream(file);
		byte[] bytes = new byte[1024];

		FileChannel fc1 = f1.getChannel();
		ByteBuffer b1 = ByteBuffer.allocate(25);//初始化缓冲区大小
		int len = 0;
		while ((len = fc1.read(b1)) > 0) {// position<limit时返回true
			b1.flip();
			b1.get(bytes, 0, len);
			System.out.print(new String(bytes, 0, len));
			b1.clear();
		}
		fc1.close();//关闭通道
	}
}
