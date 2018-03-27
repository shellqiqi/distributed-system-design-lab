package seu;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static seu.MessageUtil.*;

public class ReceiverThread implements Runnable {

    private Socket socket;

    private Lock lock = new ReentrantLock();

    public ReceiverThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            DataInputStream inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            short message = inputStream.readShort();
            lock.lock();
            App.resource += getParameter(message);
            App.log("rcv", (InetSocketAddress) socket.getRemoteSocketAddress(), getParameter(message));
            lock.unlock();
            inputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
