package seu.socket;

import seu.simulation.SimulateMessage;

import java.util.Date;
import java.util.Vector;

import static seu.utility.ConfigUtil.dateFormat;

/**
 * Send control messages to control the whole nodes behavior.
 * Call SenderThread to send messages.
 */
public class Sender implements Runnable {

    private Vector<SimulateMessage> senderControlMessages = new Vector<>();

    /**
     * Construct sender with sequence of control messages from SimulateApp
     * then change the absolute time to the interval time.
     *
     * @param simulateControlMessages control messages from SimulateApp.
     */
    public Sender(Vector<SimulateMessage> simulateControlMessages) {
        for (int i = 0; i < simulateControlMessages.size(); i++) {
            if (i >= simulateControlMessages.size() - 1) {
                SimulateMessage message = simulateControlMessages.elementAt(i);
                message.time = 0;
                senderControlMessages.add(simulateControlMessages.elementAt(i));
            } else {
                SimulateMessage message = simulateControlMessages.elementAt(i);
                message.time = simulateControlMessages.elementAt(i + 1).time - simulateControlMessages.elementAt(i).time;
                senderControlMessages.add(message);
            }
        }
    }

    @Override
    public void run() {
        try {
            for (SimulateMessage message : senderControlMessages) {
                if (message.command == 5) {
                    stop(message);
                } else {
                    SenderThread senderThread = new SenderThread(message.from, message.toString());
                    Thread thread = new Thread(senderThread);
                    thread.start();
                    System.out.println(dateFormat.format(new Date()) + " Send " + message.toString() + " to " + message.from);
                    Thread.sleep(message.time);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stop all nodes.
     *
     * @param message stop message with command 5.
     */
    private void stop(SimulateMessage message) {
        for (char otherNode : new char[]{'i', 'j', 'k'}) {
            SenderThread senderThread = new SenderThread(otherNode, message.toString());
            Thread thread = new Thread(senderThread);
            thread.start();
            System.out.println(dateFormat.format(new Date()) + " Send " + message.toString() + " to " + otherNode);
            Receiver.enableServer = false;
        }
    }
}
