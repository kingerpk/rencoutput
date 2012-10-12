package com.everlightsz.ncoutput;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.Socket;

import javax.xml.rpc.holders.ByteWrapperHolder;

public class UpLoadClient {
	public static void main(String[] args) throws Exception {
		String path = null;
		String fileName = null;
		String ip = null;
		path = "F:/temdata/ncputput/wps.doc";
		fileName = path.substring(path.lastIndexOf("/") + 1);
		
		
		ip = "192.168.1.100";
		Socket socket = new Socket(ip, 3000);
		FileInputStream fs = new FileInputStream(path);
		byte[] bytes = new byte[1024];
		BufferedOutputStream bos = new BufferedOutputStream(socket
				.getOutputStream());
		DataOutputStream dos=new DataOutputStream(bos);
		dos.writeUTF(fileName);
		dos.flush();
		int len = 0;
		while ((len = fs.read(bytes)) != -1) {
			dos.write(bytes, 0, len);
		}
		dos.flush();
		bos.close();
		dos.close();
		fs.close();
		socket.close();
		System.out.println("文件上传完毕！");
	}
}
