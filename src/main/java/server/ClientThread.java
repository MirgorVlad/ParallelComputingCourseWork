package server;

import org.example.index.ConcurrentInvertedIndex;
import org.example.util.DocUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.FutureTask;

// ClientHandler class
class ClientThread extends Thread {
    private final DataInputStream dis;
    private final DataOutputStream dos;
    private final Socket s;
    ConcurrentInvertedIndex concurrentInvertedIndex = new ConcurrentInvertedIndex();

    public ClientThread(Socket s, DataInputStream dis, DataOutputStream dos) {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    @Override
    public void run() {
        String received;
        List<FutureTask<?>> futureTaskList = new ArrayList<>();
        while (true) {
            try {
                dos.writeUTF("Enter command: ");
                received = dis.readUTF();
                System.out.println("Received: " + received);

                if (received.equals("build")) {
                    String path = dis.readUTF();
                    System.out.println("Path: " + path);
                    futureTaskList = concurrentInvertedIndex.buildIndex(DocUtil
                            .readDocuments(path));
                    dos.write(1);

                } else if (received.equals("status")) {
                    double count = 0;
                    for (FutureTask<?> task : futureTaskList) {
                        if (task.isDone()) {
                            count++;
                        }
                    }
                    dos.writeDouble((count / futureTaskList.size()) * 100);
                } else if (received.equals("search")) {
                    String query = dis.readUTF();
                    System.out.println("Query: " + query);
                    Map<String, Set<Integer>> stringSetMap = concurrentInvertedIndex.searchQuery(query);
                    dos.writeUTF(stringSetMap.toString());
                } else if (received.equals("time")) {
                    String time = concurrentInvertedIndex.getTime();
                    System.out.println("Time: " + time);
                    dos.writeUTF(time);
                } else if (received.equals("exit")) {
                    dos.writeUTF("Close connection");
                    this.s.close();
                    break;
                } else {
                    dos.writeUTF("Unknown command");
                }

            } catch (Exception e) {
                break;
            }
        }

        try {
            // closing resources
            this.dis.close();
            this.dos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
