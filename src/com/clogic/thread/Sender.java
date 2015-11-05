package com.clogic.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Created by clogic on 2015. 11. 2..
 */
public class Sender extends Thread {

    PrintWriter writer;

    public Sender(PrintWriter writer) {
        this.writer = writer;
    }

    @Override
    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                String line = reader.readLine();
                if(line.contains("GET")) {
                    continue;
                }
                writer.println(line);
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String msg) {
        writer.println(msg);
        writer.flush();
    }
}
