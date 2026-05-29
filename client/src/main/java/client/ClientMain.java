package client;

import chess.*;

public class ClientMain {
    public static void main(String[] args) {
        String serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }

        System.out.println("♕ Welcome to the CS 240 Chess Client, type \"help\" to get started");

        Client client = new Client(serverUrl);
    }
}
