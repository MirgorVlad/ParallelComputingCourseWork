package org.example.client;

import java.io.*;
import java.net.Socket;

public class Client {
    // initialize socket and input output streams
    private Socket socket = null;
    private DataOutputStream out = null;
    private DataInputStream in = null;
    private BufferedReader reader = null;

    // constructor to put ip address and port
    public Client(String address, int port) {
        try {
            socket = new Socket(address, port);
            // takes input from terminal
            reader = new BufferedReader(new InputStreamReader(System.in));

            // sends output to the socket
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());

            // print the initial data
            System.out.println(in.readUTF());
        } catch (Exception e) {
            System.out.println(e);
        }

        // string to read message from input
        appLoop();

        // close the connection
        try {
            reader.close();
            out.close();
            socket.close();
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void appLoop() {
        String line = "";

        // keep reading until "Exit" is input
        while (!line.equals("exit")) {
            try {
                line = reader.readLine();
                out.writeUTF(line);

                if (line.equals("build")) {
                    String path = reader.readLine();
                    out.writeUTF(path);
                    out.flush();

                    byte resultStatus = in.readByte();
                    System.out.println("status: " + resultStatus);
                    if (resultStatus == 1) {
                        System.out.println("Data has been sent");
                    } else {
                        System.out.println("Data hasn't been sent");
                    }

                }
                else if (line.equals("status")) {
                    double resultStatus = in.readDouble();
                    System.out.println("Status: " + resultStatus + "%");
                }
                else if (line.equals("time")) {
                    String resultStatus = in.readUTF();
                    System.out.println("Time: " + resultStatus + "ms");
                }
                else if (line.equals("search")) {
                    String query = reader.readLine();
                    out.writeUTF(query);
                    out.flush();

                    String searchResult = in.readUTF();
                    System.out.println(searchResult);
                }

                System.out.println(in.readUTF());
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 5057);
    }
}