package org.example.server;
import org.example.index.ConcurrentInvertedIndex;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(5057);
        ConcurrentInvertedIndex concurrentInvertedIndex = new ConcurrentInvertedIndex();
        //taskProcessingThread.start();
        while (true) {
            Socket s = null;
            try {
                // socket object to receive incoming client requests
                s = ss.accept();

                System.out.println("A new client is connected : " + s);

                // obtaining input and out streams
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                System.out.println("Assigning new thread for this client");

                // create a new thread object
                Thread t = new ClientThread(s, dis, dos, concurrentInvertedIndex);

                // Invoking the start() method
                t.start();

            } catch (Exception e) {
                if(s != null) {
                    s.close();
                }
            }
        }
    }
}

