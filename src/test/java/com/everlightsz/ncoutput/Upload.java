package com.everlightsz.ncoutput;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
//使用TCP协议写一个可以上传文件的服务器和客户端。  
public class Upload {  
    public static void main(String[] args) throws Exception {  
        ServerSocket ss = new ServerSocket(3000);  
        Socket socket = ss.accept();  
        new Thread(new Receive(socket)) {  
        }.start();  
    }  
}  
class Receive implements Runnable {  
    private Socket socket;  
    public Receive(Socket socket) {  
        this.socket = socket;  
    }  
    public void run() {  
        try {  
        	
            BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());           
            DataInputStream dis=new DataInputStream(bis);
            String fileName = dis.readUTF();  
            byte[] bytes = new byte[1024];  
            File receiveFile=new File("f:/temdata/ncputput/receive/"+fileName);
            FileOutputStream fos = new FileOutputStream(receiveFile);  
            BufferedOutputStream bos = new BufferedOutputStream(fos);  
            int len = 0;  
            while ((len = dis.read(bytes)) != -1) {  
                bos.write(bytes, 0, len);  
            }  
            bos.close();  
            fos.close();  
            bis.close();  
            dis.close();
            socket.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
}  
