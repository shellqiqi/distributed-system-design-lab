package seu;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.print("请输入主机IP（监听端口888）：");
        Scanner scanner = new Scanner(System.in);
        String serverIP = scanner.next();
        Socket socket = new Socket(serverIP, 888);
        ObjectInputStream inputStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
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
        System.out.println("Client socket closed");
    }

    private static void logLine(Message message) {
        System.out.printf("%-10d%-10d%-15s%-15s\n",
                message.getRandom1(),
                message.getRandom2(),
                message.getTimer(),
                new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));
    }
}
