package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class AndroidService {
    public static void main(String[] args) throws IOException {
        ServerSocket serivce = new ServerSocket(30000);
        while (true) {
            //等待客户端连接
            Socket socket = serivce.accept();
            new Thread(new AndroidRunable(socket)).start();
        }
    }

    public static class AndroidRunable implements Runnable {

        Socket socket = null;

        public AndroidRunable(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            // 向android客户端输出hello worild
            String line = null;
            InputStream input;
            OutputStream output;
            String str = "你好!";
            try {
                //向客户端发送信息
                output = socket.getOutputStream();
                input = socket.getInputStream();
                BufferedReader bff = new BufferedReader(
                        new InputStreamReader(input));
                output.write(str.getBytes("gbk"));
                output.flush();
                //半关闭socket
                socket.shutdownOutput();
                //获取客户端的信息
                while ((line = bff.readLine()) != null) {
                    System.out.print(line);
                }
                //关闭输入输出流
                output.close();
                bff.close();
                input.close();
                socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
