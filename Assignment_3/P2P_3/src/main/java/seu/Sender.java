package seu;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static seu.App.*;

public class Sender implements Runnable {

    private String IP1;
    private int port1;
    private String IP2;
    private int port2;
    private Random random;

    private Socket socket;
    private DataOutputStream outputStream;

    private Lock lock = new ReentrantLock();

    public Sender(String IP1, int port1, String IP2, int port2, int seed) {
        this.IP1 = IP1;
        this.port1 = port1;
        this.IP2 = IP2;
        this.port2 = port2;
        this.random = new Random(seed);
    }

    @Override
    public void run() {
        try {
            int counter1 = 0;
            int counter2 = 0;
            for (int i = 0; i < 10; i++) {
                Thread.sleep(getRandomInterval(12));
                if (counter1 >= 5) {
                    send(2); counter2++;
                } else if (counter2 >= 5) {
                    send(1); counter1++;
                } else {
                    if (random.nextBoolean()) {
                        send(1); counter1++;
                    } else {
                        send(2); counter2++;
                    }
                }
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private void connect(int i) throws IOException {
        switch (i) {
            case 1:
                socket = new Socket(IP1, port1);
                break;
            case 2:
                socket = new Socket(IP2, port2);
                break;
            default:
                throw new IOException("Connection argument illegal.");
        }
        socket.setSoTimeout(10000);
        outputStream = new DataOutputStream(socket.getOutputStream());
    }

    private void close() throws IOException {
        outputStream.close();
        socket.close();
    }

    private void send(int i) throws IOException, InterruptedException {
        connect(i);
        lock.lock();
        int transmission = App.resource / 4;
        App.resource -= transmission;
        App.log("send", (InetSocketAddress) socket.getRemoteSocketAddress(), transmission);
        lock.unlock();
        Thread.sleep(500);
        outputStream.writeInt(transmission);
        outputStream.flush();
        close();
    }
}
