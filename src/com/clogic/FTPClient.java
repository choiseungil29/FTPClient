package com.clogic;

import com.clogic.thread.Receiver;
import com.clogic.thread.Sender;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by clogic on 2015. 11. 4..
 */
public class FTPClient implements Observer {

    Queue<String> messageQueue;
    Receiver cmdReceiver;
    Receiver pasvReceiver;
    Sender sender;

    Socket cmdSocket;
    Socket pasvSocket;

    String serverIP;

    public FTPClient() throws IOException {
        serverIP = "203.250.148.132";
        System.out.println("Connecting server. Server IP : " + serverIP);

        messageQueue = new LinkedList<>();

        cmdSocket = new Socket(serverIP, 2234);
        cmdReceiver = new Receiver(new BufferedReader(new InputStreamReader(cmdSocket.getInputStream())), "cmd ");
        cmdReceiver.setObserver(this);
        cmdReceiver.start();

        sender = new Sender(new PrintWriter(new OutputStreamWriter(cmdSocket.getOutputStream())));
        sender.start();

        login("testid", "test1234");
        //cmdReceiver.start();
    }

    public void login(String id, String password) {
        sender.sendMessage("USER " + id);
        sender.sendMessage("PASS " + password);
    }

    public void get(String filename) throws IOException {
        FileOutputStream fos = new FileOutputStream("/Users/clogic/Desktop/" + filename);
        
        BufferedInputStream dataInput = new BufferedInputStream(pasvSocket.getInputStream());
        int n = 0;
        byte[] b = new byte[1024];
        while ((n = dataInput.read(b, 0, b.length)) != -1) {
            fos.write(b);
            System.out.println("write");
        }
        fos.close();
        System.out.println("end");
    }

    @Override
    public void sendMessage(String message) {
        try {
            if(message.contains("230")) {
                sender.sendMessage("PASV");
            }
            if(message.contains("227")) {
                String ip = message.substring(message.indexOf("(")+1, message.length()-1);
                int pasvPort = Integer.parseInt(ip.split(",")[4]) * 256 + Integer.parseInt(ip.split(",")[5]);
                pasvSocket = new Socket(serverIP, pasvPort);
                pasvReceiver = new Receiver(new BufferedReader(new InputStreamReader(pasvSocket.getInputStream())), "pasv");
                pasvReceiver.setObserver(this);
                pasvReceiver.start();
                System.out.println("Create passive socket");
                String filename = "1024x1024.jpg";
                download(filename);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void download(String filename) throws IOException {
        sender.sendMessage("RETR " + filename);
        get(filename);
    }
}
