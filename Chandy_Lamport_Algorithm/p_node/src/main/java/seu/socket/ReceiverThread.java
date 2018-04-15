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

/**
 * Receive message from c node and p node.
 * Process business logic.
 */
public class ReceiverThread implements Runnable {

    private Socket socket;
    private ObjectInputStream inputStream;

    private Lock lock = new ReentrantLock();

    /**
     * Construct a receive thread to process received snapshot.
     *
     * @param socket socket Receive gives.
     * @throws IOException throw when socket error occurs.
     */
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
                        shutdown();
                        break;
                    default:
                        throw new Exception("Unsupported message command");
                }
            }
            inputStream.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Send resource to target node.
     *
     * @param targetNode target node.
     * @param resource   resource.
     */
    private void sendResourceThroughChannel(char targetNode, int resource) {
        lock.lock();
        RESOURCE -= resource;
        lock.unlock();
        Message message = Message.getInstanceOfResourceTransmit(resource);
        SenderThread senderThread = new SenderThread(targetNode, message.toString(), getDelay(targetNode));
        Thread thread = new Thread(senderThread);
        thread.start();
    }

    /**
     * Start a snapshot.
     *
     * @param snapshotId snapshot id.
     * @throws Exception throw when local node name is unsupported.
     */
    private void startSnapshot(int snapshotId) throws Exception {
        lock.lock();
        SNAPSHOT_TABLE.put(snapshotId, Snapshot.getInstanceOfStartSnapshot(snapshotId));
        lock.unlock();
        broadcastSnapshots(snapshotId);
    }

    /**
     * Get resource.
     *
     * @param sourceNode node resource send from.
     * @param resource   resource.
     */
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

    /**
     * Get a snapshot.
     *
     * @param sourceNode node snapshot send from.
     * @param snapshotId snapshot id.
     * @throws Exception throw when local node name is unsupported.
     */
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

    /**
     * Shutdown the main thread.
     */
    private void shutdown() {
        Receiver.enableServer = false;
    }

    /**
     * Broadcast snapshots to other nodes.
     *
     * @param snapshotId snapshot id.
     * @throws Exception throw when local node name is unsupported.
     */
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
