package seu;

import java.io.ObjectOutputStream;
import java.net.Socket;

import static seu.ConfigUtil.*;

public class SenderThread implements Runnable {

    private char targetNode;
    private String content;
    private int delay;

    public SenderThread(char targetNode, String content, int delay) {
        this.targetNode = targetNode;
        this.content = content;
        this.delay = delay;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(delay);
            Socket socket = new Socket(getIP(targetNode), getPort(targetNode));
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(content);
            outputStream.flush();
            outputStream.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
