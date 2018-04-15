package seu.socket;

import seu.simulation.SimulateMessage;

import java.util.Vector;

public class Sender implements Runnable {

    private Vector<SimulateMessage> senderControlMessages = new Vector<>();

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
                    System.out.println("Send " + message.toString() + " to " + message.from);
                    Thread.sleep(message.time);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void stop(SimulateMessage message) {
        for (char otherNode : new char[] {'i', 'j', 'k'}) {
            SenderThread senderThread = new SenderThread(otherNode, message.toString());
            Thread thread = new Thread(senderThread);
            thread.start();
            System.out.println("Send " + message.toString() + " to " + otherNode);
            Receiver.enableServer = false;
        }
    }
}
