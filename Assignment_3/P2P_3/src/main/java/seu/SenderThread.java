package seu;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SenderThread implements Runnable {

    private Socket socket;
    private int transmission;
    private int delay;

    public SenderThread(Socket socket, int transmission, int delay) {
        this.socket = socket;
        this.transmission = transmission;
        this.delay = delay;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(delay);
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeInt(transmission);
            outputStream.flush();
            outputStream.close();
            socket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
