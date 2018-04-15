package seu.socket;

import seu.pojo.Snapshot;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReceiverThread implements Runnable {

    private Socket socket;
    private ObjectInputStream inputStream;

    private Lock lock = new ReentrantLock();

    public ReceiverThread(Socket socket) throws IOException {
        this.socket = socket;
        inputStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            Object o = inputStream.readObject();
            if (o != null) {
                Snapshot snapshot = new Snapshot((String) o);
                // TODO: statistics for snapshot
            }
            inputStream.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
