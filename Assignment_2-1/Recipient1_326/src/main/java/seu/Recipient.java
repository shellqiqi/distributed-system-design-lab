package seu;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Recipient {

    public void start(int port) throws IOException, ClassNotFoundException {
        ServerSocket serverSocket = new ServerSocket(port);
        Socket socket = serverSocket.accept();
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        System.out.printf("%-10s%-10s%-15s%-15s\n", "random1", "random2", "timer", "localtime");
        for (int i = 0; i < 20; i++) {
            Object object = inputStream.readObject();
            if (object != null) {
                Message received = (Message)object;
                if (received.getRandom1() == 2) break;
                logLine(received);
            } else break;
        }
        inputStream.close();
        socket.close();
        serverSocket.close();
        System.out.println("Done.");
    }

    private void logLine(Message message) {
        System.out.printf("%-10d%-10d%-15s%-15s\n",
                message.getRandom1(),
                message.getRandom2(),
                message.getTimer(),
                new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));
    }
}
