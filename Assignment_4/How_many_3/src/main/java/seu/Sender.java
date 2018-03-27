package seu;

import java.io.IOException;
import java.net.Socket;
import java.util.Random;

import static seu.App.*;

public class Sender implements Runnable {

    private String IP1;
    private int port1;
    private String IP2;
    private int port2;
    private Random random;

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
            for (int i = 0; i < 50; i++) {
                Thread.sleep(getRandomInterval(1));
                if (counter1 >= 25) {
                    send(2); counter2++;
                } else if (counter2 >= 25) {
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

    private void send(int i) throws IOException {
        Socket socket;
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
        Thread thread = new Thread(new SenderThread(socket));
        thread.start();
    }
}
