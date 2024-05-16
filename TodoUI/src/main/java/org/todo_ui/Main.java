package org.todo_ui;

public class Main {
    public static void main(String[] args) {

        Thread serverThread = new Thread(new Server(8085));
        serverThread.setName("#TodoUI Server Thread ");
        serverThread.start();
    }
}