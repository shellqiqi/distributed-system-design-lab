package seu;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Receiver implements Runnable {

    private int port;
    public static int transmissionReceiveCount = 0;
    public static int resourceResponseCount = 0;

    public Receiver(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        while (transmissionReceiveCount < 50 * 2 ||
                resourceResponseCount < 10 * 2) {
            try {
                serverSocket.setSoTimeout(15000);
                Socket socket = serverSocket.accept();
                Thread thread = new Thread(new ReceiverProcessor(socket));
                thread.start();
            } catch (SocketTimeoutException e) {
                // Retry
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
