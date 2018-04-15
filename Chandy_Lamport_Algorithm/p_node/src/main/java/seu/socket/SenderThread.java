package seu.socket;

import java.io.ObjectOutputStream;
import java.net.Socket;

import static seu.utility.ConfigUtil.getIP;
import static seu.utility.ConfigUtil.getPort;

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
            Socket socket = new Socket(getIP(targetNode), getPort(targetNode));
            Thread.sleep(delay);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(content);
            outputStream.flush();
            outputStream.close();
            socket.close();
            System.out.println("Send " + content + " to " + targetNode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
