package seu.socket;

import seu.pojo.Message;
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
                Message message = new Message((String) o);
                System.out.println("Receive " + message.toString());
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
                        getSnapshot(message.node, message.snapshotId);
                        break;
                    case 5:
                    default:
                        shutdown();
                        break;
                }
            }
            inputStream.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendResourceThroughChannel(char targetNode, int resource) {
        lock.lock();
        RESOURCE -= resource;
        lock.unlock();
        Message message = Message.getInstanceOfResourceTransmit(resource);
        SenderThread senderThread = new SenderThread(targetNode, message.toString(), getDelay(targetNode));
        Thread thread = new Thread(senderThread);
        thread.start();
    }

    private void startSnapshot(int snapshotId) throws Exception {
        lock.lock();
        SNAPSHOT_TABLE.put(snapshotId, Snapshot.getInstanceOfStartSnapshot(snapshotId));
        lock.unlock();
        broadcastSnapshots(snapshotId);
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

    private void getSnapshot(char sourceNode, int snapshotId) throws Exception {
        lock.lock();
        boolean containsSnapshot = SNAPSHOT_TABLE.containsKey(snapshotId);
        if (containsSnapshot) {
            SNAPSHOT_TABLE.get(snapshotId).cancelListen(sourceNode);
        } else {
            SNAPSHOT_TABLE.put(snapshotId, Snapshot.getInstanceOfGetSnapshot(sourceNode, snapshotId));
        }
        SNAPSHOT_TABLE.get(snapshotId).receivedCount++;
        lock.unlock();
        if (!containsSnapshot) broadcastSnapshots(snapshotId);
        if (containsSnapshot && SNAPSHOT_TABLE.get(snapshotId).isComplete()) {
            SenderThread senderThread = new SenderThread('c', SNAPSHOT_TABLE.get(snapshotId).toString(), 0);
            Thread thread = new Thread(senderThread);
            thread.start();
        }
    }

    private void shutdown() {
        Receiver.enableServer = false;
    }

    private void broadcastSnapshots(int snapshotId) throws Exception {
        Message message = Message.getInstanceOfSnapshot(snapshotId);
        for (char targetNode :
                getOtherNodes()) {
            SenderThread senderThread = new SenderThread(targetNode, message.toString(), getDelay(targetNode));
            Thread thread = new Thread(senderThread);
            thread.start();
        }
    }
}
