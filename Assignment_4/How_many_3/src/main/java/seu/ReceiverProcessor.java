package seu;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static seu.MessageUtil.*;

public class ReceiverProcessor implements Runnable {

    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    private Lock lock = new ReentrantLock();

    public ReceiverProcessor(Socket socket) throws IOException {
        this.socket = socket;
        inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        outputStream = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            short message = inputStream.readShort();
            switch (getCode(message)) {
                case 0b00:
                    getResource(getParameter(message));
                    break;
                case 0b01:
                    responseSummationRequest();
                    break;
                case 0b10: throw new Exception("Illegal message - Summation response cannot be received.");
                default: throw new Exception("Illegal message - Undefined message.");
            }
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getResource(int resource) {
        lock.lock();
        App.resource += resource;
        Receiver.transmissionReceiveCount++;
        App.logger.log(1, (InetSocketAddress) socket.getRemoteSocketAddress(), 0b00, resource, App.resource, new Date());
        lock.unlock();
    }

    private void responseSummationRequest() throws IOException {
        lock.lock();
        outputStream.writeShort(getMessage(0b10, App.resource));
        outputStream.flush();
        App.logger.log(0, (InetSocketAddress) socket.getRemoteSocketAddress(), 0b10, App.resource, 0, new Date());
        Receiver.resourceResponseCount++;
        lock.unlock();
    }
}
