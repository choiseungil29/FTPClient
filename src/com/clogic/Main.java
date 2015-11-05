package com.clogic;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            FTPClient client = new FTPClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
