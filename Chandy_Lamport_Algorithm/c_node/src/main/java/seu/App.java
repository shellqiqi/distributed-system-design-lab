package seu;

import seu.pojo.Snapshot;
import seu.simulation.SimulateApp;
import seu.socket.Receiver;
import seu.socket.Sender;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import static seu.utility.ConfigUtil.*;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入对端IP：");
        OPPOSITE_IP = scanner.next();
        System.out.print("请输入资源转移次数：");
        TRANSMIT_TIMES = scanner.nextInt();
        System.out.print("请输入快照次数：");
        SNAPSHOT_TIMES = scanner.nextInt();
        System.out.print("请输入随机数种子：");
        RANDOM = new Random(scanner.nextInt());
        System.out.print("请输入最小时间间隔：");
        TOLERATE = scanner.nextInt();

        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(new File("result.txt")));
        System.out.println("快照答案：");
        SimulateApp simulateApp = new SimulateApp();
        for (Map.Entry<Integer, Snapshot> snapshot : simulateApp.snapshots.entrySet()) {
            String log = snapshot.getValue().toString() + " total: " + snapshot.getValue().total() + "\n";
            System.out.print(log);
            outputStream.write(log.getBytes());
            outputStream.flush();
        }
        outputStream.close();

        Receiver receiver = new Receiver();
        Thread receiverThread = new Thread(receiver);
        receiverThread.start();

        Sender sender = new Sender(simulateApp.controlMessageSequence);
        Thread senderThread = new Thread(sender);
        senderThread.start();

        receiverThread.join();
        senderThread.join();

        System.out.println("主程序结束");
    }
}
