package seu;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static seu.ConfigUtil.*;

public class ReceiverThread implements Runnable {

    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private Lock lock = new ReentrantLock();

    public ReceiverThread(Socket socket) throws IOException {
        this.socket = socket;
        inputStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        outputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        // TODO: business logic.
        try {
            Object o = inputStream.readObject();
            if (o != null) {
                Message message = new Message((String) o);
                switch (message.command) {
                    case 1:
                        sendResourceThroughChannel(message.node, message.resource);
                        break;
                    case 2:
                        startSnapshot(message.snapshotId);
                        break;
                    case 3:
                        getResource(message.node, message.resource);
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    default:
                        break;
                }
            }
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendResourceThroughChannel(char targetNode, int resource) {
        Message message = Message.getInstanceOfResourceTransmit(resource);
        SenderThread senderThread = new SenderThread(targetNode, message.toString(), getDelay(targetNode));
        Thread thread = new Thread(senderThread);
        thread.start();
    }

    private void startSnapshot(int snapshotId) throws Exception {
        lock.lock();
        SNAPSHOT_TABLE.put(snapshotId, Snapshot.getInstanceOfStartSnapshot(snapshotId));
        lock.unlock();
        Message message = Message.getInstanceOfSnapshot(snapshotId);
        for (char targetNode :
                getOtherNodes()) {
            SenderThread senderThread = new SenderThread(targetNode, message.toString(), getDelay(targetNode));
            Thread thread = new Thread(senderThread);
            thread.start();
        }
    }

    private void getResource(char sourceNode, int resource) {
        lock.lock();
        RESOURCE += resource;
        for (Integer snapshotId :
                SNAPSHOT_TABLE.keySet()) {
            if (SNAPSHOT_TABLE.get(snapshotId).isListen(sourceNode)) {
                SNAPSHOT_TABLE.get(snapshotId).addChannelResource(sourceNode, resource);
            }
        }
        lock.unlock();
    }
}
