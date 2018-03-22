package seu;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Recipient {

    public void start(int port) throws IOException, ClassNotFoundException {
        FileOutputStream fileOS = new FileOutputStream(new File("result.txt"));
        BufferedOutputStream bufferedOS = new BufferedOutputStream(fileOS);
        ServerSocket serverSocket = new ServerSocket(port);
        Socket socket = serverSocket.accept();
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        System.out.printf("%-10s%-10s%-15s%-15s\n", "random1", "random2", "timer", "localtime");
        int count = 0;
        while (count <= 5) {
            Object object = inputStream.readObject();
            if (object != null) {
                Message received = (Message)object;
                if (received.getRandom1() == 2) break;
                bufferedOS.write(logLine(received).getBytes());
                bufferedOS.flush();
                count++;
            } else break;
        }
        inputStream.close();
        socket.close();
        serverSocket.close();
        bufferedOS.close();
        fileOS.close();
        System.out.println("Done.");
    }

    private String logLine(Message message) {
        String s = String.format("%-10d%-10d%-15s%-15s\n",
                message.getRandom1(),
                message.getRandom2(),
                message.getTimer(),
                new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));
        System.out.print(s);
        return s;
    }
}
