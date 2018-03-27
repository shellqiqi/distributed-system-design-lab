package seu;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Receiver implements Runnable {

    private int port;

    public Receiver(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
            for (int i = 0; i < 50; i++) {
                Socket socket = serverSocket.accept();
                Thread thread = new Thread(new ReceiverThread(socket));
                thread.start();
            }
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
