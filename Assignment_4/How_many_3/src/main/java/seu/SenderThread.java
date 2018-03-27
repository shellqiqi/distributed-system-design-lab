package seu;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SenderThread implements Runnable {

    private Socket socket;

    private Lock lock = new ReentrantLock();

    public SenderThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            lock.lock();
            int transmission = App.resource / 4;
            App.resource -= transmission;
            App.log("send", (InetSocketAddress) socket.getRemoteSocketAddress(), transmission);
            lock.unlock();
            Thread.sleep(1000);
            outputStream.writeInt(transmission);
            outputStream.flush();
            outputStream.close();
            socket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
