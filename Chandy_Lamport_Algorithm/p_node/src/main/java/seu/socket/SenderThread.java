package seu.socket;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

import static seu.utility.ConfigUtil.*;

/**
 * Sending message string to target.
 */
public class SenderThread implements Runnable {

    private char targetNode;
    private String content;
    private int delay;

    /**
     * Construct sender thread that send message to
     * the given target node with message content.
     * Delay due to channel from local node to target node.
     *
     * @param targetNode the target node.
     * @param content    the content.
     * @param delay      then channel delay.
     */
    public SenderThread(char targetNode, String content, int delay) {
        this.targetNode = targetNode;
        this.content = content;
        this.delay = delay;
    }

    @Override
    public void run() {
        try {
            System.out.println(dateFormat.format(new Date()) + " Send " + content + " to " + targetNode);
            Socket socket = new Socket(getIP(targetNode), getPort(targetNode));
            Thread.sleep(delay);
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
