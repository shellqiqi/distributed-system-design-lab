package seu;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * The instance of the class send messages to the target.
 * Every event that sends message will get one thread of this class.
 */
public class MessageSender implements Runnable {

    private Socket socket;
    private short message;
    private int delay;

    /**
     * Constructor of message sender with NO channel delay.
     * @param socket target socket.
     * @param message message.
     */
    public MessageSender(Socket socket, short message) {
        this(socket, message, 0);
    }

    /**
     * Constructor of message sender.
     * @param socket target socket.
     * @param message message.
     * @param delay channel delay.
     */
    public MessageSender(Socket socket, short message, int delay) {
        this.socket = socket;
        this.message = message;
        this.delay = delay;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(delay);
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeShort(message);
            outputStream.flush();
            outputStream.close();
            socket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
