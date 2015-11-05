package com.clogic.thread;

import com.clogic.FTPClient;
import com.clogic.Observer;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by clogic on 2015. 11. 2..
 */
public class Receiver extends Thread {

    BufferedReader reader;
    String tag;

    private Observer observer;

    public Receiver(BufferedReader reader, String tag) {
        this.reader = reader;
        this.tag = tag;
    }

    public void setObserver(Observer observer) {
        this.observer = observer;
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = reader.readLine()) != null) {
                System.out.println(tag + " : " + message);
                observer.sendMessage(message);
            }
            System.out.println(tag + " : out");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
