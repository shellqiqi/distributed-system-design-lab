package seu.socket;

import seu.pojo.Snapshot;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static seu.utility.ConfigUtil.*;

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
                System.out.println(snapshot.toString());
                lock.lock();
                if (!SNAPSHOT_TABLE.containsKey(snapshot.id)) {
                    SNAPSHOT_TABLE.put(snapshot.id, snapshot);
                } else {
                    SNAPSHOT_TABLE.get(snapshot.id).merge(snapshot);
                    if (SNAPSHOT_TABLE.get(snapshot.id).isComplete())
                        System.out.println("Complete: " + SNAPSHOT_TABLE.get(snapshot.id));
                }
                lock.unlock();
            }
            inputStream.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
